package es.gobcan.istac.indicators.core.dspl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DsplData {

    private Set<Row>                 data;

    private String                   dataFileName;
    private Map<String, Set<String>> localisedColumns;
    private Set<Column>              columnDefinitions;
    private List<String>             columnsOrder;

    public DsplData() {
        data = new HashSet<Row>();
        localisedColumns = new HashMap<String, Set<String>>();
        columnDefinitions = new HashSet<Column>();
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setRows(Collection<Row> rows) {
        for (Row row : rows) {
            addRow(row);
        }
    }

    private void addRow(Row row) {
        data.add(row);
        processRow(row);
    }

    public Map<String, Set<String>> getLocalisedColumns() {
        return localisedColumns;
    }

    public void setColumnsToOrder(List<String> columnsOrder) {
        this.columnsOrder = columnsOrder;
    }

    public List<String> getAllColumnsOrderedFullName() {
        List<String> columnOrder = new ArrayList<String>();
        for (String colName : columnsOrder) {
            columnOrder.addAll(getAllPosibleNamesForColumn(colName));
        }

        // Add rest of columns
        Set<String> restColumns = new HashSet<String>();
        for (Column col : getColumnDefinitions()) {
            if (!columnsOrder.contains(col.getName())) {
                restColumns.add(col.getName());
            }
        }

        for (String colName : restColumns) {
            columnOrder.addAll(getAllPosibleNamesForColumn(colName));
        }

        return columnOrder;
    }

    private List<String> getAllPosibleNamesForColumn(String colName) {
        List<String> colNames = new ArrayList<String>();

        Set<String> locales = localisedColumns.get(colName);
        // Not localised column
        if (locales == null) {
            colNames.add(colName);
        } else {
            List<String> localesSorted = new ArrayList<String>(locales);
            Collections.sort(localesSorted);
            for (String locale : localesSorted) {
                colNames.add(colName + "_" + locale);
            }
        }
        return colNames;
    }

    private void processRow(Row row) {
        for (Column col : row.getColumns()) {
            if (col instanceof TextColumn) {
                TextColumn textCol = (TextColumn) col;
                if (textCol.isLocalised()) {
                    Set<String> cols = localisedColumns.get(col.getName());
                    if (cols == null) {
                        cols = new HashSet<String>();
                        localisedColumns.put(col.getName(), cols);
                    }
                    cols.add(textCol.getLocale());
                }
            }
            columnDefinitions.add(col);
        }
    }

    public Set<Column> getColumnDefinitions() {
        return columnDefinitions;
    }

    public List<TextColumn> getTextColumns() {
        List<TextColumn> cols = new ArrayList<DsplData.TextColumn>();
        for (Column col : columnDefinitions) {
            if (col instanceof TextColumn) {
                cols.add((TextColumn) col);
            }
        }
        return cols;
    }

    public List<DateColumn> getDateColumns() {
        List<DateColumn> cols = new ArrayList<DsplData.DateColumn>();
        for (Column col : columnDefinitions) {
            if (col instanceof DateColumn) {
                cols.add((DateColumn) col);
            }
        }
        return cols;
    }

    public List<FloatColumn> getFloatColumns() {
        List<FloatColumn> cols = new ArrayList<DsplData.FloatColumn>();
        for (Column col : columnDefinitions) {
            if (col instanceof FloatColumn) {
                cols.add((FloatColumn) col);
            }
        }
        return cols;
    }

    public Set<String> getColumnNames() {
        Set<String> columnNames = new HashSet<String>();
        for (Column col : columnDefinitions) {
            columnNames.add(col.getName());
        }
        return columnNames;
    }

    public List<Row> getSortedData() {
        List<Row> sortedRows = new ArrayList<DsplData.Row>(this.data);

        final List<String> allColumnsOrder = getAllColumnsOrderedFullName();

        Collections.sort(sortedRows, new Comparator<Row>() {

            @Override
            public int compare(Row o1, Row o2) {
                return Row.multiColumnComparator(allColumnsOrder, o1, o2);
            }

        });

        return sortedRows;
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("Data: [").append("\n");
        for (Row row : data) {
            strBuild.append(row).append("\n");
        }
        strBuild.append("]");
        return strBuild.toString();
    }

    public static class Row {

        private Map<Column, String> values;

        public Row() {
            values = new HashMap<Column, String>();
        }

        public void addColumn(Column column, String value) {
            values.put(column, value);
        }

        public String getCommaSeparatedFieldsInOrder(List<String> columnFullNames) {
            StringBuilder strBuild = new StringBuilder();
            for (String fullName : columnFullNames) {
                String value = getValueForFullNameColumn(fullName);
                if (value != null) {
                    strBuild.append(value);
                }
                strBuild.append(",");
            }
            // remove last comma
            String rowStr = strBuild.toString();
            return rowStr.substring(0, rowStr.length() - 1);
        }

        public String getValueForFullNameColumn(String columnFullName) {
            Column col = new TextColumn(columnFullName);
            return values.get(col);
        }

        public Set<Column> getColumns() {
            return values.keySet();
        }

        public static int multiColumnComparator(List<String> columnOrder, Row rowLeft, Row rowRight) {
            boolean decisionMade = false;
            int decision = 0;

            for (int i = 0; i < columnOrder.size() && !decisionMade; i++) {
                String columnToCompareName = columnOrder.get(i);
                Column column = new TextColumn(columnToCompareName);

                String valueLeft = rowLeft.values.get(column);
                String valueRight = rowRight.values.get(column);

                if (valueLeft != null && valueRight != null) {
                    decision = valueLeft.compareTo(valueRight);
                } else if (valueLeft == valueRight && valueLeft == null) {
                    decision = 0;
                } else if (valueLeft != null) {
                    // null is less
                    decision = 1;
                } else {
                    decision = -1;
                }

                decisionMade = (decision != 0);
            }
            return decision;
        }

        @Override
        public String toString() {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append("{");
            for (Column col : values.keySet()) {
                strBuild.append(col.getFullName()).append(": ").append(values.get(col)).append(", ");
            }
            strBuild.append("}");
            return strBuild.toString();
        }
    }

    public static abstract class Column {

        protected String name;
        private String   type;

        protected Column(String name, String type) {
            this.name = name;
        }

        public String getFullName() {
            return name;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public int hashCode() {
            return getFullName().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof Column) {
                Column other = (Column) obj;
                return getFullName().equals(other.getFullName());
            }
            return false;
        }
    }

    public static class TextColumn extends Column {

        private String locale;

        public TextColumn(String name, String locale) {
            super(name, "string");
            this.locale = locale;
        }

        public TextColumn(String name) {
            super(name, "string");
        }

        public boolean isLocalised() {
            return locale != null;
        }

        @Override
        public String getFullName() {
            return locale != null ? name + "_" + locale : name;
        }

        public String getLocale() {
            return locale;
        };
    }

    public static class FloatColumn extends Column {

        public FloatColumn(String name) {
            super(name, "float");
        }
    }

    public static class DateColumn extends Column {

        protected String format;

        public DateColumn(String name, String format) {
            super(name, "date");
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }
}
