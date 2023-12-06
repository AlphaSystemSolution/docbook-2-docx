package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.openxml.builder.wml.PPrBuilder;
import org.docbook.model.ListItem;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.NumPr;

import static com.alphasystem.openxml.builder.wml.WmlAdapter.getNumPr;
import static com.alphasystem.openxml.builder.wml.WmlBuilderFactory.getPPrBuilder;

/**
 * @author sa
 */
public class ListItemBuilder extends BlockBuilder<ListItem> {

    private long number = -1;
    private long level = -1;

    public ListItemBuilder(Builder<?> parent, ListItem listitem, int indexInParent) {
        super(parent, listitem, indexInParent);
    }

    @Override
    protected void initContent() {
        content = source.getContent();
        listParaProperties = getPPrBuilder().withPStyle(configurationUtils.getDefaultListStyle()).getObject();
    }

    @Override
    protected void preProcess() {
        PPr pPr = listParaProperties;
        var currentParent = (BlockBuilder<?>) parent;
        while (currentParent != null) {
            final PPr listParaProperties = currentParent.getListParaProperties();
            if (listParaProperties != null) {
                pPr = listParaProperties;
                break;
            }
            currentParent = (BlockBuilder<?>) currentParent.getParent();
        }
        NumPr numPr = ((getNumber() < 0) || (getLevel() < 0)) ? null : getNumPr(getNumber(), getLevel());
        paraProperties = new PPrBuilder(pPr, paraProperties).withNumPr(numPr).getObject();
    }

    @Override
    protected Builder<?> getChildBuilder(Object o, int index) {
        final var builder = super.getChildBuilder(o, index);
        if (index == 1) {
            paraProperties.setNumPr(null);
        }
        return builder;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

}
