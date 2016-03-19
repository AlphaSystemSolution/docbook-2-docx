package com.alphasystem.docbook.builder.model;

import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.openxml.builder.wml.AbstractListItem;
import org.docbook.model.Example;
import org.docx4j.wml.NumberFormat;

import static java.lang.String.format;
import static org.docx4j.wml.NumberFormat.DECIMAL;

/**
 * @author sali
 */
public abstract class DocumentCaption extends AbstractListItem<DocumentCaption> {

    public static final DocumentCaption EXAMPLE = new DocumentCaption(12,
            ConfigurationUtils.getInstance().getString(format("%s.title", Example.class.getName())), DECIMAL) {

        @Override
        public String getValue(int i) {
            final AsciiDocumentInfo documentInfo = ApplicationController.getContext().getDocumentInfo();
            return format("%s %%%s.", documentInfo.getExampleCaption(), i);
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public DocumentCaption getNext() {
            return null;
        }
    };

    DocumentCaption(int numberId, String styleName, NumberFormat numberFormat) {
        super(numberId, styleName, numberFormat);
    }

    @Override
    public boolean linkStyle() {
        return true;
    }

    @Override
    public long getLeftIndent(int i) {
        return 432;
    }

    @Override
    public String getMultiLevelType() {
        return "multilevel";
    }
}
