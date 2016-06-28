/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author aav
 */
public class ResponseParser {
    public List<Map<String, String>> parseResponse(String response, String dataId) throws Exception {
        return parseResponse(new ByteArrayInputStream(response.getBytes("UTF-8")), dataId);
    }
    
    public List<Map<String, String>> parseResponse(InputStream response, String dataId) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(response);
        
        boolean isDataBlock = false;
        while(reader.hasNext()) {
            int tagType = reader.next();
            
            if(XMLStreamConstants.START_ELEMENT == tagType && "data".equals(reader.getLocalName()) && dataId.equals(reader.getAttributeValue(null, "id"))) {
                isDataBlock = true;
            }
            
            if(isDataBlock && XMLStreamConstants.END_ELEMENT == tagType && "data".equals(reader.getLocalName())) {
                isDataBlock = false;
                break;
            }
            
            if(isDataBlock && XMLStreamConstants.START_ELEMENT == tagType && "row".equals(reader.getLocalName())) {
                Map<String, String> row = parseRow(reader);
                result.add(row);
            }
        }
        
        return result;
    }
    
    private Map<String, String> parseRow(XMLStreamReader reader) {
        Map<String, String> result = new HashMap<>();
        
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attributeName = reader.getAttributeLocalName(i);
            String attributeValue = reader.getAttributeValue(i);
            
            result.put(attributeName, attributeValue);
        }
        
        return result;
    }
}
