package org.opennms.netmgt.ncs.model;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import org.opennms.netmgt.ncs.model.NCSComponent.DependencyRequirements;

public class JaxbTest {
	
	

	@Test
	public void testMarshall() throws JAXBException, UnsupportedEncodingException {
		
		final String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
				"<component xmlns=\"http://xmlns.opennms.org/xsd/model/ncs\">\n" + 
				"    <foreignSource>P2P</foreignSource>\n" + 
				"    <foreignId>123</foreignId>\n" + 
				"    <type>Service</type>\n" + 
				"    <name>CokeP2P</name>\n" + 
				"    <component>\n" + 
				"        <foreignSource>P2P</foreignSource>\n" + 
				"        <foreignId>1234</foreignId>\n" + 
				"        <type>ServiceElement</type>\n" + 
				"        <name>PE1:ge-1/0/2</name>\n" + 
				"        <component>\n" + 
				"            <foreignSource>P2P</foreignSource>\n" + 
				"            <foreignId>12345</foreignId>\n" + 
				"            <type>ServiceElementComponent</type>\n" + 
				"            <name>ge-1/0/2.50</name>\n" + 
				"            <component>\n" + 
				"                <foreignSource>P2P</foreignSource>\n" + 
				"                <foreignId>2</foreignId>\n" + 
				"                <type>PhysicalInterface</type>\n" + 
				"                <name>ge-1/0/2</name>\n" + 
				"            </component>\n" + 
				"        </component>\n" + 
				"        <component>\n" + 
				"            <foreignSource>P2P</foreignSource>\n" + 
				"            <foreignId>A1234A</foreignId>\n" + 
				"            <type>ServiceElementComponent</type>\n" + 
				"            <name>PE1:vcid(50)</name>\n" + 
				"            <dependenciesRequired>ANY</dependenciesRequired>\n" + 
				"            <component>\n" + 
				"                <foreignSource>P2P</foreignSource>\n" + 
				"                <foreignId>LSPA-PE1-PE2</foreignId>\n" + 
				"                <type>ServiceElementComponent</type>\n" + 
				"                <name>lspA-PE1-PE2</name>\n" + 
				"            </component>\n" + 
				"            <component>\n" + 
				"                <foreignSource>P2P</foreignSource>\n" + 
				"                <foreignId>LSPB-PE1-PE2</foreignId>\n" + 
				"                <type>ServiceElementComponent</type>\n" + 
				"                <name>lspB-PE1-PE2</name>\n" + 
				"            </component>\n" + 
				"        </component>\n" + 
				"    </component>\n" + 
				"    <component>\n" + 
				"        <foreignSource>P2P</foreignSource>\n" + 
				"        <foreignId>4321</foreignId>\n" + 
				"        <type>ServiceElement</type>\n" + 
				"        <name>PE2:ge-3/1/4</name>\n" + 
				"        <component>\n" + 
				"            <foreignSource>P2P</foreignSource>\n" + 
				"            <foreignId>43215</foreignId>\n" + 
				"            <type>ServiceElementComponent</type>\n" + 
				"            <name>ge-3/1/4.50</name>\n" + 
				"            <component>\n" + 
				"                <foreignSource>P2P</foreignSource>\n" + 
				"                <foreignId>3</foreignId>\n" + 
				"                <type>PhysicalInterface</type>\n" + 
				"                <name>ge-3/1/4</name>\n" + 
				"            </component>\n" + 
				"        </component>\n" + 
				"        <component>\n" + 
				"            <foreignSource>P2P</foreignSource>\n" + 
				"            <foreignId>B4321B</foreignId>\n" + 
				"            <type>ServiceElementComponent</type>\n" + 
				"            <name>PE2:vcid(50)</name>\n" + 
				"            <dependenciesRequired>ANY</dependenciesRequired>\n" + 
				"            <component>\n" + 
				"                <foreignSource>P2P</foreignSource>\n" + 
				"                <foreignId>LSPA-PE2-PE1</foreignId>\n" + 
				"                <type>ServiceElementComponent</type>\n" + 
				"                <name>lspA-PE2-PE1</name>\n" + 
				"            </component>\n" + 
				"            <component>\n" + 
				"                <foreignSource>P2P</foreignSource>\n" + 
				"                <foreignId>LSPB-PE2-PE1</foreignId>\n" + 
				"                <type>ServiceElementComponent</type>\n" + 
				"                <name>lspB-PE2-PE1</name>\n" + 
				"            </component>\n" + 
				"        </component>\n" + 
				"    </component>\n" + 
				"</component>\n" + 
				"";
		
		NCSComponent svc = new NCSBuilder()
		.setType("Service")
		.setName("CokeP2P")
		.setForeignSource("P2P")
		.setForeignId("123")
		.pushComponent()
			.setType("ServiceElement")
			.setName("PE1:ge-1/0/2")
			.setForeignSource("P2P")
			.setForeignId("1234")
			.pushComponent()
				.setType("ServiceElementComponent")
				.setName("ge-1/0/2.50")
				.setForeignSource("P2P")
				.setForeignId("12345")
				.pushComponent()
					.setType("PhysicalInterface")
					.setName("ge-1/0/2")
					.setForeignSource("P2P")
					.setForeignId("2")
				.popComponent()
			.popComponent()
			.pushComponent()
				.setType("ServiceElementComponent")
				.setName("PE1:vcid(50)")
				.setForeignSource("P2P")
				.setForeignId("A1234A")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent()
					.setType("ServiceElementComponent")
					.setName("lspA-PE1-PE2")
					.setForeignSource("P2P")
					.setForeignId("LSPA-PE1-PE2")
				.popComponent()
				.pushComponent()
					.setType("ServiceElementComponent")
					.setName("lspB-PE1-PE2")
					.setForeignSource("P2P")
					.setForeignId("LSPB-PE1-PE2")
				.popComponent()
			.popComponent()
		.popComponent()
		.pushComponent()
			.setType("ServiceElement")
			.setName("PE2:ge-3/1/4")
			.setForeignSource("P2P")
			.setForeignId("4321")
			.pushComponent()
				.setType("ServiceElementComponent")
				.setName("ge-3/1/4.50")
				.setForeignSource("P2P")
				.setForeignId("43215")
				.pushComponent()
					.setType("PhysicalInterface")
					.setName("ge-3/1/4")
					.setForeignSource("P2P")
					.setForeignId("3")
				.popComponent()
			.popComponent()
			.pushComponent()
				.setType("ServiceElementComponent")
				.setName("PE2:vcid(50)")
				.setForeignSource("P2P")
				.setForeignId("B4321B")
				.setDependenciesRequired(DependencyRequirements.ANY)
				.pushComponent()
					.setType("ServiceElementComponent")
					.setName("lspA-PE2-PE1")
					.setForeignSource("P2P")
					.setForeignId("LSPA-PE2-PE1")
				.popComponent()
				.pushComponent()
					.setType("ServiceElementComponent")
					.setName("lspB-PE2-PE1")
					.setForeignSource("P2P")
					.setForeignId("LSPB-PE2-PE1")
				.popComponent()
			.popComponent()
		.popComponent()
		.get();
		
		// Create a Marshaller
		JAXBContext context = JAXBContext.newInstance(NCSComponent.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		// save the output in a byte array
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		//marshall the output
		marshaller.marshal(svc, out);

		// verify its matches the expected results
		String result = new String(out.toByteArray(), "UTF-8");
		assertEquals(expectedXML, result);
		
	}

}
