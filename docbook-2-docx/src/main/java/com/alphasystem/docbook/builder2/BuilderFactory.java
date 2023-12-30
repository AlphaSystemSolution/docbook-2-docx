package com.alphasystem.docbook.builder2;

import com.alphasystem.docbook.builder2.impl.block.InformalTableBuilder;
import com.alphasystem.docbook.builder2.impl.block.SimpleParaBuilder;
import com.alphasystem.docbook.builder2.impl.block.TitleBuilder;
import com.alphasystem.docbook.builder2.impl.inline.EmphasisBuilder;
import com.alphasystem.docbook.builder2.impl.inline.PhraseBuilder;
import com.alphasystem.docbook.builder2.impl.inline.TextBuilder;
import com.alphasystem.util.AppUtil;
import org.docbook.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BuilderFactory {

    private static BuilderFactory instance;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SimpleParaBuilder simpleParaBuilder = new SimpleParaBuilder();
    private final PhraseBuilder phraseBuilder = new PhraseBuilder();
    private final EmphasisBuilder emphasisBuilder = new EmphasisBuilder();
    private final TextBuilder textBuilder = new TextBuilder();
    private final InformalTableBuilder informalTableBuilder = new InformalTableBuilder();
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

        if (AppUtil.isInstanceOf(SimplePara.class, o)) {
            return simpleParaBuilder.process((SimplePara) o);
        } else if (AppUtil.isInstanceOf(Phrase.class, o)) {
            return phraseBuilder.process((Phrase) o);
        } else if (AppUtil.isInstanceOf(Emphasis.class, o)) {
            return emphasisBuilder.process((Emphasis) o);
        } else if (AppUtil.isInstanceOf(String.class, o)) {
            return textBuilder.process((String) o);
        } else if (AppUtil.isInstanceOf(InformalTable.class, o)) {
            return informalTableBuilder.process((InformalTable) o);
        } else if (AppUtil.isInstanceOf(Title.class, o)) {
            return titleBuilder.process((Title) o);
        } else {
            logger.warn("Builder not defined for class: " + o.getClass().getName());
            return null;
        }
    }
}
