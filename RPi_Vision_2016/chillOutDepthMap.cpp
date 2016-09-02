#include "opencv2/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/calib3d/calib3d.hpp"

#include <networktables/NetworkTable.h>

#include <stdlib.h>
#include <stdio.h>

using namespace cv;
using namespace std;
using namespace nt;

int main()
{
	FILE *parameter_file = NULL;

	char roborio_ipaddr[32] = "10.17.78.23";
	
	/********* OpenCV parameter section *******/
	int frameWidth =  320;
	int frameHeight = 240;

	int nDisparities = 16;
	int SADWindowSize = 21;   // should be odd size

	/******** OpenCV parameter section *******/	

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
        Mat inputImg0, inputImg1, grayImg0, grayImg1;
	Mat imgDisparity16S, imgDisparity8U;

	grayImg0 = Mat(frameWidth, frameHeight, CV_8UC1);
	grayImg1 = Mat(frameWidth, frameHeight, CV_8UC1);

        if ( ! cap0.read(inputImg0) )
            break;

        if ( ! cap1.read(inputImg1) )
            break;
	
	cvtColor(inputImg0, grayImg0, CV_BGR2GRAY);
	cvtColor(inputImg1, grayImg1, CV_BGR2GRAY);

	imgDisparity16S = Mat(frameWidth, frameHeight, CV_16S);
	imgDisparity8U = Mat(frameWidth, frameHeight, CV_8UC1);

	Ptr<StereoBM> sbm = StereoBM::create(nDisparities,SADWindowSize);

	sbm->compute(grayImg0, grayImg1,imgDisparity16S);
	
	double minVal, maxVal;
	minMaxLoc(imgDisparity16S, &minVal, &maxVal);

	imgDisparity16S.convertTo(imgDisparity8U,CV_8UC1,255/(maxVal - minVal));

	/*** Image output code - debug only ****/
	//imshow("capture 0",inputImg0);
	//imshow("capture 1",inputImg1);
	//imshow("gray 0",grayImg0);
	//imshow("gray 1",grayImg1);
	imshow("disparityImg",imgDisparity8U);

        int k = waitKey(1);
        if ( k==27 )
            break;
    }
    return 0;
}

