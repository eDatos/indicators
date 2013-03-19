[#ftl]
[#include "/inc/includes.ftl"]
[@template.base migas='Indicadores']

<div id="indicators"></div>

<script type="text/html" id="indicatorTmpl">
	<div>
		<p><%= getLabel(title) %></p>		
	</div>
</script>

<script type="text/html" id="noResultsTmpl">
	<div>No hay resultado para la búsqueda "<strong><%= query %></strong>"</div>
</script>

<script>
	var IndicatorsCollection = Backbone.Collection.extend({
		
		url: apiContext + '/indicators/?limit=1000',

		initialize : function(){
			this.fetch();
			_.bindAll(this);
		},
		
		parse : function(response) {
			return response.items;
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
    	},
    	
    	comparator : function(item) {
			return item.get("subjectCode");
		}
	});
	
	var IndicatorView = Backbone.View.extend({
		template : _.template($('#indicatorTmpl').html()),
		
		render : function(){
			return this.template(this.model.toJSON());
		}
	});
	
	var IndicatorsView = Backbone.View.extend({

		noResultsHtml : _.template($('#noResultsTmpl').html()),
		
		initialize : function(){
			this.collection.bind("filterChange", this.render, this);
			this.collection.bind("reset", this.render, this);
		},
		
		render : function(){
			var filtered = this.collection.filtered();
			if (filtered.length > 0){
				var self = this;
				var viewHtml = '';
				var subjectCodeLastIndicator = '';
				filtered.forEach(function(model){
					var indicatorsView = new IndicatorView({ model : model});
					var subViewHtml = indicatorsView.render();
					
					var subjectCodeIndicator = model.get("subjectCode");
					if (subjectCodeLastIndicator != subjectCodeIndicator) {
						viewHtml += '<h3>' + getLabel(model.get("subjectTitle")) + '</h3>';
						subjectCodeLastIndicator = subjectCodeIndicator;
					}
					viewHtml += subViewHtml; 
				});
				$(self.el).html(viewHtml);
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
		var indicatorsCollection = new IndicatorsCollection();		
		var indicatorsView = new IndicatorsView({el : $("#indicators"), collection : indicatorsCollection});
		indicatorsView.render();
		new SearchView({el : $("#indicators-search"), collection : indicatorsCollection});
	});
	
</script>

[/@template.base]