package com.wxxr.nirvana.context;


public interface NirvanaContext {
    
	Object getApplicationValue(String key);
    String getHeaderValue(String key);
    Object getParameterValue(String key);
    Object getSessionValue(String key);
    
    void setAppicationValue(String key, Object value);
    void setSessionValue(String key, Object value);
    void setHeaderValue(String key, String value);
    
}
