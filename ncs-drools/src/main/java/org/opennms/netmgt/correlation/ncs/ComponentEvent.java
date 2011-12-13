package org.opennms.netmgt.correlation.ncs;

import org.opennms.netmgt.xml.event.Event;

public class ComponentEvent {

	protected Component m_component;
	protected Event m_event;
	
	protected ComponentEvent(Component component, Event event) {
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
		return getClass().getSimpleName() + " [" +
				"component=" + m_component + 
				", event=" + m_event + 
				"]";
	}

}
