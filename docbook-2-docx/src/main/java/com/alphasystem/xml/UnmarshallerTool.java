package com.alphasystem.xml;

import com.alphasystem.SystemException;
import com.alphasystem.docbook.ApplicationController;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;

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
        }  catch (Exception ex) {
            throw new SystemException(ex.getMessage(), ex);
        }
    }

    @Deprecated
    public <T> T unmarshal(String source, Class<T> declaredType) throws SystemException {
        return null;
    }

}
