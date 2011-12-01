/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.correlation.ncs;

import static org.opennms.core.utils.InetAddressUtils.addr;

import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.correlation.drools.DroolsCorrelationEngine;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.ncs.NCSBuilder;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponent.DependencyRequirements;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.annotation.Autowired;

public class DependencyRulesTest extends CorrelationRulesTestCase {
	
	@Autowired
	NCSComponentRepository m_repository;
	
	@Before
	public void setUp() {
		
		NCSComponent svc = new NCSBuilder("Service", "NA-Service", "123")
		.setName("CokeP2P")
		.pushComponent("ServiceElement", "NA-ServiceElement", "8765")
			.setName("PE1:SE1")
			.setNodeIdentity("space", "1111-PE1")
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:jnxVpnIf")
				.setName("jnxVpnIf")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfDown")
				.setAttribute("jnxVpnIfType", "5")
				.setAttribute("jnxVpnIfName", "ge-1/0/2.50")
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:link")
					.setName("link")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/linkUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/linkDown")
					.setAttribute("linkName", "ge-1/0/2")
				.popComponent()
			.popComponent()
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:jnxVpnPw-vcid(50)")
				.setName("jnxVpnPw-vcid(50)")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwDown")
				.setAttribute("jnxVpnPwType", "5")
				.setAttribute("jnxVpnPwName", "ge-1/0/2.50")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:lspA-PE1-PE2")
					.setName("lspA-PE1-PE2")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspA-PE1-PE2")
				.popComponent()
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:lspB-PE1-PE2")
					.setName("lspB-PE1-PE2")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspB-PE1-PE2")
				.popComponent()
			.popComponent()
		.popComponent()
		.pushComponent("ServiceElement", "NA-ServiceElement", "9876")
			.setName("PE2:SE1")
			.setNodeIdentity("space", "2222-PE2")
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:jnxVpnIf")
				.setName("jnxVpnIf")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfDown")
				.setAttribute("jnxVpnIfType", "5")
				.setAttribute("jnxVpnIfName", "ge-3/1/4.50")
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:link")
					.setName("link")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/linkUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/linkDown")
					.setAttribute("linkName", "ge-3/1/4")
				.popComponent()
			.popComponent()
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)")
				.setName("jnxVpnPw-vcid(50)")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwDown")
				.setAttribute("jnxVpnPwType", "5")
				.setAttribute("jnxVpnPwName", "ge-3/1/4.50")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:lspA-PE2-PE1")
					.setName("lspA-PE2-PE1")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspA-PE2-PE1")
				.popComponent()
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:lspB-PE2-PE1")
					.setName("lspB-PE2-PE1")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspB-PE2-PE1")
				.popComponent()
			.popComponent()
		.popComponent()
		.get();
		
		m_repository.save(svc);

	}
    
    @Test
    public void testDependencyRules() throws Exception {
        getAnticipator().reset();
		
		/*
        anticipate( createInitializedEvent( 1, 1 ) );
		
		EventBuilder bldr = new EventBuilder( "serviceImpacted", "Drools" );
		bldr.setNodeid( 1 );
		bldr.setInterface( addr( "10.1.1.1" ) );
		bldr.setService( "HTTP" );
		bldr.addParam("CAUSE", 17 );
		
		anticipate( bldr.getEvent() );
		
		bldr = new EventBuilder( "applicationImpacted", "Drools" );
		bldr.addParam("APP", "e-commerce" );
		bldr.addParam("CAUSE", 17 );
		
		anticipate( bldr.getEvent() );
		
		bldr = new EventBuilder( "applicationImpacted", "Drools" );
		bldr.addParam("APP", "hr-portal" );
		bldr.addParam("CAUSE", 17 );
		
		anticipate( bldr.getEvent() );
		*/
		
		DroolsCorrelationEngine engine = findEngineByName("dependencyRules");
		
		Event event = createVpnPwDownEvent( 1, "10.1.1.1", "5", "ge-3/1/4.50" );
		event.setDbid(17);
		System.err.println("SENDING VpnPwDown EVENT!!");
		engine.correlate( event );
		
		getAnticipator().verifyAnticipated();
		
		/*
		bldr = new EventBuilder( "serviceRestored", "Drools" );
		bldr.setNodeid( 1 );
		bldr.setInterface( addr( "10.1.1.1" ) );
		bldr.setService( "HTTP" );
		bldr.addParam("CAUSE", 17 );
		
		anticipate( bldr.getEvent() );
		
		bldr = new EventBuilder( "applicationRestored", "Drools" );
		bldr.addParam("APP", "e-commerce" );
		bldr.addParam("CAUSE", 17 );
		
		anticipate( bldr.getEvent() );
		
		bldr = new EventBuilder( "applicationRestored", "Drools" );
		bldr.addParam("APP", "hr-portal" );
		bldr.addParam("CAUSE", 17 );
		
		anticipate( bldr.getEvent() );
		
		event = createNodeRegainedServiceEvent( 1, "10.1.1.1", "ICMP" );
		event.setDbid(18);
		System.err.println("SENDING nodeRegainedService EVENT!!");
		engine.correlate( event );
		
		getAnticipator().verifyAnticipated();
		*/
    }
    
	private Event createVpnPwDownEvent( int nodeid, String ipaddr, String pwtype, String pwname ) {
		
		return new EventBuilder("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwDown", "Drools")
				.setNodeid(nodeid)
				.setInterface( addr( ipaddr ) )
				.addParam("jnxVpnIfType", "5")
				.addParam("jnxVpnIfName", "ge-1/0/2.50")
				.getEvent();
	}

	private Event createInitializedEvent(int symptom, int cause) {
        return new EventBuilder("initialized", "Drools").getEvent();
    }

    // Currently unused
//    private Event createRootCauseEvent(int symptom, int cause) {
//        return new EventBuilder(createNodeEvent("rootCauseEvent", cause)).getEvent();
//    }


    public Event createNodeDownEvent(int nodeid) {
        return createNodeEvent(EventConstants.NODE_DOWN_EVENT_UEI, nodeid);
    }
    
    public Event createNodeUpEvent(int nodeid) {
        return createNodeEvent(EventConstants.NODE_UP_EVENT_UEI, nodeid);
    }
    
    public Event createNodeLostServiceEvent(int nodeid, String ipAddr, String svcName)
    {
    	return createSvcEvent("uei.opennms.org/nodes/nodeLostService", nodeid, ipAddr, svcName);
    }
    
    public Event createNodeRegainedServiceEvent(int nodeid, String ipAddr, String svcName)
    {
    	return createSvcEvent("uei.opennms.org/nodes/nodeRegainedService", nodeid, ipAddr, svcName);
    }
    
    private Event createSvcEvent(String uei, int nodeid, String ipaddr, String svcName)
    {
    	return new EventBuilder(uei, "Drools")
    		.setNodeid(nodeid)
    		.setInterface( addr( ipaddr ) )
    		.setService( svcName )
    		.getEvent();
    		
    }

    private Event createNodeEvent(String uei, int nodeid) {
        return new EventBuilder(uei, "test")
            .setNodeid(nodeid)
            .getEvent();
    }
    



}