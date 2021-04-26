[#ftl]
[#include "/includes.ftl"]
[@template.base]

<div id="content-right">
	<div>
		<p><a href="${serverURL}/indicatorsSystems">[@apph.messageEscape 'menu.indicators-systems'/]</a></p>
		<p><a href="${serverURL}/indicators">[@apph.messageEscape 'menu.indicators'/]</a></p>

        <p><strong>[@apph.messageEscape 'page.widgets.title'/]</strong></p>
        <p><a href="${serverURL}/widgets/creator?type=temporal">[@apph.messageEscape 'entity.widgets.type.temporal.label'/]</a></p>
        <p><a href="${serverURL}/widgets/creator?type=lastData">[@apph.messageEscape 'entity.widgets.type.lastData.label'/]</a></p>
        <p><a href="${serverURL}/widgets/creator?type=recent">[@apph.messageEscape 'entity.widgets.type.recent.label'/]</a></p>

	</div>
</div>

[/@template.base]