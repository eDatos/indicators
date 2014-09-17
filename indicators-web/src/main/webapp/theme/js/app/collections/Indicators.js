(function () {
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
                q : 'subjectCode EQ "' + subjectCode + '"',
                limit : 9999
            };
            return this.fetch({data : data});
        },

        fetchBySubjectCodeAndGeographicalValueCode : function (subjectCode, geographicalValue) {
            var data = {
                q : 'subjectCode EQ "' + subjectCode + '" AND geographicalValue EQ "' + geographicalValue + '"',
                limit : 9999
            };
            return this.fetch({data : data});
        }

    });

    _.extend(App.collections.Indicators.prototype, App.mixins.JsonpSync);

}());