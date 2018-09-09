package com.pdm;

import java.util.List;

public class Table {
    private String tableId;
    private String tableName;
    private String tableCode;
    private String parentTableId;
    private List<Column> allColumns;
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getTableCode() {
        return tableCode;
    }
    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
    public List<Column> getAllColumns() {
        return allColumns;
    }
    public void setAllColumns(List<Column> allColumns) {
        this.allColumns = allColumns;
    }
    public String getParentTableId() {
        return parentTableId;
    }
    public void setParentTableId(String parentTableId) {
        this.parentTableId = parentTableId;
    }
}
