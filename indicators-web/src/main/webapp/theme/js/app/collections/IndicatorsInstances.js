(function () {
    "use strict";

    App.collections.IndicatorsInstances = App.collections.IndicatorsBase.extend({

        model : App.models.IndicatorInstance,

        url : function () {
            if (this.systemId) {
                return apiUrl + "/indicatorsSystems/" + this.systemId + "/indicatorsInstances";
            }
        },

        fetchBySystemCodeAndGeographicalValueCode : function (systemCode, geographicalValueCode) {
            this.systemId = systemCode;
            return this.fetch({
                data : {
                    q : 'geographicalValue EQ "' + geographicalValueCode + '"',
                    limit : 9999
                }
            });
        },

        fetchBySystemCode : function (systemCode) {
            this.systemId = systemCode;
            return this.fetch();
        }

    });

    _.extend(App.collections.IndicatorsInstances.prototype, App.mixins.JsonpSync);

}());
