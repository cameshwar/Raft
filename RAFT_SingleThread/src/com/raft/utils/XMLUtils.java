package com.raft.utils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

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
}
