package org.opennms.netmgt.correlation.ncs;

import java.util.List;

import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.xml.event.Event;

public interface NCSCorrelationService {
	
	List<NCSComponent> findComponentsThatDependOn(Long componentId);
	
	List<NCSComponent> findComponentsByNodeIdAndEventParameters(Event e, String... parameterNames);

}
