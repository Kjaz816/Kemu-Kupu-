#!/bin/bash

#Change directory into Kēmu Kupu
cd assignment-3-and-project-team-07/Kemu Kupu

#Run the program
java -Djdk.gtk.version=2 --module-path /home/student/javafx-sdk-11.0.2/javafx --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar Kēmu_Kupu.jar
