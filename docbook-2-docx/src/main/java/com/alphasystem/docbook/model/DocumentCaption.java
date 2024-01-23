package com.alphasystem.docbook.model;

import com.alphasystem.docbook.util.ConfigurationUtils;
import com.alphasystem.docx4j.builder.wml.HeadingList;
import org.docbook.model.Example;
import org.docbook.model.Table;

import static com.alphasystem.docbook.util.ConfigurationUtils.getInstance;
import static java.lang.String.format;

/**
 * @author sali
 */
public abstract class DocumentCaption extends HeadingList<DocumentCaption> {

    public static final DocumentCaption EXAMPLE = new DocumentCaption(Example.class, ConfigurationUtils.getInstance().getExampleCaption()) {

        @Override
        public String getValue(int i) {
            return format("%s %%%s.", caption, i);
        }

    }; // Example

    public static final DocumentCaption TABLE = new DocumentCaption(Table.class, ConfigurationUtils.getInstance().getTableCaption()) {

        @Override
        public String getValue(int i) {
            return format("%s %%%s.", caption, i);
        }

    }; // Table

    protected final String caption;

    DocumentCaption(Class<?> titleType, String caption) {
        super(getInstance().getTitleStyle(titleType.getName()));
        setName(titleType.getSimpleName());
        this.caption = caption;
    }

    @Override
    public long getLeftIndent(int i) {
        return 432;
    }

}
