/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2016-01-07 16:22:48
 * @version $Id$
 */
(function(doc,win){
	var listeners = [];
	var readyFn = function(readyCallback){
		listeners.push(readyCallback);
	};
	win.ready = readyFn;
	if (doc.readyState=="complete"){   
		notify();
	}else{   
		doc.onreadystatechange = function(){   
		    if (doc.readyState == "complete"){   
		        notify();
		     }   
		}   
	}

	function notify(){
		for (var i = 0; i < listeners.length; i++) {
			listeners[i].apply(null,listeners[i].arguments);
		}
	}
})(document,window);

