package com.wxxr.nirvana.workbench.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.workbench.IActionManager;
import com.wxxr.nirvana.workbench.IActionProxy;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

public class ActionManager extends BaseExtensionPointManager implements
		IActionManager {
	private static final Log log = LogFactory.getLog(ActionManager.class);
	private static final IActionProxy[] NO_CHILD = new IActionProxy[0];
	
	private static final String ELEMENT_NAME="action";
	private static final String ATT_ID="id";
	private static final String ATT_CLASS="class";
		
	protected Map<String, IActionProxy> actions = new ConcurrentHashMap<String, IActionProxy>();
	protected Map<String, List<String>> exts = new ConcurrentHashMap<String, List<String>>();
	public ActionManager(){
		super(UIConstants.UI_NAMESPACE,UIConstants.EXTENSION_POINT_ACTIONS);
	}

	protected IActionProxy createNewAction(IConfigurationElement elem) throws Exception{
		String id = elem.getAttribute(ATT_ID);
		IActionProxy action = getAction(id);
		if(action == null){
			action = new ActionProxy();
			String clazz = elem.getAttribute(ATT_CLASS);
			if(!StringUtils.isBlank(clazz)){
				IPluginDescriptor plugin = getUIPlatform().getPluginDescriptor(elem.getNamespaceIdentifier());
				Object realAction = createPluginObject(clazz, plugin);
				action.setAction(realAction);
			}
			action.init(elem);
			actions.put(elem.getNamespaceIdentifier() + "."
					+ elem.getAttribute("id"), action);
		}
		return action;
	}
	
	@Override
	protected void processExtensionAdded(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		List<String> list = exts.get(ext.getUniqueIdentifier());
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if((elem != null)&&ELEMENT_NAME.equalsIgnoreCase(elem.getName())){
				try {
					IActionProxy action = createNewAction(elem);
					if(list == null){
						list = new LinkedList<String>();
						exts.put(ext.getUniqueIdentifier(), list);
					}
					if(!list.contains(elem.getNamespaceIdentifier() + "."
							+ elem.getAttribute("id"))){
						list.add(elem.getNamespaceIdentifier() + "."
								+ elem.getAttribute("id"));
					}
				} catch (Exception e) {
					log.warn("Failed to create layout from configuration element :"+elem, e);
				}
			}
		}		
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		List<String> list = exts.remove(ext.getUniqueIdentifier());
		if((list == null)||list.isEmpty()){
			return;
		}
		String[] ids = list.toArray(new String[list.size()]);
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			IActionProxy action = actions.remove(id);
			if(action != null){
				action.destory();
			}
		}
	}
  

	public IActionProxy getAction(String actionId) {
		return actions.get(actionId);
	}

	public String[] getAllActionIds() {
		if(actions.isEmpty()){
			return UIConstants.EMPTY_STRING_ARRAY;
		}
		return actions.keySet().toArray(new String[actions.size()]);
	}

	public IActionProxy[] getAllActions() {
		if(actions.isEmpty()){
			return NO_CHILD;
		}
		return actions.values().toArray(new IActionProxy[actions.size()]);
	}

	public IActionProxy removeAction(String actionId) {
		return actions.remove(actionId);
	}

	public void destroy() {
		super.stop();
		
	}
}
