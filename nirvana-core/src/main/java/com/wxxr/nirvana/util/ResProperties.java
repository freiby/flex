package com.wxxr.nirvana.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;


public class ResProperties extends HashMap {

	private static final String WHITESPACES = " \t\r\f\n";
	private static final String SEPERATOR = "<-->";
	private static final String DEFAULT_CHARACTERSET = "ISO-8859-1";
	
	private String seperator = SEPERATOR;
	/**
	 * Constructor for ResProperties.
	 */
	public ResProperties() {
		super();
	}

	/**
	 * Constructor for ResProperties.
	 * @param arg0
	 */
	public ResProperties(Properties arg0) {
		super(arg0);
	}
	public synchronized void load(InputStream in) throws IOException {
		load(in,null);
	}
	/**
	 * @see java.util.Properties#load(java.io.InputStream)
	 */
	public synchronized void load(InputStream in, String characterset) throws IOException {
		if(characterset == null)
			characterset = DEFAULT_CHARACTERSET;
		BufferedReader bufferedreader =
			new BufferedReader(new InputStreamReader(in, characterset));
		String s;	
		while((s = readLine(bufferedreader)) != null){
			int i = s.indexOf(seperator);
			if(i != -1) {
				String key = trim(s.substring(0,i));
				String val = trim(s.substring(i+seperator.length()));
				if((key != null) && (key.length() > 0))
					put(key,val);
			}
		}
	}
	private boolean continueLine(String s) {
		int i = 0;
		for (int j = s.length() - 1; j >= 0 && s.charAt(j--) == '\\';)
			i++;

		return i % 2 == 1;
	}
	
	private String readLine(BufferedReader bread) throws IOException{
		StringBuffer buf = null;
		String s = null;
		while(true) {
			s=bread.readLine();
			if(s == null)
				return null;
			s = trim(s);
			if(s.length() == 0) {
				if(buf == null)
					continue;
				else
					break;
			}
			if(continueLine(s)) {
				if(buf == null)	
					buf = new StringBuffer(s);
				else
					buf.append(s);
				buf.setCharAt(buf.length()-1,' ');
			} else {
				break;
			}
		}
		if(buf == null)
			return s;
		else
			return (s != null) ? buf.append(s).toString() : buf.toString();
	}
	
	private String trim(String s) {
		if(s == null)
			return null;
		int len = s.length();
		int i=0,k=len-1;
		for(;i < len ;i++ ) {
			if(WHITESPACES.indexOf(s.charAt(i)) == -1)
				break;
		}
		for(; k >= 0 ; k--){
			if(WHITESPACES.indexOf(s.charAt(k)) == -1)
				break;
		}
		if( k > i)
			return s.substring(i,k+1);
		else
			return "";
	}
	public void save(OutputStream out) throws IOException{
		save(out,null,null);
	}
	
	public void save(OutputStream out, String title) throws IOException{
		save(out,title,null);
	}	
	
	public void save(OutputStream out, String title, String characterset) throws IOException {
		if(characterset == null)
			characterset = DEFAULT_CHARACTERSET;
		BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(out,characterset));
		if(title != null)	{
			bwriter.write("#");
			bwriter.newLine();
			bwriter.write("#"+title);
			bwriter.newLine();
			bwriter.write("#");
			bwriter.newLine();
			bwriter.newLine();
		}
		for(Iterator itr = keySet().iterator() ; itr.hasNext() ; ) {
			String key = (String)itr.next();
			if(key == null)
				continue;
			String val = (String)get(key);
			bwriter.write(key);
			bwriter.write("\t"+seperator+"\t");
			if(val != null)	
				bwriter.write(val);
			bwriter.newLine();
		}
		bwriter.flush();
	}
	
	public void addProperty(String name, String value) {
		if(name == null)
			return;
		name = trim(name);
		if(name.length() == 0)
			return;
		value = trim(value);
		put(name,value);
	}
	
	public String removeProperty(String name) {
		if(name == null)
			return null;
		name = trim(name);
		if(name.length() == 0)
			return null;
		return (String)remove(name);
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}
}
