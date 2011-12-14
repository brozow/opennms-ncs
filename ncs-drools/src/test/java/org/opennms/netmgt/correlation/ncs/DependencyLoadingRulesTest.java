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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.opennms.core.utils.InetAddressUtils.addr;

import java.util.List;

import org.drools.FactHandle;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.netmgt.correlation.drools.DroolsCorrelationEngine;
import org.opennms.netmgt.dao.DistPollerDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsDistPoller;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.ncs.NCSBuilder;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponent.DependencyRequirements;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

public class DependencyLoadingRulesTest extends CorrelationRulesTestCase {
	
	@Autowired
	private NCSComponentRepository m_repository;
	
	@Autowired
	private DistPollerDao m_distPollerDao;
	
	@Autowired
	private NodeDao m_nodeDao;

	private int m_pe1NodeId;
	
	private int m_pe2NodeId;

	private long m_pwCompId;
	
	@Before
	public void setUp() {
		
		OnmsDistPoller distPoller = new OnmsDistPoller("localhost", "127.0.0.1");
		
		m_distPollerDao.save(distPoller);
		
		
		NetworkBuilder bldr = new NetworkBuilder(distPoller);
		bldr.addNode("PE1").setForeignSource("space").setForeignId("1111-PE1");
		
		m_nodeDao.save(bldr.getCurrentNode());
		
		m_pe1NodeId = bldr.getCurrentNode().getId();
		
		bldr.addNode("PE2").setForeignSource("space").setForeignId("2222-PE2");
		
		m_nodeDao.save(bldr.getCurrentNode());
		
		m_pe2NodeId = bldr.getCurrentNode().getId();
		
		NCSComponent svc = new NCSBuilder("Service", "NA-Service", "123")
		.setName("CokeP2P")
		.pushComponent("ServiceElement", "NA-ServiceElement", "8765")
			.setName("PE1:SE1")
			.setNodeIdentity("space", "1111-PE1")
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:jnxVpnIf")
				.setName("jnxVpnIf")
				.setNodeIdentity("space", "1111-PE1")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfDown")
				.setAttribute("jnxVpnIfType", "5")
				.setAttribute("jnxVpnIfName", "ge-1/0/2.50")
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:link")
					.setName("link")
					.setNodeIdentity("space", "1111-PE1")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/linkUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/linkDown")
					.setAttribute("linkName", "ge-1/0/2")
				.popComponent()
			.popComponent()
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:jnxVpnPw-vcid(50)")
				.setName("jnxVpnPw-vcid(50)")
				.setNodeIdentity("space", "1111-PE1")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwDown")
				.setAttribute("jnxVpnPwType", "5")
				.setAttribute("jnxVpnPwName", "ge-1/0/2.50")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:lspA-PE1-PE2")
					.setName("lspA-PE1-PE2")
					.setNodeIdentity("space", "1111-PE1")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspA-PE1-PE2")
				.popComponent()
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "8765:lspB-PE1-PE2")
					.setName("lspB-PE1-PE2")
					.setNodeIdentity("space", "1111-PE1")
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
				.setNodeIdentity("space", "2222-PE2")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnIfDown")
				.setAttribute("jnxVpnIfType", "5")
				.setAttribute("jnxVpnIfName", "ge-3/1/4.50")
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:link")
					.setName("link")
					.setNodeIdentity("space", "2222-PE2")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/linkUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/linkDown")
					.setAttribute("linkName", "ge-3/1/4")
				.popComponent()
			.popComponent()
			.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)")
				.setName("jnxVpnPw-vcid(50)")
				.setNodeIdentity("space", "2222-PE2")
				.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwUp")
				.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/jnxVpnPwDown")
				.setAttribute("jnxVpnPwType", "5")
				.setAttribute("jnxVpnPwName", "ge-3/1/4.50")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:lspA-PE2-PE1")
					.setName("lspA-PE2-PE1")
					.setNodeIdentity("space", "2222-PE2")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspA-PE2-PE1")
				.popComponent()
				.pushComponent("ServiceElementComponent", "NA-SvcElemComp", "9876:lspB-PE2-PE1")
					.setName("lspB-PE2-PE1")
					.setNodeIdentity("space", "2222-PE2")
					.setUpEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathUp")
					.setDownEventUei("uei.opennms.org/vendor/Juniper/traps/mplsLspPathDown")
					.setAttribute("mplsLspName", "lspB-PE2-PE1")
				.popComponent()
			.popComponent()
		.popComponent()
		.get();
		
		m_repository.save(svc);
		
		m_pwCompId = svc.getSubcomponent("NA-ServiceElement", "9876")
		                 .getSubcomponent("NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)")
		                 .getId();

	}
    
	@Test
	@DirtiesContext
	public void testSingleRequestToLoadDependenciesOfTypeAll() {
		// Get engine
        DroolsCorrelationEngine engine = findEngineByName("dependencyLoadingRules");
        
        assertEquals("Expected nothing but got " + engine.getMemoryObjects(), 0, engine.getMemorySize());
        
        Component c = new Component(m_pwCompId, "ServiceElementComponent", "jnxVpnPw-vcid(50)", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)", DependencyRequirements.ALL);
        
        // pretend to be a using rule that inserts the DependenciesNeeded fact
        DependenciesNeeded dependenciesNeeded = new DependenciesNeeded(c, "A");
		engine.getWorkingMemory().insert( dependenciesNeeded );
        engine.getWorkingMemory().fireAllRules();
        
        List<Object> memObjects = engine.getMemoryObjects();
        
        // remove the object we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);

        // expect the component to be inserted
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );
        
        // expect a dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        DependsOn dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );
        
        
        Component parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
        
	}
    
	@Test
	@DirtiesContext
	public void testSingleRequestToLoadDependenciesOfTypeAllAndWithdrawn() {
		// Get engine
        DroolsCorrelationEngine engine = findEngineByName("dependencyLoadingRules");
        
        assertEquals("Expected nothing but got " + engine.getMemoryObjects(), 0, engine.getMemorySize());
        
        Component c = new Component(m_pwCompId, "ServiceElementComponent", "jnxVpnPw-vcid(50)", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)", DependencyRequirements.ALL);
        
        // pretend to be a using rule that inserts the DependenciesNeeded fact
        DependenciesNeeded dependenciesNeeded = new DependenciesNeeded( c, "A" );
		FactHandle depsNeeded = engine.getWorkingMemory().insert( dependenciesNeeded );
        engine.getWorkingMemory().fireAllRules();
        
        List<Object> memObjects = engine.getMemoryObjects();
        
        // remove the object we inserted from the list so we can avoid messing with it below
        memObjects.remove( dependenciesNeeded );

        // expect the component to be inserted
        assertTrue( memObjects.contains( c ) );
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );
        
        // expect a dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        DependsOn dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );
        
        
        Component parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
		
		// simulate other rules retracting the dep
		engine.getWorkingMemory().retract( depsNeeded );
		engine.getWorkingMemory().fireAllRules();

		// rules should unload the dependencies and the component
        assertEquals("unexpected objects in working memory: "+engine.getMemoryObjects(), 0, engine.getMemorySize() );
        
	}
    

	@Test
	@DirtiesContext
	public void testMultipleRequestsToLoadDependenciesOfTypeAll() {
		// Get engine
        DroolsCorrelationEngine engine = findEngineByName("dependencyLoadingRules");
        
        assertEquals("Expected nothing but got " + engine.getMemoryObjects(), 0, engine.getMemorySize());
        
        Component c = new Component(m_pwCompId, "ServiceElementComponent", "jnxVpnPw-vcid(50)", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)", DependencyRequirements.ALL);
        
        // pretend to be a using rule that inserts the DependenciesNeeded fact
        DependenciesNeeded dependenciesNeeded = new DependenciesNeeded(c, "A");
		engine.getWorkingMemory().insert( dependenciesNeeded );
        engine.getWorkingMemory().fireAllRules();
        
        List<Object> memObjects = engine.getMemoryObjects();
        
        // remove the object we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);
        
        // expect the component to be inserted
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        DependsOn dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );
        
        
        Component parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
        
        // now reqest it again from another requestor
		DependenciesNeeded dependenciesNeeded2 = new DependenciesNeeded(c, "B");
		engine.getWorkingMemory().insert( dependenciesNeeded2 );
        engine.getWorkingMemory().fireAllRules();

        memObjects = engine.getMemoryObjects();
        
        // remove the objects we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);
        memObjects.remove(dependenciesNeeded2);
        
        // expect the component to have been inserted only once
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a single dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );

        parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
	}
    

	@Test
	@DirtiesContext
	public void testMultipleRequestsToLoadDependenciesOfTypeAllAndOneWithdrawn() {
		// Get engine
        DroolsCorrelationEngine engine = findEngineByName("dependencyLoadingRules");
        
        assertEquals("Expected nothing but got " + engine.getMemoryObjects(), 0, engine.getMemorySize());
        
        Component c = new Component(m_pwCompId, "ServiceElementComponent", "jnxVpnPw-vcid(50)", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)", DependencyRequirements.ALL);
        
        // pretend to be a using rule that inserts the DependenciesNeeded fact
        DependenciesNeeded dependenciesNeeded = new DependenciesNeeded(c, "A");
        FactHandle depsNeeded = engine.getWorkingMemory().insert( dependenciesNeeded );
        engine.getWorkingMemory().fireAllRules();
        
        List<Object> memObjects = engine.getMemoryObjects();
        
        // remove the object we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);
        
        // expect the component to be inserted
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        DependsOn dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );
        
        
        Component parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
        
        // now reqest it again from another requestor
		DependenciesNeeded dependenciesNeeded2 = new DependenciesNeeded(c, "B");
		FactHandle depsNeeded2 = engine.getWorkingMemory().insert(dependenciesNeeded2);
        engine.getWorkingMemory().fireAllRules();

        memObjects = engine.getMemoryObjects();
        
        // remove the objects we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);
        memObjects.remove(dependenciesNeeded2);
        
        // expect the component to have been inserted only once
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a single dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );

        parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
		
		
		// simulate other rules retracting the dep
		engine.getWorkingMemory().retract( depsNeeded );
		engine.getWorkingMemory().fireAllRules();
		
        memObjects = engine.getMemoryObjects();
        
        // remove the objects we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded2);
        
        // expect the component to have been inserted only once
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a single dependsOn object
        assertEquals( "unexpected number of objects: "+memObjects, 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );

        parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );

	}
    

	@Test
	@DirtiesContext
	public void testMultipleRequestsToLoadDependenciesOfTypeAllAndAllWithdrawn() {
		// Get engine
        DroolsCorrelationEngine engine = findEngineByName("dependencyLoadingRules");
        
        assertEquals("Expected nothing but got " + engine.getMemoryObjects(), 0, engine.getMemorySize());
        
        Component c = new Component(m_pwCompId, "ServiceElementComponent", "jnxVpnPw-vcid(50)", "NA-SvcElemComp", "9876:jnxVpnPw-vcid(50)", DependencyRequirements.ALL);
        
        // pretend to be a using rule that inserts the DependenciesNeeded fact
        DependenciesNeeded dependenciesNeeded = new DependenciesNeeded(c, "A");
        FactHandle depsNeeded = engine.getWorkingMemory().insert( dependenciesNeeded );
        engine.getWorkingMemory().fireAllRules();
        
        List<Object> memObjects = engine.getMemoryObjects();
        
        // remove the object we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);
        
        // expect the component to be inserted
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        DependsOn dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );
        
        
        Component parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
        
        // now reqest it again from another requestor
		DependenciesNeeded dependenciesNeeded2 = new DependenciesNeeded(c, "B");
		FactHandle depsNeeded2 = engine.getWorkingMemory().insert(dependenciesNeeded2);
        engine.getWorkingMemory().fireAllRules();

        memObjects = engine.getMemoryObjects();
        
        // remove the objects we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded);
        memObjects.remove(dependenciesNeeded2);
        
        // expect the component to have been inserted only once
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a single dependsOn object
        assertEquals( 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );

        parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );
		
		
		// simulate other rules retracting the dep
		engine.getWorkingMemory().retract( depsNeeded );
		engine.getWorkingMemory().fireAllRules();
		
        memObjects = engine.getMemoryObjects();
        
        // remove the objects we inserted from the list so we can avoid messing with it below
        memObjects.remove(dependenciesNeeded2);
        
        // expect the component to have been inserted only once
        assertTrue( memObjects.contains( c ));
        
        // remove it so we can work on the dependencies
        memObjects.remove( c );

        // expect a single dependsOn object
        assertEquals( "unexpected number of objects: "+memObjects, 1, memObjects.size() );
        
        assertTrue( memObjects.get(0) instanceof DependsOn );
        
        dep = (DependsOn) memObjects.get(0);
        
        assertSame( c, dep.getB() );

        parent = dep.getA();
		assertEquals( "NA-ServiceElement", parent.getForeignSource() );
		assertEquals( "9876", parent.getForeignId() );

		// simulate other rules retracting the dep
		engine.getWorkingMemory().retract( depsNeeded2 );
		engine.getWorkingMemory().fireAllRules();
		
		// rules should unload the dependencies and the component
        assertEquals("unexpected objects in working memory: "+engine.getMemoryObjects(), 0, engine.getMemorySize() );

	}
    // dependencies must be loaded when needed by propagation rules
    // loaded deps needed by multiple events should not load more than once
    // deps no longer needed by one event should remain loaded if need by others
    // deps no longer needed by any event should be unloaded


	// two kinds of needs... DependentsNeeded meaning I need to ensure the things that depend on
    // component A are loaded
    
    // also need a DependenciesNeeded meaning I need to ensure that the things that component A
    // depends on are loaded.
    
    // to imagine the use cases...
    // 1.  component A is down so ensure that DependentsNeeded is asserted for that component so
    // that all the necessary components are loaded.  After this is asserted then other rules
    // based on DependsOn with that component as a target can fire
    
    
}
