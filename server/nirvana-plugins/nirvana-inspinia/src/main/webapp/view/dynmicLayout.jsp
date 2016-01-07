<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/nirvana-tag" prefix="nirvana"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="page-wrapper" class="gray-bg dashbard-1" style="min-height: 100%;" page="${pageId}">
	<c:forEach var="row" items="${rows}" >
		<div class="row ">
			<c:forEach var="column" items="${row.columns}" >
			    <div class="col-md-4">
			    	<div view="${column.viewId}">
			    		<jsp:include page="${column.viewUrl}"  flush="false"/>
			    	</div>
			    </div>
			</c:forEach>
		</div>
	</c:forEach>


	<form action="doPage.action" method="post"> 
		<label for="id">actionId</label>
	    <input id="id" type="text" name="actionId" value="com.wxxr.nirvana.test.addViewAction" />
	    <div></div>
	    <label for="page">toPage</label> 
	    <input id="page" type="text" name="toPage" value="com.wxxr.nirvana.test.lionPage" /> 
	    <div></div>
	    <label for="method">method</label> 
	    <input id="method" type="text" name="method" value="addView" /> 
	    <div></div>
	    <input type="submit" style="width:100px" value="commit"></input>
    <form/>

</div>
