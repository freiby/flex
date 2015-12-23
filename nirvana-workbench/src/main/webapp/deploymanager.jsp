<%@ page language="java" contentType="text/html; charset=utf-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %> 
<!DOCTYPE html>
<html> 
<head> 
<title>部署页面</title> 
</head> 
<body> 
	<style type="text/css">
		.border{
			border: solid 1px red;

		}
		.undeploy{
			margin-top: 10px;
		}
	</style>
<div class="deploy border">
	<s:form action="deploy" method="post"> 
	    <s:textfield name="url" label="url"/> 
	    <s:submit/> 
	</s:form> 
</div>


<div class="undeploy border">
	<s:form action="undeploy" method="post"> 
	    <s:textfield name="id" label="id"/> 
	    <s:textfield name="version" label="version"/> 
	    <s:submit/> 
	</s:form> 
</div>
</body> 
</html> 
