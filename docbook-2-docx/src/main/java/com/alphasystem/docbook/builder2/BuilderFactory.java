package com.alphasystem.docbook.builder2;

import com.alphasystem.docbook.builder2.impl.block.*;
import com.alphasystem.docbook.builder2.impl.inline.EmphasisBuilder;
import com.alphasystem.docbook.builder2.impl.inline.PhraseBuilder;
import com.alphasystem.docbook.builder2.impl.inline.TextBuilder;
import com.alphasystem.xml.UnmarshallerConstants;
import org.docbook.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BuilderFactory {

    private static BuilderFactory instance;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EmphasisBuilder emphasisBuilder = new EmphasisBuilder();
    private final InformalTableBuilder informalTableBuilder = new InformalTableBuilder();

    private final ItemizedListBuilder itemizedListBuilder = new ItemizedListBuilder();

    private final OrderedListBuilder orderedListBuilder = new OrderedListBuilder();
    private final PhraseBuilder phraseBuilder = new PhraseBuilder();
    private final SimpleParaBuilder simpleParaBuilder = new SimpleParaBuilder();
    private final TextBuilder textBuilder = new TextBuilder();
    private final TitleBuilder titleBuilder = new TitleBuilder();

    public static synchronized BuilderFactory getInstance() {
        if (instance == null) {
            instance = new BuilderFactory();
        }
        return instance;
    }

    /*
     * Do not let any one instantiate this class
     */
    private BuilderFactory() {
    }

    public List<Object> process(Object o) {
        if (o == null) {
            return null;
        }

        if (UnmarshallerConstants.isSimpleParaType(o)) {
            return simpleParaBuilder.process((SimplePara) o);
        } else if (UnmarshallerConstants.isPhraseType(o)) {
            return phraseBuilder.process((Phrase) o);
        } else if (UnmarshallerConstants.isEmphasisType(o)) {
            return emphasisBuilder.process((Emphasis) o);
        } else if (UnmarshallerConstants.isStringType(o)) {
            return textBuilder.process((String) o);
        } else if (UnmarshallerConstants.isInformalTableType(o)) {
            return informalTableBuilder.process((InformalTable) o);
        } else if (UnmarshallerConstants.isTitleType(o)) {
            return titleBuilder.process((Title) o);
        } else if (UnmarshallerConstants.isOrderedListType(o)) {
           return orderedListBuilder.process((OrderedList) o);
        } else if (UnmarshallerConstants.isItemizedListType(o)) {
            return itemizedListBuilder.process((ItemizedList) o);
        } else {
            logger.warn("Builder not defined for class: " + o.getClass().getName());
            return null;
        }
    }
}
