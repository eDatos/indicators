[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<script src="[@spring.url "/theme/js/libs/underscore-min-1.3.1.js" /]"></script>
<script src="[@spring.url "/theme/js/libs/backbone-min-0.9.2.js" /]"></script>
<script src="[@spring.url "/theme/js/libs/i18n.js" /]"></script>

<div id="indicators-search" style="display:none;">
	<input type="text">
</div>

<div id="indicators"></div>
<script type="text/html" id="indicatorsTmpl">
	<div>
		<h3><%= getLabel(subject.title, 'es') %></h3>
		<% for (i in indicators) {
			var indicator = indicators[i]; 
		%>
			<p><%= getLabel(indicator.title, 'es') %></p>			
		<% } %>
	</div>
</script>

<script type="text/html" id="noResultsTmpl">
	<div>No hay resultado para la búsqueda "<strong><%= query %></strong>"</div>
</script>

<script>
	function getLabel(internationalString, locale){
		var localisedString = _.find(internationalString.texts, function(text) {
			return text.locale == locale;
		});
		if (localisedString) {
			return localisedString.label;
		} 
		return '';
	};

	function containsLowerCase(a, b){
		return a.toLowerCase().indexOf(b.toLowerCase()) != -1;     
	};

	var IndicatorsCollection = Backbone.Collection.extend({
		
		initialize : function(){
			_.bindAll(this);
		},
		
		search : function(query){
			this.query = query.trim();
			this.trigger("filterChange", "");
		},
		
		filtered : function() {			
      		if (this.query && this.query.length > 0) {
      			var query = this.query;
      			return this.filter(function(indicatorBySubject){ 
      				var code = indicatorBySubject.get('subject').get('code');
      				return containsLowerCase(code, query);      				 
      			});
      		}
      		return this;
    	}	
	});
	
	var IndicatorView = Backbone.View.extend({
		template : _.template($('#indicatorsTmpl').html()),
		
		render : function(){
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		}
	});
	
	var IndicatorsView = Backbone.View.extend({

		noResultsHtml : _.template($('#noResultsTmpl').html()),
		
		initialize : function(){
			this.collection.bind("filterChange", this.render, this);
		},
		
		render : function(){
			var filtered = this.collection.filtered();
			if (filtered.length > 0){
				var self = this;
				//TODO esto se puede optimizar para no hacer una única inserción en el DOM
				$(self.el).html("");
				filtered.forEach(function(model){
					var indicatorsView = new IndicatorView({ model : model});
					var subViewHtml = indicatorsView.render().el;
					
					$(self.el).append(subViewHtml); 
				});
			} else if (this.collection.query != null) {
				$(this.el).html(this.noResultsHtml({ query : this.collection.query }));
			}
		}
	});
	
	var SearchView = Backbone.View.extend({
		events : {
			'keyup .search': 'searchBoxKeydown'
		},
		
		searchBoxKeydown : function(e){
			var text = $(".search", this.el).val();		
			this.collection.search(text);
		}
	});
	
	$(function(){
		var indicatorsCollection = new IndicatorsCollection(${indicatorsBySubject});
		var indicatorsView = new IndicatorsView({el : $("#indicators"), collection : indicatorsCollection});
		indicatorsView.render();
		new SearchView({el : $("#indicators-search"), collection : indicatorsCollection});
	});
	
</script>

[/@template.base]