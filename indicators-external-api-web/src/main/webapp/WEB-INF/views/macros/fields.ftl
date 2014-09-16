[#ftl strip_whitespace=true]
[#--
 * fieldOutput
 --]
 [#macro fieldOutput value label attributes="" class="" hint="" rendered=true]
	[#if rendered]
		[@spring.bind value /]
		
		<div class="field" >
			<div class="label label-output">
				<div><em>&nbsp;</em>[@spring.messageText label label /]</div>
			</div>
			<div class="output">
				<div>${spring.stringStatusValue}</div>
			</div>
			<div class="clearfix"></div>
		</div>
	[/#if]
[/#macro]