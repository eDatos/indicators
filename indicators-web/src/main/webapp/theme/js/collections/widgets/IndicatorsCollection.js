/**
 *
 */
var IndicatorsCollection = Backbone.Collection.extend({

    model : IndicatorInstanceModel,

    url : function(){
        if (this.systemId == null)
            return null;

        return apiContext + "/indicatorsSystems/" + this.systemId + "/indicatorsInstances";
    },

    parse : function(response){
        return response.items;
    },

    setSystemId : function(systemId){
        this.systemId = systemId;
    },

    getGeographicalValues : function(){
        var all = [];
        this.each(function(indicator){
            var dimension = indicator.get('dimension');
            if(dimension !== undefined){
                var geographicalValues = dimension.GEOGRAPHICAL.representation;
                all = all.concat(geographicalValues);
            }
        });

        all = _.compact(all);
        var iterator = function(indicator){
            return indicator.code;
        };

        all = _.sortBy(all, iterator);
        return _.uniq(all, true, iterator);
    }

});
