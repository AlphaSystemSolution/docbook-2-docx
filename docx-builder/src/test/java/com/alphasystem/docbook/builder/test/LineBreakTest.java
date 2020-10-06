package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Article;
import org.docbook.model.ItemizedList;
import org.testng.annotations.Test;

/**
 * @author sali
 */
public class LineBreakTest extends AbstractTest {

    @Test
    public void testLineBreak(){
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        addResult(p1, 0, 2, "Line Break Test", readXml("lineBreak", ItemizedList.class));
    }
}
