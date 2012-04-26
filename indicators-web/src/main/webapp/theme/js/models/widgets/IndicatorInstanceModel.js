/**
 *
 */

var IndicatorInstanceModel = Backbone.Model.extend({

    idAttribute: "id",

    url : function(){
        return this.get('selfLink');
    },

    isFetched : function(){
        // Debería comprobar algún valor más?
        return this.has("dimension");
    }

});
