package com.alphasystem.docbook.builder.model;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.openxml.builder.wml.HeadingList;
import org.docbook.model.Example;
import org.docbook.model.Table;

import static com.alphasystem.docbook.util.ConfigurationUtils.getInstance;
import static java.lang.String.format;

/**
 * @author sali
 */
public abstract class DocumentCaption extends HeadingList<DocumentCaption> {

    public static final DocumentCaption EXAMPLE = new DocumentCaption(Example.class) {

        @Override
        public String getValue(int i) {
            final var documentInfo = ApplicationController.getContext().getDocumentInfo();
            return format("%s %%%s.", documentInfo.getExampleCaption(), i);
        }

    }; // Example

    public static final DocumentCaption TABLE = new DocumentCaption(Table.class) {

        @Override
        public String getValue(int i) {
            final var documentInfo = ApplicationController.getContext().getDocumentInfo();
            return format("%s %%%s.", documentInfo.getTableCaption(), i);
        }

    }; // Table

    DocumentCaption(Class<?> titleType) {
        super(getInstance().getString(format("%s.title", titleType.getName())));
        setName(titleType.getSimpleName());
    }

    @Override
    public long getLeftIndent(int i) {
        return 432;
    }

}
