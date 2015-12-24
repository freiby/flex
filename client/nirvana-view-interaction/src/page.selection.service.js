/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2015-12-24 18:11:40
 * @version $Id$
 *
 */

(function($) {
    function PageSelectionService() {
        var partListeners = [];
        var postPartListeners = [];
        var activePart;
        var activeProvider;
        var selListener = function(event) {
            var evt = event.clone();
            evt.part = activePart;
            this.dispatchEvent(evt);
        }

        this.addSelectionListener = function(listener, partId, nullSelection){
        	if(partId != null) {
				return;
			}
			foreach(method in partListeners) {
				if(method["listener"] == listener){
					method["nullSelection"] = nullSelection;
					return;
				}
			}
			var f = function(event) {
				// var m = arguments.callee["listener"]; //Function
				var m = listener;
				// var nullSel: = arguments.callee["nullSelection"];//Boolean
				var nullSel = nullSelection;
				if((event.part && event.selection)||(nullSel)){
					m.apply(null,[event]);
				}
			}
			// f["listener"] = listener;
			// f["nullSelection"] = nullSelection;
			partListeners.push(f);
			this.addEventListener(SelectionChangedEvent.TYPE_SELECTION_CHANGED,f);
			this.addEventListener(SelectionChangedEvent.TYPE_DEACTIVE,f);
        }

        this.removeSelectionListener = function(listener, partId){
        	if(partId != null) {
				return;
			}
			var f = null;
			var length = partListeners.length;
			for(var i=0; i<length; i++){
				if(method["listener"] == partListeners[i]){
					f = method;
					break;
				}
			}
			if(f != null) { 
				this.removeEventListener(SelectionChangedEvent.TYPE_SELECTION_CHANGED,f);
				this.removeEventListener(SelectionChangedEvent.TYPE_DEACTIVE,f);
				_.remove(partListeners,f);
			}
        };

        this.setActivePart = function(newPart){

        };

        this.fireSelection = function(part,selelected){

        }

    }

    PageSelectionService.prototype.dispatchEvent = function(event) {
    };

     PageSelectionService.prototype.addEventListener = function(type, listener) {
    };



    function SelectionChangedEvent(type,provider,selection) {//type:String,provider:ISelectionProvider,selection:Object,bubbles:Boolean=false, cancelable:Boolean=false
    	this._provider = provider;
		this._selection = selection;
//		private var _selection:ISelection;
		var _part; //IWorkbenchPart
		
		this.clone =  function(){
			var evt = new SelectionChangedEvent(type,selectionProvider,selection);
			evt.part = this.part;
			return evt;
		}
    }

    SelectionChangedEvent.TYPE_SELECTION_CHANGED = "SelectionChanged";
	SelectionChangedEvent.TYPE_DEACTIVE = "deactive";	
    
}(jQuery));
