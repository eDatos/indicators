[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<script src="[@spring.url "/theme/js/libs/underscore-min.js" /]"></script>
<script src="[@spring.url "/theme/js/libs/backbone-min.js" /]"></script>
<script src="[@spring.url "/theme/js/libs/i18n.js" /]"></script>

<div id="indicators-search">
	<input type="text" class="search">
</div>

<div class="clearfix" ></div>
<div class="sep-20">&nbsp;</div>

<div id="indicators"></div>

<script type="text/html" id="indicatorsTmpl">
	<div class="indicators-item">
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
      			return this.filter(function(indicator){ 
      				var code = indicator.get('code');
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
		var indicatorsCollection = new IndicatorsCollection(${indicators});
		var indicatorsView = new IndicatorsView({el : $("#indicators"), collection : indicatorsCollection});
		indicatorsView.render();
		new SearchView({el : $("#indicators-search"), collection : indicatorsCollection});
	});
	
</script>

[/@template.base]