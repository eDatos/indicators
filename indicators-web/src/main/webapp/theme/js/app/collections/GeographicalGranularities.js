(function () {
    "use strict";

    App.collections.GeographicalGranularities = Backbone.Collection.extend({

        model : App.models.GeographicalGranularity,

        url : function () {
            var result = apiContext + "/geographicGranularities/";
            return result;
        }

    });

    _.extend(App.collections.GeographicalGranularities.prototype, App.mixins.JsonpSync);

}());