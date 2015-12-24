package com.wxxr.nirvana.workbench;

public interface IProductManager {
	IProduct[] getAllProducts();

	IProduct getProductById(String id);

	IProduct getProductByName(String name);

	// IProduct getCurrentProduct();
	void start();

}
