(function () {
    "use strict";

    App.views.Select2View = Backbone.View.extend({

        initialize : function (options) {
            // Prepare events
            var self = this;

            this.$input = $('<input type="hidden">');
            this.$el.html(this.$input);

            this.collection.on('reset', this.collectionReset, this);
            this.collection.on('syncStart', function (model) {
                if (model === this.collection) {
                    this.loading();
                }
            }, this);

            this.$input.on('change', function () {
                self.trigger('change', self.$('input').select2('data'));
            });

            _.bindAll(this, "_format");
            this.render();
        },

        _getData : function () {
            var data = this.collection.toJSON();
            _.each(data, function (value) {
                value.id = value[this.options.idAttribute];
            }, this);


            var sortedData = _.sortBy(data, function (value) {
                return value[this.options.textAttribute];
            }, this);


            return sortedData;
        },

        _format : function (item) {
            var result = item[this.options.textAttribute];
            return result;
        },

        _defaultValues : function () {
            var values = {
                formatSelection : this._format,
                formatResult : this._format,
                multiple : this.options.multiple,
                width : this.options.width,
                formatNoMatches : function () {
                    return "No hay resultados";
                },
                placeholder : ""
            };
            return values;
        },

        _updateSelectWithDefaults : function (values) {
            var extendedValues = _.extend({}, this._defaultValues(), values);
            this.$input.select2(extendedValues);
        },

        render : function () {
            this._updateSelectWithDefaults({
                data : {
                    results : this._getData(),
                    text : this.options.textAttribute,
                    id : "code"
                }
            });
            this.$input.select2("enable");
        },

        emptySelection : function () {
            this.$input.val('');
            this.$input.trigger("change");
        },

        loading : function () {
            this.emptySelection();
            this._updateSelectWithDefaults({
                data : [],
                placeholder : "Cargando..."
            });
            this.$input.select2("disable");
        },

        collectionReset : function () {
            //this.$input.select2("enable");
            this.emptySelection();
            this.render();
        }

    });

}());
