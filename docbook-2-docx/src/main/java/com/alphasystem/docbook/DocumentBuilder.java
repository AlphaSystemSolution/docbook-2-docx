package com.alphasystem.docbook;

import com.alphasystem.commons.SystemException;
import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.util.FileUtil;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.xml.UnmarshallerTool;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import java.nio.file.Path;


/**
 * @author sali
 */
public class DocumentBuilder {

    static {
        // initialize Application controller
        ApplicationController.getInstance();
    }

    public static Path buildDocument(final DocumentInfo documentInfo, Path docxPath) throws SystemException {
        ApplicationController.startContext(documentInfo);
        final var unmarshallerTool = new UnmarshallerTool();
        try {
            WmlAdapter.save(docxPath.toFile(), unmarshallerTool.unmarshal(documentInfo.getContent()));
        } catch (Docx4JException e) {
            throw new SystemException(e.getMessage(), e);
        } finally {
            ApplicationController.endContext();
        }
        return docxPath;
    }

    public static Path buildDocument(final DocumentInfo documentInfo) throws SystemException {
        return buildDocument(documentInfo, FileUtil.getDocxFile(documentInfo.getSrcFile().toPath()));
    }
}
