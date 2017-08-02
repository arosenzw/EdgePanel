# EdgeScreen
Preliminary code for the Edge app 

## General config information
1. The edge panel requires the following permission in the AndroidManifest.xml file
`<uses-permission android:name="com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY" />`
2. Add the following lines to your manifest file
```
<receiver android:name="com.example.EdgeWidgetProvider">
    <intent-filter>
        <!--This update is specific to edge single plus-->
        <action android:name="com.samsung.android.cocktail.v2.action.COCKTAIL_UPDATE" />
    </intent-filter>

    <meta-data
        android:name="com.samsung.android.cocktail.provider"
        android:resource="@xml/edgepanel_cocktail_config" />
</receiver>

<meta-data
    android:name="com.samsung.android.cocktail.mode"
    android:value="edge_single_plus" /> <!--Specific to single plus mode-->
```
where `edgepanel_cocktail_config` looks something like
```
<?xml version="1.0" encoding="utf-8"?>
<cocktail-provider xmlns:android="http://schemas.android.com/apk/res/android"
    previewImage="@drawable/apps_edge"
    label="@string/app_name"
    launchOnClick="mainapp.MainActivity"
    description="Edge Panel Widget"
    cocktailWidth="550"
    configure="com.example.EdgeConfigure">
</cocktail-provider>
```
3. Check if the device supports the edge panel before calling edge panel code (e.g. updateEdgePanelData()) using the following code
```
public static boolean edgeIsSupported(Context context) {
    boolean edgeSupported = false;

    Slook slook = new Slook();
    try {
        slook.initialize(context);
    } catch (SsdkUnsupportedException e) {
        // Device not supported
        edgeSupported = false;
    }

    if (slook.isFeatureEnabled(Slook.COCKTAIL_PANEL)) {
        // COCKTAIL_PANEL specific to edge single plus
        edgeSupported = true;
    }

    return edgeSupported;
}

if (edgeIsSupported) { ... }
```
4. Add the necessary slook and edge panel sdk libraries to your project
```
compile files('libs/slook_v1.4.0.jar')
compile files('libs/sdk-v1.0.0.jar')
```

## In order to run on the Edge simulator
1. Create an Android virtual device running Android 24 (Nougat 7.0)
2. Install the edge_simulator-v1.4.0.apk file by running the command `adb install edge_simulator-v1.4.0.apk`
3. Add edge_simulator_v1.4.0.jar to your libs folder and replace the slook_v1.4.0.jar dependency with edge_simulator_v1.4.0.jar

For an application that uses ListViews (such as this one), you'll also have to do the following steps:

4. Add `android:sharedUserId="com.samsung.android.cocktailbar"` under the package declaration in your AndroidManifest file
5. Create a file `listview_simulator_config.xml` file with the following contents:
```
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="100dp"
    android:minHeight="100dp" />
```
6. Add the following lines to the EdgeWidgetProvider receiver entry in your AndroidManifest file:
```
<meta-data android:name="android.appwidget.provider"
                android:resource="@xml/listview_simulator_config"/>
<intent-filter>
    <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
</intent-filter>
```
7. Add the sim_test.jks file to your project directory
8. Run the following command: `adb shell appwidget grantbind --package com.samsung.android.cocktailbar --user 0`
9. Sign the project with the following credentials:
   * Key Alias: androiddebugkey
   * Key Password: android
   * Store File: sim_test.jks
   * Store Password: android

## In order to run on Edge phone
Simply remove everything related to the simulator, including the signing key. Make sure you reset the dependency to slook.jar instead of edge_simulator.jar

### Resources
More details can be found (here)[http://developer.samsung.com/galaxy/edge], including the jar files and the guides for (general edge programming)[http://developer.samsung.com/galaxy/edge/edge-guide] and for the (simulator)[http://developer.samsung.com/galaxy/edge/how-to-use-edge-simulator].
