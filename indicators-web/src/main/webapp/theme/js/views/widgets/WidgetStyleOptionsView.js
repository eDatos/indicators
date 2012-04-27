WidgetStyleOptionsView = Backbone.View.extend({

    initialize : function(options){
        var options = options || {};

        this.template = templateManager.get('styleOptionsTmpl');
    },

    render : function(){
        this.$el.html(this.template());

        this.cacheInputSelectors();
        this.setInputsValFromModel();
        this.bindColorPickers();
        this.configureSlider();

        this.delegateEvents();

        return this;
    },

    configureSlider : function(){
        var self = this;

        self.inputs.widthSlider.slider({
            min : 100,
            max : 700,
            value : self.model.get('width'),
            slide : function(event, ui){
                self.model.set('width', ui.value);
                self.inputs.width.val(ui.value);
            }
        });
    },

    bindColorPicker : function(input, property){
        var self = this;
        var defaultVal = input.val();
        input.ColorPicker({
            color : defaultVal,
            onChange : function(hsb, hex, rgb){
                var value = '#' + hex;
                input.val(value);
                self.model.set(property, value);
            }
        }).bind('keyup', function(){
                $(this).ColorPickerSetColor(this.value);
                self.model.set(property, value);
            });
    },

    bindColorPickers : function(){
        this.bindColorPicker(this.inputs.borderColor, 'borderColor');
        this.bindColorPicker(this.inputs.textColor, 'textColor');
    },

    cacheInputSelectors : function(){
        this.inputs = {};
        this.inputs.title = $("[name='title']", this.$el);
        this.inputs.width = $("[name='width']", this.$el);
        this.inputs.widthSlider = $(".width-slider", this.$el);
        this.inputs.borderColor = $("[name='border-color']", this.$el);
        this.inputs.textColor = $("[name='text-color']", this.$el);
    },

    setInputsValFromModel : function(){
        this.inputs.title.val(this.model.get('title'));
        this.inputs.width.val(this.model.get('width'));
        this.inputs.borderColor.val(this.model.get('borderColor'));
        this.inputs.textColor.val(this.model.get('textColor'));
    },

    events : {
        'keyup [name="title"]' : 'setTitle',
        'keyup [name="width"]' : 'setWidth'
    },

    setTitle : function(){
        var value = this.inputs.title.val();
        this.model.set('title', value);
    },

    setWidth : function(){
        var value = this.inputs.width.val();
        this.model.set('width', value);
        this.inputs.widthSlider.slider('value', value);
    }

});