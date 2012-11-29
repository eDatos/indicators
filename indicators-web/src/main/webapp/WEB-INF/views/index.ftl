[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="content-right">
	<div>
		<p><a href="[@spring.url "/indicatorsSystems"/]">[@apph.messageEscape 'menu.indicators-systems'/]</a></p>
		<p><a href="[@spring.url "/indicators"/]">[@apph.messageEscape 'menu.indicators'/]</a></p>

        <p><strong>Widgets</strong></p>
        <p><a href="[@spring.url "/widgets/creator?type=temporal"/]">Widget temporal</a></p>
        <p><a href="[@spring.url "/widgets/creator?type=lastData"/]">Widget último dato</a></p>
        <p><a href="[@spring.url "/widgets/creator?type=recent"/]">Widget último dato más reciente</a></p>

	</div>
</div>

[/@template.base]