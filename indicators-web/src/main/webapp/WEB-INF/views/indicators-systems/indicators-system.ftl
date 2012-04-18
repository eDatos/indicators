[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]


<div class="listadoTablas">
	<div id="indicators-system-view"></div>
	<div>
		<ul class="subcaps" style="margin: 0px; padding-left: 4px;">
			<div id="elements-view"></div>
		</ul>
	</div>
</div>

<script type="text/html" id="indicatorsSystemTemplate">
	<div class="h2roundbox">
		<div class="h2top"></div>
		<div class="h2content">
			<%= getLabel(title) %>
		</div>
	</div>
	[#include "/inc/language-selector.ftl"]
	<% if (description != null) { %>
		<br />	
		<div>
			<p><%= getLabel(description) %></p>
		</div>
	<% } %>	
</script>

<script type="text/html" id="elementTemplate">

	<% if (elementTypeDimension) { %>
		<div style="clear: both;" ></div>
		<% if (level == 1) { %>
			<br />
			<h3 id="cab_1"><%= getLabel(dimension.title) %></a></h3>
		<% } else if (level == 2) { %>
			<li>
				<span class="itemSubcapitulo"><%= getLabel(dimension.title) %></span>
			</li>
		<% } else { %>
			<li style="font-weight: bold;">
				<span class="itemSubSubcapitulo" style="margin-left:<%= (level - 2)* 20 %>px"></span><%= getLabel(dimension.title) %></span>
			</li>
		<% } %>	
	<% } else if (elementTypeIndicatorInstance) { %>
		<li>		
	  		<div style="clear: both;">
				<div class="itemControlInfo">
					<img style="padding-right:3px" border="0" src="[@spring.url "/theme/images/tabla.gif"/]" />
				</div>
				<div class="itemTabla">
					<a class="nouline" href="PENDIENTE_CONF/jaxi-web/tabla.do?indicators=IDENTIFIER"><%= getLabel(indicatorInstance.title) %></a>
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
				if (subelements != '') {
					html += '<ul class="subcaps">';
					var subelementsCollection = new ElementsCollection(subelements);				
					var subelementsCollectionView = new ElementsCollectionView({collection : subelementsCollection, level : this.options.level + 1});				
					html += subelementsCollectionView.render();
					html += '</ul>';
				}
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