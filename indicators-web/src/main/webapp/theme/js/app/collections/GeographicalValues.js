(function () {
    "use strict";

    App.collections.GeographicalValues = Backbone.Collection.extend({

        url : function () {
            return apiContext + "/geographicalValues";
        },

        model : App.models.GeographicalValue,

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