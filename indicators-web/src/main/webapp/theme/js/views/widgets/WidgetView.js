/**
 *
 */

var WidgetView = Backbone.View.extend({
    render : function(){
        var model = new WidgetOptionsModel();
        var optionsView = new WidgetOptionsView({el : $('#widget-options'), model : model});
        var widgetView = new WidgetPreviewView({el : $('#widget-preview-content'), model : model});
        var codeView = new WidgetCodeView({el : $('#code-container'), model : model});
    }
});
