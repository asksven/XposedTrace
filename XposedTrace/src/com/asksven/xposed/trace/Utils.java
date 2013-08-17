/**
 * 
 */
package com.asksven.xposed.trace;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.util.Log;

/**
 * @author sven
 *
 */
public class Utils
{

	public static String getSpeakingAlarmType(int alarmType)
	{
		String alarmTypeSpeaking = "";
		switch (alarmType)
		{
			case AlarmManager.ELAPSED_REALTIME:
				alarmTypeSpeaking = "ELAPSED_REALTIME";
				break;
			case AlarmManager.ELAPSED_REALTIME_WAKEUP:
				alarmTypeSpeaking = "ELAPSED_REALTIME_WAKEUP";
				break;
			default:
				alarmTypeSpeaking = "unknown (" + alarmType + ")";
		}
		
		return alarmTypeSpeaking;

	}
	
	public static String getSpeakingPendingIntent(PendingIntent intent)
	{
		// we are interested in the private field target
		
		return "PendingIntent: " 
				+ intent.getTargetPackage() 
//				+ " " + intent.getIntent() Sender().getTargetPackage() 
				+ " " + intent.toString();
	}
	
	public static void logMethodCall(MethodHookParam param)
	{

		String args = "";
		if (param == null)
		{
			Log.i(XposedHooks.TAG,"  " + "native call");
			return;
		}
		
		for (int i=0; i < param.args.length; i++)
		{
			if (param.args[i] != null)
			{
				args += "[" + i + "] : " + param.args[i].toString() + " ";
			}
		}
		String className = "";
		if (param.thisObject != null) className = param.thisObject.getClass().getCanonicalName();
		
		String methodName = "";
		if (param.method != null) methodName = param.method.getName();
		
		Log.i(XposedHooks.TAG,
				className 
				+ "." 
				+ methodName + " was called with arguments " + args);
		// log the callstack 
		Log.i(XposedHooks.TAG,"Callstack");
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for (int i=6; i < trace.length; i++)
		{
			Log.i(XposedHooks.TAG,"  " + trace[i].getClassName() + " " + trace[i].getMethodName());
		}
	}
}
