[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="indicators-system-view" />

<script type="text/html" id="indicatorsSystemTmpl">
	<div>
		<p><%= code %></p>
		<p><%= getLabel(title, 'es') %></p>
		<p><%= getLabel(description, 'es') %></p>
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