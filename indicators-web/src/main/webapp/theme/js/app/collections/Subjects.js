(function () {
    "use strict";

    App.collections.Subjects = Backbone.Collection.extend({

        model : App.models.Subject,

        url : function () {
            return apiContext + '/subjects/';
        }

    });

    _.extend(App.collections.Subjects.prototype, App.mixins.JsonpSync);

}());
