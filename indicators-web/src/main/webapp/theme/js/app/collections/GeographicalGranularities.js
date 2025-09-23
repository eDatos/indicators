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
            return this.fetch({ data : {systemCode : systemCode }, headers: { "api-key": apiKey } });
        },

        fetchBySubjectCode : function (subjectCode) {
            return this.fetch({ data : {subjectCode : subjectCode }, headers: { "api-key": apiKey } });
        },

        fetchAll : function () {
            return this.fetch({ data: { excludeUnusedGranularities: true }, headers: { "api-key": apiKey } });
        }

    });

    _.extend(App.collections.GeographicalGranularities.prototype, App.mixins.JsonpSync);

}(window._));