[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div class="listadoTablas">
	<div id="indicators-system-view"></div>
	<div>
		<ul style="margin: 0px; padding-left: 4px;">
			<div id="elements-view"></div>
		</ul>
	</div>
</div>

<script type="text/html" id="indicatorsSystemTemplate">
	<div class="h2roundbox">
		<div class="h2top"></div>
		<div class="h2content">
			<%= getLabel(title, 'es') %>
		</div>
	</div>
	<% if (description != null) { %>
		<br />	
		<div>
			<p><%= getLabel(description, 'es') %></p>
		</div>
	<% } %>	
</script>

<script type="text/html" id="elementTemplate">

	<% if (elementTypeDimension) { %>
		<div style="clear: both;" ></div>
		<% if (level == 1) { %>
			<br />
			<h3 id="cab_1">ES DIMENSIÓN DE NIVEL 1 <%= getLabel(dimension.title, 'es') %></a></h3>
		<% } else if (level == 2) { %>
			<li>
				<span class="itemSubcapitulo">ES DIMENSIÓN DE NIVEL 2 <%= getLabel(dimension.title, 'es') %></span>
			</li>
		<% } else { %>
			<li>
				<span style="margin-left:<%= level * 20 %>px"></span>ES DIMENSIÓN DE NIVEL > 2 <%= getLabel(dimension.title, 'es') %></span>
			</li>
		<% } %>	
		<ul class="subcaps">
	<% } else if (elementTypeIndicatorInstance) { %>
		<li>		
	  		<div style="clear: both;">
				<div class="itemControlInfo">
					<img style="padding-right:3px" border="0" src="[@spring.url "/theme/images/tabla.gif"/]" />
				</div>
				<div class="itemTabla">
					<a class="nouline" href="URL_TO_JAXI"><%= getLabel(indicatorInstance.title, 'es') %></a>
				</div>
			</div>								
		</li>
	<% } %>		
</script>

<script>
	
	var IndicatorsSystemView = Backbone.View.extend({
		template : _.template($('#indicatorsSystemTemplate').html()),
		
		render : function(){
			$(this.el).html(this.template(this.model));
			return this;
		}
	});
	
	var ElementView = Backbone.View.extend({
		template : _.template($('#elementTemplate').html()),
		
		render : function(){
			
			this.model.set({level: this.options.level});
			
			// Element
			var html = this.template(this.model.toJSON());
			
			// Subelements
			if (this.model.get('elementTypeDimension')) {
				var subelements = this.model.get('subelements');
				var subelementsCollection = new ElementsCollection(subelements);				
				
				var subelementsCollectionView = new ElementsCollectionView({collection : subelementsCollection, level : this.options.level + 1});				
				html += subelementsCollectionView.render();
			}
			
			return html;
		}
	});
		
	var ElementsCollection = Backbone.Collection.extend({
	});
	
	var ElementsCollectionView = Backbone.View.extend({	
		
		render : function(){
		
			var viewHtml = '';
			var self = this;
			this.collection.forEach(function(model){
				var elementView = new ElementView({ model : model, level : self.options.level});
				var subViewHtml = elementView.render();
				viewHtml += subViewHtml;
				
				console.log(subViewHtml);
				
			});
			return viewHtml;
		}
	});
	
	$(function(){
		var indicatorsSystemView = new IndicatorsSystemView({el: '#indicators-system-view', model: ${indicatorsSystem}});
		indicatorsSystemView.render();
		
		var elementsCollection = new ElementsCollection(${indicatorsSystemStructure});
		var elementsCollectionView = new ElementsCollectionView({collection : elementsCollection, level : 1});
		var elementsCollectionViewHtml = elementsCollectionView.render();
		$("#elements-view").html(elementsCollectionViewHtml);
	});
	
</script>

[/@template.base]