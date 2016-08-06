#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include <networktables/NetworkTable.h>

#include <stdlib.h>
#include <stdio.h>

using namespace cv;
using namespace std;
using namespace nt;

Mat hsvImg;
Mat binaryImg;

// color filter params
int minColor_h = 30;
int minColor_s = 50;
int minColor_v = 50;

int maxColor_h = 70;
int maxColor_s = 255;
int maxColor_v = 255;

int hue_min_slider, hue_max_slider;
int sat_min_slider, sat_max_slider;
int val_min_slider, val_max_slider;

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

	// initialize slider values
	hue_min_slider = minColor_h;
	hue_max_slider = maxColor_h;
	sat_min_slider = minColor_s;
	sat_max_slider = maxColor_s;
	val_min_slider = minColor_v;
	val_max_slider = maxColor_v;

	// slider control window - named "Thresholds"
	namedWindow("Thresholds", 1);
	createTrackbar("Hue Min", "Thresholds", &hue_min_slider, 255, on_min_hue);
	createTrackbar("Hue Max", "Thresholds", &hue_max_slider, 255, on_max_hue);
	createTrackbar("Sat Min", "Thresholds", &sat_min_slider, 255, on_min_sat);
	createTrackbar("Sat Max", "Thresholds", &sat_max_slider, 255, on_max_sat);
	createTrackbar("Val Min", "Thresholds", &val_min_slider, 255, on_min_val);
	createTrackbar("Val Max", "Thresholds", &val_max_slider, 255, on_max_val);

    	// capture loop - runs forever
    	while( cap.isOpened() )
    	{
        	Mat inputImg;
        
		if ( ! cap.read(inputImg) )
            		break;

	        cvtColor( inputImg, hsvImg, CV_BGR2HSV );

		// color threshold input image into binary image
		inRange(hsvImg, Scalar(minColor_h, minColor_s, minColor_v), Scalar(maxColor_h, maxColor_s, maxColor_v), binaryImg);	
	
		//imshow("original",inputImg);
		imshow("Color Threshold",binaryImg);

        	int k = waitKey(1);
        	if ( k==27 )
            		break;
	
	}

	return 0;
}