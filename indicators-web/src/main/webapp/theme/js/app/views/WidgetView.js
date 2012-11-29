(function () {
    "use strict";

    App.views.WidgetView = Backbone.View.extend({

        initialize : function (options) {
            this.widgetOptions = new App.models.WidgetOptions(options);

            // Handle widget type error
            this.widgetOptions.on('error', function (model, error) {
                alert(error);
            });

            this.widgetPreviewView = new App.views.WidgetPreviewView({el : $('#widget-preview-content'), model : this.widgetOptions});

            this.views = {};

            var type = this.widgetOptions.get('type');
            if (type === "lastData") {
                this.views.data = new App.views.WidgetDataOptionsLastDataView({model : this.widgetOptions});
            } else if (type === "recent") {
                this.views.data = new App.views.WidgetDataOptionsRecentView({model : this.widgetOptions});
            } else if (type === "temporal") {
                this.views.data = new App.views.WidgetDataOptionsTemporalView({model : this.widgetOptions});
            }

            this.views.style = new App.views.WidgetStyleOptionsView({model : this.widgetOptions});
            this.views["export"] = new App.views.WidgetCodeView({model : this.widgetOptions});
            this.tabView = new App.views.TabView({el : '#widget-options-tabs', views : this.views});

            var self = this;
            this.views.data.on('updatePreviewData', function () {
                self.widgetPreviewView.updateData();
            });
        },

        render : function () {
            this.tabView.render();
        }

    });
}());
