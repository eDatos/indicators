(function () {
    "use strict";

    App.models.TimeGranularity = Backbone.Model.extend({
        idAttribute : "code",

        parse : function (response) {
            response.title = response.title.__default__;
            return response;
        }

    });

}());
