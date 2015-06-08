(function () {
    "use strict";

    App.collections.TimeGranularities = Backbone.Collection.extend({
        model : App.models.TimeGranularity,

        parse : function (response) {
            return response.items;
        }
    });
    
}());