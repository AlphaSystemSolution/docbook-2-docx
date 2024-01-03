package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.impl.AbstractBuilder;
import com.alphasystem.docbook.model.NotImplementedException;
import com.alphasystem.xml.UnmarshallerConstants;
import org.docbook.model.ListItem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ListBuilder<S> extends AbstractBuilder<S> {

    protected ListBuilder() {
        super(null);
    }

    @Override
    protected List<Object> processChildContent(List<Object> childContent) {
        return childContent.stream().map(content -> {
            if (UnmarshallerConstants.isListItemType(content)) {
                final var listItem = (ListItem) content;
                return listItem.getContent().stream().map(obj -> builderFactory.process(obj, this)).flatMap(Collection::stream)
                        .collect(Collectors.toList());
            } else {
                throw new NotImplementedException(source, content);
            }
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    protected List<Object> doProcess(List<Object> processedChildContent) {
        return processedChildContent;
    }
}
