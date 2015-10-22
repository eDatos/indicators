[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="content-right">
	<div>
		<p><a href="${serverURL}/indicatorsSystems">[@apph.messageEscape 'menu.indicators-systems'/]</a></p>
		<p><a href="${serverURL}/indicators">[@apph.messageEscape 'menu.indicators'/]</a></p>

        <p><strong>Widgets</strong></p>
        <p><a href="${serverURL}/widgets/creator?type=temporal">Widget gráfico de evolución</a></p>
        <p><a href="${serverURL}/widgets/creator?type=lastData">Widget últimos datos</a></p>
        <p><a href="${serverURL}/widgets/creator?type=recent">Widget últimos indicadores actualizados</a></p>

	</div>
</div>

[/@template.base]