package com.alphasystem.docbook.model;

import org.docbook.model.*;

import java.util.List;

public class DocBookTableAdapter {

    private final Table table;
    private final InformalTable informalTable;

    private DocBookTableAdapter(InformalTable informalTable, Table table) {
        if (table != null && informalTable != null) {
            throw new IllegalArgumentException("Only one of table or informalTable is allowed.");
        }
        this.table = table;
        this.informalTable = informalTable;
    }

    public List<TableGroup> getTableGroup() {
        if (informalTable != null) {
            return informalTable.getTableGroup();
        } else if (table != null) {
            return table.getTableGroup();
        }
        return null;
    }

    public Frame getFrame() {
        if (informalTable != null) {
            return informalTable.getFrame();
        } else if (table != null) {
            return table.getFrame();
        }
        return null;
    }

    public Choice getRowSep() {
        if (informalTable != null) {
            return informalTable.getRowSep();
        } else if (table != null) {
            return table.getRowSep();
        }
        return null;
    }

    public Choice getColSep() {
        if (informalTable != null) {
            return informalTable.getColSep();
        } else if (table != null) {
            return table.getColSep();
        }
        return null;
    }

    public String getTableStyle() {
        if (informalTable != null) {
            return informalTable.getTableStyle();
        } else if (table != null) {
            return table.getTableStyle();
        }
        return null;
    }

    public static DocBookTableAdapter fromInformalTable(InformalTable informalTable) {
        if (informalTable == null) {
            throw new IllegalArgumentException("informalTable cannot be null.");
        }
        return new DocBookTableAdapter(informalTable, null);
    }

    public static DocBookTableAdapter fromTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("table cannot be null.");
        }
        return new DocBookTableAdapter(null, table);
    }
}
