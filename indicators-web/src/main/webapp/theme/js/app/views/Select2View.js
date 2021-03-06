(function () {
    "use strict";

    App.views.Select2View = Backbone.View.extend({

        initialize: function (options) {
            _.defaults(options, {
                sort: true
            });

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

        _getData: function () {
            var data = this.collection.toJSON();
            _.each(data, function (value) {
                value.id = value[this.options.idAttribute];
            }, this);

            if (this.options.sort) {
                var sortedData = _.sortBy(data, function (value) {
                    var trimmedLowerCase = _.string.trim(value[this.options.textAttribute].toLocaleLowerCase());
                    return _.string.slugify(trimmedLowerCase);
                }, this);
                return sortedData;
            } else {
                return data;
            }
        },

        _format: function (item) {
            var result = item[this.options.textAttribute];
            return result;
        },

        _defaultValues: function () {
            var values = {
                formatSelection: this._format,
                formatResult: this._format,
                multiple: this.options.multiple,
                width: this.options.width,
                formatNoMatches: function () {
                    return EDatos.common.I18n.translate("SELECT2.NO_MATCHES");
                },
                placeholder: ""
            };
            return values;
        },

        _updateSelectWithDefaults: function (values) {
            var extendedValues = _.extend({}, this._defaultValues(), values);
            this.$input.select2(extendedValues);
        },

        render: function () {
            this._updateSelectWithDefaults({
                data: {
                    results: this._getData(),
                    text: this.options.textAttribute,
                    id: "code"
                }
            });
            this.$input.select2("enable");
        },

        emptySelection: function () {
            this.$input.val('');
            this.$input.trigger("change");
        },

        loading: function () {
            this.emptySelection();
            this._updateSelectWithDefaults({
                data: [],
                placeholder: EDatos.common.I18n.translate("SELECT2.LOADING")
            });
            this.$input.select2("disable");
        },

        collectionReset: function () {
            //this.$input.select2("enable");
            this.emptySelection();
            this.render();
        }

    });

}());
