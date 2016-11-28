package com.alphasystem.docbook.builder.test;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.DocumentContext;
import com.alphasystem.openxml.builder.wml.WmlPackageBuilder;
import com.alphasystem.xml.UnmarshallerTool;
import org.docbook.model.Article;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.Style;
import org.docx4j.wml.Styles;
import org.testng.annotations.BeforeSuite;

import java.util.List;

import static com.alphasystem.docbook.ApplicationController.DEFAULT_TEMPLATE;
import static com.alphasystem.docbook.ApplicationController.STYLES_PATH;
import static com.alphasystem.docbook.builder.model.DocumentCaption.EXAMPLE;
import static com.alphasystem.docbook.builder.model.DocumentCaption.TABLE;
import static org.testng.Assert.fail;

/**
 * @author sali
 */
public class Setup extends AbstractTest {

    @BeforeSuite
    public void setup() {
        try {
            UnmarshallerTool unmarshallerTool = new UnmarshallerTool();
            DocumentContext documentContext = new DocumentContext(unmarshallerTool.getDocumentInfo(), new Article());
            ApplicationController.startContext(documentContext);

            final WmlPackageBuilder wmlPackageBuilder = new WmlPackageBuilder(DEFAULT_TEMPLATE)
                    .styles(STYLES_PATH).multiLevelHeading(EXAMPLE).multiLevelHeading(TABLE);

            final StyleDefinitionsPart styleDefinitionsPart = wmlPackageBuilder.getPackage().getMainDocumentPart().getStyleDefinitionsPart();
            final Styles styles = styleDefinitionsPart.getContents();
            final List<Style> list = styles.getStyle();
            list.forEach(style -> documentContext.getDocumentStyles().add(style.getStyleId()));
            wmlPackage = wmlPackageBuilder.getPackage();
            mainDocumentPart = wmlPackage.getMainDocumentPart();
            documentContext.setMainDocumentPart(mainDocumentPart);
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }
}
