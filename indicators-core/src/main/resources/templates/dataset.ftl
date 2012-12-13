[#ftl]
<dspl xmlns="http://schemas.google.com/dspl/2010" 
      xmlns:geo="http://www.google.com/publicdata/dataset/google/geo"
      xmlns:time="http://www.google.com/publicdata/dataset/google/time"
      xmlns:entity="http://www.google.com/publicdata/dataset/google/entity"
      xmlns:unit="http://www.google.com/publicdata/dataset/google/unit"
      xmlns:quantity="http://www.google.com/publicdata/dataset/google/quantity">
      
    <import namespace="http://www.google.com/publicdata/dataset/google/geo"/>
	<import namespace="http://www.google.com/publicdata/dataset/google/time"/>
   	<import namespace="http://www.google.com/publicdata/dataset/google/entity"/>
   	<import namespace="http://www.google.com/publicdata/dataset/google/unit"/>
   	<import namespace="http://www.google.com/publicdata/dataset/google/quantity"/>
   	
   	
   	<info>
   		[#assign info = dataset.getDatasetInfo()]
   		[#if info.getName()?? && !info.getName().isEmpty()]
   		<name>
			[#if info.getName().hasNotLocalisedValue()]
			<value>${info.getName().getValue()}</value>
			[/#if]
			[#list info.getName().getLocales() as locale]
			<value xml:lang="${locale}">${info.getName().getText(locale)}</value>
			[/#list]
		</name>
		[/#if]
		[#if info.getDescription()?? && !info.getDescription().isEmpty()]
		<description>
			[#if info.getDescription().hasNotLocalisedValue()]
			<value>${info.getDescription().getValue()}</value>
			[/#if]
			[#list info.getDescription().getLocales() as locale]
			<value xml:lang="${locale}">${info.getDescription().getText(locale)}</value>
			[/#list]
		</description>
		[/#if]
		[#if info.getUrl()?? && !info.getUrl().isEmpty()]
		<url>
			[#if info.getUrl().hasNotLocalisedValue()]
			<value>${info.getUrl().getValue()}</value>
			[/#if]
			[#list info.getUrl().getLocales() as locale]
			<value xml:lang="${locale}">${info.getUrl().getText(locale)}</value>
			[/#list]
		</url>
		[/#if]
   	</info>
   	
   	<provider>
      	<name>
         	<value>Proveedor</value>
      	</name>
      	<url>
         	<value>http://proveedor.com/es</value>
      	</url>
   	</provider>
   	
   	[#if dataset.getTopics()?has_content]
   	<topics>
   	[#list dataset.getTopics() as topic]
   		<topic id="${topic.getId()}">
   			<info>
		   		[#assign info = topic.getInfo()]
		   		[#if info.getName()?? && !info.getName().isEmpty()]
		   		<name>
					[#if info.getName().hasNotLocalisedValue()]
					<value>${info.getName().getValue()}</value>
					[/#if]
					[#list info.getName().getLocales() as locale]
					<value xml:lang="${locale}">${info.getName().getText(locale)}</value>
					[/#list]
				</name>
				[/#if]
				[#if info.getDescription()?? && !info.getDescription().isEmpty()]
				<description>
					[#if info.getDescription().hasNotLocalisedValue()]
					<value>${info.getDescription().getValue()}</value>
					[/#if]
					[#list info.getDescription().getLocales() as locale]
					<value xml:lang="${locale}">${info.getDescription().getText(locale)}</value>
					[/#list]
				</description>
				[/#if]
			</info>
   		</topic>
   	[/#list]
   	</topics>
   	[/#if]
   	
   	<concepts>
   	[#list dataset.getConcepts() as concept]
   		<concept id="${concept.getId()}" extends="${concept.getExtend()}">
   			<info>
   				[#assign info = concept.getInfo()]
   				[#if info.getName()?? && !info.getName().isEmpty()]
	   				<name>
	   				[#if info.getName().hasNotLocalisedValue()]
	   					<value>${info.getName().getValue()}</value>
	   				[/#if]
	   				[#list info.getName().getLocales() as locale]
	   					<value xml:lang="${locale}">${info.getName().getText(locale)}</value>
	   				[/#list]
	   				</name>
   				[/#if]
   				[#if info.getDescription()?? && !info.getDescription().isEmpty()]
	   				<description>
	   				[#if info.getDescription().hasNotLocalisedValue()]
	   					<value>${info.getDescription().getValue()}</value>
	   				[/#if]
	   				[#list info.getDescription().getLocales() as locale]
	   					<value xml:lang="${locale}">${info.getDescription().getText(locale)}</value>
	   				[/#list]
	   				</description>
   				[/#if]
   			</info>
   			
   			[#list concept.getTopics() as topicRef]
   			<topic ref="${topicRef}"/>
   			[/#list]
   			
   			[#list concept.getConceptAttributes() as attribute]
   			<attribute id="${attribute.getId()}" concept="${attribute.getConceptRef()}">
   				[#if attribute.getValue().hasNotLocalisedValue()]
   					<value>${attribute.getValue().getValue()}</value>
   				[/#if]
   				[#list attribute.getValue().getLocales() as locale]
   					<value xml:lang="${locale}">${attribute.getValue().getText(locale)}</value>
   				[/#list]
   			</attribute>
   			[/#list]
   			
   			[#list concept.getSimpleAttributes() as attribute]
   			<attribute id="${attribute.getId()}">
   				[#if attribute.getValue().hasNotLocalisedValue()]
   					<value>${attribute.getValue().getValue()}</value>
   				[/#if]
   				[#list attribute.getValue().getLocales() as locale]
   					<value xml:lang="${locale}">${attribute.getValue().getText(locale)}</value>
   				[/#list]
   			</attribute>
   			[/#list]
   			
   			[#if concept.getTable()??]
   			<table ref="${concept.getTable().getId()}">
   				[#assign localisedColumns = concept.getTable().getData().getLocalisedColumns()]
   				[#list localisedColumns?keys as prop]
   					[#list localisedColumns[prop] as locale]
   						<mapProperty ref="${prop}" xml:lang="${locale}" toColumn="${prop}_${locale}"/>
   					[/#list]
   				[/#list]
   			</table>
   			[/#if]
   		</concept>
   	[/#list]
   	</concepts>
   	
   	<slices>
   	[#list dataset.getSlices() as slice]
   		<slice id="${slice.getId()}">
   			[#list slice.getDimensions() as dim]
   			<dimension concept="${dim}"/>
   			[/#list]
   			[#list slice.getMetrics() as metric]
   			<metric concept="${metric}"/>
   			[/#list]
   			[#if slice.getTable()??]
   			<table ref="${slice.getTable().getId()}"/>
   			[/#if]
   		</slice>
   	[/#list]
   	</slices>
   	
   	<tables>
   	[#list dataset.getTables() as table]
   		[#assign localisedColumns = table.getData().getLocalisedColumns()]
   		<table id="${table.getId()}">
   			[#list table.getData().getDateColumns() as col]
   			<column id="${col.getFullName()}" type="date" format="${col.getFormat()}"/>
   			[/#list]
   			[#list table.getData().getTextColumns() as col]
   			<column id="${col.getFullName()}" type="string"/>
   			[/#list]
   			[#list table.getData().getFloatColumns() as col]
   			<column id="${col.getFullName()}" type="float"/>
   			[/#list]
			<data>
				<file format="csv" encoding="utf-8">${table.getData().getDataFileName()}</file>
			</data>
   		</table>
   	[/#list]
   	</tables>
   	
</dspl>
      