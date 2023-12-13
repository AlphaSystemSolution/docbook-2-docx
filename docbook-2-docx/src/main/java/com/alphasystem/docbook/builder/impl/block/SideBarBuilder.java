package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import org.docbook.model.SideBar;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;

import java.util.ArrayList;

/**
 * @author sali
 */
public class SideBarBuilder extends TableBasedBlockBuilder<SideBar> {

    public SideBarBuilder(Builder<?> parent, SideBar obj, int indexInParent) {
        super(parent, obj, indexInParent);
    }

    @Override
    protected void initContent() {
        content = new ArrayList<>();
        content.addAll(source.getTitleContent());
        content.addAll(source.getContent());
    }

    @Override
    protected void preProcess() {
        tbl = applicationController.getSideBarTable();
        final Tr tr = (Tr) tbl.getContent().get(0);
        tc = (Tc) tr.getContent().get(0);
    }

}
