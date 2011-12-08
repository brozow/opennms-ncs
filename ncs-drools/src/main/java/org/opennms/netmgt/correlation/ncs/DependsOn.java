package org.opennms.netmgt.correlation.ncs;



public class DependsOn {
	private Component m_a;
	private Component m_b;
	
	public DependsOn() {}
	
	public DependsOn(Component a, Component b)
	{
		m_a = a;
		m_b = b;
	}

	public Component getA() {
		return m_a;
	}

	public void setA(Component a) {
		m_a = a;
	}

	public Component getB() {
		return m_b;
	}

	public void setB(Component b) {
		m_b = b;
	}
	
	@Override
	public String toString() {
		return "DependsOn[ a=" + m_a + ", b=" + m_b + " ]";
	}
	
	
}
