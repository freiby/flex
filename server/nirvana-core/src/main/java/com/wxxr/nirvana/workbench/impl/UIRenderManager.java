package com.wxxr.nirvana.workbench.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wxxr.nirvana.platform.IConfigurationElement;
import com.wxxr.nirvana.platform.IExtension;
import com.wxxr.nirvana.platform.IPluginDescriptor;
import com.wxxr.nirvana.workbench.IRender;
import com.wxxr.nirvana.workbench.IUIRenderManager;
import com.wxxr.nirvana.workbench.UIConstants;
import com.wxxr.nirvana.workbench.config.BaseExtensionPointManager;

public class UIRenderManager extends BaseExtensionPointManager implements
		IUIRenderManager {

	public static final String RENDER_ELEMENT_NAME = "uirender";
	public static final String ATT_ID = "id";
	public static final String ATT_NAME = "name";
	private static final String ATT_CLASS = "class";

	private static final Log log = LogFactory.getLog(UIRenderManager.class);

	protected Map<String, IRender> renders = new HashMap<String, IRender>();

	public UIRenderManager() {
		super(UIConstants.UI_NAMESPACE, UIConstants.EXTENSION_POINT_RENDER);
	}

	private IRender createNewRender(IConfigurationElement elem) throws Exception {
		String renderId = elem.getNamespaceIdentifier() + "."
				+ elem.getAttribute(ATT_ID);
		IRender render = find(renderId);
		if (render != null) {
			return render;
		}
		String clazz = elem.getAttribute(ATT_CLASS);
		if (StringUtils.isNotBlank(clazz)) {
			IPluginDescriptor plugin = getUIPlatform().getPluginDescriptor(
					elem.getNamespaceIdentifier());
			render = (IRender) createPluginObject(clazz, plugin);
		}
		render.init( elem);
		synchronized (renders) {
			renders.put(renderId, render);
		}
		return render;
	}
	
	@Override
	protected void processExtensionAdded(IExtension ext) {
		if (ext == null) {
			return;
		}
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if ((elem != null)
					&& RENDER_ELEMENT_NAME.equalsIgnoreCase(elem.getName())) {
				try {
					createNewRender(elem);
				} catch (Exception e) {
					log.warn(
							"Failed to create workbench render from configuration element :"
									+ elem, e);
				}
			}
		}
	}

	@Override
	protected void processExtensionRemoved(IExtension ext) {
		IConfigurationElement[] configs = ext.getConfigurationElements();
		for (int i = 0; i < configs.length; i++) {
			IConfigurationElement elem = configs[i];
			if ((elem != null)
					&& RENDER_ELEMENT_NAME.equalsIgnoreCase(elem.getName())) {
				renders.remove(elem.getNamespaceIdentifier() + "."
						+ elem.getAttribute(ATT_ID));
			}
		}
	}

	public IRender find(String id) {
		if(renders.containsKey(id)){
			return renders.get(id);
		}
		return null;
	}

	public void destroy() {
		
	}

}
