package com.alphasystem.docbook.builder.test;

import org.docx4j.wml.Tbl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class AdmonitionTest extends AbstractTest {

    public AdmonitionTest() {
        super("Admonitions");
    }

    @Test
    public void testCaution() {
        addTestTitle("Caution Admonition");
        processContent(readXml("caution"));
        assertSize(3);
        final var content = mainDocumentPart.getContent();
        // number of content are 5, admonition caption + empty para in second column + content
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 2)), 5);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testCaution"})
    public void testImportant() {
        addTestTitle("Important Admonition");
        processContent(readXml("important"));
        assertSize(3);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 2)), 6);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testImportant"})
    public void testNote() {
        addTestTitle("Note Admonition");
        processContent(readXml("note"));
        assertSize(3);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 2)), 7);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testNote"})
    public void testTip() {
        addTestTitle("Tip Admonition");
        processContent(readXml("tip"));
        assertSize(3);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 2)), 8);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testTip"})
    public void testWarning() {
        addTestTitle("Warning Admonition");
        processContent(readXml("warning"));
        assertSize(3);
        final var content = mainDocumentPart.getContent();
        assertEquals(getTableContentSize((Tbl) content.get(content.size() - 2)), 5);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testWarning"})
    public void testSideBar() {
        addTestTitle("SideBar test");
        processContent(readXml("side-bar"));
        assertSize(3);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testSideBar"})
    public void testExample() {
        addTestTitle("Example test");
        processContent(readXml("example"));
        assertSize(4);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testExample"})
    public void testInformalExample() {
        addTestTitle("Table with admonition text");
        processContent(readXml("informal-example"));
        assertSize(3);
        addHorizontalLine();
    }

    @Test(dependsOnMethods = {"testInformalExample"})
    public void testTableWithAdmonition() {
        addTestTitle("InformalExample text");
        processContent(readXml("table-with-admonition"));
        assertSize(4);
        addHorizontalLine();
    }


}
