/**
 * 
 */
package org.apache.tiles.impl.ext;

import org.apache.tiles.definition.DefinitionsFactory;

/**
 * @author fudapeng
 *
 */
public class ContainerContext {
	
	private BasicSiteContainer container;
	public ContainerContext(BasicSiteContainer container) {
		super();
		this.container = container;
	}
	public DefinitionsFactory  getDefinitionsFactory(){
		return container.getDefinitionsFactory();
	}
}
