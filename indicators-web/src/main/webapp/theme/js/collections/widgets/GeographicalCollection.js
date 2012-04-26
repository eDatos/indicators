/**
 *
 */

var GeographicalCollection = Backbone.Collection.extend({

    model : Backbone.Model.extend({
        idAttribute: "code"
    }),

    initialize : function(options){
        options || (options = {});
        if(options.indicators){
            this.indicators = options.indicators;
            this.indicators.on('change', this.reloadData, this);
            this.indicators.on('reset', this.reloadData, this);
        }
    },

    reloadData : function(){
        var geographicalValues = this.indicators.getGeographicalValues();
        this.reset(geographicalValues);
    }
});
