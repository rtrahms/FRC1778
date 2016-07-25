all:	red_object_tracking greentrack2 chillOutObjTracker
red_object_tracking: red_object_tracking.cpp
	g++ `pkg-config opencv cvblob --cflags --libs` red_object_tracking.cpp -o red_object_tracking

greentrack2: greentrack2.cpp
	g++ `pkg-config opencv --cflags --libs` -std=gnu++11 greentrack2.cpp -lntcore -o greentrack2

chillOutObjTracker: chillOutObjTracker.cpp
	g++ `pkg-config opencv --cflags --libs` -std=gnu++11 chillOutObjTracker.cpp -lntcore -o chillOutObjTracker

