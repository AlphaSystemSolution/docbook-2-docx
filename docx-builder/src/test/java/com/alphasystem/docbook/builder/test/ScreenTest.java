package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Article;
import org.docbook.model.Screen;
import org.testng.annotations.Test;

public class ScreenTest extends AbstractTest {

    @Test
    public void testScreen() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        addResult(p1, 0, 3, "Screen Test", readXml("screen", Screen.class));
    }
}
