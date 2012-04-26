/**
 *
 */

var PaginatedItemViewWithSelection = Backbone.View.extend({
    initialize : function(options){
        this.template = options.template;
    },

    events : {
        "click .item" : "selectItem"
    },

    render : function(){
        var json = this.model.toJSON();
        var html = this.template(this.model.toJSON());
        this.$el.html(html);
        return this;
    },

    selectItem : function(e){
        e.preventDefault();
        this.trigger("selectItem", this.model);
    }
});
