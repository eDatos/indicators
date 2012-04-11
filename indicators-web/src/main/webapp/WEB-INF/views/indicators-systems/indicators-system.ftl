[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="indicators-system-view" />

<script type="text/html" id="indicatorsSystemTmpl">
	<div class="listadoTablas">
		<div class="h2roundbox">
			<div class="h2top"></div>
			<div class="h2content">
				<%= getLabel(title, 'es') %>
			</div>
		</div>
	</div>
	<br />		
	<div>
		<p><%= getLabel(description, 'es') %>fdaf</p>
	</div>
</script>

<script>
	
	var IndicatorsSystemView = Backbone.View.extend({
	 	el: '#indicators-system-view',
		template : _.template($('#indicatorsSystemTmpl').html()),
		
		render : function(){
			$(this.el).html(this.template(this.model));
			return this;
		}
	});
	
	$(function(){
		var indicatorsSystemView = new IndicatorsSystemView({model: ${indicatorsSystem}});
		indicatorsSystemView.render();
	});
	
</script>

[/@template.base]