package com.wxxr.nirvana;

import com.wxxr.nirvana.inspinia.Column;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RowColumnsTest extends TestCase {

	private int size = 3;
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testrows(){
		int rs = call();
        Assert.assertEquals(1, rs);
        size = 4;
        rs = call();
        Assert.assertEquals(1, rs);
        size = 8;
        rs = call();
        Assert.assertEquals(2, rs);
        size = 9;
        rs = call();
        Assert.assertEquals(3, rs);
	}

	private int call(){
		boolean isDivisible = (size % Column.COUNT) == 0;
		int rs = 0;
        if (isDivisible) {
        	rs = size / Column.COUNT;
        } else {
        	rs = size / Column.COUNT + 1;
        }
        return rs;
	}
}
