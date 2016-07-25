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
    SimpleBlobDetector::Params params;
	
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

	// initialize network table for comm with the robot
	
	static std::shared_ptr<NetworkTable> table;
	NetworkTable::SetIPAddress("10.17.78.77");
	NetworkTable::SetClientMode();
	NetworkTable::Initialize();
	table = NetworkTable::GetTable("RPIComm/Data_Table");
	

    VideoCapture cap(0);     // get 'any' cam

    while( cap.isOpened() )   // check if we succeeded
    {
        Mat frame,src_gray,hsv,binary, frame_with_keys;
        if ( ! cap.read(frame) )
            break;
       cvtColor( frame, hsv, CV_BGR2HSV );
	//inRange(hsv, Scalar(20,100, 100), Scalar(30,255, 255), binary);	/*yellow*/
	//inRange(hsv, Scalar(157,72, 156), Scalar(180,169, 255), binary);	/*pink*/
	inRange(hsv, Scalar(30,50, 50), Scalar(70,255, 255), binary);		/*green*/
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

	//printf("keypoints size = %d\n",keypoints.size());

	//for (int i=0; i< keypoints.size(); i++)
	//	printf("keypoints[%d]  = (%4.2f,%4.2f)\n",i,keypoints[i].pt.x,keypoints[i].pt.y);
	

	//imshow("lalala",binary);
	//imshow("original",frame);

	imshow("keys",frame_with_keys);

        int k = waitKey(1);
        if ( k==27 )
            break;
    }
    return 0;
}
