package com.jerryrat.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ServerCofig {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private File file = new File("/home/anil/proxyProject/JerryRat-JR/JerryRat/src/com/jerryrat/server/server.xml");

    public String getContent(String tagName) throws ParserConfigurationException, IOException, SAXException {
        Element root = getElement();
        NodeList node = root.getElementsByTagName(tagName);
        Node data = node.item(0);
        return data.getTextContent();
    }

    private Element getElement() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        return doc.getDocumentElement();
    }

}
