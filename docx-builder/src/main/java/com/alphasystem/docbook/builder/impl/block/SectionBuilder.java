package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.Section;

import static com.alphasystem.util.AppUtil.isInstanceOf;

/**
 * @author sali
 */
public class SectionBuilder extends BlockBuilder<Section> {

    private int level;

    public SectionBuilder(Builder<?> parent, Section section, int indexInParent) {
        super(parent, section, indexInParent);
        initLevel();
    }

    @Override
    protected void initContent() {
        content = source.getContent();
    }

    private void initLevel() {
        if (isInstanceOf(ArticleBuilder.class, parent)) {
            // must be top level section
            level = 1;
        } else if (isInstanceOf(SectionBuilder.class, parent)) {
            // must be one of sub level section, increase the level of parent by 1
            SectionBuilder parentSection = (SectionBuilder) parent;
            level = parentSection.getLevel() + 1;
        } else {
            logger.warn("unhandled parent of section {}", parent.getClass().getName());
            level = -1;
        }
    }

    public int getLevel() {
        return level;
    }
}
