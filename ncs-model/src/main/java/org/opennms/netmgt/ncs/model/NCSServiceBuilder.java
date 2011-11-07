package org.opennms.netmgt.ncs.model;

public class NCSServiceBuilder {
	
	NCSService m_currentService = null;
			
	NCSServiceElement m_currentElement = null;
	
	NCSServiceElementComponent m_currentComponent = null;
	
	NCSServiceBuilder newService(long id) {
		m_currentService = new NCSService();
		m_currentService.setId(id);
		return this;
	}
	
	NCSServiceBuilder addElement(long id) {
		m_currentElement = new NCSServiceElement();
		m_currentElement.setId(id);
		if (m_currentService != null) {
			m_currentService.getSubComponents().add(m_currentElement);
		}
		return this;
	}
	
	NCSServiceBuilder addComponent(long id)
	{
		m_currentComponent = new NCSServiceElementComponent();
		m_currentComponent.setId(id);
		if (m_currentElement != null) {
			m_currentElement.getSubComponents().add(m_currentComponent);
		}
		return this;
	}
	
	NCSService getService() {
		return m_currentService;
	}

}
