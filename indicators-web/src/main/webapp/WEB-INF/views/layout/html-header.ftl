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
<link rel="stylesheet" href="${serverURL}/theme/js/libs/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/js/libs/colorpicker/css/colorpicker.min.css" type="text/css" media="screen, projection" />

<link rel="stylesheet" href="${serverURL}/theme/css/gobcanoverwrite.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/css/main.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="${serverURL}/theme/js/widgets/widgets.css" type="text/css" media="screen, projection" />

<script src="${serverURL}/theme/js/vendor.min.js"></script>

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