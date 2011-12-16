/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.rest.stats;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.stats.AlarmStatisticsService;
import org.opennms.web.rest.MultivaluedMapImpl;
import org.opennms.web.rest.OnmsRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.spi.resource.PerRequest;

/**
 * Basic Web Service using REST for NCS Components
 *
 * @author <a href="mailto:brozow@opennms.org">Matt Brozowski</a>
 */
@Component
@PerRequest
@Scope("prototype")
@Path("stats/alarms")
@Transactional
public class AlarmStatsRestService extends OnmsRestService {

	private static Logger s_log = LoggerFactory.getLogger(AlarmStatsRestService.class);
    static final Pattern m_severityPattern;

    static {
        final String severities = StringUtils.join(OnmsSeverity.names(), "|");
        m_severityPattern = Pattern.compile("\\s+(\\{alias\\}.)?severity\\s*(\\!\\=|\\<\\>|\\<\\=|\\>\\=|\\=|\\<|\\>)\\s*'?(" + severities + ")'?");
    }

	@Autowired
	AlarmStatisticsService m_statisticsService;

    @Context 
    UriInfo m_uriInfo;

    @GET
    public AlarmStatisticsList getStats() {
        final AlarmStatisticsList stats = new AlarmStatisticsList();

        stats.setTotalCount(m_statisticsService.getTotalCount(getQueryFilters(m_uriInfo.getQueryParameters())));
        stats.setAcknowledgedCount(m_statisticsService.getAcknowledgedCount(getQueryFilters(m_uriInfo.getQueryParameters())));

        return stats;
    }

    @Entity
    @XmlRootElement(name = "alarms")
    public static class AlarmStatisticsList extends LinkedList<StatisticsContainer> {
        private int m_totalCount = 0;
        private int m_acknowledgedCount = 0;

        @XmlAttribute(name="count")
        public int getCount() {
            return this.size();
        }

        // The property has a getter "" but no setter. For unmarshalling, please define setters.
        public void setCount(final int count) {}

        @XmlAttribute(name="totalCount")
        public int getTotalCount() {
            return m_totalCount;
        }

        public void setTotalCount(final int count) {
            m_totalCount = count;
        }

        @XmlAttribute(name="acknowledgedCount")
        public int getAcknowledgedCount() {
            return m_acknowledgedCount;
        }
        
        public void setAcknowledgedCount(final int count) {
            m_acknowledgedCount = count;
        }

        @XmlAttribute(name="unacknowledgedCount")
        public int getUnacknowledgedCount() {
            return m_totalCount - m_acknowledgedCount;
        }
        
        public void setUnacknowledgedCount(final int count) {}
    }

    private OnmsCriteria getQueryFilters(MultivaluedMap<String,String> params) {
        translateSeverity(params);

        OnmsCriteria criteria = new OnmsCriteria(OnmsAlarm.class);

        setLimitOffset(params, criteria, DEFAULT_LIMIT, false);
        addOrdering(params, criteria, false);
        // Set default ordering
        addOrdering(
            new MultivaluedMapImpl(
                new String[][] { 
                    new String[] { "orderBy", "lastEventTime" }, 
                    new String[] { "order", "desc" } 
                }
            ), criteria, false
        );
        addFiltersToCriteria(params, criteria, OnmsAlarm.class);


        criteria.setFetchMode("firstEvent", FetchMode.JOIN);
        criteria.setFetchMode("lastEvent", FetchMode.JOIN);
        
        criteria.createAlias("node", "node", CriteriaSpecification.LEFT_JOIN);
        criteria.createAlias("node.snmpInterfaces", "snmpInterface", CriteriaSpecification.LEFT_JOIN);
        criteria.createAlias("node.ipInterfaces", "ipInterface", CriteriaSpecification.LEFT_JOIN);

        return getDistinctIdCriteria(OnmsAlarm.class, criteria);
    }

