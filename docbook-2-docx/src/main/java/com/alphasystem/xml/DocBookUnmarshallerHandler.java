package com.alphasystem.xml;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.model.DocBookTableAdapter;
import com.alphasystem.docbook.model.DocumentCaption;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.TocGenerator;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.*;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import javax.xml.bind.UnmarshallerHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class DocBookUnmarshallerHandler implements UnmarshallerHandler {

    private final static String NEW_LINE = System.lineSeparator();
    private static final String ARTICLE = "article";
    private static final String SECTION = "section";
    private static final String TITLE = "title";
    private static final String INFORMAL_TABLE = "informaltable";
    private static final String TABLE_GROUP = "tgroup";
    private static final String COLUMN_SPEC = "colspec";
    private static final String TABLE_HEAD = "thead";
    private static final String TABLE_BODY = "tbody";
    private static final String TABLE_FOOTER = "tfoot";
    private static final String ROW = "row";
    private static final String SIMPLE_PARA = "simpara";
    private static final String PHRASE = "phrase";
    private static final String EMPHASIS = "emphasis";
    private static final String INFO = "info";
    private static final String DATE = "date";
    private static final String ENTRY = "entry";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BuilderFactory builderFactory = BuilderFactory.getInstance();
    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final DocumentContext documentContext;
    private WordprocessingMLPackage wordprocessingMLPackage;
    private WmlPackageBuilder wmlPackageBuilder;
    private MainDocumentPart mainDocumentPart;
    private String currentText = "";
    private int sectionLevel = 0;
    private int listLevel = -1;
    private DocBookTableAdapter tableAdapter;
    private TablePart curentTablePart;
    private TableHeader tableHeader;
    private TableBody tableBody;
    private TableFooter tableFooter;
    private Row currentRow;
    private Entry currentEntry;
    private final Stack<Object> docbookObjects = new Stack<>();

    public DocBookUnmarshallerHandler(final DocumentContext documentContext) {
        this.documentContext = documentContext;
    }

    @Override
    public Object getResult() throws IllegalStateException {
        return wordprocessingMLPackage;
    }

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() {
        ApplicationController.startContext(documentContext);
        ApplicationController.getContext().setCurrentListLevel(listLevel);
        try {
            wmlPackageBuilder = WmlPackageBuilder.createPackage(configurationUtils.getTemplate())
                    .styles(configurationUtils.getStyles());
            wordprocessingMLPackage = wmlPackageBuilder.getPackage();
            mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();

            final var styleDefinitionsPart = mainDocumentPart.getStyleDefinitionsPart();
            final var styles = styleDefinitionsPart.getContents();
            final var list = styles.getStyle();
            list.forEach(style -> documentContext.getDocumentStyles().add(style.getStyleId()));

            final var documentInfo = documentContext.getDocumentInfo();
            if (documentInfo.isSectionNumbers()) {
                wmlPackageBuilder.multiLevelHeading();
            }
            if (documentInfo.getExampleCaption() != null) {
                wmlPackageBuilder.multiLevelHeading(DocumentCaption.EXAMPLE);
            }
            if (documentInfo.getTableCaption() != null) {
                wmlPackageBuilder.multiLevelHeading(DocumentCaption.TABLE);
            }
            documentContext.setMainDocumentPart(mainDocumentPart);
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void endDocument() {
        final var documentInfo = documentContext.getDocumentInfo();
        if (documentInfo.isToc() && documentInfo.isSectionNumbers()) {
            new TocGenerator().level(5).tocHeading(documentInfo.getTocTitle()).level(5)
                    .mainDocumentPart(mainDocumentPart).generateToc();
        }
        ApplicationController.endContext();
        if (!docbookObjects.isEmpty()) {
            logger.warn("===================================");
            logger.warn("Docbook objects are non empty");
            while (!docbookObjects.isEmpty()) {
                var builder = docbookObjects.pop();
                logger.warn("Object: " + builder.getClass().getName());
            }

            logger.warn("===================================");
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        logger.info("startPrefixMapping: prefix = {}, uri = {}", prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) {
        logger.info("startPrefixMapping: prefix = {}", prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        logger.debug("Start of element: localName = {}, qName = {}", localName, qName);
        final var id = getId(attributes);
        switch (localName) {
            case ARTICLE:
                sectionLevel = 0;
                docbookObjects.push(new Article().withId(id));
                break;
            case SECTION:
                sectionLevel += 1;
                docbookObjects.push(new Section().withId(id));
                break;
            case TITLE:
                startTitle();
                break;
            case SIMPLE_PARA:
                startSimplePara(id, attributes);
                break;
            case INFORMAL_TABLE:
                startInformalTable(id, attributes);
                break;
            case TABLE_GROUP:
                startTableGroup(attributes);
                break;
            case COLUMN_SPEC:
                startColumnSpec(attributes);
                break;
            case TABLE_HEAD:
                startTableHeader(attributes);
                break;
            case TABLE_BODY:
                startTableBody(attributes);
                break;
            case TABLE_FOOTER:
                startTableFooter(attributes);
                break;
            case ROW:
                currentRow = new Row();
                break;
            case ENTRY:
                startEntry(attributes);
                break;
            case PHRASE:
                startPhrase(id, attributes);
                break;
            case EMPHASIS:
                starEmphasis(id, attributes);
                break;
            case INFO:
            case DATE:
                // ignored
                currentText = "";
                break;
            default:
                logger.warn("Unhandled start element: localName = {}", localName);
                currentText = "";
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        logger.debug("End of element: localName = {}, qName = {}", localName, qName);
        switch (localName) {
            case ARTICLE:
            case SECTION:
                sectionLevel -= 1;
                docbookObjects.pop();
                break;
            case TITLE:
                endTitle();
                break;
            case SIMPLE_PARA:
                endSimplePara();
                break;
            case INFORMAL_TABLE:
                endInformalTable();
                break;
            case PHRASE:
                endPhrase();
                break;
            case EMPHASIS:
                endEmphasis();
                break;
            case TABLE_HEAD:
                endTableHeader();
                break;
            case TABLE_BODY:
                endTableBody();
                break;
            case TABLE_FOOTER:
                endTableFooter();
                break;
            case ROW:
                endRow();
                break;
            case ENTRY:
                endEntry();
                break;
            case INFO:
            case DATE:
            case TABLE_GROUP:
            case COLUMN_SPEC:
                // ignored
                break;
            default:
                logger.warn("Unhandled end element: localName = {}", localName);
                break;
        }
        currentText = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        var s = "";
        try {
            s = new String(Arrays.copyOfRange(ch, start, start + length));
        } catch (Exception ex) {
            s = NEW_LINE;
        }

        final var newLine = NEW_LINE.equals(s);
        if (!newLine) {
            // logger.info("characters: string = \"{}\", whitespace = {}, start = {}, length = {}, total_length = {}", s, newLine, start, length, ch.length);
            currentText += s;
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) {
        var s = "";
        try {
            s = new String(Arrays.copyOfRange(ch, start, start + length));
        } catch (Exception ex) {
            s = NEW_LINE;
        }
        logger.warn("ignorableWhitespace: string = \"{}\", start = {}, length = {}, total_length = {}", s, start, length, ch.length);
    }

    @Override
    public void processingInstruction(String target, String data) {
        switch (target) {
            case "asciidoc-toc":
                documentContext.getDocumentInfo().setToc(true);
                break;
            case "asciidoc-numbered":
                documentContext.getDocumentInfo().setSectionNumbers(true);
                wmlPackageBuilder.multiLevelHeading();
                break;
            case "asciidoc-pagebreak":
                mainDocumentPart.addObject(WmlAdapter.getPageBreak());
                break;
            default:
                logger.info("Unhandled processing instruction: target = {}", target);
                break;
        }
    }

    @Override
    public void skippedEntity(String name) {
        logger.warn("skippedEntity: {}", name);
    }

    private void pushText() {
        if (StringUtils.isNotBlank(currentText)) {
            docbookObjects.push(currentText);
            mergeUpward();
            currentText = "";
        }
    }

    // blocks

    private void startTitle() {
        final var parent = docbookObjects.peek();
        final var titleStyle = configurationUtils.getTitleStyle(sectionLevel, parent.getClass());
        docbookObjects.push(new Title().withId(Utils.getId(parent)).withRole(titleStyle));
    }

    private void endTitle() {
        pushText();
        var title = (Title) docbookObjects.pop();
        addProcessedContent(builderFactory.process(title));
    }

    private void startSimplePara(String id, Attributes attributes) {
        // pushText();
        final var simplePara = new SimplePara().withId(id).withRole(getAttributeValue("role", attributes));
        docbookObjects.push(simplePara);
    }

    private void endSimplePara() {
        pushText();
        var simplePara = (SimplePara) docbookObjects.pop();
        final var list = builderFactory.process(simplePara);
        if (currentEntry == null) {
            addProcessedContent(list);
        } else {
            list.forEach(l -> currentEntry.getContent().add(l));
        }
    }

    private void startInformalTable(String id, Attributes attributes) {
        final var role = getAttributeValue("role", attributes);
        final var frame = UnmarshallerUtils.toFrame(getAttributeValue("frame", attributes));
        final var rowSep = UnmarshallerUtils.toChoice(getAttributeValue("rowsep", attributes));
        final var colSep = UnmarshallerUtils.toChoice(getAttributeValue("colsep", attributes));
        final var tableStyle = getAttributeValue("tabstyle", attributes);
        final var style = getAttributeValue("style", attributes);
        final var informalTable = new InformalTable().withId(id).withRole(role).withFrame(frame).withRowSep(rowSep)
                .withColSep(colSep).withTableStyle(tableStyle).withStyle(style);
        tableAdapter = DocBookTableAdapter.fromInformalTable(informalTable);
        docbookObjects.push(informalTable);
    }

    private void endInformalTable() {
        final var obj = docbookObjects.pop();
        if (!AppUtil.isInstanceOf(InformalTable.class, obj)) {
            throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
        }
        addProcessedContent(builderFactory.process(tableAdapter.getInformalTable()));
    }

    private void startTableGroup(Attributes attributes) {
        final var cols = getAttributeValue("cols", attributes);
        tableAdapter.addTableGroup(new TableGroup().withCols(cols));
    }

    private void startColumnSpec(Attributes attributes) {
        final var columnName = getAttributeValue("colname", attributes);
        final var columnWidth = getAttributeValue("colwidth", attributes);
        final var columnSpec = new ColumnSpec().withColumnName(columnName).withColumnWidth(columnWidth);
        tableAdapter.addColumnSpec(columnSpec);
    }

    private void startTableHeader(Attributes attributes) {
        curentTablePart = TablePart.HEADER;
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toVerticalAlign(getAttributeValue("valign", attributes));
        tableHeader = new TableHeader().withId(getId(attributes)).withAlign(align).withVAlign(valign);
    }

    private void endTableHeader() {
        tableAdapter.addTableHeader(tableHeader);
        tableHeader = null;
        curentTablePart = null;
    }

    private void startTableBody(Attributes attributes) {
        curentTablePart = TablePart.BODY;
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toVerticalAlign(getAttributeValue("valign", attributes));
        tableBody = new TableBody().withId(getId(attributes)).withAlign(align).withVAlign(valign);
    }

    private void endTableBody() {
        tableAdapter.addTableBody(tableBody);
        tableBody = null;
        curentTablePart = null;
    }

    private void startTableFooter(Attributes attributes) {
        curentTablePart = TablePart.FOOTER;
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toVerticalAlign(getAttributeValue("valign", attributes));
        tableFooter = new TableFooter().withId(getId(attributes)).withAlign(align).withVAlign(valign);
    }

    private void endTableFooter() {
        tableAdapter.addTableFooter(tableFooter);
        curentTablePart = null;
        tableFooter = null;
    }

    private void endRow() {
        switch (curentTablePart) {
            case HEADER:
                tableHeader.getRow().add(currentRow);
                break;
            case BODY:
                tableBody.getRow().add(currentRow);
                break;
            case FOOTER:
                tableFooter.getRow().add(currentRow);
                break;
        }
        currentRow = null;
    }

    private void startEntry(Attributes attributes) {
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toBasicVerticalAlign(getAttributeValue("valign", attributes));
        final var nameStart = getAttributeValue("namest", attributes);
        final var nameEnd = getAttributeValue("nameend", attributes);
        final var moreRows = getAttributeValue("morerows", attributes);
        currentEntry = new Entry().withAlign(align).withValign(valign).withNameStart(nameStart).withNameEnd(nameEnd)
                .withMoreRows(moreRows);
    }

    private void endEntry() {
        currentRow.getContent().add(currentEntry);
        currentEntry = null;
    }

    // inlines
    private void startPhrase(String id, Attributes attributes) {
        pushText();
        final var phrase = new Phrase().withId(id).withRole(getAttributeValue("role", attributes));
        docbookObjects.push(phrase);
    }

    private void endPhrase() {
        endInline();
        mergeUpward();
    }

    private void starEmphasis(String id, Attributes attributes) {
        pushText();
        final var emphasis = new Emphasis().withId(id).withRole(getAttributeValue("role", attributes));
        docbookObjects.push(emphasis);
    }

    private void endEmphasis() {
        endInline();
        mergeUpward();
    }

    private void endInline() {
        if (StringUtils.isNotBlank(currentText)) {
            final var obj = docbookObjects.pop();
            if (AppUtil.isInstanceOf(Phrase.class, obj)) {
                final var phrase = (Phrase) obj;
                phrase.getContent().add(currentText);
                docbookObjects.push(phrase);
                // inlineObjects.add(phrase);
            } else if (AppUtil.isInstanceOf(Emphasis.class, obj)) {
                final var emphasis = (Emphasis) obj;
                emphasis.getContent().add(currentText);
                docbookObjects.push(emphasis);
                // inlineObjects.add(emphasis);
            } else {
                throw new IllegalArgumentException("Unhandled object: " + obj.getClass().getName());
            }
        } else {
            final var obj = docbookObjects.peek();
            logger.warn("Current text is blank for: " + obj.getClass().getName());
        }
    }

    private void mergeUpward() {
        final var child = docbookObjects.pop();
        final var parent = docbookObjects.pop();
        if (AppUtil.isInstanceOf(Phrase.class, parent)) {
            final var obj = (Phrase) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (AppUtil.isInstanceOf(Emphasis.class, parent)) {
            final var obj = (Emphasis) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (AppUtil.isInstanceOf(SimplePara.class, parent)) {
            final var obj = (SimplePara) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (AppUtil.isInstanceOf(Title.class, parent)) {
            final var obj = (Title) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else {
            throw new IllegalArgumentException("Unhandled object: " + parent.getClass().getName());
        }
    }

    private void addProcessedContent(List<Object> content) {
        content.forEach(mainDocumentPart::addObject);
    }

    private static String getId(Attributes attributes) {
        var id = getAttributeValue("id", attributes);
        return id == null ? IdGenerator.nextId() : id;
    }

    private static String getAttributeValue(String attributeName, Attributes attributes) {
        return attributes.getValue(attributeName);
    }

    private enum TablePart {
        HEADER, BODY, FOOTER;
    }

}
