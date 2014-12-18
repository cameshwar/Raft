package com.raft.rpc;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.raft.constants.IRaftConstants;
import com.raft.utils.XMLUtils;

public class XMLGenerationRPC implements XMLRpc{
	
	private XMLOutputFactory factory = null;
	
	private XMLStreamWriter writer;
	
	private ByteArrayOutputStream byteArray;
	
	public XMLGenerationRPC() {
		this.factory = XMLUtils.getXMLOutputFactory();
		this.byteArray = new ByteArrayOutputStream();
	}
	
	@Override
	public void processRPC() {
		
		this.
		createDocument().
			startDocument().
				createRootElement(IRaftConstants.APPEND_ENTRIES_RPC).
					createElement(IRaftConstants.TERM, new String[]{"123"}).
						createElement(IRaftConstants.LEADER_ID, new String[]{""}).
						createRootElement(IRaftConstants.ENTRIES).
							createElement(IRaftConstants.ENTRY, new String[] {"123","456"}).
						endRootElement().
				endRootElement().
			endDocument();
		
	}
	
	public byte[] getXMLStringArray() {
		return byteArray.toByteArray();
	}
	
	private XMLGenerationRPC createDocument() {
		try {
			this.writer = factory.createXMLStreamWriter(byteArray);
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	private XMLGenerationRPC startDocument() {
		try {
			writer.writeStartDocument();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	private XMLGenerationRPC endDocument() {
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

	private XMLGenerationRPC createRootElement(String elementValue) {
		createStartElement(elementValue);
		return this;
	}
	
	private XMLGenerationRPC endRootElement() {
		createEndElement();
		return this;
	}
	
	
	private XMLGenerationRPC createElement(String elementName, String value[]) {
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
