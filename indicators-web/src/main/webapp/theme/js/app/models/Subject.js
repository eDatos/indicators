(function (_) {
    "use strict";

    App.models.Subject = Backbone.Model.extend({

        urlRoot : function () {
            return apiUrl + '/subjects';
        },

        parse : function (response) {
            var result = response;
            result.title = response.title[currentLocale] || response.title.__default__;
            return result;
        }

    });

    _.extend(App.models.Subject.prototype, App.mixins.JsonSync);

}(window._));
