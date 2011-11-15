package org.opennms.netmgt.ncs.rest;

import static org.junit.Assert.*;

import org.junit.Test;

public class NCSRestTest extends AbstractSpringJerseyRestTestCase {

	@Test
	public void test() throws Exception {

		String url = "/ncs";

		// Testing GET Collection
		String xml = sendRequest(GET, url, 200);
		
		System.err.println(xml);
		//assertTrue(xml.contains("Darwin TestMachine 9.4.0 Darwin Kernel Version 9.4.0"));

	}

}
