package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Article;
import org.docbook.model.Section;
import org.docbook.model.Title;
import org.testng.annotations.Test;

import static com.alphasystem.docbook.builder.test.DataFactory.createSection;
import static com.alphasystem.docbook.builder.test.DataFactory.createTitle;

/**
 * @author sali
 */
public class SectionLevelTest extends AbstractTest {

    @Test
    public void testSectionLevel1Title() {
        final Builder parent = builderFactory.getBuilder(null, new Article(), -1);
        final Title title = createTitle("Section 1");
        addResult(parent, 0, 1, "Section Level 1 Test", createSection("section-1", title));
    }

    @Test(dependsOnMethods = "testSectionLevel1Title")
    public void testSectionLevel2Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Title title = createTitle("Section 2");
        addResult(p2, 0, 1, "Section Level 2 Test", createSection("section-2", title));
    }

    @Test(dependsOnMethods = "testSectionLevel2Title")
    public void testSectionLevel3Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Builder p3 = builderFactory.getBuilder(p2, new Section(), 0);
        final Title title = createTitle("Section 3");
        addResult(p3, 0, 1, "Section Level 3 Test", createSection("section-3", title));
    }

    @Test(dependsOnMethods = "testSectionLevel3Title")
    public void testSectionLevel4Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Builder p3 = builderFactory.getBuilder(p2, new Section(), 0);
        final Builder p4 = builderFactory.getBuilder(p3, new Section(), 0);
        final Title title = createTitle("Section 4");
        addResult(p4, 0, 1, "Section Level 4 Test", createSection("section-4", title));
    }

    @Test(dependsOnMethods = "testSectionLevel4Title")
    public void testSectionLevel5Title() {
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final Builder p3 = builderFactory.getBuilder(p2, new Section(), 0);
        final Builder p4 = builderFactory.getBuilder(p3, new Section(), 0);
        final Builder p5 = builderFactory.getBuilder(p4, new Section(), 0);
        final Title title = createTitle("Section 5");
        addResult(p5, 0, 1, "Section Level 5 Test", createSection("section-5", title));
    }
}
