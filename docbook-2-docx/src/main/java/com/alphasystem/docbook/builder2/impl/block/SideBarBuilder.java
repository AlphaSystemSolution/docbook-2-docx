package com.alphasystem.docbook.builder2.impl.block;

import com.alphasystem.docbook.builder2.Builder;
import com.alphasystem.docbook.builder2.impl.JavaScriptBasedBuilder;
import com.alphasystem.util.AppUtil;
import org.docbook.model.SideBar;
import org.docbook.model.Title;
import org.docx4j.wml.Tbl;

import java.util.List;
import java.util.Objects;

public class SideBarBuilder extends JavaScriptBasedBuilder<SideBar, Tbl> {

    public SideBarBuilder(SideBar source, Builder<?> parent) {
        super(source, parent);
    }

    @Override
    protected Title getTitle() {
        return null;
    }

    @Override
    protected FunctionInput<Tbl> initFunctionInputs(List<Object> processedChildContent) {
        final var title = (Title) source.getTitleContent().stream().filter(c -> AppUtil.isInstanceOf(Title.class, c))
                .findFirst().orElse(null);
        final var args = new Object[2];
        if (Objects.nonNull(title)) {
           args[0] = builderFactory.process(title, this).get(0);
        }
        args[1] = processedChildContent;
        return new FunctionInput<>(configurationUtils.getString("sidebar.functionName"), Tbl.class, args);
    }
}
