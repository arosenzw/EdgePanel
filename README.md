# EdgeScreen
Preliminary code for the Edge app 

## In order to run on the Edge simulator
<ol>
<li>Create an Android virtual device running Android 24 (Nougat 7.0)</li>
<li>Install the edge_simulator-v1.4.0.apk file by running the command `adb install edge_simulator-v1.4.0.apk`</li>
<li>Add the edge_simulator_v1.4.0.jar file to the libs folder</li>
</ol>

Since this application uses ListView objects, you'll also have to do the following steps:
<li>Add `android:sharedUserId="com.samsung.android.cocktailbar"` under the package declaration in your AndroidManifest file</li>
<li>Add the sim_test.jks file to your project directory</li>
<li>Run the following command: `adb shell appwidget grantbind --package com.samsung.android.cocktailbar --user 0`</li>
<li>Sign the project with the following credentials:
    <ul>
        <li>Key Alias: androiddebugkey</li>
        <li>Key Password: android</li>
        <li>Store File: sim_test.jks</li>
        <li>Store Password: android</li>
    </ul>
</li>

## In order to run on Edge phone
Simply remove everything related to the simulator, including the signing key.