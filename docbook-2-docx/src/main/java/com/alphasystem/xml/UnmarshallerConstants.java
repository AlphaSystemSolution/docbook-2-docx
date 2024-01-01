package com.alphasystem.xml;

import org.docbook.model.*;

import static com.alphasystem.util.AppUtil.isInstanceOf;

public interface UnmarshallerConstants {

    String ARTICLE = "article";
    String COLUMN_SPEC = "colspec";
    String DATE = "date";
    String EMPHASIS = "emphasis";
    String ENTRY = "entry";
    String INFO = "info";
    String INFORMAL_TABLE = "informaltable";
    String ITEMIZED_LIST = "itemizedlist";
    String LIST_ITEM = "listitem";
    String ORDERED_LIST = "orderedlist";
    String PHRASE = "phrase";
    String ROW = "row";
    String SIMPLE_PARA = "simpara";
    String SECTION = "section";
    String TABLE_BODY = "tbody";
    String TABLE_FOOTER = "tfoot";
    String TABLE_GROUP = "tgroup";
    String TABLE_HEAD = "thead";
    String TITLE = "title";

    static boolean isArticleType(Object o) {
        return isInstanceOf(Article.class, o);
    }

    static boolean isColumnSpecType(Object o) {
        return isInstanceOf(ColumnSpec.class, o);
    }

    static boolean isEmphasisType(Object o) {
        return isInstanceOf(Emphasis.class, o);
    }

    static boolean isEntryType(Object o) {
        return isInstanceOf(Entry.class, o);
    }

    static boolean isInformalTableType(Object o) {
        return isInstanceOf(InformalTable.class, o);
    }

    static boolean isItemizedListType(Object o) {
        return isInstanceOf(ItemizedList.class, o);
    }

    static boolean isListItemType(Object o) {
        return isInstanceOf(ListItem.class, o);
    }

    static boolean isOrderedListType(Object o) {
        return isInstanceOf(OrderedList.class, o);
    }

    static boolean isParaType(Object o) {
        return isInstanceOf(Para.class, o);
    }

    static boolean isPhraseType(Object o) {
        return isInstanceOf(Phrase.class, o);
    }

    static boolean isRowType(Object o) {
        return isInstanceOf(Row.class, o);
    }

    static boolean isSectionType(Object o) {
        return isInstanceOf(Section.class, o);
    }

    static boolean isStringType(Object o) {
        return isInstanceOf(String.class, o);
    }

    static boolean isSimpleParaType(Object o) {
        return isInstanceOf(SimplePara.class, o);
    }

    static boolean isTableBodyType(Object o) {
        return isInstanceOf(TableBody.class, o);
    }

    static boolean isTableFooterType(Object o) {
        return isInstanceOf(TableFooter.class, o);
    }

    static boolean isTableHeaderType(Object o) {
        return isInstanceOf(TableHeader.class, o);
    }

    static boolean isTableGroupType(Object o) {
        return isInstanceOf(TableGroup.class, o);
    }

    static boolean isTitleType(Object o) {
        return isInstanceOf(Title.class, o);
    }

    static boolean isTrType(Object o) {
        return isInstanceOf(Tr.class, o);
    }

    static boolean isParaTypes(Object o) {
        return isSimpleParaType(o) || isParaType(o);
    }
}
