/**
 *
 */
(function () {
    "use strict";

    App.collections.IndicatorSystems = Backbone.Collection.extend({

        model : App.models.IndicatorSystem,

        url : function () {
            return apiUrl + '/indicatorsSystems/';
        },

        parse : function (response) {
            return response.items;
        },
        
        fetchWithoutLimit : function(options) {
        	options = options || { 
        		data : {} 
        	};
        	_.defaults(options.data, { limit : 1000 });
        	return this.fetch(options);
        }

    });

    _.extend(App.collections.IndicatorSystems.prototype, App.mixins.JsonpSync);

}());


