package com.wxxr.nirvana.platform;


/**
 * This interface allows extension providers to control how the instances provided to extension-points are being created 
 * by referring to the factory instead of referring to a class. For example, the following extension to the preference page 
 * extension-point uses a factory called <code>PreferencePageFactory</code>.   
 * <code><pre>
 *    <extension point="org.eclipse.ui.preferencePages">
 *    <page  name="..."  class="org.eclipse.update.ui.PreferencePageFactory:org.eclipse.update.ui.preferences.MainPreferencePage"> 
 *    </page>
 *  </extension>
 *  </pre>
 *  </code>
 * 
 * <p>
 * Effectively, factories give full control over the create executable extension process.
 *  </p><p>
 * The factories are responsible for handling the case where the concrete instance implement {@link IExecutableExtension}.
 * </p><p>
 * Given that factories are instantiated as executable extensions, they must provide a 0-argument public constructor.
 * Like any other executable extension, they can configured by implementing {@link org.eclipse.core.runtime.IExecutableExtension} interface.
 * </p><p>
 * This interface can be used without OSGi running.
 * </p>
 * @see org.eclipse.core.runtime.IConfigurationElement
 */
public interface IExecutableExtensionFactory {
	/**
	 * Creates and returns a new instance.
	 * 
	 * @exception CoreException if an instance of the executable extension
	 *   could not be created for any reason
	 */
	Object create() throws CoreException;
}
