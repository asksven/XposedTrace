/**
 * 
 */
package com.asksven.xposed.trace;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedHelpers;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XC_MethodHook;
/**
 * @author sven
 *
 */
public class XposedHooks implements IXposedHookLoadPackage
{
	public static final String TAG = "XposedTest";
	
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable
	{
		
		XSharedPreferences sharedPrefs = new XSharedPreferences(lpparam.appInfo.packageName);
//		XposedBridge.log("Loaded app: " + lpparam.packageName);
		
		if (!lpparam.packageName.equals("android"))
			return;
		
		// AlarmManager
		boolean doLog = sharedPrefs.getBoolean("trace_alarm_manager", false);
		if (doLog)
		{
			traceClass("android.app.AlarmManager", lpparam);
		}
		
		// AlarmManagerService debug log
		doLog = sharedPrefs.getBoolean("trace_alarm_manager_service", false);
		if (doLog)
		{
			systemLogforClass("com.android.server.AlarmManagerService", "localLOGV", lpparam);
		}

		// AlarmManagerService debug l
		doLog = sharedPrefs.getBoolean("trace_location_manager_service", false);
		if (doLog)
		{
			systemLogforClass("com.android.server.LocationManagerService", "D", lpparam);
		}
		// LocationManager
		doLog = sharedPrefs.getBoolean("trace_location_manager", false);
		if (doLog)
		{
			traceClass("android.location.LocationManager", lpparam);
		}
	}
	
	/**
	 * Add a trace for each mthod call fof class className
	 * @param className the name of the class
	 * @param lpparam the package parameters @see LoadPackageParams
	 * @return true if all methods could be hooked
	 */
	private boolean traceClass(String className, LoadPackageParam lpparam)
	{
		boolean ret = true;
		Class<?> classForTrace = XposedHelpers.findClass(className, lpparam.classLoader);
		if (classForTrace != null)
		{
			XposedBridge.log(classForTrace.getName() + " found in " + lpparam.packageName);
			try
			{
				// hook all methods
				for (Member method : classForTrace.getDeclaredMethods())
				{
					XposedBridge.hookMethod(method, new XC_MethodHook()
					{
						@Override
						protected void afterHookedMethod(MethodHookParam param) throws Throwable
						{
							Utils.logMethodCall(param);
						}
					});
					Log.d(TAG, "method " + method + " hooked");
				}
			}
			catch (NoSuchMethodError e)
			{
				Log.d(TAG, "some method was not found.");
				ret = false;
			}

		}
		else
		{
			Log.d(TAG, classForTrace.getName() + " not found in " + lpparam.packageName);
		}
		
		return ret;
	}
	

	/**
	 * Enables the default debug log (if given) for a class by setting the private static field controlling
	 * debugging to true (see source of the class to find the field name. This is done after the cctor was called 
	 * @param className the name of the class
	 * @param fieldName the name of the field controlling debug logging
	 * @param lpparam lpparam the package parameters @see LoadPackageParams
	 * @return true if the hook could be placed
	 */
	private boolean systemLogforClass(String className, final String fieldName, LoadPackageParam lpparam)
	{
		boolean ret = true;
		Class<?> classForDebugLog = XposedHelpers.findClass(className, lpparam.classLoader);
		if (classForDebugLog != null)
		{
			Log.d(TAG, classForDebugLog.getName() + " found in " + lpparam.packageName);
			try
			{
				Constructor<?> cctor = XposedHelpers.findConstructorExact(classForDebugLog, Context.class);
				
				XposedBridge.hookMethod(cctor, new XC_MethodHook()
				{
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable
					{
						XposedHelpers.setBooleanField(param.thisObject, fieldName, true);
						Log.d(TAG, fieldName + " set to " + XposedHelpers.getBooleanField(param.thisObject, fieldName));
						Utils.logMethodCall(param);
					}
				});
				Log.d(TAG, "constructor hooked");

			}
			catch (NoSuchMethodError e)
			{
				Log.d(TAG, "some method was not found.");
				ret = false;
			}

		}
		else
		{
			Log.d(TAG, className + " not found in " + lpparam.packageName);
			ret = false;
		}
		
		return ret;
	}

}
