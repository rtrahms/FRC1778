#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include <networktables/NetworkTable.h>

#include <stdlib.h>
#include <stdio.h>

using namespace cv;
using namespace std;
using namespace nt;

int main() {

    	VideoCapture cap(0);     // get 'any' cam
    	//VideoCapture cap("/dev/video1");       // get second camera

    	// initialize frame size
    	if (cap.isOpened()) {
		cap.set(CV_CAP_PROP_FRAME_WIDTH,320);
		cap.set(CV_CAP_PROP_FRAME_HEIGHT,240);
    	}

	int lowThreshold = 20;
	int ratio = 3;
	int kernel_size = 3;
	const int max_lowThreshold = 100;

	//char *window_name = "edge_map";
	//namedWindow( window_name, CV_WINDOW_AUTOSIZE);
	//createTrackbar("Min Threshold", window_name, &lowThreshold, max_lowThreshold, CannyThreshold);

    	// capture loop - runs forever
    	while( cap.isOpened() )
    	{
        	Mat inputImg, grayscale, edges;
        
		if ( ! cap.read(inputImg) )
            		break;

        	cvtColor( inputImg, grayscale, CV_BGR2GRAY );
	
		blur(grayscale, edges, Size(3,3));
		Canny(edges, edges, 40, 50, 3);

		//imshow("original",inputImg);
		imshow("edges",edges);

        	int k = waitKey(1);
        	if ( k==27 )
            		break;
	
	}

	return 0;
}