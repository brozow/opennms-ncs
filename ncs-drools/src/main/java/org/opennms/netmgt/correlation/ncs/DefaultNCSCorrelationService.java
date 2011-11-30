package org.opennms.netmgt.correlation.ncs;

import java.util.List;

import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.ncs.persistence.NCSComponentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultNCSCorrelationService implements NCSCorrelationService {
	
	@Autowired
	NCSComponentDao m_componentDao;

	@Override
	public List<NCSComponent> findComponentsThatDependOn(Long componentId) {
		
		NCSComponent comp = m_componentDao.get(componentId);
		
		List<NCSComponent> parents = m_componentDao.findComponentsThatDependOn(comp);
		
		for(NCSComponent parent : parents) {
			m_componentDao.initialize(parent);
		}
		
		return parents;

	}


}
