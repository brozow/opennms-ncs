package org.opennms.netmgt.correlation.ncs;

import org.opennms.netmgt.ncs.persistence.NCSComponentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DefaultNCSCorrelationService implements NCSCorrelationService {
	
	@Autowired
	NCSComponentDao m_componentDao;


}
