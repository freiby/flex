/**
 * 
 * @authors fudapeng (icefirefd@163.com)
 * @date    2016-01-12 16:04:06
 * @version $Id$
 * depend jquery.jsonrpcclient.js
 */

(function($) {
    'use strict';
    var Monitor = function(targetId){
    	this.show = false;
    	this.progress = {};
    	this.maskTarget = null;
        var that = this;
        var pushMessage = function(key, message){
            if(!(key in that.progress)){
                that.progress.key = message;
            }
        };
    	this.showMonitor = function(key, message){
    		if(!this.show){
    			if(this.maskTarget == null){
    				var $maskTarget = null;
    				if(targetId === undefined || targetId == null){
    					$maskTarget = $('body'); 
    					this.maskTarget = $maskTarget;
    				}else{
    					$maskTarget = $(targetId);
    					this.maskTarget = $maskTarget; 
    				}
    			}
                pushMessage(key, message);
    			this.maskTarget.showLoading();
    			this.show = true;
    		}else{
                pushMessage(key, message);
    			
    		}
    	};
    	this.hideMonitor = function(key){
    		if(this.show && this.maskTarget != null){
    			this.maskTarget.hideLoading();
    			this.show = false;
    		}
    	}

    };

    $.invorkRpc = function(ajaxurl, method, params, rpcId, successCb, errorCb, isMonitor) {
        var jsonRpcClient = new $.JsonRpcClient({'ajaxUrl': ajaxurl});
        if(isMonitor){
        	monitorIns.showMonitor(ajaxurl + method, "invork method " + method);
        }
        

        jsonRpcClient.call(
            method, params, rpcId,
            function(result) {
            	console.log( method + ' success result');
                if(successCb !== null){
                	successCb.apply(null,[result]);
                	hideMonitor(ajaxurl + method);
                }

            },
            function(error) {
                console.log( method + ' error result' + error);
                if(errorCb !== null){
                	errorCb.apply(null,[error]);
                	hideMonitor(ajaxurl + method);
                }
            }
        )
        var hideMonitor = function(key){
        	monitorIns.hideMonitor(key);
        }
    };

    var monitorIns = new Monitor();

})(jQuery);
