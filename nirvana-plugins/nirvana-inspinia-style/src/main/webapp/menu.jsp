<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/nirvana-tag" prefix="nirvana"%>

<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="dropdown profile-element"> <span>
                    <img alt="image" class="img-circle" src="./framework/theme/inspinia/img/profile_small.jpg">
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
            <li class="active">
                <a href="./INSPINIA _ Dashboard_files/INSPINIA _ Dashboard.html"><i class="fa fa-th-large"></i> <span class="nav-label">Dashboards</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse in">
                    <li class="active"><a href="./INSPINIA _ Dashboard_files/INSPINIA _ Dashboard.html">Dashboard v.1</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/dashboard_2.html">Dashboard v.2</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/dashboard_3.html">Dashboard v.3</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/dashboard_4_1.html">Dashboard v.4</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/dashboard_5.html">Dashboard v.5 <span class="label label-primary pull-right">NEW</span></a></li>
                </ul>
            </li>
            <li>
                <a href="http://webapplayers.com/inspinia_admin-v2.3/layouts.html"><i class="fa fa-diamond"></i> <span class="nav-label">Layouts</span></a>
            </li>
            <li>
                <a href="#"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">Graphs</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_flot.html">Flot Charts</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_morris.html">Morris.js Charts</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_rickshaw.html">Rickshaw Charts</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_chartjs.html">Chart.js</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_chartist.html">Chartist</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_peity.html">Peity Charts</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/graph_sparkline.html">Sparkline Charts</a></li>
                </ul>
            </li>
            <li>
                <a href="http://webapplayers.com/inspinia_admin-v2.3/mailbox.html"><i class="fa fa-envelope"></i> <span class="nav-label">Mailbox </span><span class="label label-warning pull-right">16/24</span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/mailbox.html">Inbox</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/mail_detail.html">Email view</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/mail_compose.html">Compose email</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/email_template.html">Email templates</a></li>
                </ul>
            </li>
            <li>
                <a href="http://webapplayers.com/inspinia_admin-v2.3/metrics.html"><i class="fa fa-pie-chart"></i> <span class="nav-label">Metrics</span>  </a>
            </li>
            <li>
                <a href="http://webapplayers.com/inspinia_admin-v2.3/widgets.html"><i class="fa fa-flask"></i> <span class="nav-label">Widgets</span></a>
            </li>
            <li>
                <a href="#"><i class="fa fa-edit"></i> <span class="nav-label">Forms</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/form_basic.html">Basic form</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/form_advanced.html">Advanced Plugins</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/form_wizard.html">Wizard</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/form_file_upload.html">File Upload</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/form_editors.html">Text Editor</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-desktop"></i> <span class="nav-label">App Views</span>  <span class="pull-right label label-primary">SPECIAL</span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/contacts.html">Contacts</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/profile.html">Profile</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/profile_2.html">Profile v.2</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/contacts_2.html">Contacts v.2</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/projects.html">Projects</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/project_detail.html">Project detail</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/teams_board.html">Teams board</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/social_feed.html">Social feed</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/clients.html">Clients</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/full_height.html">Outlook view</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/vote_list.html">Vote list</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/file_manager.html">File manager</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/calendar.html">Calendar</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/issue_tracker.html">Issue tracker</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/blog.html">Blog</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/article.html">Article</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/faq.html">FAQ</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/timeline.html">Timeline</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/pin_board.html">Pin board</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-files-o"></i> <span class="nav-label">Other Pages</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/search_results.html">Search results</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/lockscreen.html">Lockscreen</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/invoice.html">Invoice</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/login.html">Login</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/login_two_columns.html">Login v.2</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/forgot_password.html">Forget password</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/register.html">Register</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/404.html">404 Page</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/500.html">500 Page</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/empty_page.html">Empty page</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-globe"></i> <span class="nav-label">Miscellaneous</span><span class="label label-info pull-right">NEW</span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/toastr_notifications.html">Notification</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/nestable_list.html">Nestable list</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/agile_board.html">Agile board</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/timeline_2.html">Timeline v.2</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/diff.html">Diff</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/sweetalert.html">Sweet alert</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/idle_timer.html">Idle timer</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/spinners.html">Spinners</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/tinycon.html">Live favicon</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/google_maps.html">Google maps</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/code_editor.html">Code editor</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/modal_window.html">Modal window</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/forum_main.html">Forum view</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/validation.html">Validation</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/tree_view.html">Tree view</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/chat_view.html">Chat view</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/masonry.html">Masonry</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-flask"></i> <span class="nav-label">UI Elements</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/typography.html">Typography</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/icons.html">Icons</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/draggable_panels.html">Draggable Panels</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/buttons.html">Buttons</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/video.html">Video</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/tabs_panels.html">Panels</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/tabs.html">Tabs</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/notifications.html">Notifications &amp; Tooltips</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/badges_labels.html">Badges, Labels, Progress</a></li>
                </ul>
            </li>
            <li>
                <a href="http://webapplayers.com/inspinia_admin-v2.3/grid_options.html"><i class="fa fa-laptop"></i> <span class="nav-label">Grid options</span></a>
            </li>
            <li>
                <a href="#"><i class="fa fa-table"></i> <span class="nav-label">Tables</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/table_basic.html">Static Tables</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/table_data_tables.html">Data Tables</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/table_foo_table.html">Foo Tables</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/jq_grid.html">jqGrid</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-shopping-cart"></i> <span class="nav-label">E-commerce</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/ecommerce_products_grid.html">Products grid</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/ecommerce_product_list.html">Products list</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/ecommerce_product.html">Product edit</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/ecommerce_product_detail.html">Product detail</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/ecommerce-orders.html">Orders</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/ecommerce_payments.html">Credit Card form</a></li>
                </ul>
            </li>
            <li>
                <a href="#"><i class="fa fa-picture-o"></i> <span class="nav-label">Gallery</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level collapse">
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/basic_gallery.html">Lightbox Gallery</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/slick_carousel.html">Slick Carousel</a></li>
                    <li><a href="http://webapplayers.com/inspinia_admin-v2.3/carousel.html">Bootstrap Carousel</a></li>
                </ul>
            </li>
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
</nav>