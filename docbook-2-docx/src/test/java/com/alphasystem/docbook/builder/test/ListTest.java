package com.alphasystem.docbook.builder.test;

import org.docbook.model.*;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;

/**
 * @author sali
 */
public class ListTest extends AbstractTest {

    @Test
    public void testVariableListEntryBuilder() {
        final Term term = createTerm("Entry title");
        final SimplePara simplePara = createSimplePara(null, "This text is under simple para and it has to be indented using \"ListParagraph\" style without any numbering.");
        final ListItem listItem = createListItem(null, simplePara);
        addResult(null, 0, 2, "VariableListEntry Test", createVariableListEntry(listItem, term));
    }

    @Test(dependsOnMethods = {"testVariableListEntryBuilder"})
    public void testVariableListBuilder() {
        addResult(null, 0, 12, "VariableList Test", readXml("variablelist", VariableList.class));
    }

    @Test(dependsOnMethods = {"testVariableListBuilder"})
    public void testHorizontalList() {
        final Row row1 = createRow(createEntry(null, null, createSimplePara(null, "CPU")),
                createEntry(null, null, createSimplePara(null, "The brain of the computer.")));
        final Row row2 = createRow(createEntry(null, null, createSimplePara(null, "Hard drive")),
                createEntry(null, null, createSimplePara(null, "Permanent storage for operating system and/or user files.")));
        final Row row3 = createRow(createEntry(null, null, createSimplePara(null, "RAM")),
                createEntry(null, null, createSimplePara(null, "Temporarily stores information the CPU uses during operation.")));
        final TableBody tableBody = createTableBody(null, VerticalAlign.TOP, row1, row2, row3);
        final TableGroup tableGroup = createTableGroup(null, tableBody, null, 15, 85);
        final InformalTable informalTable = createInformalTable("horizontal", Frame.NONE, Choice.ZERO, Choice.ZERO, tableGroup);
        addResult(null, 0, 1, "Table With Custom Style", informalTable);
    }
}
