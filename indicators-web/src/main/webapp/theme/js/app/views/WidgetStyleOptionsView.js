(function () {
    "use strict";

    App.views.WidgetStyleOptionsView = Backbone.View.extend({

        template: App.loadTemplate('style-options'),

        initialize: function () {
            this.modelBinder = new Backbone.ModelBinder();
            this.model.on('change:style', this.render, this);
        },

        _isLastDataOrRecent: function () {
            var type = this.model.get('type');
            return (type === "lastData") || (type === "recent");
        },

        _isTemporal: function () {
            var type = this.model.get('type');
            return type === "temporal";
        },

        render: function () {
            var context = {
                showSparkLines: this._isLastDataOrRecent(),
                showAxisAndLegend: this._isTemporal(),
                showSideView: this._isLastDataOrRecent(),
                showCustomStyle: this.model.get('style') === 'custom',
                showTextColor: !this._isTemporal(),
                showScale: this._isTemporal()
            };
            this.$el.html(this.template(context));

            this.$('.old-browser-warning').qtip({
                content: EDatos.common.I18n.translate('OPTIONS.OLD_BROWSER_WARNING'),
                show: 'mouseover',
                hide: 'mouseout'
            });

            this.$('.sparkline-max-help').qtip({
                content: EDatos.common.I18n.translate('OPTIONS.STYLE.SPARKLINES.MAX.DESCRIPTION'),
                show: 'mouseover',
                hide: 'mouseout'
            });

            this.$('.scale-natural-lib-help').qtip({
                content: EDatos.common.I18n.translate('OPTIONS.STYLE.SCALE.NATURAL_LIB.DESCRIPTION'),
                show: 'mouseover',
                hide: 'mouseout'
            });

            this.$('.scale-natural-help').qtip({
                content: EDatos.common.I18n.translate('OPTIONS.STYLE.SCALE.NATURAL.DESCRIPTION'),
                show: 'mouseover',
                hide: 'mouseout'
            });

            this.$('.scale-minmax-help').qtip({
                content: EDatos.common.I18n.translate('OPTIONS.STYLE.SCALE.MINMAX.DESCRIPTION'),
                show: 'mouseover',
                hide: 'mouseout'
            });

            this.bindColorPickers();
            this.bindSlider();

            this.modelBinder.bind(this.model, this.$el);

            return this;
        },

        bindSlider: function () {
            var self = this;
            var $slider = this.$(".width-slider");
            $slider.slider({
                min: 100,
                max: 700,
                value: self.model.get('width'),
                slide: function (event, ui) {
                    self.model.set('width', ui.value);
                }
            });
            self.model.on('change:width', function (model, value) {
                $slider.slider('value', value);
            });
        },

        bindColorPicker: function (input, property) {
            var self = this;
            var defaultVal = this.model.get(property);
            var $colorPicker = input.ColorPicker({
                color: defaultVal,
                onChange: function (hsb, hex, rgb) {
                    var value = '#' + hex;
                    self.model.set(property, value);
                }
            });

            self.model.on('change:' + property, function (model, value) {
                $colorPicker.ColorPickerSetColor(value);
            });
        },

        bindColorPickers: function () {
            this.bindColorPicker(this.$("[name='headerColor']"), 'headerColor');
            this.bindColorPicker(this.$("[name='titleColor']"), 'titleColor');
            this.bindColorPicker(this.$("[name='borderColor']"), 'borderColor');
            this.bindColorPicker(this.$("[name='textColor']"), 'textColor');
            this.bindColorPicker(this.$("[name='indicatorNameColor']"), 'indicatorNameColor');
        },

        events: {
            "keyup input": 'keyup'
        },

        keyup: function (e) {
            // Meake color pickr works with keyup events
            $(e.target).trigger('change');
        }

    });

}());