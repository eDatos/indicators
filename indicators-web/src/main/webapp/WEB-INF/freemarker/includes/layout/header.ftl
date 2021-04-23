[#ftl]
    
    [#if page_title?has_content]
        <title>${page_title}</title>
    [#else]
        <title>[@spring.message "app.title" /]</title>
    [/#if]
    
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=IE8">
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />		
    <meta http-equiv="Content-language" content="es" />	    
    
    <meta name="keywords" content="gobierno canarias, instituto canario de estadística, istac, indicadores, indicators ${extra_keywords}" />
    [#if description?has_content]
    	<meta name="description" content="${description}" />
    [#else]
    	<meta name="description" content=" Instituto Canario de Estadística " />
    [/#if]
    
    <meta name="robots" content="all" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="imagetoolbar" content="no" />
