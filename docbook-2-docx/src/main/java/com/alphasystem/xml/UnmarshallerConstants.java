package com.alphasystem.xml;

import org.docbook.model.*;

import static com.alphasystem.util.AppUtil.isInstanceOf;

public interface UnmarshallerConstants {

    String ARTICLE = "article";
    String CAUTION = "caution";
    String COLUMN_SPEC = "colspec";
    String CROSS_REFERENCE = "xref";
    String DATE = "date";
    String EMPHASIS = "emphasis";
    String ENTRY = "entry";
    String IMPORTANT = "important";
    String INFO = "info";
    String INFORMAL_TABLE = "informaltable";
    String ITEMIZED_LIST = "itemizedlist";
    String LINK = "link";
    String LIST_ITEM = "listitem";
    String LITERAL = "literal";
    String NOTE = "note";
    String ORDERED_LIST = "orderedlist";
    String PHRASE = "phrase";
    String ROW = "row";
    String SIMPLE_PARA = "simpara";
    String SECTION = "section";
    String SUBSCRIPT = "subscript";
    String SUPERSCRIPT = "superscript";
    String TABLE = "table";
    String TABLE_BODY = "tbody";
    String TABLE_FOOTER = "tfoot";
    String TABLE_GROUP = "tgroup";
    String TABLE_HEAD = "thead";
    String TERM = "term";
    String TIP = "tip";
    String TITLE = "title";
    String WARNING = "warning";

    static boolean isArticleType(Object o) {
        return isInstanceOf(Article.class, o);
    }

    static boolean isCautionType(Object o) {
        return isInstanceOf(Caution.class, o);
    }

    static boolean isColumnSpecType(Object o) {
        return isInstanceOf(ColumnSpec.class, o);
    }

    static boolean isCrossReferenceType(Object o) {
        return isInstanceOf(CrossReference.class, o);
    }

    static boolean isEmphasisType(Object o) {
        return isInstanceOf(Emphasis.class, o);
    }

    static boolean isEntryType(Object o) {
        return isInstanceOf(Entry.class, o);
    }

    static boolean isFormalParaType(Object o) {
        return isInstanceOf(FormalPara.class, o);
    }

    static boolean isImportantType(Object o) {
        return isInstanceOf(Important.class, o);
    }

    static boolean isInformalTableType(Object o) {
        return isInstanceOf(InformalTable.class, o);
    }

    static boolean isItemizedListType(Object o) {
        return isInstanceOf(ItemizedList.class, o);
    }

    static boolean isLinkType(Object o) {
        return isInstanceOf(Link.class, o);
    }

    static boolean isListItemType(Object o) {
        return isInstanceOf(ListItem.class, o);
    }

    static boolean isLiteralType(Object o) {
        return isInstanceOf(Literal.class, o);
    }

    static boolean isNoteType(Object o) {
        return isInstanceOf(Note.class, o);
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

    static boolean isSimpleParaType(Object o) {
        return isInstanceOf(SimplePara.class, o);
    }

    static boolean isStringType(Object o) {
        return isInstanceOf(String.class, o);
    }

    static boolean isSubscriptType(Object o) {
        return isInstanceOf(Subscript.class, o);
    }

    static boolean isSuperscriptType(Object o) {
        return isInstanceOf(Superscript.class, o);
    }

    static boolean isTableType(Object o) {
        return isInstanceOf(Table.class, o);
    }

    static boolean isTableBodyType(Object o) {
        return isInstanceOf(TableBody.class, o);
    }

    static boolean isTableFooterType(Object o) {
        return isInstanceOf(TableFooter.class, o);
    }

    static boolean isTableGroupType(Object o) {
        return isInstanceOf(TableGroup.class, o);
    }

    static boolean isTableHeaderType(Object o) {
        return isInstanceOf(TableHeader.class, o);
    }

    static boolean isTermType(Object o) {
        return isInstanceOf(Term.class, o);
    }

    static boolean isTipType(Object o) {
        return isInstanceOf(Tip.class, o);
    }

    static boolean isTitleType(Object o) {
        return isInstanceOf(Title.class, o);
    }

    static boolean isTrType(Object o) {
        return isInstanceOf(Tr.class, o);
    }

    static boolean isWarningType(Object o) {
        return isInstanceOf(Warning.class, o);
    }

    static boolean isParaTypes(Object o) {
        return isSimpleParaType(o) || isParaType(o) || isFormalParaType(o);
    }
}
