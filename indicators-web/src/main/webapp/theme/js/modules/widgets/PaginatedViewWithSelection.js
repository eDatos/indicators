/**
 *
 */

var PaginatedViewWithSelection = PaginatedView.extend({
    selectedItems : new Backbone.Collection(),

    initialize : function(){
        console.log('initialize');

        PaginatedView.prototype.initialize.call(this); // initialize super

        this.$elSelected = $("<div></div>");
        this.$el.append(this.$elSelected);

        this.selectedItems.on("add", this.renderSelectedItems, this);
        this.selectedItems.on("remove", this.renderSelectedItems, this);
    },

    renderContentItem : function(item){
        var view = new PaginatedViewWithSelection({template : this.contentItemTemplate, model : item});
        view.on('selectItem', this.toggleItem, this);
        return view.render().el;
    },

    renderSelectedItems : function(){
        this.$elSelected.text(JSON.stringify(this.selectedItems.toJSON()));
    },

    toggleItem : function(selectedItem){
        // TODO cada vez que se carga la página el cid del recurso cambia buscar otro método para comparar, id?
        var itemInCollection = this.selectedItems.getByCid(selectedItem.cid);
        if(itemInCollection === undefined){
            this.selectedItems.add(selectedItem);
        }else{
            this.selectedItems.remove(itemInCollection);
        }
        console.log('selectedItems', this.selectedItems.models);
    }
});

var PaginatedViewWithSelection = Backbone.View.extend({
    events : {
        "click .item" : "selectItem"
    },

    render : function(){
        var html = this.template(this.model.toJSON());
        this.$el.html(html);
        return this;
    },

    selectItem : function(e){
        e.preventDefault();
        this.trigger("selectItem", this.model);
    }
});