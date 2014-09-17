(function () {
    "use strict";

    App.collections.Subjects = Backbone.Collection.extend({

        model : App.models.Subject,

        url : function () {
            return apiUrl + '/subjects/';
        }

    });

    _.extend(App.collections.Subjects.prototype, App.mixins.JsonpSync);

}());
