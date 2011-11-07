package org.opennms.netmgt.ncs.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ManagedService")
public class NCSService extends NCSComponent {
	
	
	@XmlElement(name="ManagedServiceElement")
	public Set<NCSComponent> getServiceElements() {
		return getSubComponents();
	}
	

}
