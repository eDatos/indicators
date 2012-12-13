[#ftl]
[#list table.getData().getAllColumnsOrderedFullName() as colName]${colName}[#if colName_has_next],[/#if][/#list]
[#compress]
[#list table.getData().getSortedData() as row]
	${row.getCommaSeparatedFieldsInOrder(table.getData().getAllColumnsOrderedFullName())}
[/#list]
[/#compress]