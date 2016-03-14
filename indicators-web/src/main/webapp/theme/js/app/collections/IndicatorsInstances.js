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
            return this.fetchWithoutLimit({
                data : {
                    q : 'geographicalValue EQ "' + geographicalValueCode + '"'
                }
            });
        },

        fetchBySystemCode : function (systemCode) {
            this.systemId = systemCode;
            return this.fetchWithoutLimit();
        }

    });

    _.extend(App.collections.IndicatorsInstances.prototype, App.mixins.JsonpSync);

}());
