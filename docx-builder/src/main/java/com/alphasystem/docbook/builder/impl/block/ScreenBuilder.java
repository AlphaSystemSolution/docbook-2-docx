package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.Screen;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

public class ScreenBuilder extends TableBasedBlockBuilder<Screen> {

    public ScreenBuilder(Builder<?> parent, Screen screen, int indexInParent) {
        super(parent, screen, indexInParent);
    }

    @Override
    protected void initContent() {
        content = source.getContent();
    }

    @Override
    protected void preProcess() {
        tbl = applicationController.getScreenTable();
        final Tr tr = (Tr) tbl.getContent().get(0);
        tc = (Tc) tr.getContent().get(0);
    }
}
