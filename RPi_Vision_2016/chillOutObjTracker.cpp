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
	// OpenCV blob detector param obj
    	SimpleBlobDetector::Params params;

	FILE *parameter_file = NULL;
	
	/********* OpenCV parameter section *******/
	int frameWidth =  640;
	int frameHeight = 480;

	params.filterByColor = true;
	params.blobColor = 255;

	// Filter by Area.
	params.filterByArea = true;
	params.minArea = 1000;
	params.maxArea = 1000000;
	
	// Filter by Circularity
	params.filterByCircularity = false;
	params.minCircularity = 0.1;
	
	// Filter by Convexity
	params.filterByConvexity = false;
	params.minConvexity = 0.87;
	
	// Filter by Inertia
	params.filterByInertia = false;
	params.minInertiaRatio = 0.01;

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
		fscanf(parameter_file,"frameWidth = %d\n",&frameWidth);
		fscanf(parameter_file,"frameHeight = %d\n",&frameHeight);
		fscanf(parameter_file,"minArea = %f\n",&(params.minArea));
		fscanf(parameter_file,"maxArea = %f\n",&(params.maxArea));
		fscanf(parameter_file,"minCircularity = %f\n",&(params.minCircularity));
		fscanf(parameter_file,"minConvexity = %f\n",&(params.minConvexity));
		fscanf(parameter_file,"minInertiaRatio = %f\n",&(params.minInertiaRatio));
		fscanf(parameter_file,"minColor_h = %d\n",&minColor_h);
		fscanf(parameter_file,"minColor_s = %d\n",&minColor_s);
		fscanf(parameter_file,"minColor_v = %d\n",&minColor_v);
		fscanf(parameter_file,"maxColor_h = %d\n",&maxColor_h);
		fscanf(parameter_file,"maxColor_s = %d\n",&maxColor_s);
		fscanf(parameter_file,"maxColor_v = %d\n",&maxColor_v);
	}
	fclose(parameter_file);
	printf("File read complete.\n");
	printf("frameWidth = %d\n",frameWidth);
	printf("frameHeight = %d\n",frameHeight);
	printf("minArea = %f\n",params.minArea);
	printf("maxArea = %f\n",params.maxArea);
	printf("minCircularity = %f\n",params.minCircularity);
	printf("minConvexity = %f\n",params.minConvexity);
	printf("minInertiaRatio = %f\n",params.minInertiaRatio);
	printf("minColor_h = %d\n",minColor_h);
	printf("minColor_s = %d\n",minColor_s);
	printf("minColor_v = %d\n",minColor_v);
	printf("maxColor_h = %d\n",maxColor_h);
	printf("maxColor_s = %d\n",maxColor_s);
	printf("maxColor_v = %d\n",maxColor_v);
	

	// initialize network table for comm with the robot
	
	static std::shared_ptr<NetworkTable> table;
	NetworkTable::SetIPAddress("10.17.78.77");
	NetworkTable::SetClientMode();
	NetworkTable::Initialize();
	table = NetworkTable::GetTable("RPIComm/Data_Table");
	

    VideoCapture cap(0);     // get 'any' cam
    //VideoCapture cap(1);       // get second camera

    // initialize frame size
    if (cap.isOpened()) {
	cap.set(CV_CAP_PROP_FRAME_WIDTH,frameWidth);
	cap.set(CV_CAP_PROP_FRAME_HEIGHT,frameHeight);
    }

    // capture loop - runs forever
    while( cap.isOpened() )
    {
        Mat frame,src_gray,hsv,binary, frame_with_keys;
        if ( ! cap.read(frame) )
            break;
       cvtColor( frame, hsv, CV_BGR2HSV );
	//inRange(hsv, Scalar(20,100, 100), Scalar(30,255, 255), binary);	/*yellow*/
	//inRange(hsv, Scalar(157,72, 156), Scalar(180,169, 255), binary);	/*pink*/
	inRange(hsv, Scalar(minColor_h, minColor_s, minColor_v), Scalar(maxColor_h, maxColor_s, maxColor_v), binary);		/*green*/
	//inRange(hsv, Scalar(40,0, 180), Scalar(135,110, 255), binary);       /*white*/
      	//inRange(hsv, Scalar(200,0, 0), Scalar(210,255, 255), binary);		/*purple*/

	// Set up detector with params
	Ptr<SimpleBlobDetector> detector = SimpleBlobDetector::create(params);   

	std::vector<KeyPoint> keypoints;
	detector->detect(binary,keypoints);

	//drawKeypoints(binary,keypoints,frame_with_keys,Scalar(0,255,255),DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
	drawKeypoints(frame,keypoints,frame_with_keys,Scalar(0,255,255),DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
 
	// write some data to the robot
	table->PutNumber("targets",(float)keypoints.size());
	if (keypoints.size() > 0) {
		table->PutNumber("targetX",keypoints[0].pt.x);
		table->PutNumber("targetY",keypoints[0].pt.y);
	}

	/*** blob detection output - debug only ***/
	//printf("keypoints size = %d\n",keypoints.size());
	//for (int i=0; i< keypoints.size(); i++)
	//	printf("keypoints[%d]  = (%4.2f,%4.2f)\n",i,keypoints[i].pt.x,keypoints[i].pt.y);
	

	/*** Image output code - debug only ****/
	//imshow("lalala",binary);
	//imshow("original",frame);
	imshow("keys",frame_with_keys);

        int k = waitKey(1);
        if ( k==27 )
            break;
    }
    return 0;
}
