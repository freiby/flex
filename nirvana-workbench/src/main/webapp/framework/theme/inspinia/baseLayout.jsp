<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><tiles:insertAttribute name="title" /></title>
    <link href="./framework/theme/inspinia/css/bootstrap.min.css" rel="stylesheet">
    <link href="./framework/theme/inspinia/css/font-awesome.css" rel="stylesheet">
    <!-- Toastr style -->
    <link href="./framework/theme/inspinia/css/toastr.min.css" rel="stylesheet">
    <link href="./framework/theme/inspinia/css/animate.css" rel="stylesheet">
    <link href="./framework/theme/inspinia/css/style.css" rel="stylesheet">
    <tiles:insertAttribute name="resourcejsheader" />
    
</head>

<body class="pace-done">

	<div id="wrapper">
		<tiles:insertAttribute name="menu" />
		<tiles:insertAttribute name="content" />
	</div>
	<div class="theme-config"><!--  right config panel -->
        <div class="theme-config-box">
            <div class="spin-icon">
                <i class="fa fa-cogs fa-spin"></i>
            </div>
            <div class="skin-setttings">
                <div class="title">Configuration</div>
                <div class="setings-item">
                    <span>
                        Collapse menu
                    </span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="collapsemenu">
                            <label class="onoffswitch-label" for="collapsemenu">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="setings-item">
                    <span>
                        Fixed sidebar
                    </span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="fixedsidebar" class="onoffswitch-checkbox" id="fixedsidebar">
                            <label class="onoffswitch-label" for="fixedsidebar">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="setings-item">
                    <span>
                        Top navbar
                    </span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="fixednavbar" class="onoffswitch-checkbox" id="fixednavbar">
                            <label class="onoffswitch-label" for="fixednavbar">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="setings-item">
                    <span>
                        Boxed layout
                    </span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="boxedlayout" class="onoffswitch-checkbox" id="boxedlayout">
                            <label class="onoffswitch-label" for="boxedlayout">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="setings-item">
                    <span>
                        Fixed footer
                    </span>
                    <div class="switch">
                        <div class="onoffswitch">
                            <input type="checkbox" name="fixedfooter" class="onoffswitch-checkbox" id="fixedfooter">
                            <label class="onoffswitch-label" for="fixedfooter">
                                <span class="onoffswitch-inner"></span>
                                <span class="onoffswitch-switch"></span>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="title">Skins</div>
                <div class="setings-item default-skin">
                    <span class="skin-name ">
                         <a href="http://webapplayers.com/inspinia_admin-v2.3/index.html#" class="s-skin-0">
                             Default
                         </a>
                    </span>
                </div>
                <div class="setings-item blue-skin">
                    <span class="skin-name ">
                        <a href="http://webapplayers.com/inspinia_admin-v2.3/index.html#" class="s-skin-1">
                            Blue light
                        </a>
                    </span>
                </div>
                <div class="setings-item yellow-skin">
                    <span class="skin-name ">
                        <a href="http://webapplayers.com/inspinia_admin-v2.3/index.html#" class="s-skin-3">
                            Yellow/Purple
                        </a>
                    </span>
                </div>
                <div class="setings-item ultra-skin">
                    <span class="skin-name ">
                        <a target="_blank" href="http://webapplayers.com/inspinia_admin-v2.3/md_skin/" class="md-skin">
                            Material Design
                        </a>
                    </span>
                </div>
            </div>
        </div>
    </div>
    <!-- Mainly scripts -->
    <script src="./framework/theme/inspinia/js/jquery-2.1.1.js"></script>
    <script src="./framework/theme/inspinia/js/bootstrap.min.js"></script>
    <script src="./framework/theme/inspinia/js/jquery.metisMenu.js"></script>
    <script src="./framework/theme/inspinia/js/jquery.slimscroll.min.js"></script>
    <!-- Custom and plugin javascript -->
    <script src="./framework/theme/inspinia/js/inspinia.js"></script>
    <!-- jQuery UI -->
    <script src="./framework/theme/inspinia/js/jquery-ui.min.js"></script>
    <!-- Toastr -->
    <script src="./framework/theme/inspinia/js/toastr.min.js"></script>
    <script src="./framework/theme/inspinia/js/embeddedjs1.js"></script>
    <script src="./framework/theme/inspinia/js/embeddedjs2.js"></script>

    
    <script src="./framework/theme/inspinia/js/embeddedjs3.js"></script>


    <!-- Flot -->
    <script src="./framework/theme/inspinia/js/jquery.flot.js"></script>
    <script src="./framework/theme/inspinia/js/jquery.flot.tooltip.min.js"></script>
    <script src="./framework/theme/inspinia/js/jquery.flot.spline.js"></script>
    <script src="./framework/theme/inspinia/js/jquery.flot.resize.js"></script>
    <script src="./framework/theme/inspinia/js/jquery.flot.pie.js"></script>


    <!-- GITTER -->
    <!-- // <script src="./framework/theme/inspinia/js/jquery.gritter.min.js"></script> -->
    <!-- Sparkline -->
    <script src="./framework/theme/inspinia/js/jquery.sparkline.min.js"></script>
    <!-- Sparkline demo data  -->
    <script src="./framework/theme/inspinia/js/sparkline-demo.js"></script>
    <!-- ChartJS-->
    <!-- // <script src="./framework/theme/inspinia/js/Chart.min.js"></script> -->
    <tiles:insertAttribute name="resourcejsfooter" />
</body>
</html>