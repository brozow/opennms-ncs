package org.opennms.netmgt.correlation.ncs;


import org.opennms.netmgt.xml.event.Event;

public class ComponentUpEvent {
    
    private Component m_component;
    private Event     m_event;
    
    public ComponentUpEvent(Component component, Event event) {
        m_component = component;
        m_event = event;
    }
    
    public Component getComponent() {
        return m_component;
    }
    
    public void setComponent(Component component) {
        m_component = component;
    }
    
    public Event getEvent() {
		return m_event;
	}

	public void setEvent(Event event) {
		m_event = event;
	}

	@Override
	public String toString() {
		return "ComponentUpEvent [" +
				"component=" + m_component + 
				", event=" + m_event + 
				"]";
	}


}
