package org.opennms.netmgt.correlation.ncs;

import java.util.List;

import org.opennms.netmgt.model.ncs.NCSComponent;

public interface NCSCorrelationService {
	
	List<NCSComponent> findComponentsThatDependOn(Long componentId);

	

}
