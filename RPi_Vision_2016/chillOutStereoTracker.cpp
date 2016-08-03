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
	int targetIndex0 = -1;
	bool targetDetected0 = false;
	int targetIndex1 = -1;
	bool targetDetected1 = false;

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
	

    VideoCapture cap0(0);     // get 'any' cam
    VideoCapture cap1(1);       // get second camera

    // initialize frame size for capture 0
    if (cap0.isOpened()) {
	cap0.set(CV_CAP_PROP_FRAME_WIDTH,frameWidth);
	cap0.set(CV_CAP_PROP_FRAME_HEIGHT,frameHeight);
    }

    // initialize frame size for capture 1
    if (cap1.isOpened()) {
	cap1.set(CV_CAP_PROP_FRAME_WIDTH,frameWidth);
	cap1.set(CV_CAP_PROP_FRAME_HEIGHT,frameHeight);
    }

    // capture loop - runs forever
    while( cap0.isOpened() && cap1.isOpened())
    {
        Mat inputImg0, inputImg1, src_gray, hsvImg;
	Mat binaryImg0, binaryImg1, frame_with_keys;
	Mat erosionImg, dilationImg;
	Mat contourImg0, contourImg1;

	vector<Vec4i> hierarchy;
	vector<vector<Point>> contours0, contours1;

	// get input images from both webcams
        if ( ! cap0.read(inputImg0) )
            break;

        if ( ! cap1.read(inputImg1) )
            break;

	// color threshold input image into binary image
        cvtColor( inputImg0, hsvImg, CV_BGR2HSV );
	inRange(hsvImg, Scalar(minColor_h, minColor_s, minColor_v), Scalar(maxColor_h, maxColor_s, maxColor_v), binaryImg0);		/*green*/
        cvtColor( inputImg1, hsvImg, CV_BGR2HSV );
	inRange(hsvImg, Scalar(minColor_h, minColor_s, minColor_v), Scalar(maxColor_h, maxColor_s, maxColor_v), binaryImg1);		/*green*/

	// find contours from dilated image, place in list (vector)
	findContours(binaryImg0, contours0, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,Point(0,0));
	findContours(binaryImg1, contours1, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE,Point(0,0));

	// find the convex hull objects
	vector<vector<Point>> hulls0 (contours0.size());	
	for (int i=0; i< contours0.size(); i++)
	{
		convexHull(Mat(contours0[i]), hulls0[i], false);
	}

	vector<vector<Point>> hulls1 (contours1.size());	
	for (int i=0; i< contours1.size(); i++)
	{
		convexHull(Mat(contours1[i]), hulls1[i], false);
	}

	// create a bounding rect for each convex hull
	vector<vector<Point>> contours_poly0(contours0.size());
	vector<Rect> boundRect0(contours0.size());
	vector<Point2f>center0(contours0.size());
	vector<float>radius0(contours0.size());

	for (int i=0; i<contours0.size(); i++)
	{
		approxPolyDP(Mat(hulls0[i]),contours_poly0[i], 3, true);
		//boundRect0[i] = boundingRect(Mat(contours_poly0[i]));
		minEnclosingCircle((Mat)contours_poly0[i], center0[i], radius0[i]);
	}

	int maxTargetRadius0 = -1;
	int targetIndex0 = -1;
	targetDetected0 = false;
	for (int i=0; i<contours0.size(); i++)
	{
		// see if this target meets the minimum radius requirement
		if (radius0[i] > minRadius)
		{
			targetDetected0 = true;

			// see if this target is the biggest so far
			if (radius0[i] > maxTargetRadius0)
			{
				targetIndex0 = i;
				maxTargetRadius0 = radius0[i];
			}
		}
	}

	// create a bounding rect for each convex hull
	vector<vector<Point>> contours_poly1(contours1.size());
	vector<Rect> boundRect1(contours1.size());
	vector<Point2f>center1(contours1.size());
	vector<float>radius1(contours1.size());

	for (int i=0; i<contours1.size(); i++)
	{
		approxPolyDP(Mat(hulls1[i]),contours_poly1[i], 3, true);
		//boundRect1[i] = boundingRect(Mat(contours_poly1[i]));
		minEnclosingCircle((Mat)contours_poly1[i], center1[i], radius1[i]);
	}


	int maxTargetRadius1 = -1;
	int targetIndex1 = -1;
	targetDetected1 = false;
	for (int i=0; i<contours1.size(); i++)
	{
		// see if this target meets the minimum radius requirement
		if (radius1[i] > minRadius)
		{
			targetDetected1 = true;

			// see if this target is the biggest so far
			if (radius1[i] > maxTargetRadius1)
			{
				targetIndex1 = i;
				maxTargetRadius1 = radius1[i];
			}
		}
	}

	// create contour image
	contourImg0 = Mat::zeros(binaryImg0.size(),CV_8UC3);
	contourImg1 = Mat::zeros(binaryImg1.size(),CV_8UC3);
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
	if (targetDetected0)
	{
		// write target info out to roborio
		table->PutNumber("targets",(float)1.0f);
		table->PutNumber("targetX",center0[targetIndex0].x);
		table->PutNumber("targetY",center0[targetIndex0].y);
		table->PutNumber("targetRadius",radius0[targetIndex0]);
		table->PutNumber("frameWidth",(float)frameWidth);
		table->PutNumber("frameHeight",(float)frameHeight);

		Scalar colorWhite = Scalar(255, 255, 255);  // white

		// draw the target on contour0 image
		circle(contourImg0, center0[targetIndex0], (int)radius0[targetIndex0],colorWhite,1,8,0);

		// draw the target on contour1 image
		circle(contourImg1, center1[targetIndex1], (int)radius1[targetIndex1],colorWhite,1,8,0);
	}
	else
	{
		// let roborio know that no target is detected
		table->PutNumber("targets",(float)0.0f);
		printf("No target\n");
	}
	

	/*** Image output code - debug only ****/
	//imshow("capture 0",inputImg0);
	//imshow("capture 1",inputImg1);
	imshow("threshold binary0",binaryImg0);
	imshow("threshold binary1",binaryImg1);
	//imshow("erosionImg",erosionImg);
	//imshow("dilationImg",dilationImg);
	//imshow("contours 0",contourImg0);
	//imshow("contours 1",contourImg1);
	//imshow("keys",frame_with_keys);

        int k = waitKey(1);
        if ( k==27 )
            break;
    }
    return 0;
}

