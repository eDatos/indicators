(function () {
    "use strict";

    var MIN_SLIDER_PERCENTAGE_VALUE = 10;
    var MAX_SLIDER_PERCENTAGE_VALUE = 100;
    var MIN_SLIDER_PIXEL_VALUE = 100;
    var MAX_SLIDER_PIXEL_VALUE = 700;
    var DEFAULT_PERCENTAGE_WIDTH = 100;
    var DEFAULT_PIXEL_WIDTH = 423;

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
                min: self.model.get("widthUnit") === '%' ? MIN_SLIDER_PERCENTAGE_VALUE : MIN_SLIDER_PIXEL_VALUE,
                max: self.model.get("widthUnit") === '%' ? MAX_SLIDER_PERCENTAGE_VALUE : MAX_SLIDER_PIXEL_VALUE,
                value: this.model.get("widthQuantity"),
                slide: function (event, ui) {
                    self.model.set("widthQuantity", ui.value);
                }
            });
            self.model.on('change:widthQuantity', function (model, value) {
                $slider.slider('value', value);
                self.model.set('width', value + self.model.get("widthUnit"));
            });
            self.model.on('change:widthUnit', function () {
                var widthUnitIsPercentage = (self.model.get("widthUnit") === '%');

                $slider.slider('option', 'min', widthUnitIsPercentage ? MIN_SLIDER_PERCENTAGE_VALUE : MIN_SLIDER_PIXEL_VALUE);
                $slider.slider('option', 'max', widthUnitIsPercentage ? MAX_SLIDER_PERCENTAGE_VALUE : MAX_SLIDER_PIXEL_VALUE);

                self.model.set("widthQuantity", widthUnitIsPercentage ? DEFAULT_PERCENTAGE_WIDTH : DEFAULT_PIXEL_WIDTH)
                self.model.set('width', self.model.get("widthQuantity") + self.model.get("widthUnit"));
                $slider.slider('value', self.model.get("widthQuantity"));
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
