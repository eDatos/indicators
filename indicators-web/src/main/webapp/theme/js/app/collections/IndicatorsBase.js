(function () {
    "use strict";

    App.collections.IndicatorsBase = Backbone.Collection.extend({

        parse : function (response) {
            return response.items;
        },

        filterByIds : function (ids) {
            var filteredModels = this.filter(function (model) {
                return _.contains(ids, model.id);
            });
            return filteredModels;
        },

        fetchAll : function () {
            var requests = [];
            this.each(function (indicator) {
                if (!indicator.isFetched()) {
                    requests.push(indicator.fetch());
                }
            });
            return $.when.apply(this, requests);
        },

        getGeographicalValues : function () {
            var all = [];
            this.each(function (model) {
                var result = model.getGeographicalValues();
                all = all.concat(result);
            });
            all = _.compact(all);
            return all;
        }

    });

}());