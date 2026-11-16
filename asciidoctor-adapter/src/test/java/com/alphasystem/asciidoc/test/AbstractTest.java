package com.alphasystem.asciidoc.test;

import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import com.alphasystem.asciidoc.model.DocumentInfo;
import com.alphasystem.asciidoc.util.DocumentConverter;
import com.alphasystem.commons.SystemException;
import com.alphasystem.commons.util.AppUtil;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docx4j.builder.wml.WmlAdapter;
import com.alphasystem.xml.UnmarshallerTool;
import jakarta.xml.bind.JAXBElement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public abstract class AbstractTest {

  protected static final String DEFAULT_TITLE = "DefaultTitle";
  private static final String DATA_PATH = System.getProperty("data.path");
  protected static final String FILE_NAME = "asciidoc_to_docx.docx";

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
      processContent(createBannerXml(testTitle));
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

  void processContent(String xml) {
    try {
      System.out.println(xml);
      unmarshallerTool.unmarshal(xml);
    } catch (SystemException ex) {
      fail("Test failed", ex);
    }
  }

  void processAdoc(String name) {
    final var sourcePath = get(DATA_PATH, format("%s.adoc", name));
    try {
      final var content =
          DocumentConverter.convertToDocBook(sourcePath).getDocumentInfo().getContent();
      System.out.println(content);
      unmarshallerTool.unmarshal(content);
    } catch (SystemException ex) {
      fail("Test failed", ex);
    }
  }

  void addTestTitle(String title) {
    processContent(createBannerXml(title));
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

    return contents.stream()
        .map(
            content -> {
              if (AppUtil.isInstanceOf(R.class, content)) {
                final var r = (R) content;
                return getRawText(result, r.getContent());
              } else if (AppUtil.isInstanceOf(Text.class, content)) {
                final var text = (Text) content;
                return result + text.getValue();
              } else if (AppUtil.isInstanceOf(P.Hyperlink.class, content)) {
                final var hyperlink = (P.Hyperlink) content;
                return getRawText(result, hyperlink.getContent());
              } else if (AppUtil.isInstanceOf(CTBookmark.class, content)
                  || AppUtil.isInstanceOf(JAXBElement.class, content)) {
                return "";
              } else {
                return content.getClass().getName() + " ";
              }
            })
        .collect(Collectors.joining());
  }

  String readAdoc(String name) {
    final var sourcePath = get(DATA_PATH, format("%s.adoc", name));
    try {
      return new String(Files.readAllBytes(sourcePath));
    } catch (Exception ex) {
      fail(String.format("Fail to read file: %s", sourcePath.getFileName().toString()), ex);
    }
    return null;
  }

  Path getDataPath(String name) {
    return get(DATA_PATH, name);
  }

  void assertSize(int expected) {
    assertEquals(mainDocumentPart.getContent().size() - previousSize, expected);
  }

  private static String createBannerXml(String title) {
    return format(
        "<article xmlns=\"http://docbook.org/ns/docbook\"><info><title>%s</title></info></article>",
        title);
  }
}
