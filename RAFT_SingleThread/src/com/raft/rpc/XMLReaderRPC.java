package com.raft.rpc;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.raft.constants.IRaftConstants;
import com.raft.utils.XMLUtils;

public class XMLReaderRPC implements XMLRpc{
	
	private XMLInputFactory factory;
	
	
	private XMLEventReader eventReader;	
	
	private Map<String, Object> valueMap;


	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public XMLReaderRPC() {
		this.factory = XMLUtils.getXMLInputFactory();
	}
	
	public XMLReaderRPC readDocument(String data) {
		try {
			this.eventReader = factory.createXMLEventReader(new StringReader(data));
			this.valueMap = new HashMap<String, Object>();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	public void processRPC() {
		String elementName = null;
		List<String> entries = new ArrayList<String>();
		try {
			while(this.eventReader.hasNext()){
				XMLEvent event = this.eventReader.nextEvent();
			    if(event.isStartElement()){				    	
			    	elementName = event.asStartElement().getName().getLocalPart();
			    	if(elementName.equals(IRaftConstants.ENTRIES)) {
			    		this.valueMap.put(elementName, entries);
			    	}
			    } else if(event.isCharacters()){
			    	if(elementName.equals(IRaftConstants.ENTRY)) {
			    		entries.add(event.asCharacters().getData());
			    	} else
			    		this.valueMap.put(elementName, event.asCharacters().getData());
			    }
			}
			this.eventReader.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
