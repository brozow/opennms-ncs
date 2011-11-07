package org.opennms.netmgt.ncs.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ManagedServiceElement")
public class NCSServiceElement extends NCSComponent {
	
	@XmlElement(name="ManagedServiceElementComponent")
	public Set<NCSComponent> getServiceComponents() {
		return getSubComponents();
	}

}
