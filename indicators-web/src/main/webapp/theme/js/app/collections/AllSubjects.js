(function (_) {
    "use strict";

    App.collections.AllSubjects = Backbone.Collection.extend({

        initialize: function (models, options) {
            this.options = options || {};
        },

        url : function () {
            return srmRestUrl + '/categoryschemes/' + srmAgency + '/' + srmResource + '/' + srmVersion + '/categories.json';
        },

        parse: function (response) {
            return response.category;
        },
    });

    _.extend(App.collections.AllSubjects.prototype, App.mixins.JsonpSync);

}(window._));
