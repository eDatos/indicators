(function () {
    "use strict";

    App.views.WidgetPreviewView = Backbone.View.extend({
        initialize : function () {
            this.initializeWidget();
            this.bindModelToWidget();
        },

        initializeWidget : function () {
            if (this.widget) {
                $(this.widget).remove();
            }
            var widgetOptions = this.model.toJSON();
            widgetOptions.el = this.el;
            widgetOptions.url = apiBaseUrl;

            var self = this;
            this.widget = new IstacWidget(widgetOptions, function (widget) {
                self.widget = widget;
            });
        },

        bindModelToWidget : function () {
            //Bind to model changes
            var self = this;

            var bindings = {

                title : function (title) {
                    self.widget.set('title', title);
                },

                width : _.debounce(function (width) {
                    self.widget.set('width', width);
                    self.widget.render();
                }, 500),

                borderColor : function (borderColor) {
                    self.widget.set('borderColor', borderColor);
                    this.widget.render();
                },

                textColor : function (textColor) {
                    self.widget.set('textColor', textColor);
                },

                _default : function (key, value) {
                    this.widget.set(key, value);
                    this.widget.render();
                }
            };

            self.model.on('change', function (model) {
                _.each(model.changedAttributes(), function (value, key) {
                    var binding = bindings[key];
                    var defaultBinding = bindings._default;
                    if (_.isFunction(binding)) {
                        binding.call(self, value);
                    } else if (_.isFunction(defaultBinding)) {
                        defaultBinding.call(self, key, value);
                    }
                });
            });

        },

        updateData : function () {
            this.widget.reloadData();
        }

    });
}());

