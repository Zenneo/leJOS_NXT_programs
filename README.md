This project contains some leJOS scripts that we created for our school project. It is tested with leJOS 0.9.1beta3 running openJDK7.

##Scripts:

###leJOS Gruppen-Projekt/src/
Contains a group project where multiple NXTs form a small production facilty with a transport and storage system for sweets. This folder contains only one group's code.
The robot build by this group is operated by 2 NXTs. Each one has its own folder with all its classes inside.

####vehicle/
This NXT controls the movement of the robot. It determines the robots position through push sensors. The vehicle itself is moving on rails.

######Main.java
Main file that contains the main loop and general logic.

######Movement.java
Handles the vehicles movement and keeps track of what operations are currently processed.

######Position.java
Determines the position of the vehicle based on touch sensors.

######LCDthread.java
A class expanding Thread that updates the current status on the LCD screen of the NXT.
