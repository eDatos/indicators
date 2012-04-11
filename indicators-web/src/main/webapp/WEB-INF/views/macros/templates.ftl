[#ftl]
[#macro base]
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8" />		
		<meta http-equiv="Content-language" content="es" />	
		<meta name="robots" content="all" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta name="keywords" content="gobierno canarias " />
		<meta name="description" content=" Instituto Canario de Estadística " />
		<meta http-equiv="imagetoolbar" content="no" />
		
		<title>[@spring.message "app.title" /]</title>
		<link rel="shortcut icon" href="http://www.gobiernodecanarias.org/gc/img/favicon.ico"/>
		
	 	<!-- Css -->
		<link rel="stylesheet" href="[@spring.url "/theme/css/reset.css"/]" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="http://www.gobiernodecanarias.org/gc/css/estilos.css" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="[@spring.url "/theme/css/gobcanoverwrite.css"/]" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="[@spring.url "/theme/css/main.css"/]" type="text/css" media="screen, projection" />

		<script type="text/javascript">
			var context = "[@spring.url '' /]";
		</script>
		
		<!-- Js -->
		<script type="text/javascript" src="[@spring.url "/theme/js/libs/indicators-utils.js"                     /]" ></script>
		<script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery-1.7.1.js"                         /]" ></script>
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery-ui-1.8.17.custom.js"              /]" ></script>
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery.json-2.3.min.js"                  /]" ></script>    
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery-disable-text-selection-1.0.0.js"  /]" ></script>
        <script type="text/javascript" src="[@spring.url "/theme/js/libs/underscore-min-1.3.1.js" 				  /]"></script>
		<script type="text/javascript" src="[@spring.url "/theme/js/libs/backbone-min-0.9.2.js"					  /]"></script>
		<script type="text/javascript" src="[@spring.url "/theme/js/libs/i18n.js"								  /]"></script>
	</head>
	<body>
		<div id="principal_interior">
			<div id="cabecera">
				<div id="cab_superior">
					<ul>
						<li><a href="http://www.gobiernodecanarias.org/istac/herramientas/rss.jsp" accesskey="r" title="Rich Site Summary (RSS) (tecla de acceso: r)"><img height="14" width="14" style="vertical-align: text-bottom; float: none;" src="[@spring.url "/theme/gobcan/img/atom.png"/]" title="RSS" alt="RSS" /> RSS</a></li>
						<li>|</li>
						<li><a href="http://www.gobiernodecanarias.org/istac/servicios/atencion.jsp" accesskey="o" title="Contacte con nosotros (tecla de acceso: o)">Contacto</a></li>
				
					</ul>
				</div>

				<h1><a href="http://www.gobiernodecanarias.org/istac" title="P&aacute;gina principal del Instituto Canario de Estadística (ISTAC) - Opciones de accesibilidad (tecla de acceso: i)" accesskey="i">Instituto Canario de Estadística</a></h1>
				<div id="menu_contextual">
					<ul class="menu">
						  <li class="selec"><a href="/istac/estadisticas.jsp" accesskey="2" title="Estadísticas (tecla de acceso: 2)">Estadísticas</a></li>
						  <li class="inactive"><a href="/istac/el_istac.jsp" accesskey="3" title="El ISTAC (tecla de acceso: 3)">El ISTAC</a></li>
						  <li class="inactive"><a href="/istac/webescolar" accesskey="4" title="WEB Escolar (tecla de acceso: 4)">WEB Escolar</a></li>
					</ul>
				</div>
			</div>
			<div style="clear:both;"></div>
			<div id="migas">
				<p class="txt">[@apph.messageEscape 'app.header.whereis'/]</p>
				<ul>
					<li>
						<a href="/istac">Inicio</a>
					</li>
					<li>
						<strong>TODO<strong>
					</li>
				</ul>
			</div>

			<div id="bloq_interior">
				<div class="bloque_completo">
					<div class="conten">	
						[#nested]
					</div>
				</div>
			</div>
			   <div id="pie">
				<p class="izda">[@apph.messageEscape 'app.footer.gobcan' /]</p>
				<div class="dcha">
					<ul>
						<li class="nobarra3"><a href="http://www.gobiernodecanarias.org/avisolegal.html">[@apph.messageEscape 'app.footer.legal-advice' /]</a></li>
						<li><a href="http://www.gobiernodecanarias.org/sugrec/">[@apph.messageEscape 'app.footer.suggests' /]</a></li>
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>
[/#macro]