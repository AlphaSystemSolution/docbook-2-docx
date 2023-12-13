package com.alphasystem.xml;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshallerHandler;
import java.util.ArrayList;
import java.util.List;


public class MyUnmarshallerHandlerWrapper implements UnmarshallerHandler {

    private final List<ProcessingInstruction> processingInstructions;

    private final UnmarshallerHandler handle;

    public MyUnmarshallerHandlerWrapper(UnmarshallerHandler handle) {
        this.handle = handle;
        processingInstructions = new ArrayList<>();
    }

    public List<ProcessingInstruction> getProcessingInstructions() {
        return processingInstructions;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        handle.characters(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        handle.endDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        handle.endElement(uri, localName, qName);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        handle.endPrefixMapping(prefix);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        handle.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) {
        processingInstructions.add(new ProcessingInstruction(target, data));
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        handle.setDocumentLocator(locator);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        handle.skippedEntity(name);
    }

    @Override
    public void startDocument() throws SAXException {
        handle.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        handle.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        handle.startPrefixMapping(prefix, uri);
    }

    @Override
    public Object getResult() throws JAXBException, IllegalStateException {
        return handle.getResult();
    }

}
