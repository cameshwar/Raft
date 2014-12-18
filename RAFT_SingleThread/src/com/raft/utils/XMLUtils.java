package com.raft.utils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.raft.rpc.XMLGenerationRPC;
import com.raft.rpc.XMLReaderRPC;
import com.raft.rpc.XMLRpc;

public class XMLUtils {
	
	private static XMLOutputFactory outputFactory;
	
	private static XMLInputFactory inputFactory;
	
	public static XMLOutputFactory getXMLOutputFactory() {
		if(outputFactory == null)
			outputFactory = XMLOutputFactory.newInstance();
		return outputFactory;
	}
	
	public static XMLInputFactory getXMLInputFactory() {
		if(inputFactory == null)
			inputFactory = XMLInputFactory.newInstance();
		return inputFactory;
	}
	
	public static void processRPCObject(XMLRpc xmlRpc) {
		if(xmlRpc instanceof XMLGenerationRPC) {
			((XMLGenerationRPC)xmlRpc).
				createDocument().
					createRootElement("AppendEntriesRPC_Req").
						createElement("term", new String[]{"termtext"}).
							createElement("leader_id", new String[]{""}).
							createRootElement("entries").
								createElement("entry", new String[] {"123","456"}).
							endRootElement().
					endRootElement().
				endDocument();
		} else {
			XMLStreamReader reader = ((XMLReaderRPC)xmlRpc).getReader();
			try {
				while(reader.hasNext()){
					reader.next();
				    /*if(reader.getEventType() == XMLStreamReader.START_ELEMENT){
				        System.out.println(reader.getLocalName());
				    } else */if(reader.getEventType() == XMLStreamReader.CHARACTERS){
				        System.out.println(reader.getText());
				    }
				}
				reader.close();
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

}
