#include "opencv2/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include <networktables/NetworkTable.h>

#include <stdlib.h>
#include <stdio.h>

using namespace cv;
using namespace std;
using namespace nt;

int main()
{
	FILE *parameter_file = NULL;

	char roborio_ipaddr[32];
	
	/********* OpenCV parameter section *******/
	int frameWidth =  640;
	int frameHeight = 480;

	// minimum target radius
	int minRadius = 10;
	int targetIndex = -1;
	bool targetDetected = false;

	// color filter params
	int minColor_h = 30;
	int minColor_s = 50;
	int minColor_v = 50;

	int maxColor_h = 70;
	int maxColor_s = 255;
	int maxColor_v = 255;

	/******** OpenCV parameter section *******/

	// read in parameters (if file exists)
	
	parameter_file = fopen("chillOut_params.txt","r");
	if (parameter_file != NULL) {
		printf("Reading parameters from file\n");
		fscanf(parameter_file,"roborio_ipaddr = %s\n",roborio_ipaddr);
		fscanf(parameter_file,"frameWidth = %d\n",&frameWidth);
		fscanf(parameter_file,"frameHeight = %d\n",&frameHeight);
		fscanf(parameter_file,"minRadius = %d\n",&minRadius);
		fscanf(parameter_file,"minColor_h = %d\n",&minColor_h);
		fscanf(parameter_file,"minColor_s = %d\n",&minColor_s);
		fscanf(parameter_file,"minColor_v = %d\n",&minColor_v);
		fscanf(parameter_file,"maxColor_h = %d\n",&maxColor_h);
		fscanf(parameter_file,"maxColor_s = %d\n",&maxColor_s);
		fscanf(parameter_file,"maxColor_v = %d\n",&maxColor_v);
	}
	fclose(parameter_file);
	printf("File read complete.\n");
	printf("roborio_ipaddr = %s\n",roborio_ipaddr);
	printf("frameWidth = %d\n",frameWidth);
	printf("frameHeight = %d\n",frameHeight);
	printf("minRadius = %d\n",minRadius);
	printf("minColor_h = %d\n",minColor_h);
	printf("minColor_s = %d\n",minColor_s);
	printf("minColor_v = %d\n",minColor_v);
	printf("maxColor_h = %d\n",maxColor_h);
	printf("maxColor_s = %d\n",maxColor_s);
	printf("maxColor_v = %d\n",maxColor_v);
	

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
        Mat inputImg, src_gray, hsvImg, binaryImg, frame_with_keys;
	Mat erosionImg, dilationImg, contourImg;

	vector<Vec4i> hierarchy;
	vector<vector<Point>> contours;

        if ( ! cap.read(inputImg) )
            break;

	// color threshold input image into binary image
        cvtColor( inputImg, hsvImg, CV_BGR2HSV );
	inRange(hsvImg, Scalar(minColor_h, minColor_s, minColor_v), Scalar(maxColor_h, maxColor_s, maxColor_v), binaryImg);		/*green*/

	// erode thresholded image - not used
	/*
	Mat erosionElement = getStructuringElement(MORPH_RECT, Size(7,7), Point(3,3));
	erode(binaryImg,erosionImg,erosionElement);	
	
	// dilate eroded image
	Mat dilateElement = getStructuringElement(MORPH_RECT, Size(7,7), Point(3,3));
	dilate(erosionImg, dilationImg, dilateElement);
	*/

	// find contours from dilated image, place in list (vector)
	//findContours(dilationImg, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,Point(0,0));
	findContours(binaryImg, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,Point(0,0));

	// find the convex hull objects
	vector<vector<Point>> hulls (contours.size());	
	for (int i=0; i< contours.size(); i++)
	{
		convexHull(Mat(contours[i]), hulls[i], false);
	}

	// create a bounding rect for each convex hull
	vector<vector<Point>> contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());
	vector<Point2f>center(contours.size());
	vector<float>radius(contours.size());

	for (int i=0; i<contours.size(); i++)
	{
		approxPolyDP(Mat(hulls[i]),contours_poly[i], 3, true);
		//boundRect[i] = boundingRect(Mat(contours_poly[i]));
		minEnclosingCircle((Mat)contours_poly[i], center[i], radius[i]);
	}

	int maxTargetRadius = -1;
	int targetIndex = -1;
	targetDetected = false;
	for (int i=0; i<contours.size(); i++)
	{
		// see if this target meets the minimum radius requirement
		if (radius[i] > minRadius)
		{
			targetDetected = true;

			// see if this target is the biggest so far
			if (radius[i] > maxTargetRadius)
			{
				targetIndex = i;
				maxTargetRadius = radius[i];
			}
		}
	}

	// create contour image
	contourImg = Mat::zeros(binaryImg.size(),CV_8UC3);
	/*
	for (int i=0; i< contours.size(); i++)
	{
		Scalar colorGreen = Scalar(0, 0, 255);  // green
		Scalar colorWhite = Scalar(255, 255, 255);  // white
		//drawContours(contourImg, contours, i, colorGreen, 1, 8, hierarchy, 0, Point());
		drawContours(contourImg, hulls, i, colorWhite, 1, 8, hierarchy, 0, Point());
		circle(contourImg, center[i], (int)radius[i],colorGreen,1,8,0);
	}
	*/

	// if target meets criteria, do stuff
	if (targetDetected)
	{
		// write target info out to roborio
		table->PutNumber("targets",(float)1.0f);
		table->PutNumber("targetX",center[targetIndex].x);
		table->PutNumber("targetY",center[targetIndex].y);
		table->PutNumber("targetRadius",radius[targetIndex]);
		table->PutNumber("frameWidth",(float)frameWidth);
		table->PutNumber("frameHeight",(float)frameHeight);

		// draw the target on contour image
		Scalar colorWhite = Scalar(255, 255, 255);  // white
		circle(inputImg, center[targetIndex], (int)radius[targetIndex],colorWhite,1,8,0);
		//circle(binaryImg, center[targetIndex], (int)radius[targetIndex],colorWhite,1,8,0);
		//circle(contourImg, center[targetIndex], (int)radius[targetIndex],colorWhite,1,8,0);

		printf("Target radius %3.0f detected at (%3.0f,%3.0f)\n",
			radius[targetIndex], center[targetIndex].x,center[targetIndex].y);
	}
	else
	{
		// let roborio know that no target is detected
		table->PutNumber("targets",(float)0.0f);
		printf("No target\n");
	}
	

	/*** Image output code - debug only ****/
	imshow("original",inputImg);
	//imshow("threshold binary",binaryImg);
	//imshow("erosionImg",erosionImg);
	//imshow("dilationImg",dilationImg);
	//imshow("contours",contourImg);
	//imshow("keys",frame_with_keys);

        int k = waitKey(1);
        if ( k==27 )
            break;
    }
    return 0;
}

