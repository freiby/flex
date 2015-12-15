<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/nirvana-tag" prefix="nirvana"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar-default navbar-static-side" role="navigation"><!-- start nav -->
    <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="dropdown profile-element"> <span>
                    <img alt="image" class="img-circle" src="${path}/img/profile_small.jpg">
                     </span>
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">David Williams</strong>
                     </span> <span class="text-muted text-xs block">Art Director <b class="caret"></b></span> </span>
                    </a>
                    <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a href="http://webapplayers.com/inspinia_admin-v2.3/profile.html">Profile</a></li>
                        <li><a href="http://webapplayers.com/inspinia_admin-v2.3/contacts.html">Contacts</a></li>
                        <li><a href="http://webapplayers.com/inspinia_admin-v2.3/mailbox.html">Mailbox</a></li>
                        <li class="divider"></li>
                        <li><a href="http://webapplayers.com/inspinia_admin-v2.3/login.html">Logout</a></li>
                    </ul>
                </div>
                <div class="logo-element">
                    IN+
                </div>
            </li>
             <c:forEach var="page" items="${pages}" >
                <c:choose>
                    <c:when test="${page.active}">
                        <li class="active">
                            <a href="${page.link}"><i class="fa fa-th-large"></i> <span class="nav-label">${page.name}</span> </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li >
                            <a href="${page.link}"><i class="fa fa-th-large"></i> <span class="nav-label">${page.name}</span> </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            
            <li>
                <a href="#"><i class="fa fa-sitemap"></i> <span class="nav-label">Menu Levels </span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li>
                        <a href="#">Third Level <span class="fa arrow"></span></a>
                        <ul class="nav nav-third-level collapse">
                            <li>
                                <a href="#">Third Level Item</a>
                            </li>
                            <li>
                                <a href="#">Third Level Item</a>
                            </li>
                            <li>
                                <a href="#">Third Level Item</a>
                            </li>
                        </ul>
                    </li>
                    <li><a href="#">Second Level Item</a></li>
                    <li>
                        <a href="#">Second Level Item</a></li>
                    <li>
                        <a href="#">Second Level Item</a></li>
                </ul>
            </li>
            <li>
                <a href="http://webapplayers.com/inspinia_admin-v2.3/css_animation.html"><i class="fa fa-magic"></i> <span class="nav-label">CSS Animations </span><span class="label label-info pull-right">62</span></a>
            </li>
            <li class="landing_link">
                <a target="_blank" href="http://webapplayers.com/inspinia_admin-v2.3/landing.html"><i class="fa fa-star"></i> <span class="nav-label">Landing Page</span> <span class="label label-warning pull-right">NEW</span></a>
            </li>
            <li class="special_link">
                <a href="http://webapplayers.com/inspinia_admin-v2.3/package.html"><i class="fa fa-database"></i> <span class="nav-label">Package</span></a>
            </li>
        </ul>
    </div>
</nav><!-- end nav -->