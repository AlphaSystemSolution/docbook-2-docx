package com.alphasystem.docbook.builder.test;

import org.docbook.model.*;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.*;

/**
 * @author sali
 */
public class AdmonitionTest extends AbstractTest {

    @Test
    public void testCaution() {
        Caution caution = createCaution(createSimplePara(null, "If the title line is not offset by a blank line, it gets interpreted as a section title, which we&#8217;ll discuss later."));
        addResult(null, 0, 3, "Caution Test", caution);
    }

    @Test(dependsOnMethods = {"testCaution"})
    public void testImportant() {
        Important important = createImportant(createSection(null, "There should be no blank lines between the first attribute entry and the rest of the header."));
        addResult(null, 0, 3, "Important Test", important);
    }

    @Test(dependsOnMethods = {"testImportant"})
    public void testNote() {
        Note note = createNote(createSimplePara(null, "Admonitions can also encapsulate any block content, which we&#8217;ll cover later."));
        addResult(null, 0, 3, "Note Test", note);
    }

    @Test(dependsOnMethods = {"testNote"})
    public void testTip() {
        Tip tip = createTip(createSimplePara(null, "A document title is an <emphasis>optional</emphasis> feature of an AsciiDoc document."));
        addResult(null, 0, 3, "Tip Test", tip);
    }

    @Test(dependsOnMethods = {"testTip"})
    public void testWarning() {
        Warning warning = createWarning(createSimplePara(null, "Wolpertingers are known to nest in server racks.\n" +
                "        Enter at your own risk."));
        addResult(null, 0, 3, "Warning Test", warning);
    }

    @Test(dependsOnMethods = {"testWarning"})
    public void testExample() {
        addResult(null, 0, 3, "Example Test", readXml("example", Example.class));
    }

    @Test(dependsOnMethods = {"testExample"})
    public void testInformalExample() {
        addResult(null, 0, 3, "Informal Example Test", readXml("informal-example", InformalExample.class));
    }
}
