(function (_) {
    "use strict";

    App.collections.GeographicalGranularities = Backbone.Collection.extend({

        model : App.models.GeographicalGranularity,

        url : function () {
            return apiUrl + "/geographicGranularities/";
        },
        
        parse : function (response) {
            return response.items;
        },

        fetchByIndicatorSystemCode : function (systemCode) {
            return this.fetch({ data : {systemCode : systemCode } });
        },

        fetchBySubjectCode : function (subjectCode) {
            return this.fetch({ data : {subjectCode : subjectCode } });
        },

        fetchAll : function () {
            return this.fetch({ data: { excludeUnusedGranularities: true } });
        }

    });

    _.extend(App.collections.GeographicalGranularities.prototype, App.mixins.JsonpSync);

}(window._));