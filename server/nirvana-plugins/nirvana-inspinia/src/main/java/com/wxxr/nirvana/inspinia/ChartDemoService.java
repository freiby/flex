package com.wxxr.nirvana.inspinia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.wxxr.nirvana.json.annotations.SMDMethod;

public class ChartDemoService {
	private List<Integer> datas = new ArrayList<Integer>();
	private int length = 10;
	Random r = new Random(System.currentTimeMillis());
	@SMDMethod
	public List<Integer> getDataList(String type){
		datas.clear();
		for(int i = 0; i<length; i++){
			datas.add(getRandom());
		}
		return datas;
	}
	
	public Integer getRandom(){
		return r.nextInt(100);
	}
}
