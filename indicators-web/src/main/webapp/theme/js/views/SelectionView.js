SelectionView = Backbone.View.extend({

    initialize : function(options){
        this._selection = new ToggleSelection(options);

        this.nameProperty = 'name';
        this.idProperty = 'id';

        var self = this;
        this._selection.on('change', function(){
            this.render();
            this.trigger("selectionChange", self._selection.selection);
        }, this);

        //this.collection.on('change', this.collectionChange, this);
        this.collection.on('reset', this.collectionChange, this);
    },

    events : {
        'click .selectable-item' : 'select'
    },

    collectionChange : function(){
        //TODO clear the selection?
        this._selection.clear();
        this.render();
    },

    render : function(){
        var html = '';

        var self = this;
        this.collection.each(function(item){
            var selected = self._selection.isSelected(item);
            var selectedClass = selected ? 'selected' : '';

            var label = self.getLabel(item);
            html += '<div><a href="#" class="selectable-item ' + selectedClass + '" data-value="' + item.id +'">' + label + '</a></div>'
        });

        if(this._selection.max > 0){
            var s = this._selection.max > 1 ? 's' : '';
            var selectionableItemsHtml = '<div class="selectionable-items">Puede seleccionar como máximo ' + this._selection.max + ' elemento'+s+'</div>'
            html += selectionableItemsHtml;
        }

        this.$el.html(html);
        return this;
    },

    getLabel : function(item){
        return item.get(this.nameProperty);
    },

    select : function(e){
        e.preventDefault();
        var $target = $(e.target);
        var value = $target.data('value');
        var model = this.collection.get(value);

        var selected = this._selection.select(model);
        this.render();
    },

    selection : function(){
        return this._selection.selection;
    },

    setMaxSelectionSize : function(max){
        this._selection.setMax(max);
        this.render();
    }

});
