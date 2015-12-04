<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<tiles:importResources name="jslist" type="js" classname="java.util.List" position="footer"/>

<s:iterator value="#attr.jslist" status="item" var="jsitem">
   <script type="text/javascript" src="<%=request.getContextPath()%><s:property value='jsitem'/>"></script>
</s:iterator>
<%--=pageContext.getAttribute("jslist")--%>