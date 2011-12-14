package org.opennms.netmgt.correlation.ncs;

public class DependenciesNeeded {
	
	private Component m_component;
	private Object m_requestor;
	
	public DependenciesNeeded(Component component, Object requestor) {
		m_component = component;
		m_requestor = requestor;
	}

	public Component getComponent() {
		return m_component;
	}

	public void setComponent(Component component) {
		m_component = component;
	}

	public Object getRequestor() {
		return m_requestor;
	}

	public void setRequestor(Object requestor) {
		m_requestor = requestor;
	}

	@Override
	public String toString() {
		return "DependenciesNeeded [component=" + m_component
				+ ", requestor=" + m_requestor + "]";
	}
	
	

}
