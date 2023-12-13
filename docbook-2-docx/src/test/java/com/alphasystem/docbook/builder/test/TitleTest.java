package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Article;
import org.docbook.model.Example;
import org.docbook.model.Title;
import org.testng.annotations.Test;

import java.util.List;

import static com.alphasystem.docbook.builder.test.DataFactory.*;
import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class TitleTest extends AbstractTest {

    @Test
    public void testDocumentTitle() {
        final Builder parent = builderFactory.getBuilder(null, new Article(), -1);
        final Title title = createTitle("Document Title");
        final List<Object> content = buildContent(parent, 0, title);
        assertEquals(content.size(), 1);
        addResultToDocument("Document Title Test", content.toArray());
    }

    @Test(dependsOnMethods = "testDocumentTitle")
    public void testDocumentTitleWithCustomStyle() {
        final Builder parent = builderFactory.getBuilder(null, new Article(), -1);
        final Title title = createTitle("Document Title ", createPhrase("arabicHeading1", "س ل م"));
        addResult(parent, 0, 1, "Document Title with custom style Test", title);
    }

    @Test(dependsOnMethods = "testDocumentTitleWithCustomStyle")
    public void testExampleTitle() {
        final Builder parent = builderFactory.getBuilder(null, new Example(), -1);
        addResult(parent, 0, 3, "Example Title Test", createExample("Example Title"));
    }
}
