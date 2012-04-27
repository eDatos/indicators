WidgetTypeOptionsView = Backbone.View.extend({
    initialize : function(){
        this.template = templateManager.get('typeOptionsTmpl');
        this.type = 'lastData';
    },

    events : {
        'click a' : 'setType'
    },

    render : function(){
        this.$el.html(this.template({type : this.type}));
        this.delegateEvents();
        return this;
    },

    setType : function(e){
        var $target = $(e.target);
        var $parent = $target.parent('a');
        this.type = $parent.data('type');
        this.model.set('type', this.type);
        this.render();
    }
});