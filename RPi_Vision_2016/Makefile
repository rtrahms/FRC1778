all:	chillOutObjTracker chillOutStereoTracker calibrate edgeDetector

clean:
	rm -f chillOutObjTracker chillOutStereoTracker calibrate edgeDetector

chillOutObjTracker: chillOutObjTracker.cpp
	g++ -o chillOutObjTracker chillOutObjTracker.cpp `pkg-config opencv --cflags --libs` -std=gnu++11  -lntcore -lv4l2

chillOutStereoTracker: chillOutStereoTracker.cpp
	g++ -o chillOutStereoTracker chillOutStereoTracker.cpp `pkg-config opencv --cflags --libs` -std=gnu++11  -lntcore 

calibrate: calibrate.cpp
	g++ -o calibrate calibrate.cpp `pkg-config opencv --cflags --libs` -std=gnu++11  -lntcore 

edgeDetector: edgeDetector.cpp
	g++ -o edgeDetector edgeDetector.cpp `pkg-config opencv --cflags --libs` -std=gnu++11  -lntcore 
