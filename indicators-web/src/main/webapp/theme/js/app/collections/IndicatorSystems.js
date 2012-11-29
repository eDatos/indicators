/**
 *
 */
(function () {
    "use strict";

    App.collections.IndicatorSystems = Backbone.Collection.extend({

        model : App.models.IndicatorSystem,

        url : function () {
            return apiContext + '/indicatorsSystems/';
        },

        parse : function (response) {
            return response.items;
        }

    });

    _.extend(App.collections.IndicatorSystems.prototype, App.mixins.JsonpSync);

}());


