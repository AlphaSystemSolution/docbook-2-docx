package com.alphasystem.docbook.builder.test;

import com.alphasystem.SystemException;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.DocumentBuilder;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.openxml.builder.wml.PBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import com.alphasystem.util.IdGenerator;
import com.alphasystem.xml.UnmarshallerTool;
import org.apache.commons.lang3.ArrayUtils;
import org.docbook.model.*;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.docbook.ApplicationController.DEFAULT_TEMPLATE;
import static com.alphasystem.docbook.ApplicationController.STYLES_PATH;
import static com.alphasystem.docbook.builder.model.DocumentCaption.EXAMPLE;
import static com.alphasystem.docbook.builder.model.DocumentCaption.TABLE;
import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static com.alphasystem.openxml.builder.wml.WmlAdapter.*;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * @author sali
 */
public class BuilderTest {

    private static final String DEFAULT_TITLE = "DefaultTitle";
    private static final String DATA_PATH = System.getProperty("data.path");
    private static final String TARGET_PATH = System.getProperty("target.path");

    static {
        final Path path = get(TARGET_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path).toString();
            } catch (IOException e) {
                fail(e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings({"unused"})
    private static void build(String dir, String fileNamePrefix) {
        final Path sourcePath = get(dir, format("%s.xml", fileNamePrefix));
        try {
            final DocumentContext context = DocumentBuilder.createContext(sourcePath);
            final WordprocessingMLPackage wmlPackage = DocumentBuilder.buildDocument(context);
            save(get(TARGET_PATH, format("%s.docx", fileNamePrefix)).toFile(), wmlPackage);
        } catch (SystemException | Docx4JException e) {
            fail(e.getMessage(), e);
        }
    }

    private static Object readXml(String name, Class<?> declaredType) {
        UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
        final Path sourcePath = get(DATA_PATH, format("%s.xml", name));
        try {
            final String source = new String(readAllBytes(sourcePath));
            return unmarshallerTool.unmarshal(source, declaredType);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
        return null;
    }

    private BuilderFactory builderFactory = BuilderFactory.getInstance();
    private WordprocessingMLPackage wmlPackage;
    private MainDocumentPart mainDocumentPart;

    @BeforeClass
    public void setup() {
        try {
            UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
            DocumentContext documentContext = new DocumentContext(unmarshallerTool.getDocumentInfo(), new Article());
            ApplicationController.startContext(documentContext);

            final WmlPackageBuilder wmlPackageBuilder = new WmlPackageBuilder(DEFAULT_TEMPLATE)
                    .styles(STYLES_PATH).multiLevelHeading(EXAMPLE).multiLevelHeading(TABLE);

            final StyleDefinitionsPart styleDefinitionsPart = wmlPackageBuilder.getPackage().getMainDocumentPart().getStyleDefinitionsPart();
            final Styles styles = styleDefinitionsPart.getContents();
            final List<Style> list = styles.getStyle();
            list.forEach(style -> documentContext.getDocumentStyles().add(style.getStyleId()));
            wmlPackage = wmlPackageBuilder.getPackage();
            mainDocumentPart = wmlPackage.getMainDocumentPart();
            documentContext.setMainDocumentPart(mainDocumentPart);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    private P buildPara(R... runs) {
        final PBuilder pBuilder = WmlBuilderFactory.getPBuilder();
        for (R run : runs) {
            pBuilder.addContent(run);
        }
        return pBuilder.getObject();
    }

    private void addResultToDocument(String title, Object... content) {
        mainDocumentPart.addObject(getParagraphWithStyle(DEFAULT_TITLE, title));
        for (Object o : content) {
            mainDocumentPart.addObject(o);
        }
        mainDocumentPart.addObject(getHorizontalLine());
    }

    private void addResult(Builder parent, int indexInParent, int expectedSize, String title, Object... content) {
        final List<Object> childContent = buildContent(parent, indexInParent, content);
        assertEquals(childContent.size(), expectedSize);
        addResultToDocument(title, childContent.toArray());
    }

    private void addResult(String title, R... runs) {
        addResultToDocument(title, buildPara(runs));
    }

    @SuppressWarnings("unchecked")
    private List<Object> buildContent(Builder parent, int indexInParent, Object... objects) {
        List<Object> content = new ArrayList<>();
        if (!ArrayUtils.isEmpty(objects)) {
            for (Object o : objects) {
                final Builder builder = builderFactory.getBuilder(parent, o, indexInParent);
                content.addAll(builder.buildContent());
            }
        }

        return content;
    }

    private R[] convertToRuns(List<Object> content) {
        R[] runs = new R[content.size()];
        for (int i = 0; i < content.size(); i++) {
            runs[i] = (R) content.get(i);
        }
        return runs;
    }

    @AfterClass
    public void tearDown() {
        ApplicationController.endContext();
        try {
            final File file = get(TARGET_PATH, "builder.docx").toFile();
            save(file, wmlPackage);
            Desktop.getDesktop().open(file);
        } catch (Docx4JException e) {
            fail(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = "inlineGroup")
    public void testBold() {
        final List<Object> content = buildContent(null, -1, createBold("Bold Text"));
        assertEquals(content.size(), 1);
        final R r = (R) content.get(0);
        assertEquals(r.getClass().getName(), R.class.getName());
        addResult("Bold Test", r);
    }

    @Test(groups = "inlineGroup")
    public void testItalic() {
        final List<Object> content = buildContent(null, -1, createItalic("Italic Text"));
        assertEquals(content.size(), 1);
        final R r = (R) content.get(0);
        assertEquals(r.getClass().getName(), R.class.getName());
        addResult("Italic Test", r);
    }

    @Test(groups = "inlineGroup")
    public void testTermBuilder() {
        final List<Object> content = buildContent(null, -1, createTerm("Term Title"));
        assertEquals(content.size(), 1);
        final R r = (R) content.get(0);
        assertEquals(r.getClass().getName(), R.class.getName());
        addResult("Term Test", r);
    }

    @Test(groups = "inlineGroup")
    public void testSubscript() {
        final Literal literal = createLiteral(null, "H", createSubscript(null, "2"), "O");
        final Phrase phrase = createPhrase("bold", literal);
        final List<Object> content = buildContent(null, -1, "Chemical formula for water is ", phrase);
        assertEquals(content.size(), 4);
        addResult("Subscript Test", convertToRuns(content));
    }

    @Test(groups = "inlineGroup")
    public void testSuperscript() {
        final Literal literal = createLiteral(null, "E = mc", createSuperscript(null, "2"));
        final Phrase phrase = createPhrase("bold", literal);
        final List<Object> content = buildContent(null, -1, "Einstein's theory of relativity is ", phrase);
        assertEquals(content.size(), 3);
        addResult("Superscript Test", convertToRuns(content));
    }

    @Test(groups = "titleGroup", dependsOnGroups = "inlineGroup")
    public void testDocumentTitle() {
        final Builder parent = builderFactory.getBuilder(null, new Article(), -1);
        final Title title = createTitle("Document Title");
        final List<Object> content = buildContent(parent, 0, title);
        assertEquals(content.size(), 1);
        addResultToDocument("Document Title Test", content.toArray());
    }

    @Test(groups = "titleGroup", dependsOnGroups = "inlineGroup")
    public void testDocumentTitleWithCustomStyle() {
        final Builder parent = builderFactory.getBuilder(null, new Article(), -1);
        final Title title = createTitle("Document Title ", createPhrase("arabicLabel", "س ل م"));
        addResult(parent, 0, 1, "Document Title with custom style Test", title);
    }

    @Test(groups = "titleGroup", dependsOnMethods = {"testDocumentTitleWithCustomStyle"}, dependsOnGroups = "inlineGroup")
    public void testExampleTitle() {
        final Builder parent = builderFactory.getBuilder(null, new Example(), -1);
        addResult(parent, 0, 3, "Example Title Test", createExample("Example Title"));
    }

    @Test(groups = {"titleGroup"}, dependsOnGroups = {"inlineGroup"})
    public void testSectionLevel1Title() {
        final Builder parent = builderFactory.getBuilder(null, new Article(), -1);
        final Title title = createTitle("Section 1");
        addResult(parent, 0, 1, "Section Level 1 Test", createSection("section-1", title));
    }

    @Test(groups = {"titleGroup"}, dependsOnGroups = {"inlineGroup"})
    public void testSectionLevel2Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Title title = createTitle("Section 2");
        addResult(p2, 0, 1, "Section Level 2 Test", createSection("section-2", title));
    }

    @Test(groups = {"titleGroup"}, dependsOnGroups = {"inlineGroup"})
    public void testSectionLevel3Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Builder p3 = builderFactory.getBuilder(p2, new Section(), 0);
        final Title title = createTitle("Section 3");
        addResult(p3, 0, 1, "Section Level 3 Test", createSection("section-3", title));
    }

    @Test(groups = {"titleGroup"}, dependsOnGroups = {"inlineGroup"})
    public void testSectionLevel4Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Builder p3 = builderFactory.getBuilder(p2, new Section(), 0);
        final Builder p4 = builderFactory.getBuilder(p3, new Section(), 0);
        final Title title = createTitle("Section 4");
        addResult(p4, 0, 1, "Section Level 4 Test", createSection("section-4", title));
    }

    @Test(groups = {"titleGroup"}, dependsOnGroups = {"inlineGroup"})
    public void testSectionLevel5Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Builder p3 = builderFactory.getBuilder(p2, new Section(), 0);
        final Builder p4 = builderFactory.getBuilder(p3, new Section(), 0);
        final Builder p5 = builderFactory.getBuilder(p4, new Section(), 0);
        final Title title = createTitle("Section 5");
        addResult(p5, 0, 1, "Section Level 5 Test", createSection("section-5", title));
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = "titleGroup")
    public void testItemizedList() {
        addResult(null, 0, 3, "ItemizedList Test", readXml("itemizedlist", ItemizedList.class));
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = "titleGroup")
    public void testItemizedListMoreThenOnePara() {
        final SimplePara p1 = createSimplePara(null, "First paragraph of list item, this should contain bullet mark.");
        final SimplePara p2 = createSimplePara(null, "Second paragraph of list item, this should not contain bullet mark, but should be indented properly.");
        final ListItem li1 = createListItem(null, p1, p2);
        final ListItem li2 = createListItem(null, createSimplePara(null, "Second bullet point."));
        final ItemizedList itemizedList = createItemizedList(IdGenerator.nextId(), li1, li2);
        addResult(null, 0, 3, "Itemized list with a list item having multiple para", itemizedList);
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = "titleGroup")
    public void testNestedItemizedList() {
        addResult(null, 0, 6, "ItemizedList Test", readXml("nested-itemizedlist", ItemizedList.class));
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = {"titleGroup"})
    public void testOrderedList() {
        addResult(null, 0, 3, "OrderedList Test", readXml("orderedlist", OrderedList.class));
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = {"titleGroup"})
    public void testNestedOrderedList() {
        addResult(null, 0, 5, "OrderedList Test", readXml("nested-orderedlist", OrderedList.class));
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = {"titleGroup"})
    public void testVariableListEntryBuilder() {
        final Term term = createTerm("Entry title");
        final SimplePara simplePara = createSimplePara(null, "This text is under simple para and it has to be indented using \"ListParagraph\" style without any numbering.");
        final ListItem listItem = createListItem(null, simplePara);
        addResult(null, 0, 2, "VariableListEntry Test", createVariableListEntry(listItem, term));
    }

    @Test(groups = {"listGroup"}, dependsOnGroups = {"titleGroup"}, dependsOnMethods = {"testVariableListEntryBuilder"})
    public void testVariableListBuilder() {
        addResult(null, 0, 12, "VariableList Test", readXml("variablelist", VariableList.class));
    }

    @Test(groups = {"blockGroup"}, dependsOnGroups = {"listGroup"})
    public void testCaution() {
        Caution caution = createCaution(createSimplePara(null, "If the title line is not offset by a blank line, it gets interpreted as a section title, which we&#8217;ll discuss later."));
        addResult(null, 0, 3, "Caution Test", caution);
    }

    @Test(groups = {"blockGroup"}, dependsOnGroups = {"listGroup"})
    public void testImportant() {
        Important important = createImportant(createSection(null, "There should be no blank lines between the first attribute entry and the rest of the header."));
        addResult(null, 0, 3, "Important Test", important);
    }

    @Test(groups = {"blockGroup"}, dependsOnGroups = {"listGroup"})
    public void testNote() {
        Note note = createNote(createSimplePara(null, "Admonitions can also encapsulate any block content, which we&#8217;ll cover later."));
        addResult(null, 0, 3, "Note Test", note);
    }

    @Test(groups = {"blockGroup"}, dependsOnGroups = {"listGroup"})
    public void testTip() {
        Tip tip = createTip(createSimplePara(null, "A document title is an <emphasis>optional</emphasis> feature of an AsciiDoc document."));
        addResult(null, 0, 3, "Tip Test", tip);
    }

    @Test(groups = {"blockGroup"}, dependsOnGroups = {"listGroup"})
    public void testWarning() {
        Warning warning = createWarning(createSimplePara(null, "Wolpertingers are known to nest in server racks.\n" +
                "        Enter at your own risk."));
        addResult(null, 0, 3, "Warning Test", warning);
    }

    @Test(groups = {"blockGroup"}, dependsOnMethods = {"testWarning"}, dependsOnGroups = {"listGroup"})
    public void testExample() {
        addResult(null, 0, 3, "Example Test", readXml("example", Example.class));
    }

    @Test(groups = {"blockGroup"}, dependsOnMethods = {"testExample"}, dependsOnGroups = {"listGroup"})
    public void testInformalExample() {
        addResult(null, 0, 3, "Informal Example Test", readXml("informal-example", InformalExample.class));
    }

}
