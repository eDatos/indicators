(function () {
    "use strict";

    App.views.WidgetLanguageOptionsView = Backbone.View.extend({

        template: App.loadTemplate('language-options'),

        initialize: function () {
            this.modelBinder = new Backbone.ModelBinder();
            this.model.on('change', this.render, this);
        },

        render: function () {
            var context = {
                languages: edatosInternationalizationlanguages || []
            };
            this.$el.html(this.template(context));

            this.modelBinder.bind(this.model, this.$el);

            return this;
        }

    });

}());