package com.alphasystem.docbook.builder.test;

import com.alphasystem.SystemException;
import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.IdGenerator;
import com.alphasystem.xml.UnmarshallerTool;
import jakarta.xml.bind.JAXBElement;
import org.docbook.model.SimplePara;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alphasystem.docbook.builder.test.DataFactory.createArticle;
import static com.alphasystem.docbook.builder.test.DataFactory.toXml;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class AbstractTest2 {

    protected static final String DEFAULT_TITLE = "DefaultTitle";
    private static final String DATA_PATH = System.getProperty("data.path");
    private static final String[] DATA_FILES = new String[]{};
    protected static final String FILE_NAME = "docbook_to_docx.docx";

    protected final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
    protected final String targetPath = System.getProperty("target.path");
    protected static WordprocessingMLPackage wordprocessingMLPackage;

    static {
        ApplicationController.getInstance();
        ApplicationController.startContext(new DocumentInfo());
    }

    List<Object> processContent(Object obj) {
        try {
            final var xml = toXml(obj);
            System.out.println(xml);
            final var wordprocessingMLPackage = unmarshallerTool.unmarshal(xml);
            return wordprocessingMLPackage.getMainDocumentPart().getContent();
        } catch (SystemException ex) {
            fail("Test failed", ex);
            return new ArrayList<>();
        }
    }

    void addResult(String title, List<Object> content) {
        addTestTitle(createTestTitle(title));
        addContent(content);
        addHorizontalLine();
        ;
    }

    private void addTestTitle(SimplePara simplePara) {
        addContent(processContent(createArticle(simplePara)));
    }

    /*
     * This method adds content to main document.
     */
    private void addContent(List<Object> contents) {
        contents.forEach(content -> wordprocessingMLPackage.getMainDocumentPart().addObject(content));
    }

    private void addHorizontalLine() {
        wordprocessingMLPackage.getMainDocumentPart().addObject(WmlAdapter.getHorizontalLine());
    }

    void assertText(Object obj, String expected) {
        assertEquals(getRawText(obj), expected);
    }

    private static SimplePara createTestTitle(String title) {
        return new SimplePara().withId(IdGenerator.nextId()).withRole(DEFAULT_TITLE).withContent(title);
    }

    private static String getRawText(Object content) {
        if (AppUtil.isInstanceOf(P.class, content)) {
            final var p = (P) content;
            return getRawText("", p.getContent());
        } else {
            return content.getClass().getName() + " ";
        }
    }

    private static String getRawText(String result, List<Object> contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            return result;
        }

        return contents.stream().map(content -> {
            if (AppUtil.isInstanceOf(R.class, content)) {
                final var r = (R) content;
                return getRawText(result, r.getContent());
            } else if (AppUtil.isInstanceOf(Text.class, content)) {
                final var text = (Text) content;
                return result + text.getValue();
            } else if (AppUtil.isInstanceOf(P.Hyperlink.class, content)) {
                final var hyperlink = (P.Hyperlink) content;
                return getRawText(result, hyperlink.getContent());
            } else if (AppUtil.isInstanceOf(CTBookmark.class, content) ||
                    AppUtil.isInstanceOf(JAXBElement.class, content)) {
                return "";
            } else {
                return content.getClass().getName() + " ";
            }
        }).collect(Collectors.joining());
    }
}
