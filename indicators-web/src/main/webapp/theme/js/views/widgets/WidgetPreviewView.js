/**
 *
 */

var WidgetPreviewView = Backbone.View.extend({
    initialize : function(){
        this.initializeWiget();
        this.bindModelToWidget();
    },

    initializeWiget : function(){
        if (this.widget) {
            $(this.widget).remove();
        }
        var widgetOptions = this.model.toJSON();
        widgetOptions.el = this.el;
        this.widget = new IstacWidget(widgetOptions);
    },

    bindModelToWidget : function(){
        //Bind to model changes
        this.model.on('change:title', function(model, title){
            this.widget.setTitle(title);
        }, this);
        this.model.on('change:width', function(model, width){
            this.widget.setWidth(width);
        }, this);
        this.model.on('change:borderColor', function(model, borderColor){
            this.widget.setBorderColor(borderColor);
        }, this);
        this.model.on('change:textColor', function(model, textColor){
            this.widget.setTextColor(textColor);
        }, this);
        this.model.on('change:indicators', function(model, indicators){
            this.widget.setIndicators(indicators);
        }, this);
        this.model.on('change:measures', function(model, measures){
            this.widget.setMeasures(measures);
        }, this);
        this.model.on('change:geographicalValues', function(model, geographicalValues){
            this.widget.setGeographicalValues(geographicalValues);
        }, this);

        this.model.on('change:systemId', function(model, systemId){
            this.widget.setSystemId(systemId);
        }, this);

        this.model.on('change:type', function(model, type){
            this.initializeWiget();
        }, this);


    }
});