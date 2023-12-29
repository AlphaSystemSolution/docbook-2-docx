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

    public Table getTable() {
        return table;
    }

    public InformalTable getInformalTable() {
        return informalTable;
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

    public void addTableGroup(TableGroup tableGroup) {
        if (informalTable != null) {
           informalTable.getTableGroup().add(tableGroup);
        } else if (table != null) {
           table.getTableGroup().add(tableGroup);
        }
    }

    public void addColumnSpec(ColumnSpec columnSpec) {
        if (informalTable != null) {
            informalTable.getTableGroup().get(0).getColSpec().add(columnSpec);
        } else if (table != null) {
            table.getTableGroup().get(0).getColSpec().add(columnSpec);
        }
    }

    public void addTableHeader(TableHeader tableHeader) {
        if (informalTable != null) {
            informalTable.getTableGroup().get(0).setTableHeader(tableHeader);
        } else if (table != null) {
            table.getTableGroup().get(0).setTableHeader(tableHeader);
        }
    }

    public void addTableBody(TableBody tableBody) {
        if (informalTable != null) {
            informalTable.getTableGroup().get(0).setTableBody(tableBody);
        } else if (table != null) {
            table.getTableGroup().get(0).setTableBody(tableBody);
        }
    }

    public void addTableFooter(TableFooter tableFooter) {
        if (informalTable != null) {
            informalTable.getTableGroup().get(0).setTableFooter(tableFooter);
        } else if (table != null) {
            table.getTableGroup().get(0).setTableFooter(tableFooter);
        }
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
