(function () {
    "use strict";

    App.models.IndicatorSystem = Backbone.Model.extend({

        parse : function (response) {
            var result = response;
            result.title = response.title.__default__;

            return result;
        }

    });

}());