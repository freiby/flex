/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2015-12-24 18:11:40
 * @version $Id$
   dependency lodash jquery
 *
 */

(function($) {
    function PageSelectionService() {
        this.partListeners = [];
        this.postPartListeners = [];
        this.activePart = null;
        this.activeProvider = null;
        this.eventDispatch = {};
        var that = this;
        var selListener = function(event) {
            var evt = event.clone();
            evt.part = that.activePart;
            that.dispatchEvent(evt);
        }

        this.addSelectionListener = function(listener, partId, nullSelection) {
            if (partId != null) {
                return;
            }
            for (var i in this.partListeners) {
                if (this.partListeners[i]["listener"] === listener) {
                    this.partListeners[i]["nullSelection"] = nullSelection;
                    return;
                }
            }
            var f = function(event) {
                // var m = arguments.callee["listener"]; //Function
                var m = listener;
                // var nullSel: = arguments.callee["nullSelection"];//Boolean
                var nullSel = nullSelection;
                if ((event.part && event.selection) || (nullSel)) {
                    m.apply(null, [event]);
                }
            }
            f["listener"] = listener;
            f["nullSelection"] = nullSelection;
            this.partListeners.push(f);
            this.addEventListener(SelectionChangedEvent.TYPE_SELECTION_CHANGED, f);
            this.addEventListener(SelectionChangedEvent.TYPE_DEACTIVE, f);
        }

        this.removeSelectionListener = function(listener, partId) {
            if (partId != null) {
                return;
            }
            var f = null;
            var length = this.partListeners.length;
            for (var i = 0; i < length; i++) {
                if (listener === this.partListeners[i]['listener']) {
                    f = this.partListeners[i];
                    break;
                }
            }
            if (f != null) {
                this.removeEventListener(SelectionChangedEvent.TYPE_SELECTION_CHANGED, f);
                this.removeEventListener(SelectionChangedEvent.TYPE_DEACTIVE, f);
                _.remove(this.partListeners, function(o) {
                    return o == f;
                });
            }
        };

        this.setActivePart = function(newPart) {
            if (newPart == this.activePart) {
                return;
            }

            var selectionProvider = null;

            if (newPart !== null && newPart['getSelectionProvider'] !== undefined) {
                selectionProvider = newPart.getSelectionProvider();

                if (selectionProvider == null) {
                    newPart = null;
                }
            }

            if (newPart == this.activePart) {
                return;
            }

            if (this.activePart != null) {
                if (this.activeProvider != null) {
                    this.activeProvider.removeSelectionChangedListener(selListener);
                    var sel1 = this.activeProvider.getSelection();
                    this.firedeactivePart(this.activePart, sel1);
                    this.activeProvider = null;
                }
                this.activePart = null;
            }

            this.activePart = newPart;

            if (newPart != null) {
                this.activeProvider = selectionProvider;
                // Fire an event if there's an active provider
                this.activeProvider.addSelectionChangedListener(selListener);
                var sel = this.activeProvider.getSelection();
                this.fireSelection(newPart, sel);
                // firePostSelection(newPart, sel);
            } else {
                this.fireSelection(null, null);
                // firePostSelection(null, null);
            }
        };

        this.fireSelection = function(part, selelected) {
            var evt = new SelectionChangedEvent(SelectionChangedEvent.TYPE_SELECTION_CHANGED, this.activeProvider, selelected);
            evt.part = part;
            this.dispatchEvent(evt);
        };

        this.firedeactivePart = function(part, selelected) {
            var evt = new SelectionChangedEvent(SelectionChangedEvent.TYPE_DEACTIVE, this.activeProvider, selelected);
            evt.part = part;
            this.dispatchEvent(evt);
        }

    }

    PageSelectionService.prototype.dispatchEvent = function(event) {
        if (event != null) {
            if (this.eventDispatch[event.type] !== undefined) {
                var listeners = this.eventDispatch[event.type];
                for (var i in listeners) {
                    listeners[i].apply(null, [event]);
                }

            }
        }
    };

    PageSelectionService.prototype.addEventListener = function(type, listener) {
        if (this.eventDispatch[type] === undefined) {
            var listeners = [];
            this.eventDispatch[type] = listeners;
            listeners.push(listener);
        } else {
            var lis = this.eventDispatch[type]
            var index = _.indexOf(lis, listener);
            if (index < 0) {
                lis.push(listener);
            }
        }
    };

    PageSelectionService.prototype.removeEventListener = function(type, listener) {
        if (this.eventDispatch[type] !== undefined) {
            var lis = this.eventDispatch[type];
            _.remove(lis, function(o) {
                return o == listener;
            });
        }
    };



    function SelectionChangedEvent(type, provider, selection) { //type:String,provider:ISelectionProvider,selection:Object,bubbles:Boolean=false, cancelable:Boolean=false
        this.provider = provider;
        this.selection = selection;
        this.type = type;
        //		private var _selection:ISelection;
        var _part; //IWorkbenchPart

        this.clone = function() {
            var evt = new SelectionChangedEvent(type, this.provider, this.selection);
            evt.part = this.part;
            return evt;
        }
    }

    SelectionChangedEvent.TYPE_SELECTION_CHANGED = "SelectionChanged";
    SelectionChangedEvent.TYPE_DEACTIVE = "deactive";

    $.PageSelectionService = PageSelectionService;
    $.SelectionChangedEvent = SelectionChangedEvent;
}(jQuery));

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
            if (e.part != null && e.part !== undefined && that['selectChanage'] !== undefined && that['selectChanage'] != null) {
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


$page = $("[page]");
$page.each(function(){
    $(this).webpage();
});
