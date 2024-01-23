package com.alphasystem.docbook.builder.test;

import com.alphasystem.commons.SystemException;
import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.commons.util.IdGenerator;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.xml.UnmarshallerTool;
import jakarta.xml.bind.JAXBElement;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.SimplePara;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public abstract class AbstractTest {

    protected static final String DEFAULT_TITLE = "DefaultTitle";
    private static final String DATA_PATH = System.getProperty("data.path");
    protected static final String FILE_NAME = "docbook_to_docx.docx";

    private final UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
    protected final String targetPath = System.getProperty("target.path");
    protected static int previousSize = 0;
    protected static MainDocumentPart mainDocumentPart;

    static {
        ApplicationController.getInstance();
        ApplicationController.startContext(new DocumentInfo());
        mainDocumentPart = ApplicationController.getContext().getMainDocumentPart();
    }

    private final String testTitle;

    public AbstractTest(String testTitle) {
        this.testTitle = testTitle;
    }

    @BeforeClass
    public void beforeClass() {
        if (StringUtils.isNotBlank(testTitle)) {
            processContent(createArticle(createTitle(testTitle)));
            updateCount();
        }
    }

    @AfterClass
    public void afterClass() {
        if (StringUtils.isNotBlank(testTitle)) {
            mainDocumentPart.addObject(WmlAdapter.getPageBreak());
            updateCount();
        }
    }

    @AfterMethod
    public void reset() {
        updateCount();
    }

    void updateCount() {
        previousSize = mainDocumentPart.getContent().size();
    }

    void processContent(Object obj) {
        try {
            final var xml = toXml(obj);
            System.out.println(xml);
            unmarshallerTool.unmarshal(xml);
        } catch (SystemException ex) {
            fail("Test failed", ex);
        }
    }

    void processContent(String xml) {
        try {
            System.out.println(xml);
            unmarshallerTool.unmarshal(xml);
        } catch (SystemException ex) {
            fail("Test failed", ex);
        }
    }

    void addTestTitle(String title) {
        final var titlePara = new SimplePara().withId(IdGenerator.nextId()).withRole(DEFAULT_TITLE).withContent(title);
        processContent(createArticle(titlePara));
        updateCount();
    }

    void addHorizontalLine() {
        mainDocumentPart.addObject(WmlAdapter.getHorizontalLine());
    }

    void assertText(Object obj, String expected) {
        assertEquals(getRawText(obj), expected);
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

    String readXml(String name) {
        final var sourcePath = get(DATA_PATH, format("%s.xml", name));
        try {
            return new String(Files.readAllBytes(sourcePath));
        } catch (Exception ex) {
            fail(String.format("Fail to read file: %s", sourcePath.getFileName().toString()), ex);
        }
        return null;
    }

    void assertSize(int expected) {
        assertEquals(mainDocumentPart.getContent().size() - previousSize, expected);
    }

    long getTableContentSize(Tbl table) {
        return extractContent(0, io.vavr.collection.List.ofAll(table.getContent()));
    }

    private static long extractContent(int result, io.vavr.collection.List<Object> contents) {
        if (contents.isEmpty()) {
            return result;
        }
        final var content = contents.head();
        final var tail = contents.tail();
        if (AppUtil.isInstanceOf(Tr.class, content)) {
            return extractContent(result, tail.appendAll(((Tr) content).getContent()));
        } else if (AppUtil.isInstanceOf(Tc.class, content)) {
            return extractContent(result, tail.appendAll(((Tc) content).getContent()));
        } else if (AppUtil.isInstanceOf(P.class, content)) {
            return extractContent(result, tail.appendAll(((P) content).getContent()));
        } else if (AppUtil.isInstanceOf(R.class, content)) {
            return extractContent(result + 1, tail);
        } else if (AppUtil.isInstanceOf(CTBookmark.class, content) ||
                AppUtil.isInstanceOf(JAXBElement.class, content)) {
            return extractContent(result, tail);
        } else {
            System.err.println("Unhandled type: " + content.getClass().getName());
            return extractContent(result, tail);
        }
    }
}
