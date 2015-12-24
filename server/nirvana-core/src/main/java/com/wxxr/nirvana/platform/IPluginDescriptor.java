package com.wxxr.nirvana.platform;

/**
 * A plug-in descriptor contains information about a plug-in obtained from the
 * plug-in's manifest (<code>webplugin.xml</code>) file.
 * <p>
 * Plug-in descriptors are platform-defined objects that exist in the plug-in
 * registry independent of whether a plug-in has been started. In contrast, a
 * plug-in's runtime object (<code>getPlugin</code>) generally runs
 * plug-in-defined code.
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *
 */
public interface IPluginDescriptor {
	/**
	 * Returns the extension with the given simple identifier declared in this
	 * plug-in, or <code>null</code> if there is no such extension. Since an
	 * extension might not have an identifier, some extensions can only be found
	 * via the <code>getExtensions</code> method.
	 *
	 * @param extensionName
	 *            the simple identifier of the extension (e.g.
	 *            <code>"main"</code>).
	 * @return the extension, or <code>null</code>
	 */
	public IExtension getExtension(String extensionName);

	/**
	 * Returns the extension point with the given simple identifier declared in
	 * this plug-in, or <code>null</code> if there is no such extension point.
	 *
	 * @param extensionPointId
	 *            the simple identifier of the extension point (e.g.
	 *            <code>"wizard"</code>).
	 * @return the extension point, or <code>null</code>
	 */
	public IExtensionPoint getExtensionPoint(String extensionPointId);

	/**
	 * Returns all extension points declared by this plug-in. Returns an empty
	 * array if this plug-in does not declare any extension points.
	 *
	 * @return the extension points declared by this plug-in
	 */
	public IExtensionPoint[] getExtensionPoints();

	/**
	 * Returns all extensions declared by this plug-in. Returns an empty array
	 * if this plug-in does not declare any extensions.
	 *
	 * @return the extensions declared by this plug-in
	 */
	public IExtension[] getExtensions();

	/**
	 * Returns the URL of this plug-in's install directory. This is the
	 * directory containing the plug-in manifest file, resource bundle, runtime
	 * libraries, and any other files supplied with this plug-in. This directory
	 * is usually read-only. Plug-in relative information should be written to
	 * the location provided by <code>Plugin.getStateLocation</code>.
	 *
	 * @return the URL of this plug-in's install directory
	 * @see #getPlugin()
	 * @see Plugin#getStateLocation()
	 */
	public String getInstallURL();

	/**
	 * Returns a displayable label for this plug-in. Returns the empty string if
	 * no label for this plug-in is specified in the plug-in manifest file.
	 * <p>
	 * Note that any translation specified in the plug-in manifest file is
	 * automatically applied.
	 * </p>
	 *
	 * @return a displayable string label for this plug-in, possibly the empty
	 *         string
	 * @see #getResourceString(String)
	 */
	public String getLabel();

	/**
	 * Returns the name of the provider of this plug-in. Returns the empty
	 * string if no provider name is specified in the plug-in manifest file.
	 * <p>
	 * Note that any translation specified in the plug-in manifest file is
	 * automatically applied.
	 * </p>
	 * 
	 * @return the name of the provider, possibly the empty string
	 * @see #getResourceString(String)
	 */
	public String getProviderName();

	/**
	 * Returns this plug-in's resource bundle for the current locale.
	 * <p>
	 * The bundle is stored as the <code>plugin.properties</code> file in the
	 * plug-in install directory, and contains any translatable strings used in
	 * the plug-in manifest file (<code>webplugin.xml</code>) along with other
	 * resource strings used by the plug-in implementation.
	 * </p>
	 *
	 * @return the message resource , null if the message resource is not
	 *         existing
	 */
	public MessageResourceBundle getMessageResourceBundle();

	/**
	 * Returns a resource string corresponding to the given argument value. If
	 * the argument value specifies a resource key, the string is looked up in
	 * the default resource bundle. If the argument does not specify a valid
	 * key, the argument itself is returned as the resource string. The key
	 * lookup is performed in the plugin.properties resource bundle. If a
	 * resource string corresponding to the key is not found in the resource
	 * bundle the key value, or any default text following the key in the
	 * argument value is returned as the resource string. A key is identified as
	 * a string beginning with the "%" character. Note, that the "%" character
	 * is stripped off prior to lookup in the resource bundle.
	 * <p>
	 * Equivalent to <code>getResourceString(value, getMessageResource())</code>
	 * </p>
	 *
	 * @param value
	 *            the value
	 * @return the resource string
	 * @see #getMessageResource()
	 */
	public String getResourceString(String value);

	/**
	 * Returns a resource string corresponding to the given argument value and
	 * bundle. If the argument value specifies a resource key, the string is
	 * looked up in the given resource bundle. If the argument does not specify
	 * a valid key, the argument itself is returned as the resource string. The
	 * key lookup is performed against the specified resource bundle. If a
	 * resource string corresponding to the key is not found in the resource
	 * bundle the key value, or any default text following the key in the
	 * argument value is returned as the resource string. A key is identified as
	 * a string beginning with the "%" character. Note that the "%" character is
	 * stripped off prior to lookup in the resource bundle.
	 * <p>
	 * For example, assume resource bundle plugin.properties contains name =
	 * Project Name
	 * 
	 * <pre>
	 *     getResourceString("Hello World") returns "Hello World"</li>
	 *     getResourceString("%name") returns "Project Name"</li>
	 *     getResourceString("%name Hello World") returns "Project Name"</li>
	 *     getResourceString("%abcd Hello World") returns "Hello World"</li>
	 *     getResourceString("%abcd") returns "%abcd"</li>
	 *     getResourceString("%%name") returns "%name"</li>
	 * </pre>
	 * 
	 * </p>
	 *
	 * @param value
	 *            the value
	 * @param resourceBundle
	 *            the resource bundle
	 * @return the resource string
	 * @see #getMessageResource()
	 */
	public String getResourceString(String value,
			MessageResourceBundle resourceBundle);

	/**
	 * Returns the unique identifier of this plug-in. This identifier is a
	 * non-empty string and is unique within the plug-in registry.
	 *
	 * @return the unique identifier of the plug-in (e.g.
	 *         <code>"org.eclipse.core.runtime"</code>)
	 */
	public String getUniqueIdentifier();

	/**
	 * Returns the version identifier of this plug-in.
	 *
	 * @return the plug-in version identifier
	 */
	public PluginVersionIdentifier getVersionIdentifier();

	/**
	 * Returns whether the plug-in described by this descriptor has been
	 * activated. Invoking this method will not cause the plug-in to be
	 * activated.
	 *
	 * @return <code>true</code> if this plug-in is activated, and
	 *         <code>false</code> otherwise
	 * @see #getPlugin()
	 */
	public boolean isPluginInitialized();

	public String[] getDependencies();

	public IRuntimeLibrary getRuntimeLibrary();

	public String getName();

	public boolean isLocalPlugin();

	public ClassLoader getPluginClassLoader();

	public IPluginInitializer getPluginInitializer();

	public IConfigurationElement getConfigurationElement();

	public void setVersionTimeStamp(String time);

	public String getVersionTimeStamp();
}
