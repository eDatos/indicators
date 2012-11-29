(function () {
    "use strict";

    App.views.Select2View = Backbone.View.extend({

        initialize : function (options) {
            // Prepare events
            var self = this;

            this.$input = $('<input type="hidden">');
            this.$el.html(this.$input);

            this.collection.on('reset', this.collectionReset, this);
            this.$input.on('change', function () {
                self.trigger('change', self.$('input').select2('data'));
            });

            this.collectionReset();
        },

        collectionReset : function () {
            var self = this;

            var data = this.collection.toJSON();
            _.each(data, function (value) {
                value.id = value[self.options.idAttribute];
            });
            this.$el.select2('destroy');

            var format = function (item) {
                return item[self.options.textAttribute];
            };

            this.$input.val('');
            this.$input.select2({
                data : {
                    results : data,
                    text : self.options.textAttribute,
                    id : "code"
                },
                formatSelection : format,
                formatResult : format,
                multiple : this.options.multiple,
                width : this.options.width,
                formatNoMatches : function () {
                    return "No hay resultados";
                }
            });
            this.$input.trigger('change');
        }

    });

}());
