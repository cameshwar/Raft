package com.raft.rpc;

import java.io.ByteArrayInputStream;

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
	
	public XMLReaderRPC readDocument(byte[] data) {
		try {
			this.reader = factory.createXMLStreamReader(new ByteArrayInputStream(data));
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	

}
