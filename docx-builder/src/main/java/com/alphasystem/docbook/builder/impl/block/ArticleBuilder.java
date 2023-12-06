package com.alphasystem.docbook.builder.impl.block;

import com.alphasystem.docbook.builder.Builder;
import com.alphasystem.docbook.builder.impl.BlockBuilder;
import org.docbook.model.Article;
import org.docbook.model.Info;
import org.docbook.model.Title;

import java.util.Collections;

/**
 * @author sali
 */
public class ArticleBuilder extends BlockBuilder<Article> {

    public ArticleBuilder(Builder<?> parent, Article article, int indexInParent) {
        super(parent, article, indexInParent);
    }

    @Override
    protected void initContent() {
        content = source.getContent();
        final Info info = getContent(Info.class, content);
        if (info != null) {
            final Title title = getContent(Title.class, info.getContent());
            if (title != null) {
                titleContent = Collections.singletonList(title);
            }
        }
    }

}
