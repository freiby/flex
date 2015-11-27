package com.wxxr.nirvana.util;

import java.util.Collection;
import java.util.Iterator;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IExtensionDelta;
import com.wxxr.nirvana.platform.IExtensionPoint;

public class ArrayHelper {
	
	private ArrayHelper(){
		
	}
	
	public static IConfigurationElement[] toConfigurationElements(Collection vals) {
		int size = vals.size(),cnt = 0;
		IConfigurationElement[] points = new IConfigurationElement[size];
		
		for(Iterator itr = vals.iterator(); itr.hasNext();){
			IConfigurationElement p = (IConfigurationElement)itr.next();
			if(p != null){
				points[cnt] = p;
				cnt++;
			}
		}
		return points;
	}

	/**
	 * @param vals
	 * @return
	 */
	public static IExtensionDelta[] toExtensionDeltas(Collection vals) {
		int size = vals.size(),cnt = 0;
		IExtensionDelta[] points = new IExtensionDelta[size];
		
		for(Iterator itr = vals.iterator(); itr.hasNext();){
			IExtensionDelta p = (IExtensionDelta)itr.next();
			if(p != null){
				points[cnt] = p;
				cnt++;
			}
		}
		return points;
	}


	/**
	 * @param vals
	 * @return
	 */
	public static IExtensionPoint[] toExtensionPoints(Collection vals) {
		int size = vals.size(),cnt = 0;
		IExtensionPoint[] points = new IExtensionPoint[size];
		
		for(Iterator itr = vals.iterator(); itr.hasNext();){
			IExtensionPoint p = (IExtensionPoint)itr.next();
			if(p != null){
				points[cnt] = p;
				cnt++;
			}
		}
		return points;
	}
	
	/**
	 * @param vals
	 * @return
	 */
	public static IExtension[] toExtensions(Collection vals) {
		int size = vals.size(),cnt = 0;
		IExtension[] extensions = new IExtension[size];
		
		for(Iterator itr = vals.iterator(); itr.hasNext();){
			IExtension p = (IExtension)itr.next();
			if(p != null){
				extensions[cnt] = p;
				cnt++;
			}
		}
		return extensions;
	}
	
	/**
	 * @param vals
	 * @return
	 */
	public static String[] toStrings(Collection vals) {
		int size = vals.size(),cnt = 0;
		String[] strings = new String[size];
		
		for(Iterator itr = vals.iterator(); itr.hasNext();){
			String s = (String)itr.next();
			if(s != null){
				strings[cnt] = s;
				cnt++;
			}
		}
		return strings;
	}


}
