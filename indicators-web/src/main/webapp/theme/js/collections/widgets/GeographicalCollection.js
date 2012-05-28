/**
 *
 */

var GeographicalModel = Backbone.Model.extend({
    idAttribute: "code",

    getLabel : function(){
        var res;
        if(this.has('title')){
            res = getLabel(this.get('title'));
        }else{
            res = this.get('code');
        }

        return res;
    }
});

var GeographicalCollection = Backbone.Collection.extend({

    model : GeographicalModel,

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
    },

    comparator : function(geographicalValue){
        return _.string.slugify(geographicalValue.getLabel());
    }

});
