[#ftl]
[#include "/includes.ftl"]
[@template.base]

<div id="content-right">
	<ul>
		<li><a href="${serverURL}/indicatorsSystems">[@apph.messageEscape 'menu.indicators-systems'/]</a></li>
		<li><a href="${serverURL}/indicators">[@apph.messageEscape 'menu.indicators'/]</a></li>
        <li>
            <strong>[@apph.messageEscape 'page.widgets.title'/]</strong>
            <ul>
                <li><a href="${serverURL}/widgets/creator?type=temporal">[@apph.messageEscape 'entity.widgets.type.temporal.label'/]</a></li>
                <li><a href="${serverURL}/widgets/creator?type=lastData">[@apph.messageEscape 'entity.widgets.type.lastData.label'/]</a></li>
                <li><a href="${serverURL}/widgets/creator?type=recent">[@apph.messageEscape 'entity.widgets.type.recent.label'/]</a></li>
            </ul>
        </li>
	</ul>
</div>

[/@template.base]