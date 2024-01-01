package com.alphasystem.xml;

import com.alphasystem.util.AppUtil;
import org.docbook.model.*;

import java.util.function.Function;

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

    default boolean isArticleType(Object o) {
        return isInstanceOf(Article.class, o);
    }

    default boolean isColumnSpecType(Object o) {
        return isInstanceOf(ColumnSpec.class, o);
    }

    default boolean isEmphasisType(Object o) {
        return isInstanceOf(Emphasis.class, o);
    }

    default boolean isEntryType(Object o) {
        return isInstanceOf(Entry.class, o);
    }

    default boolean isInformalTableType(Object o) {
        return isInstanceOf(InformalTable.class, o);
    }

    default boolean isItemizedListType(Object o) {
        return isInstanceOf(ItemizedList.class, o);
    }

    default boolean isListItemType(Object o) {
        return isInstanceOf(ListItem.class, o);
    }

    default boolean isOrderedListType(Object o) {
        return isInstanceOf(OrderedList.class, o);
    }

    default boolean isPhraseType(Object o) {
        return isInstanceOf(Phrase.class, o);
    }

    default boolean isRowType(Object o) {
        return isInstanceOf(Row.class, o);
    }

    default boolean isSectionType(Object o) {
        return isInstanceOf(Section.class, o);
    }

    default boolean isSimpleParaType(Object o) {
        return isInstanceOf(SimplePara.class, o);
    }

    default boolean isTableBodyType(Object o) {
        return isInstanceOf(TableBody.class, o);
    }

    default boolean isTableFooterType(Object o) {
        return isInstanceOf(TableFooter.class, o);
    }

    default boolean isTableHeaderType(Object o) {
        return isInstanceOf(TableHeader.class, o);
    }

    default boolean isTableGroupType(Object o) {
        return isInstanceOf(TableGroup.class, o);
    }

    default boolean isTitleType(Object o) {
        return isInstanceOf(Title.class, o);
    }

    default boolean isTrType(Object o) {
        return isInstanceOf(Tr.class, o);
    }
}
