/**
 *
 */

var WidgetView = Backbone.View.extend({
    render : function(){
        var model = new WidgetOptionsModel();
        var widgetView = new WidgetPreviewView({el : $('#widget-preview-content'), model : model});

        var views = {};
        views.type = new WidgetTypeOptionsView({model : model});
        views.data = new WidgetDataOptionsView({model : model});
        views.style = new WidgetStyleOptionsView({model : model});
        views.export = new WidgetCodeView({model : model});

        var tabView = new TabView({el : $('#widget-options-tabs'), views : views});
        tabView.render();
    }
});
