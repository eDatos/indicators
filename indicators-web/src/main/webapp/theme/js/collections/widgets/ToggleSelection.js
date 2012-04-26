ToggleSelection = function(options){
    var defaults = {
        max : 0
    };

    this.selection = [];
    _.extend(this, defaults, options);
};

_.extend(ToggleSelection.prototype, Backbone.Events, {
    select : function(element){
        var res;
        var index = this._indexOf(element);
        if(index !== -1){
            this.selection.splice(index, 1);
            res = false;
        }else{
            if(this.max > 0 && this.selection.length === this.max){
                this.selection.shift();
            }
            this.selection.push(element);
            res = true;
        }
        this.trigger('change');
        return res;
    },

    _indexOf : function(element){
        if(_.isUndefined(this.discriminator)){
            return _.indexOf(this.selection, element);
        }else{
            var index = -1;
            for(var i = 0; i < this.selection.length; i++){

                var actual = this.selection[i];

                if(this._comparator(actual, element)){
                    index = i;
                    break;
                }
            }
            return index;
        }
    },

    isSelected : function(element){
        return (this._indexOf(element) !== -1);
    },

    setMax : function(max){
        this.max = max;
        if(this.selection.length > max){
            this.selection = this.selection.slice(0, max);
            this.trigger('change');
        }
    },

    _comparator : function(a, b){
        if(_.isUndefined(this.discriminator)){
            return a == b;
        }else{
            return a.get(this.discriminator) === b.get(this.discriminator)
        }
    },

    clear : function(){
        this.selection = [];
        this.trigger('change');
    }

    /*
    onlyIn : function(list){
        var result = [];
        for(var i = 0; i < this.selection.length; i++){
            var elementB = this.selection[i];

            for(var j = 0; j < list.length; j++){
                var elementA = list[j]
                if(this._comparator(elementA, elementB)){
                    result.push(elementB);
                }
            }
        }
    }*/

});
