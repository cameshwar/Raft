package com.raft.rpc;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.raft.utils.XMLUtils;

public class XMLReaderRPC implements XMLRpc{
	
	private XMLInputFactory factory;
	
	private XMLStreamReader reader;
	
	public XMLStreamReader getReader() {
		return reader;
	}

	public XMLReaderRPC() {
		this.factory = XMLUtils.getXMLInputFactory();
	}
	
	public XMLReaderRPC readDocument(String data) {
		try {
			this.reader = factory.createXMLStreamReader(new StringReader(data));
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	

}
