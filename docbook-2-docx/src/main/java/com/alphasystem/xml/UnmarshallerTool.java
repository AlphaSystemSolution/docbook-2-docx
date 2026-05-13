package com.alphasystem.xml;

import com.alphasystem.commons.SystemException;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.SAXParserFactory;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @author sali
 */
public class UnmarshallerTool {
  private final DocBookUnmarshallerHandler handler = new DocBookUnmarshallerHandler();

  public WordprocessingMLPackage unmarshal(String source) throws SystemException {
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes())) {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);

      XMLReader xmlReader = spf.newSAXParser().getXMLReader();
      xmlReader.setContentHandler(handler);
      xmlReader.parse(new InputSource(inputStream));

      return (WordprocessingMLPackage) handler.getResult();
    } catch (Exception ex) {
      throw new SystemException(ex.getMessage(), ex);
    }
  }
}
