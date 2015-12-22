<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/nirvana-tag" prefix="nirvana"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fliud page-container">
	<c:forEach var="row" items="${rows}" >
		<div class="row ">
			<c:forEach var="column" items="${row.columns}" >
			    <div class="col-md-4">
			    	<jsp:include page=”<%=column.getViewUrl()%>”  flush="false">
			    </div>
			</c:forEach>
		</div>
	</c:forEach>
</div>