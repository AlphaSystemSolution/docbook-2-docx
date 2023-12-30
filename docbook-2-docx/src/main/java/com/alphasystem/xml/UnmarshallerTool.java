package com.alphasystem.xml;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.model.Admonition;
import com.alphasystem.docbook.util.ConfigurationUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;

/**
 * @author sali
 */
public class UnmarshallerTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnmarshallerTool.class);

    private static void populateProcessingInstructions(ProcessingInstruction pi, DocumentInfo documentInfo) {
        final String target = pi.getTarget();
        switch (target) {
            case "asciidoc-toc":
                documentInfo.setToc(true);
                break;
            case "asciidoc-numbered":
                documentInfo.setSectionNumbers(true);
                break;
            default:
                LOGGER.warn("unhandled processing instruction {}", target);
                break;
        }
    }

    private static final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();

    private final DocumentInfo documentInfo;

    public UnmarshallerTool() {
        this(new DocumentInfo());
    }

    public UnmarshallerTool(final DocumentInfo src) {
        documentInfo = new DocumentInfo(src);
        documentInfo.setTocTitle(configurationUtils.getTableOfContentCaption());
        documentInfo.setCautionCaption(configurationUtils.getAdmonitionCaption(Admonition.CAUTION));
        documentInfo.setImportantCaption(configurationUtils.getAdmonitionCaption(Admonition.IMPORTANT));
        documentInfo.setNoteCaption(configurationUtils.getAdmonitionCaption(Admonition.NOTE));
        documentInfo.setTipCaption(configurationUtils.getAdmonitionCaption(Admonition.TIP));
        documentInfo.setWarningCaption(configurationUtils.getAdmonitionCaption(Admonition.WARNING));
        documentInfo.setExampleCaption(configurationUtils.getExampleCaption());
        documentInfo.setTableCaption(configurationUtils.getTableCaption());
    }

    public WordprocessingMLPackage unmarshal(String source) throws SystemException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes())) {
            final var handler = new DocBookUnmarshallerHandler(new DocumentContext(documentInfo, null));

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

    public <T> T unmarshal(String source, Class<T> declaredType) throws SystemException {
        T document;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes())) {
            JAXBContext jc = JAXBContext.newInstance(declaredType);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            MyUnmarshallerHandlerWrapper unmarshallerHandler = new MyUnmarshallerHandlerWrapper(
                    unmarshaller.getUnmarshallerHandler());

            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);

            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(unmarshallerHandler);
            xmlReader.parse(new InputSource(inputStream));

            unmarshallerHandler.getProcessingInstructions().forEach(pi -> populateProcessingInstructions(pi, documentInfo));

            document = (T) unmarshallerHandler.getResult();
        } catch (Exception ex) {
            throw new SystemException(ex.getMessage(), ex);
        }
        return document;
    }

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }
}
