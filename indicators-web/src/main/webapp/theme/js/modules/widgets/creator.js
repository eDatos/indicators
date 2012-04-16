/**
 *
 */
var WidgetOptionsModel = Backbone.Model.extend({
    defaults : {
        title : 'default title',
        width : 300,
        borderColor : '#3478B0',
        textColor : '#000000',
        indicators : [],
        visibleData : {absolute : true, interanual : true, interperiodic : true}
    }
});

var WidgetOptionsView = Backbone.View.extend({
    initialize : function (options) {
        var options = options || {};

        this.cacheInputSelectors();
        this.setInputsValFromModel();
        this.bindColorPickers();
        this.configureSlider();
    },

    configureSlider : function () {
        var self = this;

        self.inputs.widthSlider.slider({
            min : 100,
            max : 500,
            value : self.model.get('width'),
            slide : function (event, ui) {
                self.model.set('width', ui.value);
                self.inputs.width.val(ui.value);
            }
        });
    },

    bindColorPicker : function (input, property) {
        var self = this;
        var defaultVal = input.val();
        input.ColorPicker({
            color : defaultVal,
            onChange : function (hsb, hex, rgb) {
                var value = '#' + hex;
                input.val(value);
                self.model.set(property, value);
            }
        }).bind('keyup', function () {
                $(this).ColorPickerSetColor(this.value);
                self.model.set(property, value);
            });
    },

    bindColorPickers : function () {
        this.bindColorPicker(this.inputs.borderColor, 'borderColor');
        this.bindColorPicker(this.inputs.textColor, 'textColor');
    },

    cacheInputSelectors : function () {
        this.inputs = {};
        this.inputs.title = $('#widget-options-style-title');
        this.inputs.width = $('#widget-options-style-width');
        this.inputs.widthSlider = $('#widget-options-style-width-slider');
        this.inputs.borderColor = $('#widget-options-style-border-color');
        this.inputs.textColor = $('#widget-options-style-text-color');

        this.inputs.visibleDataAbsolute = $('#widget-options-data-visibleDataAbsolute');
        this.inputs.visibleDataInteranual = $('#widget-options-data-visibleDataInteranual');
        this.inputs.visibleDataInterperiodic = $('#widget-options-data-visibleDataInterperiodic');

        this.inputs.type = $('#widget-options-data-type');
    },

    setInputsValFromModel : function () {
        this.inputs.title.val(this.model.get('title'));
        this.inputs.width.val(this.model.get('width'));
        this.inputs.borderColor.val(this.model.get('borderColor'));
        this.inputs.textColor.val(this.model.get('textColor'));

        var visibleData = this.model.get('visibleData');
        this.inputs.visibleDataAbsolute.prop("checked", visibleData.absolute);
        this.inputs.visibleDataInteranual.prop("checked", visibleData.interanual);
        this.inputs.visibleDataInterperiodic.prop("checked", visibleData.interperiodic);
        //this.inputs.type.val(this.model.get('type'));
    },

    events : {
        'keyup #widget-options-style-title' : 'setTitle',
        'keyup #widget-options-style-width' : 'setWidth',
        'change #widget-options-data-visibleDataAbsolute' : 'setVisibleData',
        'change #widget-options-data-visibleDataInteranual' : 'setVisibleData',
        'change #widget-options-data-visibleDataInterperiodic' : 'setVisibleData',
        'change #widget-options-data-type' : 'setType'
    },

    setTitle : function () {
        var value = this.inputs.title.val();
        this.model.set('title', value);
    },


    setWidth : function () {
        var value = this.inputs.width.val();
        this.model.set('width', value);
        this.inputs.widthSlider.slider('value', value);
    },

    setVisibleData : function(){
        var visibleData = {};
        visibleData.absolute = this.inputs.visibleDataAbsolute.prop("checked");
        visibleData.interanual = this.inputs.visibleDataInteranual.prop("checked");
        visibleData.interperiodic = this.inputs.visibleDataInterperiodic.prop("checked");
        this.model.set('visibleData', visibleData);
    },

    setType : function(){
        var type = $("option:selected", this.inputs.type).val();
        this.model.set('type', type);
    }
});

var WidgetView = Backbone.View.extend({
    initialize : function () {
        this.initializeWiget();
        this.bindModelToWidget();
    },

    initializeWiget : function(){
        if(this.widget){
            $(this.widget).remove();
        }
        var widgetOptions = this.model.toJSON();
        widgetOptions.el = this.el;
        this.widget = new Istac.Widgets.Factory(widgetOptions);
    },

    bindModelToWidget : function(){
        //Bind to model changes
        this.model.on('change:title', function (model, title) {
            this.widget.setTitle(title);
        }, this);
        this.model.on('change:width', function (model, width) {
            this.widget.setWidth(width);
        }, this);
        this.model.on('change:borderColor', function (model, borderColor) {
            this.widget.setBorderColor(borderColor);
        }, this);
        this.model.on('change:textColor', function (model, textColor) {
            this.widget.setTextColor(textColor);
        }, this);
        this.model.on('change:indicators', function (model, indicators) {
            this.widget.setIndicators(indicators)
        }, this);
        this.model.on('change:visibleData', function (model, visibleData) {
            this.widget.setVisibleData(visibleData);
        }, this);

        this.model.on('change:type', function (model, type){
            this.initializeWiget();
        }, this);
    }
});

var WidgetCodeView = Backbone.View.extend({
    initialize : function () {
        this.model.on('change', this.render, this);
        this.render();
    },

    render : function () {
        var opts = this.model.toJSON();
        $(this.el).html(JSON.stringify(this.model));
    }
});
