package org.opennms.netmgt.ncs.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@XmlRootElement(name="component")
@XmlAccessorType(XmlAccessType.FIELD)
public class NCSComponent {
	
	public enum DependencyRequirements { ANY, ALL };
	
	@XmlRootElement(name="node")
	@XmlAccessorType(XmlAccessType.FIELD)
	@Embeddable
	public static class NodeIdentification {
		@XmlAttribute(name="foreignSource", required=true)
		@Column(name = "nodeForeignSource")
		private String m_foreignSource;
		
	    @XmlAttribute(name="foreignId", required=true)
		@Column(name = "nodeForeignId")
	    private String m_foreignId;
	    
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

	    	    
	}
	
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
    @XmlAttribute(name="foreignSource", required=true)
    private String m_foreignSource;
    
    @Column(name = "foreignId")
    @XmlAttribute(name="foreignId", required=true)
    private String m_foreignId;
    
    @Column(name = "type")
    @XmlAttribute(name="type", required=true)
    private String m_type;
    
    @Column(name = "name")
    @XmlElement(name="name")
    private String m_name;
    
    @XmlElement(name="node")
    @Embedded
    private NodeIdentification m_nodeIdentification;

    @XmlElement(name="upEventUei")
    @Column(name="upEventUei")
    private String m_upEventUei;
    
    @XmlElement(name="downEventUei")
    @Column(name="downEventUei")
    private String m_downEventUei;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "depsRequired")
    @XmlElement(name="dependenciesRequired", required=false, defaultValue="ALL")
    private DependencyRequirements m_dependenciesRequired;
    
    @CollectionOfElements
    @JoinTable(name="ncs_attributes")
    @MapKey(columns=@Column(name="key"))
    @Column(name="value", nullable=false)
    @XmlElement(name = "attributes", required = false)
    @XmlJavaTypeAdapter(JAXBMapAdapter.class)
    private Map<String, String> m_attributes = new LinkedHashMap<String, String>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="subcomponents", joinColumns = { @JoinColumn(name="component_id") }, inverseJoinColumns = { @JoinColumn(name="subcomponent_id") })
    @XmlElement(name="component")
    private Set<NCSComponent> m_subcomponents = new LinkedHashSet<NCSComponent>();
    
    public NCSComponent(String type, String foreignSource, String foreignId) {
    	this();
    	m_type = type;
    	m_foreignSource = foreignSource;
    	m_foreignId = foreignId;
    }
    
    public NCSComponent() {
    }

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
	
	public NodeIdentification getNodeIdentification() {
		return m_nodeIdentification;
	}
	
	public void setNodeIdentification(NodeIdentification nodeIdentification) {
		m_nodeIdentification = nodeIdentification;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}
	
	public String getUpEventUei() {
		return m_upEventUei;
	}
	
	public void setUpEventUei(String upEventUei) {
		m_upEventUei = upEventUei;
	}
	
	public String getDownEventUei() {
		return m_downEventUei;
	}
	
	public void setDownEventUei(String downEventUei) {
		m_downEventUei = downEventUei;
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
	
	public Map<String, String> getAttributes() {
		return m_attributes;
	}
	
	public void setAttribute(String key, String value) {
		m_attributes.put(key, value);
	}
	
	public String removeAttribute(String key) {
		return m_attributes.remove(key);
	}

    
}
