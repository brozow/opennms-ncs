package org.opennms.netmgt.ncs.rest;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Properties;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.opennms.netmgt.model.ncs.NCSComponent;

public class NCSRestTest extends AbstractSpringJerseyRestTestCase {
	
	@Test
	public void generateDDL() throws Exception {
		String config = 
		"hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect\n" + 
		"hibernate.cache.use_second_level_cache=false\n" + 
		"hibernate.cache=false\n" + 
		"hibernate.cache.use_query_cache=false\n" + 
		"hibernate.jdbc.batch_size=0\n" + 
		"hibernate.format_sql=true\n" + 
		"hibernate.hbm2ddl.auto=create\n" + 
		"";
		
		Properties props = new Properties();
		props.load(new StringReader(config));
		
		AnnotationConfiguration configuration = new AnnotationConfiguration();
		configuration.setProperties(props);
		configuration.addAnnotatedClass(NCSComponent.class);
		


		
		SchemaExport se = new SchemaExport( configuration );
		se.setFormat( true );

		se.execute( true, false, false, true );
		
	}

	@Test
	public void test() throws Exception {
		

		String url = "/ncs";

		// Testing GET Collection
		String xml = sendRequest(GET, url, 200);
		
		System.err.println(xml);
		//assertTrue(xml.contains("Darwin TestMachine 9.4.0 Darwin Kernel Version 9.4.0"));

	}

}
