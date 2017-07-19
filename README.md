# EdgeScreen
Preliminary code for the Edge app 

## In order to run on the Edge simulator
1. Create an Android virtual device running Android 24 (Nougat 7.0)
2. Install the edge_simulator-v1.4.0.apk file by running the command `adb install edge_simulator-v1.4.0.apk`
3. Add the edge_simulator_v1.4.0.jar file to the libs folder

Since this application uses ListView objects, you'll also have to do the following steps:

4. Add `android:sharedUserId="com.samsung.android.cocktailbar"` under the package declaration in your AndroidManifest file
5. Create a file `listview_simulator_config.xml` file with the following contents:
```<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="100dp"
    android:minHeight="100dp" />
```
6. Add the following lines to the EdgeProvider receiver entry in your AndroidManifest file:
```
<meta-data android:name="android.appwidget.provider"
                android:resource="@xml/listview_simulator_config"/>
```
7. Add the sim_test.jks file to your project directory
8. Run the following command: `adb shell appwidget grantbind --package com.samsung.android.cocktailbar --user 0`
9. Sign the project with the following credentials:
   * Key Alias: androiddebugkey
   * Key Password: android
   * Store File: sim_test.jks
   * Store Password: android

## In order to run on Edge phone
Simply remove everything related to the simulator, including the signing key.
