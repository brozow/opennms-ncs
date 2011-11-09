package org.opennms.netmgt.ncs.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="component")
@XmlAccessorType(XmlAccessType.FIELD)
public class NCSComponent {
	
	public enum DependencyRequirements { ANY, ALL };
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @XmlElement(name="id")
    private Long m_id;

    @Version
    @Column(name = "version")
    @XmlTransient
    private Integer m_version;
    
    @Column(name = "foreignSource")
    @XmlElement(name="foreignSource")
    private String m_foreignSource;
    
    @Column(name = "foreignId")
    @XmlElement(name="foreignId", required=true)
    private String m_foreignId;
    
    @Column(name = "type")
    @XmlElement(name="type", required=true)
    private String m_type;
    
    @Column(name = "name")
    @XmlElement(name="name")
    private String m_name;

    @Column(name = "depsRequired")
    @XmlElement(name="dependenciesRequired")
    private DependencyRequirements m_dependenciesRequired;
    
    @ManyToMany
    @XmlElement(name="component")
    private Set<NCSComponent> m_subcomponents = new LinkedHashSet<NCSComponent>();

    public Long getId() {
        return m_id;
    }

	public void setId(Long id) {
        m_id = id;
    }

    public Integer getVersion() {
        return m_version;
    }

    public void setVersion(Integer version) {
        m_version = version;
    }

	public String getForeignSource() {
		return m_foreignSource;
	}

	public void setForeignSource(String foreignSource) {
		m_foreignSource = foreignSource;
	}

	public String getForeignId() {
		return m_foreignId;
	}

	public void setForeignId(String foreignId) {
		m_foreignId = foreignId;
	}

	public String getType() {
		return m_type;
	}

	public void setType(String type) {
		m_type = type;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	public DependencyRequirements getDependenciesRequired() {
		return m_dependenciesRequired;
	}

	public void setDependenciesRequired(DependencyRequirements dependenciesRequired) {
		m_dependenciesRequired = dependenciesRequired;
	}

	public Set<NCSComponent> getSubcomponents() {
		return m_subcomponents;
	}

	public void setSubcomponents(Set<NCSComponent> subComponents) {
		m_subcomponents = subComponents;
	}
	
	public void addSubcomponent(NCSComponent subComponent) {
		getSubcomponents().add(subComponent);
	}
	
	public void removeSubcomponent(NCSComponent subComponent) {
		getSubcomponents().remove(subComponent);
	}
    
}
