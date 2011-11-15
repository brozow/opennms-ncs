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

package org.opennms.netmgt.ncs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.opennms.netmgt.ncs.model.NCSBuilder;
import org.opennms.netmgt.ncs.model.NCSComponent;
import org.opennms.netmgt.ncs.model.NCSComponent.DependencyRequirements;
import org.opennms.netmgt.ncs.model.NCSComponentRepository;
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
@Path("ncs")
@Transactional
public class NCSRestService  {
	
	private static Logger s_log = LoggerFactory.getLogger(NCSRestService.class);
	
	@Autowired
	NCSComponentRepository m_componentRepo;
    
    /**
     * <p>getNodes</p>
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNodeList} object.
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public NCSComponent getComponents() {
    	
    	if (m_componentRepo == null) {
    		throw new IllegalStateException("components is null");
    	}
    	
		NCSComponent svc = new NCSBuilder("Service", "NA-Service", "123")
		.setName("CokeP2P")
		.pushComponent("ServiceElement", "NA-ServiceElement", "8765:1234")
			.setName("PE1:ge-1/0/2")
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:ge-1/0/2.50")
				.setName("ge-1/0/2.50")
				.pushComponent("PhysicalInterface", "NA-PhysIfs", "8765:ifIndex-1")
					.setName("ge-1/0/2")
				.popComponent()
			.popComponent()
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:vcid(50)")
				.setName("PE1:vcid(50)")
				.setAttribute("jnxVpnPwVpnType", "5")
				.setAttribute("jnxVpnPwVpnName", "ge-1/0/2.2")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:LSP-1234")
					.setName("lspA-PE1-PE2")
				.popComponent()
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:LSP-4321")
					.setName("lspB-PE1-PE2")
				.popComponent()
			.popComponent()
		.popComponent()
		.pushComponent("ServiceElement", "NA-ServiceElement", "9876:4321")
			.setName("PE2:ge-3/1/4")
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:ge-3/1/4.50")
				.setName("ge-3/1/4.50")
				.pushComponent("PhysicalInterface", "NA-PhysIfs", "9876:ifIndex-3")
					.setName("ge-3/1/4")
				.popComponent()
			.popComponent()
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:vcid(50)")
				.setName("PE2:vcid(50)")
				.setAttribute("jnxVpnPwVpnType", "5")
				.setAttribute("jnxVpnPwVpnName", "ge-3/1/4.2")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:LSP-1234")
					.setName("lspA-PE2-PE1")
				.popComponent()
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:LSP-4321")
					.setName("lspB-PE2-PE1")
				.popComponent()
			.popComponent()
		.popComponent()
		.get();
    	
    	m_componentRepo.save(svc);
    	
		return svc;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addComponent(NCSComponent component) {
        s_log.debug("addComponent: Adding component " + component);
        m_componentRepo.save(component);
        return Response.ok(component).build();
    }


    
}
