package com.wxxr.nirvana.platform;

public interface INameStrategy {
	String getUniqueIdentifier(String namespace, String name);
	String getNamespaceFromIdentifier(String identifier);
	String getNameFromIdentifier(String identifier);
}
