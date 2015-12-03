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
						<li class="redes_sociales">Síguenos: 
							<a href="http://www.gobiernodecanarias.org/istac/herramientas/rss.html" accesskey="r" title="Canales RSS (tecla de acceso: r)">
								<img src="${serverURL}/theme/images/atom.png" title="Canales RSS (tecla de acceso: r)" alt="Canales RSS (tecla de acceso: r)">
							</a><a href="http://www.gobiernodecanarias.org/istac/twitter" accesskey="t" title="Seguir a istac_es en Twitter (tecla de acceso: t)">
								<img src="${serverURL}/theme/images/t_mini-a.png" title="Seguir a istac_es en Twitter (tecla de acceso: t)" alt="Seguir a istac_es en Twitter (tecla de acceso: t)">
							</a><a href="http://www.slideshare.net/ISTAC" accesskey="s" title="Seguir a ISTAC en Slideshare (tecla de acceso: s)">
								<img src="${serverURL}/theme/images/slideshare.jpg" title="Seguir a ISTAC en Slideshare (tecla de acceso: s)" alt="Seguir a ISTAC en Slideshare (tecla de acceso: s)">
							</a><a href="http://www.youtube.com/user/istacES" accesskey="s" title="Seguir a ISTAC en Youtube (tecla de acceso: y)">
								<img src="${serverURL}/theme/images/youtube.png" title="Seguir a ISTAC en Youtube (tecla de acceso: y)" alt="Seguir a ISTAC en Youtube (tecla de acceso: y)">
							</a>
						</li>
						<li>|</li>
						<li><a href="http://www.gobiernodecanarias.org/istac/servicios/atencion.html" accesskey="o" title="Contacte con nosotros (tecla de acceso: o)">Contacto</a></li>				
					</ul>
				</div>

				<h1><a href="http://www.gobiernodecanarias.org/istac" title="P&aacute;gina principal del Instituto Canario de Estadística (ISTAC) - Opciones de accesibilidad (tecla de acceso: i)" accesskey="i">Instituto Canario de Estadística</a></h1>
				<div id="menu_contextual">
					<ul class="menu">
						  <li class="selec"><a href="/istac/estadisticas.jsp" accesskey="2" title="Estadísticas (tecla de acceso: 2)">Estadísticas</a></li>
						  <li class="inactive"><a href="/istac/el_istac.jsp" accesskey="3" title="El ISTAC (tecla de acceso: 3)">El ISTAC</a></li>
						  <li class="inactive"><a href="/istac/webescolar" accesskey="4" title="WEB Escolar (tecla de acceso: 4)">WEB Escolar</a></li>
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
					</a> | <a href="http://www.gobiernodecanarias.org/sugrec/">[@apph.messageEscape 'app.footer.suggests' /]</a>					
				</div>
			</div>
		</div>

        <script type="text/javascript">
            //Analytics
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-28460379-1']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();
        </script>

	</body>
</html>
[/#macro]