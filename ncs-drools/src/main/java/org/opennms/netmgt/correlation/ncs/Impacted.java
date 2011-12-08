package org.opennms.netmgt.correlation.ncs;


import org.opennms.netmgt.xml.event.Event;

public class Impacted {

	private Component m_target;
	private Event m_cause;
	
	public Impacted() {}
	
	public Impacted(Component target, Event cause)
	{
		m_target = target;
		m_cause = cause;
	}
	
	public Component getTarget() {
		return m_target;
	}
	public void setTarget(Component target) {
		m_target = target;
	}
	public Event getCause() {
		return m_cause;
	}
	public void setCause(Event cause) {
		m_cause = cause;
	}

	@Override
	public String toString() {
		return "Impacted[ target=" + m_target + ", cause=" + m_cause + " ]";
	}
	
	

}