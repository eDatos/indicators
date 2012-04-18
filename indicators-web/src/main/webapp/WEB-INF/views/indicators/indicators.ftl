[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

[#include "/layout/language-selector.ftl"]

<div id="indicators"></div>
<script type="text/html" id="indicatorsTmpl">
	<div>
		<h3><%= getLabel(subject.title) %></h3>
		<% for (i in indicators) {
			var indicator = indicators[i]; 
		%>
			<p><%= getLabel(indicator.title) %></p>			
		<% } %>
	</div>
</script>

<script type="text/html" id="noResultsTmpl">
	<div>No hay resultado para la búsqueda "<strong><%= query %></strong>"</div>
</script>

<script>
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
			return this.template(this.model.toJSON());
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
				var viewHtml = '';
				filtered.forEach(function(model){
					var indicatorsView = new IndicatorView({ model : model});
					var subViewHtml = indicatorsView.render();
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
		var indicatorsCollection = new IndicatorsCollection(${indicatorsBySubject});
		var indicatorsView = new IndicatorsView({el : $("#indicators"), collection : indicatorsCollection});
		indicatorsView.render();
		new SearchView({el : $("#indicators-search"), collection : indicatorsCollection});
	});
	
</script>

[/@template.base]