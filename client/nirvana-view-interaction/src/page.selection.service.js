/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2015-12-24 18:11:40
 * @version $Id$
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
