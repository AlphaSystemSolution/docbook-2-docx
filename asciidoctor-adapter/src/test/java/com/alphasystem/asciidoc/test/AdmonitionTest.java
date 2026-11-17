package com.alphasystem.asciidoc.test;

import static org.testng.Assert.assertEquals;

import org.docx4j.wml.Tbl;
import org.testng.annotations.Test;

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
    processAdoc("caution");
    assertSize(1);
    final var content = mainDocumentPart.getContent();
    assertEquals(getTableContentSize((Tbl) content.get(content.size() - 1)), 5);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testCaution"})
  public void testImportant() {
    addTestTitle("Important Admonition");
    processAdoc("important");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testWarning"})
  public void testNote() {
    addTestTitle("Note Admonition");
    processAdoc("note");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testImportant"})
  public void testTip() {
    addTestTitle("Tip Admonition");
    processAdoc("tip");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testTip"})
  public void testWarning() {
    addTestTitle("Warning Admonition");
    processAdoc("warning");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testNote"})
  public void testSideBar() {
    addTestTitle("SideBar test");
    processAdoc("side-bar");
    assertSize(1);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testSideBar"})
  public void testExample() {
    addTestTitle("Example test");
    processAdoc("example");
    assertSize(2);
    addHorizontalLine();
  }

  @Test(dependsOnMethods = {"testExample"})
  public void testInformalExample() {
    addTestTitle("InformalExample text");
    processAdoc("informal-example");
    assertSize(1);
    addHorizontalLine();
  }
}