    private void translateSeverity(final MultivaluedMap<String, String> params) {
        final String query = params.getFirst("query");
        // System.err.println("tranlateSeverity: query = " + query + ", pattern = " + p);
        if (query != null) {
            final Matcher m = m_severityPattern.matcher(query);
            if (m.find()) {
                // System.err.println("translateSeverity: group(1) = '" + m.group(1) + "', group(2) = '" + m.group(2) + "', group(3) = '" + m.group(3) + "'");
                final String alias = m.group(1);
                final String comparator = m.group(2);
                final String severity = m.group(3);
                final OnmsSeverity onmsSeverity = OnmsSeverity.get(severity);
                // System.err.println("translateSeverity: " + severity + " = " + onmsSeverity);
                
                final String newQuery = m.replaceFirst(" " + (alias == null? "" : alias) + "severity " + comparator + " " + onmsSeverity.getId());
                params.remove("query");
                params.add("query", newQuery);
                // System.err.println("translateSeverity: newQuery = '" + newQuery + "'");
            } else {
                // System.err.println("translateSeverity: failed to find pattern");
            }
        }
    }

    /*
	@XmlRootElement(name = "components")
	public static class ComponentList extends LinkedList<NCSComponent> {

	    private static final long serialVersionUID = 8031737923157780179L;
	    private int m_totalCount;

	    public ComponentList() {
	        super();
	    }

	    public ComponentList(Collection<? extends NCSComponent> c) {
	        super(c);
	    }

	    @XmlElement(name = "component")
	    public List<NCSComponent> getComponents() {
	        return this;
	    }

	    public void setComponents(List<NCSComponent> components) {
	        clear();
	        addAll(components);
	    }

	    @XmlAttribute(name="count")
	    public int getCount() {
	        return this.size();
	    }

	    // The property has a getter "" but no setter. For unmarshalling, please define setters.
	    public void setCount(final int count) {
	    }

	    @XmlAttribute(name="totalCount")
	    public int getTotalCount() {
	        return m_totalCount;
	    }
	    
	    public void setTotalCount(int count) {
	        m_totalCount = count;
	    }
	}
	
	@Autowired
	NCSComponentRepository m_componentRepo;
	
    @Context 
    UriInfo m_uriInfo;
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{type}/{foreignSource}:{foreignId}")
    public NCSComponent getComponent(@PathParam("type") String type, @PathParam("foreignSource") String foreignSource, @PathParam("foreignId") String foreignId) {
    	
    	if (m_componentRepo == null) {
    		throw new IllegalStateException("components is null");
    	}
    	
    	NCSComponent component = m_componentRepo.findByTypeAndForeignIdentity(type, foreignSource, foreignId);
    	
    	if (component == null) {
    		throw new WebApplicationException(Status.BAD_REQUEST);
    	}
    	
    	return component;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addComponent(NCSComponent component) {
        s_log.info("addComponent: Adding component " + component);
        m_componentRepo.save(component);
        return Response.ok(component).build();
    }


    @DELETE
    @Path("{type}/{foreignSource}:{foreignId}")
    public Response deleteComponent(@PathParam("type") String type, @PathParam("foreignSource") String foreignSource, @PathParam("foreignId") String foreignId) {
        s_log.info(String.format("deleteComponent: Deleting component of type %s and foreignIdentity %s:%s", type, foreignSource, foreignId));

    	NCSComponent component = m_componentRepo.findByTypeAndForeignIdentity(type, foreignSource, foreignId);
    	
    	
    	if (component == null) {
    		throw new WebApplicationException(Status.BAD_REQUEST);
    	}
    	
    	List<NCSComponent> parents = m_componentRepo.findComponentsThatDependOn(component);
    	
    	for(NCSComponent parent : parents)
    	{
    		parent.getSubcomponents().remove(component);
    	}
    	
    	m_componentRepo.delete(component);
    	
    	return Response.ok().build();
    }
    
    @GET
    @Path("attributes")
    public ComponentList getComponentsByAttributes() {
    	
    	List<NCSComponent> components = m_componentRepo.findComponentsWithAttribute("jnxVpnPwVpnName", "ge-3/1/4.2");
    	
    	return new ComponentList(components);
    	
    	
    }
    */
    
}
