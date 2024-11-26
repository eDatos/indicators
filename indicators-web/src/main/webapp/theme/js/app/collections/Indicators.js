(function (_) {
    "use strict";

    App.collections.Indicators = App.collections.IndicatorsBase.extend({

        model : App.models.Indicator,

        url : function () {
            return apiUrl + "/indicators/";
        },

        parse : function (response) {
            return response.items;
        },

        fetchBySubjectCode : function (subjectCode) {
            var data = {
                q : 'subjectCode EQ "' + subjectCode + '"'
            };
            return this.fetchWithoutLimit({data : data});
        },

        fetchBySubjectCodeAndGeographicalValueCode : function (subjectCode, geographicalValue, indicatorsMain) {
            var data = {
                q: 'subjectCode EQ "' + subjectCode + '" AND geographicalValue EQ "' + geographicalValue + '"' + (indicatorsMain === "onlyMain" ? 'AND isMainIndicator EQ "TRUE"' : '')
            };
            return this.fetchWithoutLimit({data : data});
        },

        fetchAllByGeographicalValueCode : function (geographicalValue, indicatorsMain) {
            var data = {
                q : 'geographicalValue EQ "' + geographicalValue + '"' + (indicatorsMain === "onlyMain" ? 'AND isMainIndicator EQ "TRUE"' : '')
            };
            return this.fetchWithoutLimit({data : data});
        }

    });

    _.extend(App.collections.Indicators.prototype, App.mixins.JsonpSync);

}(window._));
