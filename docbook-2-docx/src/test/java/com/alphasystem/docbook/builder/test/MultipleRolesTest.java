package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.util.IdGenerator;
import org.docbook.model.Article;
import org.docbook.model.Section;
import org.docbook.model.SimplePara;
import org.docx4j.wml.P;
import org.testng.annotations.Test;

import java.util.List;

import static com.alphasystem.docbook.builder.test.DataFactory.createPhrase;
import static com.alphasystem.docbook.builder.test.DataFactory.createSimplePara;
import static org.testng.Assert.assertEquals;

/**
 * @author sali
 */
public class MultipleRolesTest extends AbstractTest {

    @Test
    public void testMultipleRoles() {
        final SimplePara simplePara = createSimplePara(IdGenerator.nextId(), createPhrase("literal line-through green",
                "This text has multiple roles"),", ", createPhrase("literal", "literal"), ", ",
                createPhrase("line-through", "line-through"), ", and ", createPhrase("green", "green"));
        final Builder p1 = builderFactory.getBuilder(null, new Article(), -1);
        final Builder p2 = builderFactory.getBuilder(p1, new Section(), 0);
        final List<Object> content = buildContent(p2, -1, simplePara);
        assertEquals(content.size(), 1);
        final P p = (P) content.get(0);
        assertEquals(p.getClass().getName(), P.class.getName());
        addResultToDocument("Multiple Roles Test", p);
    }
}
