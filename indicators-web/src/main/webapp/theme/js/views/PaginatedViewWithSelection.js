/**
 *
 */


var PaginatedViewWithSelection = PaginatedView.extend({
    initialize : function(options){
        PaginatedView.prototype.initialize.call(this); // initialize super

        this._selection = new ToggleSelection({discriminator : 'id'});
        this._selection.on('change', this.renderSelectedItems, this);

        this._selection.on('change', function(){
            this.trigger("selectionChange", this._selection.selection);
        }, this);

        this.$elSelected = $("<div></div>");
        this.$el.append(this.$elSelected);
    },

    renderContentItem : function(item){
        var view = new PaginatedItemViewWithSelection({template : this.contentItemTemplate, model : item});
        view.on('selectItem', this.selectItem, this);
        return view.render().el;
    },

    renderSelectedItems : function(){
        var selected = this._selection.selection;
        var selectedIds = _.map(selected, function(item){
            return item.id
        });





        this.$elSelected.text(JSON.stringify(selectedIds));
    },

    selectItem : function(selectedItem){
        this._selection.select(selectedItem);
    },

    setMaxSelectionSize : function(max){
        this._selection.setMax(max);
    }
});



