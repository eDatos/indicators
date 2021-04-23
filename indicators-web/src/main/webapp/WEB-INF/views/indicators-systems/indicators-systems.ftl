[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]


<div style="display:none">
	<div id="indicatorsSystems-search">
		<input id="indicatorsSystems-search-input" type="text">
	</div>
</div>

<div id="indicatorsSystems"></div>

<script type="text/html" id="indicatorsSystemTmpl">
	<div>
		<p><a href='<%= serverURL %>/indicatorsSystems/<%=code%>.html'><%= getLabel(title) %></a></p>
	</div>
</script>

<script type="text/html" id="noResultsTmpl">
	<div>No hay resultado para la búsqueda "<strong><%= query %></strong>"</div>
</script>

<script>
	var IndicatorsSystemsCollection = Backbone.Collection.extend({
		
		url: apiUrl + '/indicatorsSystems/?limit=1000',
				
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
      			return this.filter(function(indicatorsSystem){ 
      				var code = indicatorsSystem.get('code');
      				return containsLowerCase(code, query);      				 
      			});
      		}
      		return this;
    	}	
	});
	
	var IndicatorsSystemView = Backbone.View.extend({
		template : _.template($('#indicatorsSystemTmpl').html()),
		
		render : function(){
			return this.template(this.model.toJSON());
		}
	});
	
	var IndicatorsSystemsView = Backbone.View.extend({

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

                var sorted = filtered.sortBy(function (model) {
                    return getLabel(model.get('title'));
                });

                _.each(sorted, function(model){
					var indicatorsSystemView = new IndicatorsSystemView({ model : model});
					var subViewHtml = indicatorsSystemView.render();
					viewHtml += subViewHtml; 
				});

				$(self.el).html(viewHtml);
			} else if (this.collection.query != null) {
				$(this.el).html(this.noResultsHtml({ query : this.collection.query }));
			}
		}
	});

	$(function(){
		var indicatorsSystemsCollection = new IndicatorsSystemsCollection();
		var indicatorsSystemsView = new IndicatorsSystemsView({el : $("#indicatorsSystems"), collection : indicatorsSystemsCollection});
		indicatorsSystemsView.render();
	});
	
</script>

[/@template.base]