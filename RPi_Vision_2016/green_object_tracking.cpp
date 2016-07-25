// Cristóbal Carnero Liñán <grendel.ccl@gmail.com>

#include <iostream>
#include <iomanip>

#if (defined(_WIN32) || defined(__WIN32__) || defined(__TOS_WIN__) || defined(__WINDOWS__) || (defined(__APPLE__) & defined(__MACH__)))
#include <cv.h>
#include <highgui.h>
#else
#include <opencv/cv.h>
#include <opencv/highgui.h>
#include "opencv2/imgproc/imgproc.h"
#endif


include <cvblob.h>

using namespace cv;
using namespace cvb;

int main()
{
  CvTracks tracks;

  cvNamedWindow("green_object_tracking", CV_WINDOW_AUTOSIZE);

  CvCapture *capture = cvCaptureFromCAM(0);
  cvGrabFrame(capture);
  IplImage *img = cvRetrieveFrame(capture);

  CvSize imgSize = cvGetSize(img);

  IplImage *frame = cvCreateImage(imgSize, img->depth, img->nChannels);

  IplConvKernel* morphKernel = cvCreateStructuringElementEx(5, 5, 1, 1, CV_SHAPE_RECT, NULL);

  //unsigned int frameNumber = 0;
  unsigned int blobNumber = 0;

  bool quit = false;
  while (!quit&&cvGrabFrame(capture))
  {
    IplImage *img = cvRetrieveFrame(capture);

    cvConvertScale(img, frame, 1, 0);

    IplImage *segmentated = cvCreateImage(imgSize, 8, 1);
    
    // Detecting green pixels:
    // (This is very slow, use direct access better...)
    /*
    for (unsigned int j=0; j<imgSize.height; j++)
      for (unsigned int i=0; i<imgSize.width; i++)
      {
	CvScalar c = cvGet2D(frame, j, i);

	double b = ((double)c.val[0])/255.;
	double g = ((double)c.val[1])/255.;
	double r = ((double)c.val[2])/255.;
	//unsigned char f = 255*((g>0.2+r)&&(g>0.2+b));
	unsigned char f = 255*((g>0.05+r)&&(g>0.05+b));

	cvSet2D(segmentated, j, i, CV_RGB(f, f, f));
      }
    */
    Mat hsv_image;
    cvtColor(frame,hsv_image,cv::COLOR_BGR2HSV);
    Mat lower_red_range;
    Mat upper_red_range;
    inRange(hsv_image,cv::Scalar(0,100,100),cv::Scalar(10,255,255),lower_red_range); 
    inRange(hsv_image,cv::Scalar(160,255,255), cv::Scalar(179,255,255), upper_red_range);
    cvAdd(segmentated,lower_red_range, upper_red_range);

    cvMorphologyEx(segmentated, segmentated, NULL, morphKernel, CV_MOP_OPEN, 1);

    //cvShowImage("segmentated", segmentated);

    IplImage *labelImg = cvCreateImage(cvGetSize(frame), IPL_DEPTH_LABEL, 1);

    CvBlobs blobs;
    unsigned int result = cvLabel(segmentated, labelImg, blobs);
    cvFilterByArea(blobs, 500, 1000000);
    cvRenderBlobs(labelImg, blobs, frame, frame, CV_BLOB_RENDER_BOUNDING_BOX);
    cvUpdateTracks(blobs, tracks, 200., 5);
    cvRenderTracks(tracks, frame, frame, CV_TRACK_RENDER_ID|CV_TRACK_RENDER_BOUNDING_BOX);

    cvShowImage("green_object_tracking", frame);

    /*std::stringstream filename;
    filename << "greenobject_" << std::setw(5) << std::setfill('0') << frameNumber << ".png";
    cvSaveImage(filename.str().c_str(), frame);*/

    cvReleaseImage(&labelImg);
    cvReleaseImage(&segmentated);

    char k = cvWaitKey(10)&0xff;
    switch (k)
    {
      case 27:
      case 'q':
      case 'Q':
        quit = true;
        break;
      case 's':
      case 'S':
        for (CvBlobs::const_iterator it=blobs.begin(); it!=blobs.end(); ++it)
        {
          std::stringstream filename;
          filename << "redobject_blob_" << std::setw(5) << std::setfill('0') << blobNumber << ".png";
          cvSaveImageBlob(filename.str().c_str(), img, it->second);
          blobNumber++;

          std::cout << filename.str() << " saved!" << std::endl;
        }
        break;
    }

    cvReleaseBlobs(blobs);

    //frameNumber++;
  }

  cvReleaseStructuringElement(&morphKernel);
  cvReleaseImage(&frame);

  cvDestroyWindow("green_object_tracking");

  return 0;
}
