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

        this.$elSelected = $("<div class='selectedItems'></div>");
        this.$el.append(this.$elSelected);
    },

    renderContentItem : function(item){
        var selected = this._selection.isSelected(item);
        var view = new PaginatedItemViewWithSelection({template : this.contentItemTemplate, model : item, selected : selected});

        view.on('selectItem', this.selectItem, this);
        return view.render().el;
    },

    renderSelectedItems : function(){
        var selecteds = this._selection.selection;

        var selectedLabel = '';
        if(selecteds.length > 0){
            selectedLabel = getLabel(selecteds[0].get('title'));
        }

        var html = "<strong>Seleccionado : </strong> " + selectedLabel;

        this.$elSelected.html(html);
    },

    selectItem : function(selectedItem){
        this._selection.select(selectedItem);
    },

    setMaxSelectionSize : function(max){
        this._selection.setMax(max);
    }
});



