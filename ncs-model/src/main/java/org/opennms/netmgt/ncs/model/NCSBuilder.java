package org.opennms.netmgt.ncs.model;

import org.opennms.netmgt.ncs.model.NCSComponent.DependencyRequirements;

public class NCSBuilder {
	
	final private NCSBuilder m_parent;
	final private NCSComponent m_component;
	
	public NCSBuilder() {
		this(null, new NCSComponent());
	}
	
	public NCSBuilder(NCSBuilder parent, NCSComponent component) {
		m_parent = parent;
		m_component = component;
	}
	
	public NCSBuilder setForeignSource(String foreignSource) {
		m_component.setForeignSource(foreignSource);
		return this;
	}
	
	public NCSBuilder setForeignId(String foreignId) {
		m_component.setForeignId(foreignId);
		return this;
	}
	
	public NCSBuilder setType(String type) {
		m_component.setType(type);
		return this;
	}
	
	public NCSBuilder setName(String name) {
		m_component.setName(name);
		return this;
	}
	
	public NCSBuilder setDependenciesRequired(DependencyRequirements requirements)
	{
		m_component.setDependenciesRequired(requirements);
		return this;
	}

	public NCSBuilder pushComponent() {
		NCSComponent sub = new NCSComponent();
		m_component.addSubcomponent(sub);
		return new NCSBuilder(this, sub);
	}
	
	public NCSBuilder popComponent() {
		return m_parent;
	}
	
	public NCSComponent get() {
		return m_component;
	}

}
