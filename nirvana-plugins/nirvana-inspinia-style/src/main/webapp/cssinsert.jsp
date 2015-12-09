<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<tiles:importResources name="jslist" type="js" />
<c:forEach var="item" items="${jslist}">
	<script type="text/javascript" src="${item}"></script>
</c:forEach>