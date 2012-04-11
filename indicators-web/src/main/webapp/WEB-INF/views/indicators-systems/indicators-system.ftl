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
		<p><%= getLabel(description, 'es') %></p>
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
		
		<% if (elementTypeDimension) { %>
			<p><%= getLabel(dimension.title, 'es') %></p>
						
		<% } else if (elementTypeIndicatorInstance) { %>
		
			<%= getLabel(indicatorInstance.title, 'es') %>
		<% } %>
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
					
			// Element
			var html = this.template(this.model.toJSON());
			
			// Subelements
			if (this.model.get('elementTypeDimension')) {
				var subelements = this.model.get('subelements');
				var subelementsCollection = new ElementsCollection(subelements);				
				
				var subelementsCollectionView = new ElementsCollectionView({collection : subelementsCollection});				
				html += subelementsCollectionView.render();
			}
			
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
			var viewHtml = '';
			this.collection.forEach(function(model){
				var elementView = new ElementView({ model : model});
				var subViewHtml = elementView.render();
				viewHtml += subViewHtml;
			});
			return viewHtml;
		}
	});
	
	$(function(){
		var indicatorsSystemView = new IndicatorsSystemView({model: ${indicatorsSystem}});
		indicatorsSystemView.render();
		
		var elementsCollection = new ElementsCollection(${indicatorsSystemStructure});
		var elementsCollectionView = new ElementsCollectionView({collection : elementsCollection});
		var elementsCollectionViewHtml = elementsCollectionView.render();
		$("#elements-view").html(elementsCollectionViewHtml);
	});
	
</script>

[/@template.base]