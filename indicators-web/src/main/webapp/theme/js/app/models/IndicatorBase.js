(function () {
    "use strict";

    /*
     Base class for Indicator and IndicatorInstance
     */
    App.models.IndicatorBase = Backbone.Model.extend({
        parse : function (response) {
            var result = response;
            result.title = response.title.__default__;
            return result;
        },

        isFetched : function () {
            return this.has('dimension');
        },

        getGeographicalValues : function () {
            if (this.has('dimension')) {
                var dimension = this.get('dimension');
                var geographicalValues = dimension.GEOGRAPHICAL.representation;

                var granularitiesArray = dimension.GEOGRAPHICAL.granularity;
                var granularities = {};
                _.each(granularitiesArray, function (granularity) {
                    granularities[granularity.code] = granularity.title.__default__;
                });

                var result = _.map(geographicalValues, function (geographicalValue) {
                    var model = new App.models.GeographicalValue({
                        code : geographicalValue.code,
                        title : geographicalValue.title.__default__,
                        granularityCode : geographicalValue.granularityCode,
                        granularityLabel : granularities[geographicalValue.granularityCode]
                    });
                    return model;
                });

                return result;
            }
        },

        getTimeGranularities : function () {
            var dimension = this.get("dimension");
            return dimension.TIME.granularity;
        },
        
        removeProtocol: function (url) {
        	return url.replace("https:","").replace("http:","");
        }

    });

    _.extend(App.models.IndicatorBase.prototype, App.mixins.JsonpSync);

}());