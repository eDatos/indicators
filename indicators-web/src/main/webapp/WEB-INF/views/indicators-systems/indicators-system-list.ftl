[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="content-right">
	<h3 class="left">[@spring.message 'page.indicators-system-list.title' /]</h3>
	
	<ul class="toolbar right">
	</ul>	
	
	<div class="clearfix" ></div>
	<div class="sep-2">&nbsp;</div>
	
	<div class="data-table">
		<div class="data-table-body">
			<table cellpadding="0" cellspacing="0">
				<colgroup>
					<col class="col-20" />
				</colgroup>
				<thead>
					<tr>
						<th class="data-table-col-15">[@spring.message 'entity.indicators-system.code' /]</th>
					</tr>
				</thead>
				<tbody>
					[#list indicatorsSystems as indicatorsSystem]
						<tr>
							<td><a href="[@spring.url "/indicators-ssytems/${indicatorsSystem.code}.html" /]">${indicatorsSystem.code}</a></td>
						</tr>
					[/#list]
				</tbody>
			</table>
		</div>
	</div>
</div>
<div class="clearfix" ></div>


[/@template.base]