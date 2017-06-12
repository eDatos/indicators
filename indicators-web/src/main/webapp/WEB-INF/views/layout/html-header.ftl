[#ftl]
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=IE8">
<meta http-equiv="Content-type" content="text/html; charset=utf-8" />		
<meta http-equiv="Content-language" content="es" />	
<meta name="robots" content="all" />
<meta http-equiv="pragma" content="no-cache" />
<meta name="keywords" content="gobierno canarias, instituto canario de estadística, istac, indicadores, indicators ${extra_keywords}" />
[#if description?has_content]
	<meta name="description" content="${description}" />
[#else]
	<meta name="description" content=" Instituto Canario de Estadística " />
[/#if]
<meta http-equiv="imagetoolbar" content="no" />

[#if page_title?has_content]
	<title>${page_title}</title>
[#else]
	<title>[@spring.message "app.title" /]</title>
[/#if]
<link rel="shortcut icon" href="//www.gobiernodecanarias.org/gc/img/favicon.ico"/>

<!-- Css -->

<link rel="stylesheet" href="${serverURL}/theme/css/reset.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="//www.gobiernodecanarias.org/gc/css/estilos.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/css/libs/jquery-ui/jquery-ui-1.8.18.custom.css" type="text/css" media="screen, projection" />

<link rel="stylesheet" href="${serverURL}/theme/css/gobcanoverwrite.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/css/main.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/js/widgets/widgets.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/js/libs/select2/select2.css" type="text/css" media="screen, projection" />

<!-- Js -->
<script type="text/javascript" src="${serverURL}/theme/js/libs/indicators-utils.js"                      ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/jquery-1.7.1.js"                          ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/jquery-ui-1.8.17.custom.js"               ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/jquery.json-2.3.min.js"                   ></script>    
<script type="text/javascript" src="${serverURL}/theme/js/libs/jquery-disable-text-selection-1.0.0.js"   ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/underscore-min-1.3.1.js" 				  ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/underscore.string.min-v.2.0.0-57.js"	  ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/backbone-min-0.9.2.js"					  ></script>
<script type="text/javascript" src="${serverURL}/theme/js/libs/i18n.js"								  ></script>

<link rel="stylesheet" href="${serverURL}/theme/js/libs/colorpicker/css/colorpicker.min.css" type="text/css" media="screen, projection" />
<script type="text/javascript" src="${serverURL}/theme/js/libs/colorpicker/js/colorpicker.js"></script>

<!-- Global variables -->
<script type="text/javascript">
	var serverURL = "${serverURL}";
	var currentLocale = "[@apph.locale /]";
	var defaultLocale = "es";
	[#if indicatorsExternalApiUrlBase??]
		var apiUrl = "${indicatorsExternalApiUrlBase}" + '/v1.0';
	[/#if]
	var visualizerUrl = "${visualizerApplicationExternalUrlBase}";
</script>