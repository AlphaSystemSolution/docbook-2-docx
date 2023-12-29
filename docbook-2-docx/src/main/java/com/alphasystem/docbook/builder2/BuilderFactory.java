package com.alphasystem.docbook.builder2;

import com.alphasystem.docbook.builder2.impl.block.SimpleParaBuilder;
import com.alphasystem.docbook.builder2.impl.inline.EmphasisBuilder;
import com.alphasystem.docbook.builder2.impl.inline.PhraseBuilder;
import com.alphasystem.docbook.builder2.impl.inline.TextBuilder;
import com.alphasystem.util.AppUtil;
import com.alphasystem.util.IdGenerator;
import org.docbook.model.Emphasis;
import org.docbook.model.Phrase;
import org.docbook.model.SimplePara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuilderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderFactory.class);
    private static BuilderFactory instance;

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

    public Builder<?, ?> getBuilder(Object o) {
        if (o == null) {
            return null;
        }

        if (AppUtil.isInstanceOf(SimplePara.class, o)) {
            return new SimpleParaBuilder((SimplePara) o);
        } else if (AppUtil.isInstanceOf(Phrase.class, o)) {
            return new PhraseBuilder((Phrase) o);
        } else if (AppUtil.isInstanceOf(Emphasis.class, o)) {
            return new EmphasisBuilder((Emphasis) o);
        } else if (AppUtil.isInstanceOf(String.class, o)) {
            return new TextBuilder((String) o, IdGenerator.nextId());
        } else {
            LOGGER.warn("Builder not defined for class: " + o.getClass().getName());
            return null;
        }
    }
}
