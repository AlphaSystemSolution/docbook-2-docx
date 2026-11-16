package com.alphasystem.asciidoc.test;

import org.testng.annotations.Test;

public class ProgramListingTest extends AbstractTest {

  public ProgramListingTest() {
    super("Program Listing");
  }

  @Test
  public void testProgramListing() {
    addTestTitle("Simple Java program");
    processAdoc("program-listing");
    assertSize(1);
    addHorizontalLine();
  }
}
