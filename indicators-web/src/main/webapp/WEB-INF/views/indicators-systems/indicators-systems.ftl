[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<script src="[@spring.url "/theme/js/libs/underscore-min.js" /]"></script>
<script src="[@spring.url "/theme/js/libs/backbone-min.js" /]"></script>
<script src="[@spring.url "/theme/js/libs/i18n.js" /]"></script>

<div id="indicatorsSystems-search">
	<input type="text" class="search">
</div>

<div class="clearfix" ></div>
<div class="sep-20">&nbsp;</div>

<div id="indicatorsSystems"></div>

<script type="text/html" id="indicatorsSystemTmpl">
	<div class="indicatorsSystems-item">
		<p><%= code %></p>
	</div>
</script>

<script type="text/html" id="noResultsTmpl">
	<div class="alert alert-block">No hay resultado para la búsqueda "<strong><%= query %></strong>"</div>
</script>

<script>

	function containsLowerCase(a, b){
		return a.toLowerCase().indexOf(b.toLowerCase()) != -1;     
	};

	var IndicatorsSystemsCollection = Backbone.Collection.extend({
		
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
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		}
	});
	
	var IndicatorsSystemsView = Backbone.View.extend({

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
					var indicatorsSystemView = new IndicatorsSystemView({ model : model});
					var subViewHtml = indicatorsSystemView.render().el;
					
					$(self.el).append(subViewHtml); 
				});
			} else {
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
		var indicatorsSystemsCollection = new IndicatorsSystemsCollection(${indicatorsSystems});
		var indicatorsSystemsView = new IndicatorsSystemsView({el : $("#indicatorsSystems"), collection : indicatorsSystemsCollection});
		indicatorsSystemsView.render();
		new SearchView({el : $("#indicatorsSystems-search"), collection : indicatorsSystemsCollection});
	});
	
</script>

[/@template.base]