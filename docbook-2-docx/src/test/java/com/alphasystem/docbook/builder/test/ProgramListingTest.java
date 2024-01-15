package com.alphasystem.docbook.builder.test;

import org.testng.annotations.Test;

public class ProgramListingTest extends AbstractTest {

    public ProgramListingTest() {
        super("Program Listing");
    }

    @Test
    public void testProgramListing() {
        addTestTitle("Simple Java program");
        processContent(readXml("program-listing"));
        assertSize(1);
        addHorizontalLine();
    }
}
