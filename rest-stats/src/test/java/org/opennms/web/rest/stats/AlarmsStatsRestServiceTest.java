package org.opennms.web.rest.stats;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opennms.web.rest.AbstractSpringJerseyRestTestCase;

public class AlarmsStatsRestServiceTest extends AbstractSpringJerseyRestTestCase {
	
	@BeforeClass
	public static void setupLogging()
	{
		BasicConfigurator.configure();
	}

	@Test
	public void testGetAlarmStats() throws Exception {
	    sendRequest(GET, "/stats/alarms", 200);
	}

	/*
	@Test
	public void testPostAService() throws Exception {
		
		sendPost("/NCS", serviceXML);
		
		String url = "/NCS/ServiceElementComponent/NA-SvcElemComp:9876%3Avcid(50)";		
		// Testing GET Collection
		String xml = sendRequest(GET, url, 200);
		
		assertTrue(xml.contains("jnxVpnPwVpnName"));
	}

	@Test
	public void testDeleteAComponent() throws Exception {
		
		sendPost("/NCS", serviceXML);
		
		String url = "/NCS/ServiceElementComponent/NA-SvcElemComp:9876%3Avcid(50)";		
		// Testing GET Collection
		String xml = sendRequest(GET, url, 200);
		
		assertTrue(xml.contains("jnxVpnPwVpnName"));
		
		sendRequest(DELETE, url, 200);
		
		sendRequest(GET, url, 400);
		
		sendRequest(GET, "/NCS/Service/NA-Service:123", 200);
		
	}
	
	@Test
	public void testGetANonExistingService() throws Exception {
		
		// This service should not exist
		String url = "/NCS/Service/hello:world";

		// Testing GET Collection
		sendRequest(GET, url, 400);
		
		//assertTrue(xml.contains("Darwin TestMachine 9.4.0 Darwin Kernel Version 9.4.0"));

	}
	
	@Test
	public void testFindAServiceByAttribute() throws Exception {
		
		sendPost("/NCS", serviceXML);
		
		String url = "/NCS/attributes";
		// Testing GET Collection
		String xml = sendRequest(GET, url, 200);
		
		assertTrue(xml.contains("jnxVpnPwVpnName"));
	}
	*/

}
