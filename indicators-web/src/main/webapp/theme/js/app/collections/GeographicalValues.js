(function () {
    "use strict";

    App.collections.GeographicalValues = Backbone.Collection.extend({

        url : function () {
            return apiUrl + "/geographicalValues";
        },

        model : App.models.GeographicalValue,

        parse : function (response) {
            return response.items;
        },

        fetchBySubjectCodeAndGeographicalGranularityCode : function (subjectCode, granularityCode) {
            return this.fetch({
                data : {
                    subjectCode : subjectCode,
                    geographicalGranularityCode : granularityCode
                }
            });
        },

        fetchByIndicatorSystemCodeAndGeographicalGranularityCode : function (systemCode, granularityCode) {
            return this.fetch({
                data : {
                    systemCode : systemCode,
                    geographicalGranularityCode : granularityCode
                }
            });
        }

    });

    _.extend(App.collections.GeographicalValues.prototype, App.mixins.JsonpSync);

}());