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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
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
@Path("NCS")
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
    @Path("{type}/{foreignSource}:{foreignId}")
    public NCSComponent getComponent(@PathParam("type") String type, @PathParam("foreignSource") String foreignSource, @PathParam("foreignId") String foreignId) {
    	
    	if (m_componentRepo == null) {
    		throw new IllegalStateException("components is null");
    	}
    	
    	NCSComponent component = m_componentRepo.findByForeignIdentity(type, foreignSource, foreignId);
    	
    	if (component == null) {
    		throw new WebApplicationException(Status.BAD_REQUEST);
    	}
    	
    	return component;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addComponent(NCSComponent component) {
        s_log.debug("addComponent: Adding component " + component);
        m_componentRepo.save(component);
        return Response.ok(component).build();
    }


    
}
