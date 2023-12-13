package com.alphasystem.docbook.handler;

import com.alphasystem.docbook.builder.model.Admonition;
import org.docbook.model.Example;
import org.docbook.model.Screen;
import org.docbook.model.SideBar;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;

/**
 * Interface for handlers for specialized block fragments, like {@link Admonition}, {@link Example}, {@link Screen},
 * and {@link SideBar}.
 *
 * @param <T> type of object is handler will return.
 * @author sali
 */
public interface BlockHandler<T> extends Handler {

    /**
     * Construct this block.
     *
     * @return An object containing the structure of this block, this could be a {@link Tbl}, {@link P}, or any other
     * valid Docx4j object.
     */
    T handleBlock();
}
