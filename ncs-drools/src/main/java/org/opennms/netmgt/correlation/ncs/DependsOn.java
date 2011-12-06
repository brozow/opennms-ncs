package org.opennms.netmgt.correlation.ncs;

import org.opennms.netmgt.model.ncs.NCSComponent;

public class DependsOn {
	private NCSComponent m_a;
	private NCSComponent m_b;
	
	public DependsOn() {}
	
	public DependsOn(NCSComponent a, NCSComponent b)
	{
		m_a = a;
		m_b = b;
	}

	public NCSComponent getA() {
		return m_a;
	}

	public void setA(NCSComponent a) {
		m_a = a;
	}

	public NCSComponent getB() {
		return m_b;
	}

	public void setB(NCSComponent b) {
		m_b = b;
	}
	
	@Override
	public String toString() {
		return "DependsOn[ a=" + m_a + ", b=" + m_b + " ]";
	}
	
	
}
