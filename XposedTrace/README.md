XposedTrace is a tool for adding method and debug trace to classes. The output goes to logcat. 

The amount of output can be configured through the application settings.

The output will be written to logcat. Here a few examples on how to retrieve it:
+ `adb logcat | grep XposedTrace` will show all trace info
+ `adb logcat | grep XposedTrace.android.location.LocationManager` will show all trace info for calls to LocationManager
+ `adb logcat | grep XposedTrace.android.app.AlarmManager.set` will show all trace info for AlarmManager.set

+ `adb logcat | grep /LocationManager` will show the debug log of LocationManager

**Credits**
This project makes use of the Xposed Framework (https://github.com/rovo89/XposedBridge)will be written to logcat
