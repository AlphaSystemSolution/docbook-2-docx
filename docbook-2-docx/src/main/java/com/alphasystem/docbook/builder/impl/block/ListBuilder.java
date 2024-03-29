package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.AbstractBuilder;
import com.alphasystem.docbook.model.ListInfo;
import com.alphasystem.commons.util.AppUtil;

public abstract class ListBuilder<S> extends AbstractBuilder<S> {

    protected ListInfo listInfo;
    protected String listStyleName;

    protected ListBuilder(S source, Builder<?> parent) {
        super(null, source, parent);
    }

    @Override
    protected void preProcess() {
        super.preProcess();
        setListStyleName();
        final var ancestorListBuilder = getParent(ListBuilder.class);
        final var allParents = getParents();
        final var variableListParents = allParents.stream().filter(ListBuilder::isVariableListBuilder).toList();
        final var otherListBuilders = allParents.stream().filter(ListBuilder::isOtherListBuilder).toList();
        final var noListBuilders = variableListParents.isEmpty() && otherListBuilders.isEmpty();
        final var nestedVariableList = AppUtil.isInstanceOf(VariableListBuilder.class, this) && variableListParents.size() == 1;
        final var varListCheck = variableListParents.size() == 1 && !nestedVariableList;

        if (noListBuilders || varListCheck) {
            final var level = 0;
            final var numberId = ApplicationController.getContext().getListNumber(listStyleName, level);
            this.listInfo = new ListInfo(numberId, level);
        } else {
            // we have nested list, get the current list info, update the level, and pass it down
            final var pli = ancestorListBuilder.getListInfo();
            var numberId = pli.getNumber();
            var level = pli.getLevel() + 1;
            if (numberId <= -1 && isOtherListBuilder(this) && otherListBuilders.isEmpty()) {
                // we are in most likely in nested variable list, don't increase the level
                level = pli.getLevel();
                numberId = ApplicationController.getContext().getListNumber(listStyleName, level);
            }
            this.listInfo = new ListInfo(numberId, level);
        }
    }

    protected abstract void setListStyleName();

    public ListInfo getListInfo() {
        return listInfo;
    }

    private static boolean isVariableListBuilder(Builder<?> builder) {
        return AppUtil.isInstanceOf(VariableListBuilder.class, builder);
    }

    private static boolean isOtherListBuilder(Builder<?> builder) {
        return AppUtil.isInstanceOf(OrderedListBuilder.class, builder) ||
                AppUtil.isInstanceOf(ItemizedListBuilder.class, builder);
    }
}
