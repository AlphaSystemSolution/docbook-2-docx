package com.alphasystem.docbook.handler;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.model.Admonition;
import org.docbook.model.Example;

/**
 * Purpose of handler is to take incoming DocBook element and convert it to to corresponding Docx4j element.
 * A marker interface.
 *
 * <p>
 * Following are the two top level handlers:
 * <ul>
 *     <li>{@link BlockHandler} &mdash; Handler for specialized blocks such as {@link Admonition}s, {@link Example}, and others.</li>
 *     <li>{@link BuilderHandler} &mdash; Handler for underlying {@link Builder}.</li>
 * </ul>
 * </p>
 *
 * @author sali
 */
public interface Handler {
}
