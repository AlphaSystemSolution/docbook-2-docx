package com.alphasystem.docbook.builder.test;

import com.alphasystem.util.IdGenerator;
import com.alphasystem.util.JAXBTool;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * @author sali
 */
public final class DataFactory {
    private static final JAXBTool jaxbTool = new JAXBTool();

    private static final ObjectFactory objectFactory = new ObjectFactory();

    static {
        jaxbTool.setMarshallerProperty("jaxb.formatted.output", false);
    }

    public static Article createArticle(Object... content) {
        return createArticle(null, content);
    }

    public static Article createArticle(String title, Object... content) {
        var article = new Article().withId(IdGenerator.nextId());
        if (StringUtils.isNotBlank(title)) {
            article.withContent(createInfo(title));
        }
        return article.withContent(content);
    }

    public static Info createInfo(Object... title) {
        return new Info().withContent(new Title().withContent(title));
    }

    public static Emphasis createBold(Object... content) {
        return createEmphasis("strong", content);
    }

    public static Caution createCaution(Object... content) {
        return objectFactory.createCaution().withContent(content);
    }

    public static Entry createEntry(Align align, BasicVerticalAlign vAlign, Object... content) {
        return createEntry(align, vAlign, null, null, null, content);
    }

    public static Entry createEntry(Align align, BasicVerticalAlign vAlign, String nameStart, String nameEnd,
                                    String moreRows, Object... content) {
        return objectFactory.createEntry().withAlign(align).withValign(vAlign).withNameStart(nameStart).withNameEnd(nameEnd)
                .withMoreRows(moreRows).withContent(content);
    }

    public static Emphasis createEmphasis(String role, Object... content) {
        return objectFactory.createEmphasis().withRole(role).withContent(content);
    }

    public static Example createExample(String title, Object... content) {
        return objectFactory.createExample().withTitleContent(createTitle(title)).withContent(content);
    }

    public static Important createImportant(Object... content) {
        return objectFactory.createImportant().withContent(content);
    }

    public static InformalTable createInformalTable(String style, Frame frame, Choice colSep, Choice rowSep,
                                                    TableGroup tableGroup) {
        return objectFactory.createInformalTable().withTableStyle(style).withFrame(frame).withColSep(colSep)
                .withRowSep(rowSep).withTableGroup(tableGroup);
    }

    public static Emphasis createItalic(Object... content) {
        return createEmphasis(null, content);
    }

    public static SimplePara createHighlight(String id, String text) {
        return createSimplePara(id, "There is a ", createEmphasis("marked", text), " in this para.");
    }

    public static ItemizedList createItemizedList(String id, Object... content) {
        return objectFactory.createItemizedList().withId(id).withContent(content);
    }

    public static ListItem createListItem(String id, Object... content) {
        return objectFactory.createListItem().withId(id).withContent(content);
    }

    public static Literal createLiteral(String id, Object... content) {
        return objectFactory.createLiteral().withId(id).withContent(content);
    }

    public static Note createNote(Object... content) {
        return objectFactory.createNote().withContent(content);
    }

    public static OrderedList createOrderedList(String id, Object... content) {
        return objectFactory.createOrderedList().withId(id).withContent(content);
    }

    public static Phrase createPhrase(String role, Object... content) {
        return objectFactory.createPhrase().withRole(role).withContent(content);
    }

    public static Row createRow(Object... content) {
        return objectFactory.createRow().withContent(content);
    }

    public static Section createSection(String id, Object... content) {
        return objectFactory.createSection().withId(id).withContent(content);
    }

    public static SimplePara createSimplePara(String id, Object... content) {
        return objectFactory.createSimplePara().withId(id).withContent(content);
    }

    public static CrossReference createCrossReference(Object content) {
        return new CrossReference().withLinkend(content);
    }

    public static Subscript createSubscript(String id, Object... content) {
        return objectFactory.createSubscript().withId(id).withContent(content);
    }

    public static Superscript createSuperscript(String id, Object... content) {
        return objectFactory.createSuperscript().withId(id).withContent(content);
    }

    public static Table createTable(String style, Frame frame, Choice colSep, Choice rowSep, Title title, TableGroup tableGroup) {
        return objectFactory.createTable().withStyle(style).withFrame(frame).withColSep(colSep).withRowSep(rowSep)
                .withTitle(title).withTableGroup(tableGroup);
    }

    public static TableBody createTableBody(Align align, VerticalAlign verticalAlign, Row... rows) {
        return objectFactory.createTableBody().withAlign(align).withVAlign(verticalAlign).withRow(rows);
    }

    public static TableGroup createTableGroup(TableHeader tableHeader, TableBody tableBody, TableFooter tableFooter,
                                              int... columnWidths) {
        List<ColumnSpec> columnSpecs = new ArrayList<>();
        for (int i = 0; i < columnWidths.length; i++) {
            ColumnSpec columnSpec = objectFactory.createColumnSpec().withColumnWidth(format("%s*", columnWidths[i]))
                    .withColumnName(format("col_%s", (i + 1)));
            columnSpecs.add(columnSpec);
        }
        return objectFactory.createTableGroup().withCols(String.valueOf(columnWidths.length)).withTableHeader(tableHeader)
                .withTableBody(tableBody).withTableFooter(tableFooter).withColSpec(columnSpecs);
    }

    public static TableFooter createTableFooter(Align align, VerticalAlign verticalAlign, Row... rows) {
        return objectFactory.createTableFooter().withAlign(align).withVAlign(verticalAlign).withRow(rows);
    }

    public static TableHeader createTableHeader(Align align, VerticalAlign verticalAlign, Row... rows) {
        return objectFactory.createTableHeader().withAlign(align).withVAlign(verticalAlign).withRow(rows);
    }

    public static Term createTerm(Object... content) {
        return objectFactory.createTerm().withContent(content);
    }

    public static Tip createTip(Object... content) {
        return objectFactory.createTip().withContent(content);
    }

    public static Title createTitle(Object... content) {
        return objectFactory.createTitle().withContent(content);
    }

    public static VariableList createVariableList(String id, Object[] content, VariableListEntry... entries) {
        return objectFactory.createVariableList().withId(id).withContent(content).withVariableListEntry(entries);
    }

    public static VariableListEntry createVariableListEntry(ListItem listItem, Term... terms) {
        return objectFactory.createVariableListEntry().withTerm(terms).withListItem(listItem);
    }

    public static Warning createWarning(Object... content) {
        return objectFactory.createWarning().withContent(content);
    }

    @SuppressWarnings("unchecked")
    public static String toXml(Object obj) {
        final var clazz = obj.getClass();
        final var r = (XmlRootElement) clazz.getAnnotation(XmlRootElement.class);
        try {
            return jaxbTool.marshall(clazz.getPackageName(), new JAXBElement(new QName(r.namespace(), r.name()), clazz, obj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
