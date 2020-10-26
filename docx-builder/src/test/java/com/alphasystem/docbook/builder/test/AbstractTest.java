package com.alphasystem.docbook.builder.test;

import com.alphasystem.SystemException;
import com.alphasystem.docbook.DocumentBuilder;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.BuilderFactory;
import com.alphasystem.openxml.builder.wml.PBuilder;
import com.alphasystem.openxml.builder.wml.WmlBuilderFactory;
import com.alphasystem.xml.UnmarshallerTool;
import org.apache.commons.lang3.ArrayUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.*;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import static org.testng.Reporter.log;

/**
 * @author sali
 */
public abstract class AbstractTest {

    private static final String DEFAULT_TITLE = "DefaultTitle";
    private static final String DATA_PATH = System.getProperty("data.path");
    protected static final String TARGET_PATH = System.getProperty("target.path");

    static {
        final Path path = get(TARGET_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                fail(e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings({"unused"})
    protected static void build(String dir, String fileNamePrefix) {
        final Path sourcePath = get(dir, format("%s.xml", fileNamePrefix));
        try {
            final DocumentContext context = DocumentBuilder.createContext(sourcePath);
            final WordprocessingMLPackage wmlPackage = DocumentBuilder.buildDocument(context);
            save(get(TARGET_PATH, format("%s.docx", fileNamePrefix)).toFile(), wmlPackage);
        } catch (SystemException | Docx4JException e) {
            fail(e.getMessage(), e);
        }
    }

    protected static Object readXml(String name, Class<?> declaredType) {
        UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
        final Path sourcePath = get(DATA_PATH, format("%s.xml", name));
        try {
            final String source = new String(readAllBytes(sourcePath));
            return unmarshallerTool.unmarshal(source, declaredType);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
        return null;
    }

    protected BuilderFactory builderFactory = BuilderFactory.getInstance();
    protected static WordprocessingMLPackage wmlPackage;
    protected static MainDocumentPart mainDocumentPart;

    P buildPara(R... runs) {
        final PBuilder pBuilder = WmlBuilderFactory.getPBuilder();
        for (R run : runs) {
            pBuilder.addContent(run);
        }
        return pBuilder.getObject();
    }

    void addResultToDocument(String title, Object... content) {
        mainDocumentPart.addObject(getParagraphWithStyle(DEFAULT_TITLE, title));
        for (Object o : content) {
            mainDocumentPart.addObject(o);
        }
        mainDocumentPart.addObject(getHorizontalLine());
    }

    protected void addResult(Builder parent, int indexInParent, int expectedSize, String title, Object... content) {
        final List<Object> childContent = buildContent(parent, indexInParent, content);
        assertEquals(childContent.size(), expectedSize);
        addResultToDocument(title, childContent.toArray());
    }

    void addResult(String title, R... runs) {
        addResultToDocument(title, buildPara(runs));
    }

    @SuppressWarnings("unchecked")
    List<Object> buildContent(Builder parent, int indexInParent, Object... objects) {
        List<Object> content = new ArrayList<>();
        if (!ArrayUtils.isEmpty(objects)) {
            log("**************************************************************************", true);
            for (Object o : objects) {
                final Builder builder = builderFactory.getBuilder(parent, o, indexInParent);
                if(builder == null) {
                    log(format("No builder found for type: %s", o), true);
                    continue;
                }
                final List c = builder.buildContent();
                log(format("Getting builder \"%s\" for \"%s\", number of child content are \"%s\".", builder.getClass().getName(),
                        o.getClass().getName(), c.size()), true);
                content.addAll(c);
            }
            log("**************************************************************************", true);
        }

        return content;
    }

    R[] convertToRuns(List<Object> content) {
        R[] runs = new R[content.size()];
        for (int i = 0; i < content.size(); i++) {
            runs[i] = (R) content.get(i);
        }
        return runs;
    }

    @BeforeMethod
    public void startTest(Method method) {
        log("-----------------------------------------------------------------------------------------", true);
        log(format("Stating test \"%s\".", method.getName()), true);
    }

    @AfterMethod
    public void endTest(Method method) {
        log(format("Test \"%s\" end.", method.getName()), true);
        log("-----------------------------------------------------------------------------------------", true);
    }

}
