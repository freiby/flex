/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2015-11-23 14:34:52
 * @version $Id$
 */



+ function($) {
    'use strict';

    var Page = function Page_ctor(name) {
        this.name = name;
        this.views = [];
        this.description = "";
    };

    Page.prototype.addView = function(view) {
        var index = _.findIndex(this.views, function(chr) {
            return chr.name == view.name;
        });
        if (index < 0) {
            this.views.push(view);
        }
    };

    Page.prototype.findViewByName = function(name) {
        var index = _.findIndex(this.views, function(chr) {
            return chr.name == name;
        });
        if (index < 0) {
            return this.views[index];
        }
    };

    Page.prototype.findViews = function() {
        return this.views;
    };

    Page.prototype.removeView = function(view) {
        var index = _.findIndex(this.views, function(chr) {
            return chr.name == view.name;
        });

        if (index > 0) {
            _.pullAt(this.views, index);
        }
    };

    var View = function View_ctor(name) {
        this.name = name;
        this.description = "";
    };

    var PageInit = function(el, options) {
        this.page = null;
        var $page = $(el);
        this.init = function() {
            if (this.page == null) {
                this.createPage();
            }
        }
    };

    PageInit.prototype.createPage = function($page) {
        var pName = $page.attr("pageName");
        Page p = new Page(pName);
        this.collectionView(p);
    };

    PageInit.prototype.collectionView = function(pageIns) {
        $('.View').each(function() {
            var viewName = $('.View').attr('viewName');
            View view = new View(viewName);
            if (pageIns != null && (typeof pageIns) !== 'undefined') {
                pageIns.push(view);
            }
        });
    };

    PageInit.prototype.findViewByName = function(viewName) {
        if (this.page != null) {
            return this.page.findViewByName(viewName);
        };
    };

    PageInit.prototype.findViews = function() {
        if (this.page != null) {
            return this.page.findViews();
        };
    };

    $.fn.webSiteBootstrap = function() {
        this.each(function() {
            if (this.length > 1) {
                console.error("page is only one");
                throw "page is only one";
            }
            var $this = $(this);
            var pageIns = $this.data('page');
            $this.data('page', pageIns || new PageInit(this, options))
            var data = this$.data('page');
            data.init();
        });
    };

}(jQuery);
