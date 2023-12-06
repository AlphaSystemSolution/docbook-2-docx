package com.alphasystem.docbook.handler.impl.block;

import com.alphasystem.asciidoc.model.AsciiDocumentInfo;
import com.alphasystem.docbook.ApplicationController;
import com.alphasystem.docbook.builder.model.Admonition;
import com.alphasystem.docbook.handler.BlockHandler;
import com.alphasystem.openxml.builder.wml.WmlAdapter;
import com.alphasystem.openxml.builder.wml.table.ColumnData;
import com.alphasystem.openxml.builder.wml.table.TableAdapter;
import org.docx4j.wml.Tbl;

/**
 * @author sali
 */
abstract class AdmonitionBlockHandler implements BlockHandler<Tbl> {

    private final Admonition admonition;
    private final double widthOfCaptionColumn;

    AdmonitionBlockHandler(Admonition admonition, double widthOfCaptionColumn) {
        this.admonition = admonition;
        this.widthOfCaptionColumn = widthOfCaptionColumn;
    }

    @Override
    public Tbl handleBlock() {
        final AsciiDocumentInfo documentInfo = ApplicationController.getContext().getDocumentInfo();
        String captionText = getAdmonitionCaption(admonition, documentInfo);
        double widthOfContentColumn = 100.0 - widthOfCaptionColumn;
        return new TableAdapter()
                .withTableStyle("AdmonitionTable")
                .withWidths(widthOfCaptionColumn, widthOfContentColumn)
                .startTable()
                .startRow()
                .addColumn(new ColumnData(0).withContent(WmlAdapter.getParagraph(captionText)))
                .addColumn(new ColumnData(1))
                .endRow()
                .getTable();
    }

    private String getAdmonitionCaption(Admonition admonition, AsciiDocumentInfo documentInfo) {
        String title = null;
        switch (admonition) {
            case CAUTION:
                title = documentInfo.getCautionCaption();
                break;
            case IMPORTANT:
                title = documentInfo.getImportantCaption();
                break;
            case NOTE:
                title = documentInfo.getNoteCaption();
                break;
            case TIP:
                title = documentInfo.getTipCaption();
                break;
            case WARNING:
                title = documentInfo.getWarningCaption();
                break;
        }
        return title;
    }
}
