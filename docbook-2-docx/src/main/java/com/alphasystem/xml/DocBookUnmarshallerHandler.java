package com.alphasystem.xml;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.docbook.model.NotImplementedException;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docbook.util.Utils;
import com.alphasystem.openxml.builder.wml.TocGenerator;
import com.alphasystem.openxml.builder.wml.UnorderedList;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import javax.xml.bind.UnmarshallerHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.alphasystem.xml.UnmarshallerConstants.*;


public class DocBookUnmarshallerHandler implements UnmarshallerHandler, UnmarshallerConstants {

    private final static String NEW_LINE = System.lineSeparator();

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BuilderFactory builderFactory = BuilderFactory.getInstance();
    private final ConfigurationUtils configurationUtils = ConfigurationUtils.getInstance();
    private final DocumentContext documentContext;
    private String currentText = "";
    private int sectionLevel = 0;
    private final Stack<Object> docbookObjects = new Stack<>();

    public DocBookUnmarshallerHandler() {
        this.documentContext = ApplicationController.getContext();
    }

    @Override
    public Object getResult() throws IllegalStateException {
        return ApplicationController.getContext().getWordprocessingMLPackage();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
        final var documentInfo = documentContext.getDocumentInfo();
        if (documentInfo.isToc() && documentInfo.isSectionNumbers()) {
            new TocGenerator().level(5).tocHeading(documentInfo.getTocTitle()).level(5).mainDocumentPart(ApplicationController.getContext().getMainDocumentPart()).generateToc();
        }
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
        logger.trace("startPrefixMapping: prefix = {}, uri = {}", prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) {
        logger.trace("endPrefixMapping: prefix = {}", prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        logger.debug("Start of element: localName = {}, qName = {}", localName, qName);
        final var id = getId(attributes);
        final var xrefLabel = getXrefLabel(attributes);
        if (StringUtils.isNotBlank(xrefLabel)) {
            ApplicationController.getContext().putLabel(id, xrefLabel);
        }
        if (StringUtils.isWhitespace(currentText)) {
            currentText = "";
        } else {
            endInline();
        }
        switch (localName) {
            case ARTICLE:
                sectionLevel = 0;
                docbookObjects.push(new Article().withId(id));
                break;
            case CAUTION:
                startCaution(id, attributes);
                break;
            case COLUMN_SPEC:
                startColumnSpec(attributes);
                break;
            case CROSS_REFERENCE:
                startCrossReference(id, attributes);
                break;
            case EMPHASIS:
                starEmphasis(id, attributes);
                break;
            case ENTRY:
                startEntry(attributes);
                break;
            case EXAMPLE:
                startExample(id);
                break;
            case FORMAL_PARA:
                startFormalPara(id, attributes);
                break;
            case IMPORTANT:
                startImportant(id, attributes);
                break;
            case INFORMAL_TABLE:
                startInformalTable(id, attributes);
                break;
            case INFORMAL_EXAMPLE:
                startInformalExample(id);
                break;
            case ITEMIZED_LIST:
                startItemizedList(id, attributes);
                break;
            case LINK:
                startLink(id, attributes);
                break;
            case LIST_ITEM:
                startListItem();
                break;
            case LITERAL:
                startLiteral(id, attributes);
                break;
            case NOTE:
                startNote(id, attributes);
                break;
            case ORDERED_LIST:
                startOrderedList(id, attributes);
                break;
            case PARA:
                startPara(id, attributes);
                break;
            case PHRASE:
                startPhrase(id, attributes);
                break;
            case PROGRAM_LISTING:
                startProgramListing(id, attributes);
                break;
            case ROW:
                startRow();
                break;
            case SECTION:
                sectionLevel += 1;
                docbookObjects.push(new Section().withId(id));
                break;
            case SIDE_BAR:
                startSideBar(id, attributes);
                break;
            case SIMPLE_PARA:
                startSimplePara(id, attributes);
                break;
            case SUBSCRIPT:
                startSubscript(id, attributes);
                break;
            case SUPERSCRIPT:
                startSuperscript(id, attributes);
                break;
            case TABLE:
                startTable(id, attributes);
                break;
            case TABLE_BODY:
                startTableBody(attributes);
                break;
            case TABLE_FOOTER:
                startTableFooter(attributes);
                break;
            case TABLE_GROUP:
                startTableGroup(attributes);
                break;
            case TABLE_HEAD:
                startTableHeader(attributes);
                break;
            case TERM:
                startTerm(id, attributes);
                break;
            case TIP:
                startTip(id, attributes);
                break;
            case TITLE:
                startTitle();
                break;
            case VARIABLE_LIST:
                startVariableList();
                break;
            case VARIABLE_LIST_ENTRY:
                startVariableListEntry();
                break;
            case WARNING:
                startWarning(id, attributes);
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
            case CAUTION:
                endCaution();
                break;
            case CROSS_REFERENCE:
                endCrossReference();
                break;
            case EMPHASIS:
                endEmphasis();
                break;
            case ENTRY:
                endEntry();
                break;
            case EXAMPLE:
                endExample();
                break;
            case FORMAL_PARA:
                endFormalPara();
                break;
            case IMPORTANT:
                endImportant();
                break;
            case INFORMAL_EXAMPLE:
                endInformalExample();
                break;
            case INFORMAL_TABLE:
                endInformalTable();
                break;
            case ITEMIZED_LIST:
            case ORDERED_LIST:
                endList();
                break;
            case LINK:
                endLink();
                break;
            case LIST_ITEM:
                endListItem();
                break;
            case LITERAL:
                endLiteral();
                break;
            case NOTE:
                endNote();
                break;
            case PARA:
                endPara();
                break;
            case PHRASE:
                endPhrase();
                break;
            case PROGRAM_LISTING:
                endProgramListing();
                break;
            case ROW:
                endRow();
                break;
            case SIDE_BAR:
                endSideBar();
                break;
            case SIMPLE_PARA:
                endSimplePara();
                break;
            case SUBSCRIPT:
                endSubscript();
                break;
            case SUPERSCRIPT:
                endSuperscript();
                break;
            case TABLE:
                endTable();
                break;
            case TABLE_BODY:
                endTableBody();
                break;
            case TABLE_FOOTER:
                endTableFooter();
                break;
            case TABLE_GROUP:
                endTableGroup();
                break;
            case TABLE_HEAD:
                endTableHeader();
                break;
            case TERM:
                endTerm();
                break;
            case TIP:
                endTip();
                break;
            case TITLE:
                endTitle();
                break;
            case VARIABLE_LIST:
                endVariableList();
                break;
            case VARIABLE_LIST_ENTRY:
                endVariableListEntry();
                break;
            case WARNING:
                endWarning();
                break;
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
                ApplicationController.getContext().getWmlPackageBuilder().multiLevelHeading();
                break;
            case "asciidoc-pagebreak":
                ApplicationController.getContext().getMainDocumentPart().addObject(WmlAdapter.getPageBreak());
                break;
            case "asciidoc-br":
            case "linebreak":
                currentText += System.lineSeparator();
                break;
            default:
                logger.warn("Unhandled processing instruction: target = {}", target);
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

    private void startCaution(String id, Attributes attributes) {
        final var caution = new Caution().withId(id).withRole(getRole(attributes));
        docbookObjects.push(caution);
    }

    private void endCaution() {
        pushText();
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

    private void startCrossReference(String id, Attributes attributes) {
        pushText();
        final var xref = new CrossReference().withId(id).withRole(getRole(attributes)).withLinkend(getAttributeValue("linkend", attributes)).withHref(getAttributeValue("href", attributes)).withEndterm(getAttributeValue("endterm", attributes));
        docbookObjects.push(xref);
    }

    private void endCrossReference() {
        endInline();
        processEndElement();
    }

    private void starEmphasis(String id, Attributes attributes) {
        pushText();
        final var emphasis = new Emphasis().withId(id).withRole(getRole(attributes));
        docbookObjects.push(emphasis);
    }

    private void endEmphasis() {
        endInline();
        processEndElement();
    }

    private void startEntry(Attributes attributes) {
        final var align = UnmarshallerUtils.toAlign(getAttributeValue("align", attributes));
        final var valign = UnmarshallerUtils.toBasicVerticalAlign(getAttributeValue("valign", attributes));
        final var nameStart = getAttributeValue("namest", attributes);
        final var nameEnd = getAttributeValue("nameend", attributes);
        final var moreRows = getAttributeValue("morerows", attributes);
        final var entry = new Entry().withAlign(align).withValign(valign).withNameStart(nameStart).withNameEnd(nameEnd).withMoreRows(moreRows);
        docbookObjects.push(entry);
    }

    private void endEntry() {
        processEndElement();
    }

    private void startExample(String id) {
        docbookObjects.push(new Example().withId(id));
    }

    private void endExample() {
        processEndElement();
    }

    private void startFormalPara(String id, Attributes attributes) {
        final var formalPara = new FormalPara().withId(id).withRole(getRole(attributes));
        docbookObjects.push(formalPara);
    }

    private void endFormalPara() {
        pushText();
        processEndElement();
    }

    private void startImportant(String id, Attributes attributes) {
        final var important = new Important().withId(id).withRole(getRole(attributes));
        docbookObjects.push(important);
    }

    private void endImportant() {
        pushText();
        processEndElement();
    }

    private void startInformalExample(String id) {
        docbookObjects.push(new InformalExample().withId(id));
    }

    private void endInformalExample() {
        processEndElement();
    }

    private void startInformalTable(String id, Attributes attributes) {
        final var role = getRole(attributes);
        final var frame = UnmarshallerUtils.toFrame(getAttributeValue("frame", attributes));
        final var rowSep = UnmarshallerUtils.toChoice(getAttributeValue("rowsep", attributes));
        final var colSep = UnmarshallerUtils.toChoice(getAttributeValue("colsep", attributes));
        final var tableStyle = getAttributeValue("tabstyle", attributes);
        final var style = getAttributeValue("style", attributes);
        final var informalTable = new InformalTable().withId(id).withRole(role).withFrame(frame).withRowSep(rowSep).withColSep(colSep).withTableStyle(tableStyle).withStyle(style);
        docbookObjects.push(informalTable);
    }

    private void endInformalTable() {
        processEndElement();
    }

    private void startItemizedList(String id, Attributes attributes) {
        var mark = getAttributeValue("mark", attributes);
        if (Objects.isNull(mark)) {
            mark = UnorderedList.values()[0].getStyleName();
        }
        docbookObjects.push(new ItemizedList().withId(id).withMark(mark));
    }

    private void startLink(String id, Attributes attributes) {
        pushText();
        final var link = new Link().withId(id).withRole(getRole(attributes)).withLinkend(getAttributeValue("linkend", attributes)).withHref(getAttributeValue("href", attributes)).withEndterm(getAttributeValue("endterm", attributes));
        docbookObjects.push(link);
    }

    private void endLink() {
        endInline();
        processEndElement();
    }

    private void endList() {
        processEndElement();
    }

    private void startListItem() {
        docbookObjects.push(new ListItem());
    }

    private void endListItem() {
        processEndElement();
    }

    private void startLiteral(String id, Attributes attributes) {
        pushText();
        final var literal = new Literal().withId(id).withRole(getRole(attributes));
        docbookObjects.push(literal);
    }

    private void endLiteral() {
        endInline();
        processEndElement();
    }

    private void startNote(String id, Attributes attributes) {
        final var note = new Note().withId(id).withRole(getRole(attributes));
        docbookObjects.push(note);
    }

    private void endNote() {
        pushText();
        processEndElement();
    }

    private void startOrderedList(String id, Attributes attributes) {
        final var numeration = UnmarshallerUtils.toNumeration(getAttributeValue("numeration", attributes), Numeration.ARABIC);
        final var startingNumber = getAttributeValue("startingnumber", attributes);
        final var orderedList = new OrderedList().withId(id).withNumeration(numeration).withStartigNumber(startingNumber);
        docbookObjects.push(orderedList);
    }

    private void startPara(String id, Attributes attributes) {
        final var para = new Para().withId(id).withRole(getRole(attributes));
        docbookObjects.push(para);
    }

    private void endPara() {
        pushText();
        processEndElement();
    }

    private void startPhrase(String id, Attributes attributes) {
        final var phrase = new Phrase().withId(id).withRole(getRole(attributes));
        docbookObjects.push(phrase);
    }

    private void endPhrase() {
        endInline();
        processEndElement();
    }

    private void startProgramListing(String id, Attributes attributes) {
        pushText();
        final var programListing = new ProgramListing().withId(id).withRole(getRole(attributes, "ProgramListingCode"));
        docbookObjects.push(programListing);
    }

    private void endProgramListing() {
        pushText();
        processEndElement();
    }

    private void startRow() {
        docbookObjects.push(new Row());
    }

    private void endRow() {
        processEndElement();
    }

    private void startSideBar(String id, Attributes attributes) {
        final var sideBar = new SideBar().withId(id).withRole(getRole(attributes));
        docbookObjects.push(sideBar);
    }

    private void endSideBar() {
        processEndElement();
    }

    private void startSimplePara(String id, Attributes attributes) {
        final var simplePara = new SimplePara().withId(id).withRole(getRole(attributes));
        docbookObjects.push(simplePara);
    }

    private void endSimplePara() {
        pushText();
        processEndElement();
    }

    private void startSubscript(String id, Attributes attributes) {
        pushText();
        final var subscript = new Subscript().withId(id).withRole(getRole(attributes));
        docbookObjects.push(subscript);
    }

    private void endSubscript() {
        endInline();
        processEndElement();
    }

    private void startSuperscript(String id, Attributes attributes) {
        pushText();
        final var superscript = new Superscript().withId(id).withRole(getRole(attributes));
        docbookObjects.push(superscript);
    }

    private void endSuperscript() {
        endInline();
        processEndElement();
    }

    private void startTable(String id, Attributes attributes) {
        final var role = getRole(attributes);
        final var frame = UnmarshallerUtils.toFrame(getAttributeValue("frame", attributes));
        final var rowSep = UnmarshallerUtils.toChoice(getAttributeValue("rowsep", attributes));
        final var colSep = UnmarshallerUtils.toChoice(getAttributeValue("colsep", attributes));
        final var tableStyle = getAttributeValue("tabstyle", attributes);
        final var style = getAttributeValue("style", attributes);
        final var table = new Table().withId(id).withRole(role).withFrame(frame).withRowSep(rowSep).withColSep(colSep).withTableStyle(tableStyle).withStyle(style);
        docbookObjects.push(table);
    }

    private void endTable() {
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

    private void startTableGroup(Attributes attributes) {
        final var cols = getAttributeValue("cols", attributes);
        final var tableGroup = new TableGroup().withCols(cols);
        docbookObjects.push(tableGroup);
    }

    private void endTableGroup() {
        processEndElement();
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

    private void startTerm(String id, Attributes attributes) {
        pushText();
        final var term = new Term().withId(id).withRole(getRole(attributes));
        docbookObjects.push(term);
    }

    private void endTerm() {
        endInline();
        processEndElement();
    }

    private void startTip(String id, Attributes attributes) {
        final var tip = new Tip().withId(id).withRole(getRole(attributes));
        docbookObjects.push(tip);
    }

    private void endTip() {
        pushText();
        processEndElement();
    }

    private void startTitle() {
        final var parent = docbookObjects.peek();
        final var titleStyle = configurationUtils.getTitleStyle(sectionLevel, parent.getClass());
        docbookObjects.push(new Title().withId(Utils.getId(parent)).withRole(titleStyle));
    }

    private void endTitle() {
        pushText();
        // title is handled differently
        final var title = (Title) docbookObjects.peek();
        final var linkText = getLinkText("", title.getContent());
        if (StringUtils.isNotBlank(linkText)) {
            ApplicationController.getContext().putLabel(title.getId(), linkText);
        }
        processEndElement();
    }

    private void startVariableList() {
        docbookObjects.push(new VariableList());
    }

    private void endVariableList() {
        processEndElement();
    }

    private void startVariableListEntry() {
        docbookObjects.push(new VariableListEntry());
    }

    private void endVariableListEntry() {
        processEndElement();
    }

    private void startWarning(String id, Attributes attributes) {
        final var warning = new Warning().withId(id).withRole(getRole(attributes));
        docbookObjects.push(warning);
    }

    private void endWarning() {
        pushText();
        processEndElement();
    }

    // START OF HANDLER FUNCTIONS TO PROCESS END ELEMENTS

    /*
     * For Article or Section types we process the content.
     */
    private void handleArticleOrSection(Object parent, Object child) {
        processContent(child);
        docbookObjects.push(parent);
    }

    private void handleCaution(Caution obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleEmphasis(Emphasis obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleEntry(Entry obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleExample(Example obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else if (isInfoType(child) || isCaptionType(child)) {
            logger.warn("Unhandled child for Example: {}", child.getClass().getName());
        } else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleFormalPara(FormalPara obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else if (isParaType(child)) obj.setPara((Para) child);
        else logger.warn("Unhandled child for FormalPara: {}", child.getClass().getName());
        docbookObjects.push(obj);
    }

    private void handleImportant(Important obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleInformalExample(InformalExample obj, Object child) {
        if (isInfoType(child) || isCaptionType(child)) {
            logger.warn("Unhandled child for InformalExample: {}", child.getClass().getName());
        } else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleInformalTable(InformalTable obj, Object child) {
        if (isTableGroupType(child)) {
            obj.getTableGroup().add((TableGroup) child);
        } else {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleItemizedList(ItemizedList obj, Object child) {
        if (isListItemType(child)) {
            obj.getListItem().add((ListItem) child);
        } else if (isTitleType(child)) {
            obj.getTitleContent().add(child);
        } else {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleLink(Link obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleListItem(ListItem obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleLiteral(Literal obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleNote(Note obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleOrderedList(OrderedList obj, Object child) {
        if (isListItemType(child)) {
            obj.getListItem().add((ListItem) child);
        } else if (isTitleType(child)) {
            obj.getTitleContent().add(child);
        } else {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handlePara(Para obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handlePhrase(Phrase obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleProgramListing(ProgramListing obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleRow(Row obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleSideBar(SideBar obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleSimplePara(SimplePara obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleSubscript(Subscript obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleSuperscript(Superscript obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleTable(Table obj, Object child) {
        if (isTableGroupType(child)) {
            obj.getTableGroup().add((TableGroup) child);
        } else if (isTitleType(child)) {
            obj.setTitle((Title) child);
        } else {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleTableBody(TableBody obj, Object child) {
        if (isRowType(child)) {
            obj.getRow().add((Row) child);
        } else if (isTrType(child)) {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleTableFooter(TableFooter obj, Object child) {
        if (isRowType(child)) {
            obj.getRow().add((Row) child);
        } else if (isColumnSpecType(child)) {
            obj.getColSpec().add((ColumnSpec) child);
        } else if (isTrType(child)) {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleTableGroup(TableGroup obj, Object child) {
        if (isTableHeaderType(child)) {
            obj.setTableHeader((TableHeader) child);
        } else if (isTableBodyType(child)) {
            obj.setTableBody((TableBody) child);
        } else if (isTableFooterType(child)) {
            obj.setTableFooter((TableFooter) child);
        } else {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleTableHeader(TableHeader obj, Object child) {
        if (isRowType(child)) {
            obj.getRow().add((Row) child);
        } else if (isColumnSpecType(child)) {
            obj.getColSpec().add((ColumnSpec) child);
        } else if (isTrType(child)) {
            throw new NotImplementedException(obj, child);
        }
        docbookObjects.push(obj);
    }

    private void handleTerm(Term obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleTip(Tip obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleTitle(Title obj, Object child) {
        obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    private void handleVariableListEntry(VariableListEntry obj, Object child) {
        if (isTermType(child)) {
            obj.getTerm().add((Term) child);
        } else if (isListItemType(child)) {
            obj.setListItem((ListItem) child);
        } else {
            logger.warn("Unhandled child \"{}\" of \"VariableListEntry\"", child.getClass().getName());
        }
        docbookObjects.push(obj);
    }

    private void handleVariableList(VariableList obj, Object child) {
        if (isVariableListEntryType(child)) {
            obj.getVariableListEntry().add((VariableListEntry) child);
        } else {
            logger.warn("Unhandled child \"{}\" of \"VariableList\"", child.getClass().getName());
        }
        docbookObjects.push(obj);
    }

    private void handleWarning(Warning obj, Object child) {
        if (isTitleType(child)) obj.getTitleContent().add(child);
        else obj.getContent().add(child);
        docbookObjects.push(obj);
    }

    // END OF HANDLER FUNCTIONS TO PROCESS END ELEMENTS

    // HELPER FUNCTIONS

    private void endInline() {
        if (StringUtils.isNotBlank(currentText)) {
            final var parent = docbookObjects.pop();
            if (isArticleType(parent)) {
                throw new NotImplementedException(parent, currentText);
            } else if (isCautionType(parent)) {
                final var obj = (Caution) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } if (isCrossReferenceType(parent)) {
                docbookObjects.push(parent);
            } else if (isEmphasisType(parent)) {
                final var obj = (Emphasis) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isEntryType(parent)) {
                final var obj = (Entry) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isExampleType(parent)) {
                final var obj = (Example) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            }  else if (isImportantType(parent)) {
                final var obj = (Important) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isInformalExampleType(parent)) {
                final var obj = (InformalExample) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isItemizedListType(parent)) {
                final var obj = (ItemizedList) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isLinkType(parent)) {
                final var obj = (Link) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isListItemType(parent)) {
                final var obj = (ListItem) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isLiteralType(parent)) {
                final var obj = (Literal) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isNoteType(parent)) {
                final var obj = (Note) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isOrderedListType(parent)) {
                final var obj = (OrderedList) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isParaType(parent)) {
                final var obj = (Para) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isPhraseType(parent)) {
                final var obj = (Phrase) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isProgramListingType(parent)) {
                final var obj = (ProgramListing) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isRowType(parent)) {
                final var obj = (Row) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isSectionType(parent)) {
                throw new NotImplementedException(parent, currentText);
            } else if (isSideBarType(parent)) {
                final var obj = (SideBar) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isSimpleParaType(parent)) {
                final var obj = (SimplePara) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isSubscriptType(parent)) {
                final var subscript = (Subscript) parent;
                subscript.getContent().add(currentText);
                docbookObjects.push(subscript);
            } else if (isSuperscriptType(parent)) {
                final var obj = (Superscript) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isTermType(parent)) {
                final var obj = (Term) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isTipType(parent)) {
                final var obj = (Tip) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isTitleType(parent)) {
                final var obj = (Title) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isVariableListType(parent)) {
                final var obj = (VariableList) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isWarningType(parent)) {
                final var obj = (Warning) parent;
                obj.getContent().add(currentText);
                docbookObjects.push(obj);
            } else if (isFormalParaType(parent) || isVariableListEntryType(parent) || isTableHeaderType(parent) ||
                    isTableGroupType(parent) || isTableFooterType(parent) || isTableBodyType(parent) || isTableType(parent) ||
                    isInformalTableType(parent)) {
                logger.trace("No applicable");
            } else {
                throw new IllegalArgumentException("Unhandled text in endInline: " + parent.getClass().getName());
            }
        } else {
            final var obj = docbookObjects.peek();
            logger.warn("Current text is blank for: " + obj.getClass().getName());
        }
        currentText = "";
    }

    private void processEndElement() {
        final var child = docbookObjects.pop();
        final var parent = docbookObjects.pop();
        logger.debug("Processing end element of \"{}\" of \"{}\".", child.getClass().getName(), parent.getClass().getName());

        if (isArticleType(parent)) {
            if (isSectionType(child)) {
                logger.warn("Not sure how to handle section");
            }
            handleArticleOrSection(parent, child);
        } else if (isCautionType(parent)) {
            handleCaution((Caution) parent, child);
        } else if (isEmphasisType(parent)) {
            handleEmphasis((Emphasis) parent, child);
        } else if (isEntryType(parent)) {
            handleEntry((Entry) parent, child);
        } else if (isExampleType(parent)) {
            handleExample((Example) parent, child);
        } else if (isFormalParaType(parent)) {
            handleFormalPara((FormalPara) parent, child);
        } else if (isImportantType(parent)) {
            handleImportant((Important) parent, child);
        } else if (isInformalExampleType(parent)) {
            handleInformalExample((InformalExample) parent, child);
        } else if (isInformalTableType(parent)) {
            handleInformalTable((InformalTable) parent, child);
        } else if (isItemizedListType(parent)) {
            handleItemizedList((ItemizedList) parent, child);
        } else if (isLinkType(parent)) {
            handleLink((Link) parent, child);
        } else if (isListItemType(parent)) {
            handleListItem((ListItem) parent, child);
        } else if (isLiteralType(parent)) {
            handleLiteral((Literal) parent, child);
        } else if (isNoteType(parent)) {
            handleNote((Note) parent, child);
        } else if (isOrderedListType(parent)) {
            handleOrderedList((OrderedList) parent, child);
        } else if (isParaType(parent)) {
            handlePara((Para) parent, child);
        } else if (isPhraseType(parent)) {
            handlePhrase((Phrase) parent, child);
        } else if (isProgramListingType(parent)) {
            handleProgramListing((ProgramListing) parent, child);
        } else if (isRowType(parent)) {
            handleRow((Row) parent, child);
        } else if (isSectionType(parent)) {
            handleArticleOrSection(parent, child);
        } else if (isSideBarType(parent)) {
            handleSideBar((SideBar) parent, child);
        } else if (isSimpleParaType(parent)) {
            handleSimplePara((SimplePara) parent, child);
        } else if (isSubscriptType(parent)) {
            handleSubscript((Subscript) parent, child);
        } else if (isSuperscriptType(parent)) {
            handleSuperscript((Superscript) parent, child);
        } else if (isTableType(parent)) {
            handleTable((Table) parent, child);
        } else if (isTableBodyType(parent)) {
            handleTableBody((TableBody) parent, child);
        } else if (isTableFooterType(parent)) {
            handleTableFooter((TableFooter) parent, child);
        } else if (isTableGroupType(parent)) {
            handleTableGroup((TableGroup) parent, child);
        } else if (isTableHeaderType(parent)) {
            handleTableHeader((TableHeader) parent, child);
        } else if (isTermType(parent)) {
            handleTerm((Term) parent, child);
        } else if (isTipType(parent)) {
            handleTip((Tip) parent, child);
        } else if (isTitleType(parent)) {
            handleTitle((Title) parent, child);
        } else if (isVariableListType(parent)) {
            handleVariableList((VariableList) parent, child);
        } else if (isVariableListEntryType(parent)) {
            handleVariableListEntry((VariableListEntry) parent, child);
        } else if (isWarningType(parent)) {
            handleWarning((Warning) parent, child);
        } else {
            throw new NotImplementedException(parent, child);
        }
    }

    private String getLinkText(String result, List<Object> contents) {
        if (Objects.isNull(contents) || contents.isEmpty()) {
            return result;
        }
        final var collectedText = contents.stream().map(content -> {
            if (isStringType(content)) {
                return (String) content;
            } else if (isEmphasisType(content)) {
                return getLinkText(result, ((Emphasis) content).getContent());
            } else if (isPhraseType(content)) {
                return getLinkText(result, ((Phrase) content).getContent());
            } else if (isSuperscriptType(content)) {
                return getLinkText(result, ((Superscript) content).getContent());
            } else {
                logger.warn("Not sure how to get text from: ");
                return "";
            }
        }).collect(Collectors.joining(""));

        return result + collectedText;
    }

    private void processContent(Object content) {
        logger.debug("Processing content: {}", content.getClass().getName());
        final var processedContent = builderFactory.process(content, null);
        if (processedContent != null) {
            processedContent.forEach(obj -> ApplicationController.getContext().getMainDocumentPart().addObject(obj));
        }
    }

    private static String getId(Attributes attributes) {
        var id = getAttributeValue("xml:id", attributes);
        return StringUtils.isBlank(id) ? IdGenerator.nextId() : id;
    }

    private static String getRole(Attributes attributes) {
        return getRole(attributes, null);
    }

    private static String getRole(Attributes attributes, String defaultRole) {
        final var role = getAttributeValue("role", attributes);
        return StringUtils.isBlank(role) ? defaultRole : role;
    }

    private static String getXrefLabel(Attributes attributes) {
        return getAttributeValue("xreflabel", attributes);
    }

    private static String getAttributeValue(String attributeName, Attributes attributes) {
        return attributes.getValue(attributeName);
    }
}
