package org.opennms.netmgt.ncs.model;

import java.util.HashSet;
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
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class NCSComponent {
	
	public enum DependencyType { ANY, ALL };
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long m_id;

    @Version
    @Column(name = "version")
    private Integer m_version;
    
    @Column(name = "dependencyType")
    private DependencyType m_dependencyType; 
    
    @ManyToMany
    private Set<NCSComponent> m_subComponents = new HashSet<NCSComponent>();

    @XmlTransient
    public Long getId() {
        return m_id;
    }

	public void setId(Long id) {
        m_id = id;
    }

    @XmlTransient
    public Integer getVersion() {
        return m_version;
    }

    public void setVersion(Integer version) {
        m_version = version;
    }
    
	protected Set<NCSComponent> getSubComponents() {
		return m_subComponents;
	}

	protected void setSubComponents(Set<NCSComponent> subComponents) {
		m_subComponents = subComponents;
	}
	
    protected DependencyType getDependencyType() {
		return m_dependencyType;
	}

	protected void setDependencyType(DependencyType dependencyType) {
		m_dependencyType = dependencyType;
	}

}
