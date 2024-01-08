package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.model.ListInfo;

import java.util.Objects;

public abstract class ListBuilder<S> extends AbstractBuilder<S> {

    protected ListInfo listInfo;
    protected String listStyleName;

    protected ListBuilder(S source, Builder<?> parent) {
        super(null, source, parent);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected void preProcess() {
        super.preProcess();
        setListStyleName();
        final var parentListBuilder = getParent(ListBuilder.class);
        if (Objects.isNull(parentListBuilder)) {
            final var level = 0;
            var numberId = ApplicationController.getContext().getListNumber(listStyleName, level);
            this.listInfo = new ListInfo(numberId, level);
        } else {
            // we have nested list, get the current list info, update the level, and pass it down
            final var pli = parentListBuilder.listInfo;
            this.listInfo = new ListInfo(pli.getNumber(), pli.getLevel() + 1);
        }
    }

    protected abstract void setListStyleName();
}
