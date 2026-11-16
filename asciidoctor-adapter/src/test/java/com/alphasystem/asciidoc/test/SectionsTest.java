package com.alphasystem.asciidoc.test;

import org.testng.annotations.Test;

/**
 * @author sali
 */
public class SectionsTest extends AbstractTest {

  public SectionsTest() {
    super("Sections");
  }

  @Test
  public void testSectionLevelTitles() {
    addTestTitle("Section Level title test");
    processAdoc("sections");
    assertSize(10);
    addHorizontalLine();
  }
}
