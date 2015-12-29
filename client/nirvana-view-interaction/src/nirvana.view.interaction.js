/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2015-11-23 14:34:52
 * @version $Id$
 * 1、找到页面的page 和 view
 * 2、选中的view 是 selectionProvider 其他view作为listener，所以监听view的mousedown事件，谁是焦点谁是provider
 */



(function($) {
    'use strict';

    var SelectionProvider = function SelectionProvider_ctor() {
        var selected = null;
        var listeners = [];
        this.getSelection = function() {
            return selected;
        };
        this.setSelection = function(value) {
            selected = value;
        };
        this.addSelectionChangedListener = function(listener) {
            var index = _.indexOf(this.listeners, listener);
            if (index < 0) {
                listeners.push(listener);
            }
        };
        this.removeSelectionChangedListener = function(listener) {
           _.remove(listeners, function(o){
                return listener == o;
           });
        };

        this.fireSelectionChanged = function() {
            var e = new $.SelectionChangedEvent($.SelectionChangedEvent.TYPE_SELECTION_CHANGED, this, selected);
            for (var i in listeners) {
                listeners[i].apply(null, [e]);
            }
        };
        this.destroy = function(){
            selected = null;
            listeners = null;
        }

    }

    var Page = function Page_ctor(id) {
        this.id = id;
        this.views = [];
        this.description = "";
        this.pageSelectionService = new $.PageSelectionService();
    };

    Page.prototype.addView = function(view) {
        var index = _.findIndex(this.views, function(chr) {
            return chr.id == view.id;
        });
        if (index < 0) {
            this.views.push(view);
        }
    };

    Page.prototype.findViewById = function(id) {
        var index = _.findIndex(this.views, function(chr) {
            return chr.id == id;
        });
        if (index > -1) {
            return this.views[index];
        }
    };

    Page.prototype.findViews = function() {
        return this.views;
    };

    Page.prototype.removeView = function(view) {
        var index = _.findIndex(this.views, function(chr) {
            return chr.id == view.id;
        });
        if (index > -1) {
            _.pullAt(this.views, index);
            view.destroy();
        }
    };

    var View = function View_ctor(id) {
        this.id = id;
        this.description = "";
        this.provider = null;
        this.page = null;
        var that = this;
        this.dom = null;
        this.selectCallbackFn = function(e) {
        // console.debug("selectCallbackFn " + e.selection);
            if (e.part != null && e.part !== undefined && e.part['selectChanage'] !== undefined && e.part['selectChanage'] != null) {
                var func = that['selectChanage'];
                if(func !== undefined && func !== null){
                     func.apply(null, [e]);
                }
            }
        }

        this.selectCallbackFn['id'] = id;

    };

    View.prototype.destroy = function(){
        this.page.pageSelectionService.removeSelectionListener(this.selectCallbackFn);
        this.removeMouseDownListener();
        this.provider.destroy();
        this.page = null;
        this.provider = null;
        this.dom = null;
    };

    View.prototype.getSelectionProvider = function() {
        if (this.provider == null) {
            this.provider = new SelectionProvider();
        }
        return this.provider;
    };

    View.prototype.setSelection = function(selection) {
        var provider = this.getSelectionProvider();
        provider.setSelection(selection);
        provider.fireSelectionChanged();
        return this.provider;
    };

    View.prototype.addMouseDownListener = function() {
        var that = this;
        $(this.dom).on('mousedown', function(e) {
            console.debug('mouserdowon viewid ' + $(that.dom).attr('view'));
            that.page.pageSelectionService.setActivePart(that);
        });
    }

    View.prototype.removeMouseDownListener = function() {
        $(this.dom).off('mousedown');
    }

    var PageInit = function(el, options) {
        this.page = null;
        var $page = $(el);
        this.init = function() {
            if (this.page == null) {
                this.page = this.createPage($page);
            }
            return this;
        }
    };

    PageInit.prototype.createPage = function($page) {
        var pageId = $page.attr("page");
        var p = new Page(pageId);
        this.collectionView(p);
        return p;
    };

    PageInit.prototype.collectionView = function(pageIns) {
        var that = this;
        $('[view]').each(function() {
            var viewId = $(this).attr('view');
            var view = new View(viewId);
            if (pageIns != null && (typeof pageIns) !== 'undefined') {
                pageIns.addView(view);
                view.page = pageIns;
                var provider = view.getSelectionProvider();
                pageIns.pageSelectionService.addSelectionListener(view.selectCallbackFn);
                var ref = $(this).attr('focusRef');
                // that.addMouseDownListener($('#' + ref)); 
                view.dom = this;
                view.addMouseDownListener();
                $(this).data('view', view);
            }
        });
        var $lastview = $('[view]:last');
        var lastview = $lastview.data("view");
        pageIns.pageSelectionService.setActivePart(lastview);

    };

    

    PageInit.prototype.findViewById = function(viewId) {
        if (this.page != null) {
            return this.page.findViewById(viewId);
        }
    };

    PageInit.prototype.findViews = function() {
        if (this.page != null) {
            return this.page.findViews();
        }
    };

    $.fn.webpage = function() {

        if (this.length > 1) {
            console.error("page is only one");
            throw "page is only one";
        }

        this.each(function() {
            var $this = $(this);
            var pageIns = $this.data('page');
            $this.data('page', pageIns || new PageInit(this).init().page)
            var data = $this.data('page');
        });
    };

}(jQuery));
