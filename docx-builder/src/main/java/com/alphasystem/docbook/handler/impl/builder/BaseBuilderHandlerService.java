package com.alphasystem.docbook.handler.impl.builder;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import com.alphasystem.docbook.builder.impl.InlineBuilder;
import com.alphasystem.docbook.handler.BuilderHandler;
import com.alphasystem.docbook.handler.BuilderHandlerFactory;
import com.alphasystem.docbook.handler.BuilderHandlerService;

import static java.lang.String.format;

/**
 * @author sali
 */
public class BaseBuilderHandlerService extends BuilderHandlerService {

    @Override
    public void initializeHandlers() {
        final BuilderHandlerFactory instance = BuilderHandlerFactory.getInstance();
        registerHandler(instance, new ArticleHandler());
        registerHandler(instance, new CautionHandler());
        registerHandler(instance, new CrossReferenceHandler());
        registerHandler(instance, new EmphasisHandler());
        registerHandler(instance, new EntryHandler());
        registerHandler(instance, new ExampleHandler());
        registerHandler(instance, new FormalParaHandler());
        registerHandler(instance, new ImportantHandler());
        registerHandler(instance, new InformalExampleHandler());
        registerHandler(instance, new InformalTableHandler());
        registerHandler(instance, new ItemizedListHandler());
        registerHandler(instance, new LinkHandler());
        registerHandler(instance, new ListItemHandler());
        registerHandler(instance, new LiteralHandler());
        registerHandler(instance, new NoteHandler());
        registerHandler(instance, new OrderedListHandler());
        registerHandler(instance, new ParaHandler());
        registerHandler(instance, new PhraseHandler());
        registerHandler(instance, new RowHandler());
        registerHandler(instance, new SectionHandler());
        registerHandler(instance, new SideBarHandler());
        registerHandler(instance, new SimpleParaHandler());
        registerHandler(instance, new SubscriptHandler());
        registerHandler(instance, new SuperscriptHandler());
        registerHandler(instance, new TableBodyHandler());
        registerHandler(instance, new TableFooterHandler());
        registerHandler(instance, new TableHandler());
        registerHandler(instance, new TableHeaderHandler());
        registerHandler(instance, new TermHandler());
        registerHandler(instance, new TextHandler());
        registerHandler(instance, new TipHandler());
        registerHandler(instance, new VariableListEntryHandler());
        registerHandler(instance, new VariableListHandler());
        registerHandler(instance, new WarningHandler());
        registerHandler(instance, new ScreenHandler());
        registerHandler(instance, BlockBuilder.class, new BlockTitleHandler());
        registerHandler(instance, InlineBuilder.class, new InlineTitleHandler());
    }

    private <B extends Builder> void registerHandler(final BuilderHandlerFactory instance, Class<B> builderClass, BuilderHandler handler) {
        String prefix = (builderClass == null) ? "" : format("%s.", builderClass.getSimpleName());
        String key = format("%s%s", prefix, handler.getModelClass().getName());
        instance.registerHandler(key, handler);
    }

    private void registerHandler(final BuilderHandlerFactory instance, BuilderHandler handler) {
       registerHandler(instance, null, handler);
    }
}
