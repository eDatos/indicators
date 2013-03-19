[#ftl]
[#include "/inc/includes.ftl"]
[@template.base migas='Sistema de indicadores']



<div class="listadoTablas">
	<div id="indicators-system-view"></div>
	<div>
		<ul class="subcaps" style="margin: 0px; padding-left: 4px;">
			<div id="elements-view"></div>
		</ul>
	</div>
</div>
<div id="indicators-system-not-found-view"></div>

<script type="text/html" id="indicatorsSystemTemplate">
	<div class="h2roundbox">
		<div class="h2top"></div>
		<div class="h2content">
			[@apph.messageEscape 'menu.indicators-systems'/]: <%= getLabel(title) %>
			<a href="<%= context %>/indicatorsSystems/<%=code%>/atom.xml?language=${.lang}" style="float: right; font-weight: normal; margin-right: 5px;" ><img height="14" width="14" style="margin-right:5px; vertical-align: text-bottom; float: none;" src="[@spring.url "/theme/images/atom.png"/]" title="RSS" alt="RSS" />RSS</a>
		</div>
	</div>

	<% if (description) { %>
		<div>
			<p><%= getLabel(description) %></p>
		</div>
	<% } %>

    <% if (objective) { %>
        <p><%= getLabel(objective) %></p>
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
		<ul class="dimensionSubElement" style="margin-left: 0px;">
		</ul>	
	<% } else if (kind == 'indicators#indicatorInstance') { %>
		<li>		
	  		<div style="clear: both;">
				<div class="itemControlInfo">
					<img id="<%= id %>" style="padding-right:3px" border="0" src="[@spring.url "/theme/images/tabla.gif"/]">
				</div>
				<div class="itemTabla">
					<%= numeration %>
					<a class="nouline" href="<%= '${jaxiUrlBase}' %>/tabla.do?instanciaIndicador=<%= id %>&sistemaIndicadores=<%= indicatorsSystemCode %>&accion=html"><%= getLabel(title) %></a>
				</div>
				<div class="clearfix"></div>
				<div class="indicatorInstanceDetail"></div>
			</div>								
		</li>
	<% } %>		
</script>

<script type="text/html" id="indicatorInstanceTemplate">
	
	<div>
		<ul>
		<% if (dimension.GEOGRAPHICAL.granularity != null) { 
		%>
			<li>
				[@apph.messageEscape 'entity.indicator-instance.geographical-granularities'/]:
		<%	for (var i = 0; i < dimension.GEOGRAPHICAL.granularity.length; i++) {
				var granularity = dimension.GEOGRAPHICAL.granularity[i];
		%>
				<%= getIndicatorInstanceSubelementDimension(granularity) %><% if (i != dimension.GEOGRAPHICAL.granularity.length - 1) { %>,<% } %>
		<%	} 
		%>
			</li>
		<% } %>
		<% if (dimension.GEOGRAPHICAL.representation != null) { 
		%>
			<li>
				[@apph.messageEscape 'entity.indicator-instance.geographical-values'/]:
		<%	for (var i = 0; i < dimension.GEOGRAPHICAL.representation.length; i++) {
				var representation = dimension.GEOGRAPHICAL.representation[i];
		%>
				<%= getIndicatorInstanceSubelementDimension(representation) %><% if (i != dimension.GEOGRAPHICAL.representation.length - 1) { %>,<% } %>
		<%	} 
		%>
			</li>
		<% } %>
				<% if (dimension.TIME.granularity != null) { 
		%>
			<li>
				[@apph.messageEscape 'entity.indicator-instance.time-granularities'/]:
		<%	for (var i = 0; i < dimension.TIME.granularity.length; i++) {
				var granularity = dimension.TIME.granularity[i];
		%>
				<%= getIndicatorInstanceSubelementDimension(granularity) %><% if (i != dimension.TIME.granularity.length - 1) { %>,<% } %>
		<%	} 
		%>
			</li>
		<% } %>
		<% if (dimension.TIME.representation != null) { 
		%>
			<li>
				[@apph.messageEscape 'entity.indicator-instance.time-values'/]:
		<%	for (var i = 0; i < dimension.TIME.representation.length; i++) {
				var representation = dimension.TIME.representation[i];
		%>
				<%= getIndicatorInstanceSubelementDimension(representation) %><% if (i != dimension.TIME.representation.length - 1) { %>,<% } %>
		<%	} 
		%>
			</li>
		<% } %>	
			<li><a href="<%= selfLink %>" target="_blank">[@apph.messageEscape 'entity.indicator-instance.self-link'/]</a></li>	
		</ul>
	</div>
</script>

<script type="text/html" id="indicatorsSystemNotFoundTemplate">
	<div>[@apph.messageEscape 'page.indicators-system.error.not-found'/]</div>
</script>

<script>

	var IndicatorsSystemModel = Backbone.Model.extend({
		
		"url": function() {
		    return apiContext + '/indicatorsSystems/' + this.get("code");
		},
				
		initialize : function(){
			this.fetch();
		}
	});
	
	var IndicatorsSystemView = Backbone.View.extend({
		template : _.template($('#indicatorsSystemTemplate').html()),
		
		initialize : function(){
			this.model.bind("change", this.render, this);
		},
		
		render : function(){
			// render is called by fetch method of IndicatorsSystemModel
			var json = this.model.toJSON();
			if (!this.model.description) {
				json.description = null;
			}
            console.log(json);
			$(this.el).html(this.template(json));
			return this;
		}
	});
	
	var IndicatorInstanceDetail = Backbone.View.extend({
		template : _.template($('#indicatorInstanceTemplate').html()),
	
		render : function(){
			$(this.el).html(this.template(this.model));
			return this;
		}	
	});
	
	
	var ElementView = Backbone.View.extend({
		template : _.template($('#elementTemplate').html()),
		
		showIndicatorInstanceDetail : function(){
			var self = this;
			$.get(this.model.get('selfLink'))
		       .success(function(indicatorInstance){		       		
		       		var $detail = $('.indicatorInstanceDetail', self.el);
		       		var view = new IndicatorInstanceDetail({el : $detail, model : indicatorInstance});
		       		view.render();
		       })
		       .error(function(){
		    	   console.log('Error!');
		       });
		},
		
		render : function(){
					
			// Numeration
			var numerationBase = '';
			if (this.options.numerationBase != '') {
				numerationBase = this.options.numerationBase + '.';
			}
			numerationBase += _.string.lpad(this.options.numeration, 2, '0');
			
			// Element
			var json = this.model.toJSON();
			json.level = this.options.level;
			json.indicatorsSystemCode = this.options.indicatorsSystemCode;
			json.numeration = numerationBase;
			
			var html = this.template(json);			
			this.$el.html(html);

			var self = this;
			$('img', this.$el).bind('click', function() {
				self.showIndicatorInstanceDetail();
			});

			// Subelements
			var kind = this.model.get('kind');
			if (kind == 'indicators#dimension') {
				var subelementsNumeration = 1;
				var subelements = this.model.get('elements');
				if (subelements != '') {
					var subelementsCollection = new ElementsCollection(subelements);
					var htmlUbication = $('.dimensionSubElement', this.$el);				
					var subelementsCollectionView = new ElementsCollectionView({el : htmlUbication,
																				collection : subelementsCollection,
																				level : this.options.level + 1,
																				numerationBase : numerationBase,
																				numeration : subelementsNumeration,
																				indicatorsSystemCode : this.options.indicatorsSystemCode});
																								
					subelementsCollectionView.render();					
					
					subelementsNumeration += 1;
				}
			}			
			return html;
		}
	});
		
	var ElementsCollection = Backbone.Collection.extend({
	});
	
	var ElementsCollectionView = Backbone.View.extend({	
		
		initialize : function(){
			this.collection.bind("reset", this.render, this);
		},
		
		render : function(){
			var self = this;
			var numeration = self.options.numeration;
			this.collection.forEach(function(model){
				var elementView = new ElementView({ model : model,
													level : self.options.level,
													numerationBase : self.options.numerationBase,
													numeration : numeration,
													indicatorsSystemCode : self.options.indicatorsSystemCode});
				elementView.render();
				
				self.$el.append(elementView.el);
				
				numeration += 1;
			});
			return this;
		}
	});
	
	var IndicatorsSystemNotFoundView = Backbone.View.extend({
		template : _.template($('#indicatorsSystemNotFoundTemplate').html()),
		
		render : function(){
			$(this.el).html(this.template(this.model));
			return this;
		}
	});
	
	var ElementsModel = Backbone.Model.extend({
		
		"url": function() {		
		    return apiContext + '/indicatorsSystems/' + this.get("code");
		},
				
		initialize : function(){
			// the following line forces 'this' to refer to the Package instance in the function `fetch_success`
			_.bindAll(this, 'fetchSuccess');
			this.bind('change', this.fetchSuccess);
			this.fetch();
			
		},
		
		fetchSuccess : function() {				
			var elementsCollection = new ElementsCollection(this.get('elements'));
			var elementsCollectionView = new ElementsCollectionView({el : '#elements-view', collection : elementsCollection, level : 1, numerationBase : '', numeration : 1, indicatorsSystemCode : this.get('code')});
			elementsCollectionView.render();
		}
	});
	
	
	$(function(){
		var indicatorsSystemCode = '${indicatorsSystemCode}';
		var indicatorsSystemModel = new IndicatorsSystemModel({code : indicatorsSystemCode});
		new IndicatorsSystemView({el: '#indicators-system-view', model: indicatorsSystemModel});
		
		var elementsCollection = new ElementsCollection();
		indicatorsSystemModel.on("change", function(){
			elementsCollection.reset(indicatorsSystemModel.get('elements'));
		});
		
		var elementsCollectionView = new ElementsCollectionView({el : '#elements-view', collection : elementsCollection, level : 1, numerationBase : '', numeration : 1, indicatorsSystemCode : indicatorsSystemCode});
	});
	
</script>
[/@template.base]