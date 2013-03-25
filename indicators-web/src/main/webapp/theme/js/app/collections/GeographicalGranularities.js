(function () {
    "use strict";

    App.collections.GeographicalGranularities = Backbone.Collection.extend({

        model : App.models.GeographicalGranularity,

        url : function () {
            return apiContext + "/geographicGranularities/";
        },

        fetchByIndicatorSystemCode : function (systemCode) {
            return this.fetch({ data : {systemCode : systemCode } });
        },

        fetchBySubjectCode : function (subjectCode) {
            return this.fetch({ data : {subjectCode : subjectCode } });
        }

    });

    _.extend(App.collections.GeographicalGranularities.prototype, App.mixins.JsonpSync);

}());