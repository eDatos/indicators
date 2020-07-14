[#ftl]
[#macro base migas='' page_title='' extra_keywords='' page_description='']
<!DOCTYPE html>
<html>
	<head>
		[#include "/layout/html-header.ftl"]
	</head>
	<body>
		<div id="principal_interior">
			<div id="cabecera">
				<div id="cab_superior">
					<ul>
						<li><a href="https://sede.gobcan.es/hacienda/" target="" accesskey="e" title="Sede electrónica">Sede electrónica</a></li>
						<li>|</li>
						<li><a href="http://www.gobiernodecanarias.org/istac/servicios/atencion.html" accesskey="o" title="Contacte con nosotros (tecla de acceso: o)">Contacto</a></li>
					</ul>
				</div>

				<h1><a href="http://www.gobiernodecanarias.org/istac" title="P&aacute;gina principal del Instituto Canario de Estadística (ISTAC) - Opciones de accesibilidad (tecla de acceso: i)" accesskey="i">Instituto Canario de Estadística</a></h1>
				<div id="menu_contextual">
					<ul class="menu">
						  <li class="selec"><a href="/istac/temas_estadisticos/" accesskey="1" title="Estadísticas (tecla de acceso: 1)">Estadísticas</a></li>
						  <li class="inactive"><a href="/istac/istac/" accesskey="2" title="El ISTAC (tecla de acceso: 2)">El ISTAC</a></li>
						  <li class="inactive"><a href="/istac/noticias/" accesskey="4" title="Noticias (tecla de acceso: 4)">Noticias</a></li>
						  <li class="inactive"><a href="/istac/datos-abiertos/" class="inactive" accesskey="5" title="Datos abiertos (tecla de acceso: 5)">Datos abiertos</a></li>
						  <li class="inactive"><a href="http://www.gobiernodecanarias.org/istac/impacto-covid-19/" class="inactive" accesskey="5" title="COVID-19 (tecla de acceso: 5)">COVID-19</a></li>
					</ul>
					[#include "/layout/buscador.ftl"]
				</div>
			</div>
			<div style="clear:both;"></div>
			<div id="migas">
				<p class="txt">[@apph.messageEscape 'app.header.whereis'/]</p>
				<ul>
					<li>
						<a href="/istac">Inicio</a>
					</li>
                    [#if migas?has_content]
                        ${migas}
                    [/#if]
				</ul>
				<div class="redes_sociales">
					<a href="http://www.gobiernodecanarias.org/istac/herramientas/rss.html" accesskey="r" title="Canales RSS (tecla de acceso: r)" target="_blank"><img src="${serverURL}/theme/images/rss_20x20.png" title="Canales RSS (tecla de acceso: r)" alt="Canales RSS (tecla de acceso: r)"></a>
					<a href="https://twitter.com/istac_es" accesskey="t" title="Seguir a istac_es en Twitter (tecla de acceso: t)" target="_blank"><img src="${serverURL}/theme/images/twitter_20x20.png" title="Seguir a istac_es en Twitter (tecla de acceso: t)" alt="Seguir a istac_es en Twitter (tecla de acceso: t)"></a> 
					<a href="https://www.slideshare.net/ISTAC" accesskey="s" title="Seguir a ISTAC en Slideshare (tecla de acceso: s)" target="_blank"><img src="${serverURL}/theme/images/Slideshare_20x20.png" title="Seguir a ISTAC en Slideshare (tecla de acceso: s)" alt="Seguir a ISTAC en Slideshare (tecla de acceso: s)"></a> 
					<a href="https://www.youtube.com/user/istacES" accesskey="s" title="Seguir a ISTAC en Youtube (tecla de acceso: y)" target="_blank"><img src="${serverURL}/theme/images/youtube_20x20.png" title="Seguir a ISTAC en Youtube (tecla de acceso: y)" alt="Seguir a ISTAC en Youtube (tecla de acceso: y)"></a> 
					<a href="https://public.tableau.com/profile/istac#!/" accesskey="s" title="Seguir a ISTAC en Tableau (tecla de acceso: u)" target="_blank"><img src="${serverURL}/theme/images/tableau_20.png" title="Seguir a ISTAC en Tableau (tecla de acceso: u)" alt="Seguir a ISTAC en Tableau (tecla de acceso: u)"></a>
				</div>
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
					<a href="http://www.gobiernodecanarias.org/istac/Open-Definition.html" target="_blank"><img alt="Open Data" height="13px" src="${serverURL}/theme/images/opendata_80x15_black.png" >
					</a> | <a href="http://www.gobiernodecanarias.org/istac/aviso_legal.html">[@apph.messageEscape 'app.footer.legal-advice' /]
					</a> | <a href="http://www.gobiernodecanarias.org/istac/politica-privacidad.html">[@apph.messageEscape 'app.footer.privacy-policy' /]</a> | <a href="http://www.gobiernodecanarias.org/principal/sugrec/">[@apph.messageEscape 'app.footer.suggests' /]</a>					
				</div>
			</div>
		</div>

        <script type="text/javascript">               
            //Analytics
            (function (i, s, o, g, r, a, m) {
            i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date(); a = s.createElement(o),
                m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
            })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');
            
            ga('create', '${analyticsGoogleTrackingId}', 'auto');
            ga('send', 'pageview');
        </script>

	</body>
</html>
[/#macro]