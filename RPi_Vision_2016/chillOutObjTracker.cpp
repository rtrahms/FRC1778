#include "opencv2/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include <networktables/NetworkTable.h>

#include <stdlib.h>
#include <stdio.h>
#include <ctime>

using namespace cv;
using namespace std;
using namespace nt;

// color filter params
int minColor_h = 69;
int minColor_s = 0;
int minColor_v = 0;

int maxColor_h = 255;
int maxColor_s = 255;
int maxColor_v = 255;

double minArea = 1000.0;
double maxArea = 30000.0;

int dilationFactor = 5;

int hue_min_slider, hue_max_slider;
int sat_min_slider, sat_max_slider;
int val_min_slider, val_max_slider;
int area_min_slider, area_max_slider;
int dilation_slider;

void on_min_hue(int, void *) 
{
	minColor_h = hue_min_slider;
}

void on_max_hue(int, void *) 
{
	maxColor_h = hue_max_slider;
}

void on_min_sat(int, void *) 
{
	minColor_s = sat_min_slider;
}

void on_max_sat(int, void *) 
{
	maxColor_s = sat_max_slider;
}

void on_min_val(int, void *) 
{
	minColor_v = val_min_slider;
}

void on_max_val(int, void *) 
{
	maxColor_v = val_max_slider;
}

void on_min_area(int, void *) 
{
	minArea = area_min_slider;
}

void on_max_area(int, void *) 
{
	maxArea = area_max_slider;
}

void on_dilation(int, void *) 
{
	dilationFactor = dilation_slider;
}



