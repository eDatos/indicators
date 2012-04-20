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
			[@apph.messageEscape 'menu.indicators-systems'/]: <%= getLabel(title) %>
		</div>
	</div>
	[#include "/layout/language-selector.ftl"]
	<% if (description != null) { %>
		<div>
			<p><%= getLabel(description) %></p>
		</div>
	<% } %>	
</script>

<script type="text/html" id="elementTemplate">

	<% if (kind == 'indicators#dimension') { %>
		<div style="clear: both;" ></div>
		<% if (level == 1) { %>
			<br />
			<h3 id="cab_1"><%= getLabel(title) %></a></h3>
		<% } else if (level == 2) { %>
			<li>
				<span class="itemSubcapitulo"><%= getLabel(title) %></span>
			</li>
		<% } else { %>
			<li style="font-weight: bold;">
				<span class="itemSubSubcapitulo" style="margin-left:<%= (level - 2)* 20 %>px"></span><%= getLabel(title) %></span>
			</li>
		<% } %>	
	<% } else if (kind == 'indicators#indicatorInstance') { %>
		<li>		
	  		<div style="clear: both;">
				<div class="itemControlInfo">
					<img style="padding-right:3px" border="0" src="[@spring.url "/theme/images/tabla.gif"/]" />
				</div>
				<div class="itemTabla">
					<%= numeration %>
					<a class="nouline" href="<%= '${jaxiUrlBase}' %>/tabla.do?indicators=PENDIENTE_IDENTIFICADOR"><%= getLabel(title) %></a>
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
			
			var numerationBase = '';
			if (this.options.numerationBase != '') {
				numerationBase = this.options.numerationBase + '.';
			}
			numerationBase += _.string.lpad(this.options.numeration, 2, '0');
			this.model.set({numeration: numerationBase});
			
			// Element
			var html = this.template(this.model.toJSON());
			
			// Subelements
			var kind = this.model.get('kind');
			if (kind == 'indicators#dimension') {
				var subelementsNumeration = 1;
				var subelements = this.model.get('elements');
				if (subelements != '') {
					html += '<ul class="subcaps">';
					var subelementsCollection = new ElementsCollection(subelements);				
					var subelementsCollectionView = new ElementsCollectionView({collection : subelementsCollection, level : this.options.level + 1, numerationBase : numerationBase, numeration : subelementsNumeration});				
					html += subelementsCollectionView.render();
					html += '</ul>';
					subelementsNumeration += 1;
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
			var numeration = self.options.numeration;
			this.collection.forEach(function(model){
				var elementView = new ElementView({ model : model, level : self.options.level, numerationBase : self.options.numerationBase, numeration : numeration});
				var subViewHtml = elementView.render();
				viewHtml += subViewHtml;
				numeration += 1;
			});
			return viewHtml;
		}
	});
	
	$(function(){

		var indicatorsSystem = ${indicatorsSystem};

		var indicatorsSystemView = new IndicatorsSystemView({el: '#indicators-system-view', model: indicatorsSystem});
		indicatorsSystemView.render();
		
		var elementsCollection = new ElementsCollection(indicatorsSystem.elements);
		var elementsCollectionView = new ElementsCollectionView({collection : elementsCollection, level : 1, numerationBase : '', numeration : 1});
		var elementsCollectionViewHtml = elementsCollectionView.render();
		$("#elements-view").html(elementsCollectionViewHtml);
	});
	
</script>
[/@template.base]