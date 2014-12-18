package com.raft.rpc;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.raft.utils.XMLUtils;

public class XMLGenerationRPC implements XMLRpc{
	
	private XMLOutputFactory factory = null;
	
	private XMLStreamWriter writer;
	
	private ByteArrayOutputStream byteArray;
	
	public XMLGenerationRPC() {
		this.factory = XMLUtils.getXMLOutputFactory();
		this.byteArray = new ByteArrayOutputStream();
	}
	
	public byte[] getXMLStringArray() {
		return byteArray.toByteArray();
	}
	
	public XMLGenerationRPC createDocument() {
		try {
			this.writer = factory.createXMLStreamWriter(byteArray);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	public XMLGenerationRPC startDocument() {
		try {
			writer.writeStartDocument();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	public XMLGenerationRPC endDocument() {
		try {
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return this;
	}

	public XMLGenerationRPC createRootElement(String elementValue) {
		createStartElement(elementValue);
		return this;
	}
	
	public XMLGenerationRPC endRootElement() {
		createEndElement();
		return this;
	}
	
	
	public XMLGenerationRPC createElement(String elementName, String value[]) {
		for(int i =0; i< value.length; i++)
			createStartElement(elementName).createTextElement(value[i]).createEndElement();
		return this;
	}
	
	private XMLGenerationRPC createStartElement(String elementName) {
		try {
			writer.writeStartElement(elementName);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	private XMLGenerationRPC createEndElement() {
		try {
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	private XMLGenerationRPC createTextElement(String value) {		
			try {
				writer.writeCharacters(value);
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return this;
	}

}
