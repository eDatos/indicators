/**
 *
 */

var PaginatedItemViewWithSelection = Backbone.View.extend({
    initialize : function(options){
        options || (options = {});
        this.template = options.template;
        this.selected = options.selected || false;
    },

    events : {
        "click .item" : "selectItem"
    },

    render : function(){
        var item = this.model.toJSON();

        var selected = this.selected;
        var renderData = {
            item : item,
            selected : selected,
            selectedClass : selected ? 'selected' : ''
        };

        var html = this.template(renderData);
        this.$el.html(html);
        return this;
    },

    selectItem : function(e){
        e.preventDefault();
        this.trigger("selectItem", this.model);
    }
});
