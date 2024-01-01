package com.alphasystem.xml;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.builder2.BuilderFactory;
import com.alphasystem.docbook.model.DocumentCaption;
import com.alphasystem.docbook.model.ListInfo;
import com.alphasystem.docbook.model.NotImplementedException;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.TocGenerator;
import com.alphasystem.openxml.builder.wml.UnorderedList;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
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
import java.util.Stack;


public class DocBookUnmarshallerHandler implements UnmarshallerHandler, UnmarshallerConstants {

    private final static String NEW_LINE = System.lineSeparator();

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BuilderFactory builderFactory = BuilderFactory.getInstance();
    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final DocumentContext documentContext;
    private WordprocessingMLPackage wordprocessingMLPackage;
    private WmlPackageBuilder wmlPackageBuilder;
    private MainDocumentPart mainDocumentPart;
    private String currentText = "";
    private int sectionLevel = 0;
    private final Stack<Object> docbookObjects = new Stack<>();
    private final Stack<ListInfo> listInfos = new Stack<>();

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
        ApplicationController.getContext().setCurrentListInfo(getCurrentListInfo());
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
                docbookObjects.push(new Row());
                break;
            case ENTRY:
                startEntry(attributes);
                break;
            case ORDERED_LIST:
                startOrderedList(id, attributes);
                break;
            case ITEMIZED_LIST:
                startItemizedList(id, attributes);
                break;
            case LIST_ITEM:
                startListItem();
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
            case PHRASE:
                endPhrase();
                break;
            case EMPHASIS:
                endEmphasis();
                break;
            case INFORMAL_TABLE:
                endInformalTable();
                break;
            case TABLE_GROUP:
                endTableGroup();
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
            case ORDERED_LIST:
            case ITEMIZED_LIST:
                endList();
                break;
            case LIST_ITEM:
                endListItem();
            case INFO:
            case DATE:
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
            processEndElement();
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
        // title is handled differently
        processContent(docbookObjects.pop());
    }

    private void startSimplePara(String id, Attributes attributes) {
        final var simplePara = new SimplePara().withId(id).withRole(getAttributeValue("role", attributes));
        docbookObjects.push(simplePara);
    }

    private void endSimplePara() {
        pushText();
        processEndElement();
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
        docbookObjects.push(informalTable);
    }

    private void endInformalTable() {
        processEndElement();
    }

    private void startTableGroup(Attributes attributes) {
        final var cols = getAttributeValue("cols", attributes);
        final var tableGroup = new TableGroup().withCols(cols);
        docbookObjects.push(tableGroup);
    }

    private void endTableGroup() {
        processEndElement();
    }

    private void startColumnSpec(Attributes attributes) {
        final var columnName = getAttributeValue("colname", attributes);
        final var columnWidth = getAttributeValue("colwidth", attributes);
        final var columnSpec = new ColumnSpec().withColumnName(columnName).withColumnWidth(columnWidth);
        final var tableGroup = (TableGroup) docbookObjects.pop();
        tableGroup.getColSpec().add(columnSpec);
        docbookObjects.push(tableGroup);
    }

    private void startTableHeader(Attributes attributes) {
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toVerticalAlign(getAttributeValue("valign", attributes));
        final var tableHeader = new TableHeader().withId(getId(attributes)).withAlign(align).withVAlign(valign);
        docbookObjects.push(tableHeader);
    }

    private void endTableHeader() {
        processEndElement();
    }

    private void startTableBody(Attributes attributes) {
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toVerticalAlign(getAttributeValue("valign", attributes));
        final var tableBody = new TableBody().withId(getId(attributes)).withAlign(align).withVAlign(valign);
        docbookObjects.push(tableBody);
    }

    private void endTableBody() {
        processEndElement();
    }

    private void startTableFooter(Attributes attributes) {
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toVerticalAlign(getAttributeValue("valign", attributes));
        final var tableFooter = new TableFooter().withId(getId(attributes)).withAlign(align).withVAlign(valign);
        docbookObjects.push(tableFooter);
    }

    private void endTableFooter() {
        processEndElement();
    }

    private void endRow() {
        processEndElement();
    }

    private void startEntry(Attributes attributes) {
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toBasicVerticalAlign(getAttributeValue("valign", attributes));
        final var nameStart = getAttributeValue("namest", attributes);
        final var nameEnd = getAttributeValue("nameend", attributes);
        final var moreRows = getAttributeValue("morerows", attributes);
        final var entry = new Entry().withAlign(align).withValign(valign).withNameStart(nameStart).withNameEnd(nameEnd)
                .withMoreRows(moreRows);
        docbookObjects.push(entry);
    }

    private void endEntry() {
        processEndElement();
    }

    private void startOrderedList(String id, Attributes attributes) {
        final var numeration = UnmarshallerUtils.toNumeration(getAttributeValue("numeration", attributes), Numeration.ARABIC);
        final var startingNumber = getAttributeValue("startingnumber", attributes);
        final var orderedList = new OrderedList().withId(id).withNumeration(numeration).withStartigNumber(startingNumber);
        docbookObjects.push(orderedList);
        pushListInfo(numeration.value(), true);
    }

    private void startItemizedList(String id, Attributes attributes) {
        final var mark = getAttributeValue("mark", attributes);
        docbookObjects.push(new ItemizedList().withId(id).withMark(mark));
        pushListInfo(mark, false);
    }

    private void endList() {
        processEndElement();
        listInfos.pop();
        var listInfo = new ListInfo();
        if (!listInfos.isEmpty()) {
            listInfo = listInfos.peek();
        }
        ApplicationController.getContext().setCurrentListInfo(listInfo);
    }

    private void startListItem() {
        docbookObjects.push(new ListItem());
    }

    private void endListItem() {
        processEndElement();
    }

    // inlines
    private void startPhrase(String id, Attributes attributes) {
        pushText();
        final var phrase = new Phrase().withId(id).withRole(getAttributeValue("role", attributes));
        docbookObjects.push(phrase);
    }

    private void endPhrase() {
        endInline();
        processEndElement();
    }

    private void starEmphasis(String id, Attributes attributes) {
        pushText();
        final var emphasis = new Emphasis().withId(id).withRole(getAttributeValue("role", attributes));
        docbookObjects.push(emphasis);
    }

    private void endEmphasis() {
        endInline();
        processEndElement();
    }

    private void endInline() {
        if (StringUtils.isNotBlank(currentText)) {
            final var obj = docbookObjects.pop();
            if (isPhraseType(obj)) {
                final var phrase = (Phrase) obj;
                phrase.getContent().add(currentText);
                docbookObjects.push(phrase);
            } else if (isEmphasisType(obj)) {
                final var emphasis = (Emphasis) obj;
                emphasis.getContent().add(currentText);
                docbookObjects.push(emphasis);
            } else {
                throw new IllegalArgumentException("Unhandled object: " + obj.getClass().getName());
            }
        } else {
            final var obj = docbookObjects.peek();
            logger.warn("Current text is blank for: " + obj.getClass().getName());
        }
    }

    private void processEndElement() {
        final var child = docbookObjects.pop();
        final var parent = docbookObjects.pop();
        logger.info("Processing end element of \"{}\" of \"{}\".", child.getClass().getName(), parent.getClass().getName());
        if (isPhraseType(parent)) {
            final var obj = (Phrase) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isEmphasisType(parent)) {
            final var obj = (Emphasis) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isSimpleParaType(parent)) {
            final var obj = (SimplePara) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isTitleType(parent)) {
            final var obj = (Title) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isInformalTableType(parent)) {
            final var obj = (InformalTable) parent;
            if (isTableGroupType(child)) {
                obj.getTableGroup().add((TableGroup) child);
            } else {
                throw new NotImplementedException(parent, child);
            }
            docbookObjects.push(obj);
        } else if (isTableGroupType(parent)) {
            final var obj = (TableGroup) parent;
            if (isTableHeaderType(child)) {
                obj.setTableHeader((TableHeader) child);
            } else if (isTableBodyType(child)) {
                obj.setTableBody((TableBody) child);
            } else if (isTableFooterType(child)) {
                obj.setTableFooter((TableFooter) child);
            } else {
                throw new NotImplementedException(parent, child);
            }
            docbookObjects.push(obj);
        } else if (isTableHeaderType(parent)) {
            final var obj = (TableHeader) parent;
            if (isRowType(child)) {
                obj.getRow().add((Row) child);
            } else if (isColumnSpecType(child)) {
                obj.getColSpec().add((ColumnSpec) child);
            } else if (isTrType(child)) {
                throw new NotImplementedException(parent, child);
            }
            docbookObjects.push(obj);
        } else if (isTableBodyType(parent)) {
            final var obj = (TableBody) parent;
            if (isRowType(child)) {
                obj.getRow().add((Row) child);
            } else if (isTrType(child)) {
                throw new IllegalArgumentException("\"Tr\" is not implemented yet");
            }
            docbookObjects.push(obj);
        } else if (isTableFooterType(parent)) {
            final var obj = (TableFooter) parent;
            if (isRowType(child)) {
                obj.getRow().add((Row) child);
            } else if (isColumnSpecType(child)) {
                obj.getColSpec().add((ColumnSpec) child);
            } else if (isTrType(child)) {
                throw new IllegalArgumentException("\"Tr\" is not implemented yet");
            }
            docbookObjects.push(obj);
        } else if (isRowType(parent)) {
            final var obj = (Row) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isEntryType(parent)) {
            final var obj = (Entry) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isOrderedListType(parent)) {
            final var obj = (OrderedList) parent;
            if (isListItemType(child)) {
                obj.getListItem().add((ListItem) child);
            } else {
                throw new NotImplementedException(parent, child);
            }
            docbookObjects.push(obj);
        } else if (isItemizedListType(parent)) {
            final var obj = (ItemizedList) parent;
            if (isListItemType(child)) {
                obj.getListItem().add((ListItem) child);
            } else {
                throw new NotImplementedException(parent, child);
            }
            docbookObjects.push(obj);
        } else if (isListItemType(parent)) {
            final var obj = (ListItem) parent;
            obj.getContent().add(child);
            docbookObjects.push(obj);
        } else if (isSectionType(parent)) {
            // now process the content add it to document
            processContent(child);
            docbookObjects.push(parent);
        } else if (isArticleType(parent)) {
            if (isSectionType(child)) {
                logger.warn("Not sure how to handle section");
            }
            processContent(child);
            docbookObjects.push(parent);
        } else {
            throw new NotImplementedException(parent, child);
        }
    }

    private void processContent(Object content) {
        logger.info("Processing content: {}", content.getClass().getName());
        final var processedContent = builderFactory.process(content);
        if (processedContent != null) {
            processedContent.forEach(mainDocumentPart::addObject);
        }
    }

    private void pushListInfo(String styleName, boolean ordered) {
        final var listItem = getItemByName(styleName, ordered);
        var level = 0L;
        if (!listInfos.isEmpty()) {
            level = listInfos.peek().getLevel() + 1L;
        }
        final var listInfo = new ListInfo(listItem.getNumberId(), level);
        ApplicationController.getContext().setCurrentListInfo(listInfo);
        listInfos.push(listInfo);
    }

    private ListInfo getCurrentListInfo() {
        var listInfo = new ListInfo();
        if (!listInfos.isEmpty()) {
            listInfo = listInfos.peek();
        }
        return listInfo;
    }

    private static String getId(Attributes attributes) {
        var id = getAttributeValue("id", attributes);
        return id == null ? IdGenerator.nextId() : id;
    }

    private static String getAttributeValue(String attributeName, Attributes attributes) {
        return attributes.getValue(attributeName);
    }

    private static com.alphasystem.openxml.builder.wml.ListItem<?> getItemByName(String styleName, boolean ordered) {
        if (ordered) {
            return com.alphasystem.openxml.builder.wml.OrderedList.getByStyleName(styleName);
        } else {
            return UnorderedList.getByStyleName(styleName);
        }
    }
}
