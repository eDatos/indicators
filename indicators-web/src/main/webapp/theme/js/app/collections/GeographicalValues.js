(function (_) {
    "use strict";

    App.collections.GeographicalValues = Backbone.Collection.extend({

        url : function () {
            return apiUrl + "/geographicalValues";
        },

        model : App.models.GeographicalValue,

        parse : function (response) {
            return window.firstTerritory ? _.sortBy(response.items, function (item) {
                return item.code === firstTerritory ? 0 : 1;
            }) : response.items;
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
        },

        fetchAllAndGeographicalGranularityCode : function (granularityCode) {
            return this.fetch({
                data : {
                    geographicalGranularityCode : granularityCode
                }
            });
        }

    });

    _.extend(App.collections.GeographicalValues.prototype, App.mixins.JsonSync);

}(window._));
