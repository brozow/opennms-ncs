package org.opennms.netmgt.ncs.model;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

public class JaxbTest {
	
	

	@Test
	public void testMarshall() throws JAXBException, UnsupportedEncodingException {
		
		final String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
				"<ManagedService>\n" + 
				"    <ManagedServiceElement xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ncsServiceElement\">\n" + 
				"        <ManagedServiceElementComponent xsi:type=\"ncsServiceElementComponent\"/>\n" + 
				"    </ManagedServiceElement>\n" + 
				"</ManagedService>\n" +
				"";
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		JAXBContext context = JAXBContext.newInstance(NCSService.class, NCSServiceElement.class, NCSServiceElementComponent.class);
		
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		NCSService ncsService = new NCSService();
		
		NCSServiceElement element = new NCSServiceElement();
		
		ncsService.getSubComponents().add(element);
		
		NCSServiceElementComponent comp1 = new NCSServiceElementComponent();
		
		element.getSubComponents().add(comp1);
		
		marshaller.marshal(ncsService, out);
		
		String result = new String(out.toByteArray(), "UTF-8");
		
		assertEquals(expectedXML, result);
		
	}

}
