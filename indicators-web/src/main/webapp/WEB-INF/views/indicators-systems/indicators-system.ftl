[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="indicators-system-view"></div>
<div id="elements-view"></div>

<script type="text/html" id="indicatorsSystemTemplate">
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

<script type="text/html" id="indicatorsSystemStructureTemplate">
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

<script type="text/html" id="elementTemplate">
	<div>
		<%= orderInLevel %>
	</div>
</script>

<script>
	
	var IndicatorsSystemView = Backbone.View.extend({
	 	el: '#indicators-system-view',
		template : _.template($('#indicatorsSystemTemplate').html()),
		
		render : function(){
			$(this.el).html(this.template(this.model));
			return this;
		}
	});
	
	var ElementView = Backbone.View.extend({
		template : _.template($('#elementTemplate').html()),
		
		render : function(){
			var html = this.template(this.model.toJSON());
			return html;
		}
	});
		
	var ElementsCollection = Backbone.Collection.extend({
		
		initialize : function(){
			_.bindAll(this);
		}
	});
	
	var ElementsCollectionView = Backbone.View.extend({	
			
		render : function(){
			var self = this;
			var viewHtml = '';
			this.collection.forEach(function(model){
				var elementView = new ElementView({ model : model});
				var subViewHtml = elementView.render();
				viewHtml += subViewHtml;
			});
			$(self.el).html(viewHtml);
		}
	});
	
	$(function(){
		var indicatorsSystemView = new IndicatorsSystemView({model: ${indicatorsSystem}});
		indicatorsSystemView.render();
		
		var elementsCollection = new ElementsCollection(${indicatorsSystemStructure});
		var elementsCollectionView = new ElementsCollectionView({el : $("#elements-view"), collection : elementsCollection});
		elementsCollectionView.render();
	});
	
</script>

[/@template.base]