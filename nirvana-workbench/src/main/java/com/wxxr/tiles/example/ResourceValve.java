package com.wxxr.tiles.example;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;


public class ResourceValve extends ValveBase {

	@Override
	public void invoke(Request arg0, Response arg1) throws IOException,
			ServletException {
		 getNext().invoke(arg0, arg1);
		 System.out.println("okokokok" + arg0.getContextPath() +  arg0.getRequestURI());
	}

}
