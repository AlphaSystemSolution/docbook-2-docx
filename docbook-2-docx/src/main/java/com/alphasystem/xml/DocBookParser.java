package com.alphasystem.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

public final class DocBookParser {

  private static final class ContextHolder {
    private static final JAXBContext CONTEXT = createContext();

    private static JAXBContext createContext() {
      try {
        return JAXBContext.newInstance("org.docbook.model");
      } catch (JAXBException ex) {
        throw new IllegalStateException("Cannot initialize the DocBook JAXB model", ex);
      }
    }
  }

  public ParsedDocBook parse(String source) throws Exception {
    final var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setXIncludeAware(false);
    final var document =
        factory.newDocumentBuilder().parse(new InputSource(new StringReader(source)));
    final var binder = ContextHolder.CONTEXT.createBinder();
    final var root = binder.unmarshal(document);
    return new ParsedDocBook(document, binder, root);
  }
}