int main()
{
	FILE *parameter_file = NULL;

	char roborio_ipaddr[32];

	/********* OpenCV static image section *******/
	Mat overlayImg;

	/********* MJPG streaming section *******/
	String outFile = "./out.mjpg";
	
	/******* timer section *****/
	clock_t startTime = clock();
	clock_t duration;
	double timeInSeconds;
	double resetVideoTimeSec = 60.0;  // Release/reset VideoWriter every so many questions
	int resetCtr = 0;
	
	/********* OpenCV parameter section *******/
	int frameWidth =  640;
	int frameHeight = 480;
	int imageCenterX, imageCenterY;

	// target area
	int targetIndex = -1;
	bool targetDetected = false;


	/******** OpenCV parameter section *******/

	// read in parameters (if file exists)
	
	parameter_file = fopen("chillOut_params.txt","r");
	if (parameter_file != NULL) {
		printf("Reading parameters from file\n");
		fscanf(parameter_file,"roborio_ipaddr = %s\n",roborio_ipaddr);
		fscanf(parameter_file,"frameWidth = %d\n",&frameWidth);
		fscanf(parameter_file,"frameHeight = %d\n",&frameHeight);
		fscanf(parameter_file,"minArea = %f\n",&minArea);
		fscanf(parameter_file,"maxArea = %f\n",&maxArea);
		fscanf(parameter_file,"minColor_h = %d\n",&minColor_h);
		fscanf(parameter_file,"maxColor_h = %d\n",&maxColor_h);
		fscanf(parameter_file,"minColor_s = %d\n",&minColor_s);
		fscanf(parameter_file,"maxColor_s = %d\n",&maxColor_s);
		fscanf(parameter_file,"minColor_v = %d\n",&minColor_v);
		fscanf(parameter_file,"maxColor_v = %d\n",&maxColor_v);
		fscanf(parameter_file,"dilationFactor = %d\n",&dilationFactor);
	}
	fclose(parameter_file);
	printf("File read complete.\n");
	printf("roborio_ipaddr = %s\n",roborio_ipaddr);
	printf("frameWidth = %d\n",frameWidth);
	printf("frameHeight = %d\n",frameHeight);
	printf("minArea = %f\n",minArea);
	printf("maxArea = %f\n",maxArea);
	printf("minColor_h = %d\n",minColor_h);
	printf("maxColor_h = %d\n",maxColor_h);
	printf("minColor_s = %d\n",minColor_s);
	printf("maxColor_s = %d\n",maxColor_s);
	printf("minColor_v = %d\n",minColor_v);
	printf("maxColor_v = %d\n",maxColor_v);
	printf("dilationFactor = %d\n",dilationFactor);
	
	// initialize slider values
	hue_min_slider = minColor_h;
	hue_max_slider = maxColor_h;
	sat_min_slider = minColor_s;
	sat_max_slider = maxColor_s;
	val_min_slider = minColor_v;
	val_max_slider = maxColor_v;
	area_min_slider = minArea;
	area_max_slider = maxArea;
	dilation_slider = dilationFactor;

	// slider control window - named "Thresholds"
	namedWindow("Thresholds", 1);
	createTrackbar("Hue Min", "Thresholds", &hue_min_slider, 255, on_min_hue);
	createTrackbar("Hue Max", "Thresholds", &hue_max_slider, 255, on_max_hue);
	createTrackbar("Sat Min", "Thresholds", &sat_min_slider, 255, on_min_sat);
	createTrackbar("Sat Max", "Thresholds", &sat_max_slider, 255, on_max_sat);
	createTrackbar("Val Min", "Thresholds", &val_min_slider, 255, on_min_val);
	createTrackbar("Val Max", "Thresholds", &val_max_slider, 255, on_max_val);
	createTrackbar("Area Min", "Thresholds", &area_min_slider, maxArea, on_min_area);
	createTrackbar("Area Max", "Thresholds", &area_max_slider, maxArea, on_max_area);
	createTrackbar("Dilation", "Thresholds", &dilation_slider, 50, on_dilation);

	// calculate image center
	imageCenterX = frameWidth/2;
	imageCenterY = frameHeight/2;

	// read in the overlay image (depends on 320x240 or 160x120, nothing else is valid)
	if (frameWidth == 320)
		overlayImg = imread("/home/pi/chillout/Vision2017_Target_Overlay1_320x240.png",CV_LOAD_IMAGE_COLOR);
	else if (frameWidth == 160)
		overlayImg = imread("/home/pi/chillout/Vision2017_Target_Overlay1_160x120.png",CV_LOAD_IMAGE_COLOR);
	else
		overlayImg = Mat::zeros(Size(frameWidth, frameHeight),CV_8UC3);

	// initialize network table for comm with the robot
	static std::shared_ptr<NetworkTable> table;
	NetworkTable::SetIPAddress(roborio_ipaddr);
	NetworkTable::SetClientMode();
	NetworkTable::Initialize();
	table = NetworkTable::GetTable("RPIComm/Data_Table");
	

    VideoCapture cap(0);     // get 'any' cam
    //VideoCapture cap("/dev/video1");       // get second camera

    // initialize frame size
    if (cap.isOpened()) {
	cap.set(CV_CAP_PROP_FRAME_WIDTH,frameWidth);
	cap.set(CV_CAP_PROP_FRAME_HEIGHT,frameHeight);
    }

    // capture loop - runs forever
    while( cap.isOpened() )
    {
        Mat inputImg, src_gray, hsvImg, binaryImg;
	Mat erosionImg, dilationImg, contourImg;
	Mat outputImg;

	vector<Vec4i> hierarchy;
	vector<vector<Point>> contours;

        if ( ! cap.read(inputImg) )
            break;

	// color threshold input image into binary image
        cvtColor( inputImg, hsvImg, CV_BGR2HSV );
	inRange(hsvImg, Scalar(minColor_h, minColor_s, minColor_v), Scalar(maxColor_h, maxColor_s, maxColor_v), binaryImg);		/*green*/

	Mat binary2 = binaryImg.clone();

	// erode thresholded image - not used
	//Mat erosionElement = getStructuringElement(MORPH_RECT, Size(7,7), Point(3,3));
	//erode(binaryImg,erosionImg,erosionElement);	
	
	// dilate image (unify pieces)
	int dil = dilationFactor;
	int dil2 = dilationFactor*2 + 1;
	Mat dilateElement = getStructuringElement(MORPH_RECT, Size(dil2,dil2), Point(dil,dil));
	//dilate(erosionImg, dilationImg, dilateElement);
	dilate(binary2, dilationImg, dilateElement);

	// find contours from dilated image, place in list (vector)
	findContours(dilationImg, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,Point(0,0));
	//findContours(binary2, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,Point(0,0));

	// find the convex hull objects
	
	vector<vector<Point>> hulls (contours.size());	
	for (int i=0; i< contours.size(); i++)
	{
		convexHull(Mat(contours[i]), hulls[i], false);
	}

	// create stats for each convex hull	
	vector<Moments>mu(hulls.size());	  // hull moments
	vector<Point2f>mc(hulls.size());       // hull mass centers
	vector<double>targetArea(hulls.size());   // hull areas

	for (int i=0; i<hulls.size(); i++)
	{
		mu[i] = moments(hulls[i], false);   // find moments
		mc[i] = Point2f(mu[i].m10/mu[i].m00, mu[i].m01/mu[i].m00);
		targetArea[i] = contourArea(hulls[i]);
	}

	int maxTargetArea = -1;
	int targetIndex = -1;
	targetDetected = false;
	for (int i=0; i<hulls.size(); i++)
	{
		// see if this target meets the minimum area requirement
		if (targetArea[i] > minArea)
		{
			targetDetected = true;

			// see if this target is the biggest so far
			if (targetArea[i] > maxTargetArea)
			{
				targetIndex = i;
				maxTargetArea = targetArea[i];
			}
		}
	}

	// create contour image
	contourImg = Mat::zeros(binaryImg.size(),CV_8UC3);
	for (int i=0; i< contours.size(); i++)
	{
		Scalar colorGreen = Scalar(0, 0, 255);  // green
		Scalar colorWhite = Scalar(255, 255, 255);  // white
		drawContours(contourImg, hulls, i, colorWhite, 2, 8, hierarchy, 0, Point());
	}

	// if target meets criteria, do stuff
	if (targetDetected)
	{
		// write target info out to roborio
		table->PutNumber("targets",(float)1.0f);
		table->PutNumber("targetX",mc[targetIndex].x - imageCenterX);
		table->PutNumber("targetY",mc[targetIndex].y - imageCenterY);
		table->PutNumber("targetArea",targetArea[targetIndex]);
		table->PutNumber("frameWidth",(float)frameWidth);
		table->PutNumber("frameHeight",(float)frameHeight);

		// draw the target on one of the images
		Scalar colorWhite = Scalar(255, 255, 255);  // white
		Scalar colorGreen = Scalar(0, 255, 0);  // green
		Scalar colorBlue = Scalar(255, 0, 0);  // blue
		drawContours(inputImg, hulls, targetIndex, colorGreen, 2, 8, hierarchy, 0, Point());
		circle(inputImg, mc[targetIndex], 3 ,colorBlue,2,6,0);

		//printf("Target area %3.0f detected at (%3.0f,%3.0f)\n",
		//	targetArea[targetIndex], mc[targetIndex].x - imageCenterX,
		//			     mc[targetIndex].y - imageCenterY);
	}
	else
	{
		// let roborio know that no target is detected
		table->PutNumber("targets",(float)0.0f);
		//printf("No target\n");
	}
	
	// create output image with overlay
	float alpha = 0.6;
	float beta = 1.0 - alpha;
	addWeighted(inputImg, alpha, overlayImg, beta, 0.0, outputImg);
	
	/*** Image output code - debug only ****/
	//imshow("original",inputImg);
	//imshow("overlay",overlayImg);
	imshow("threshold binary",binaryImg);
	imshow("contours",contourImg);
	imshow("output",outputImg);

	// write out to file (for webserver)
	VideoWriter outStream(outFile,CV_FOURCC('M','J','P','G'), 2, Size(frameWidth,frameHeight), true);
	if (outStream.isOpened())
		outStream.write(outputImg);
	else
		printf("Error! Could not open stream!\n");
	

	// check time duration - if long enough, reset VideoWriter object
	duration = clock();
	timeInSeconds = (duration - startTime) / (double) CLOCKS_PER_SEC;
	//printf("timeInSeconds = %f\n",timeInSeconds);
	if (timeInSeconds > resetVideoTimeSec)
	{
		printf("Releasing VideoWriter: %d\n",++resetCtr);
		outStream.release();
		startTime = clock();
	}

        int k = waitKey(1);
        if ( k==27 )
            break;
    }
    return 0;
}

