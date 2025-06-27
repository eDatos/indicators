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
            this.bindSliders();

            this.modelBinder.bind(this.model, this.$el);

            return this;
        },

        bindSliders: function () {
            this._bindWidthSlider();
            this._bindHeightSlider();
         },

        _bindWidthSlider: function () {
            var self = this;
            var $widthSlider = this.$(".width-slider");
            var widthSlideConfig = this._getWidthSlideConfig();
            $widthSlider.slider({
                min: widthSlideConfig.min,
                max: widthSlideConfig.max,
                value: this.model.get("widthQuantity"),
                slide: function (event, ui) {
                    self.model.set("widthQuantity", ui.value);
                }
            });
            self.model.on('change:widthQuantity', function (model, value) {
                $widthSlider.slider('value', value);
                self.model.set('width', value + self.model.get("widthUnit"));
            });
            self.model.on('change:widthUnit', function () {
                var widthSlideConfig = self._getWidthSlideConfig();
                $widthSlider.slider('option', 'min', widthSlideConfig.min);
                $widthSlider.slider('option', 'max', widthSlideConfig.max);

                if (self.model.get("widthQuantity") < widthSlideConfig.min || self.model.get("widthQuantity") > widthSlideConfig.max) {
                    self.model.set("widthQuantity", widthSlideConfig.default);
                }
                self.model.set('width', self.model.get("widthQuantity") + self.model.get("widthUnit"));
                $widthSlider.slider('value', self.model.get("widthQuantity"));
            });
        },

        _bindHeightSlider: function () {
            var self = this;
            var $heightSlider = this.$(".height-slider");
            var heightSlideConfig = this._getHeightSlideConfig();
            $heightSlider.slider({
                min: heightSlideConfig.min,
                max: heightSlideConfig.max,
                value: this.model.get("heightQuantity"),
                slide: function (event, ui) {
                    self.model.set("heightQuantity", ui.value);
                },
                disabled: true
            });
            self.model.on('change:heightQuantity', function (model, value) {
                $heightSlider.slider('value', value);
                self.model.set('height', value + self.model.get("heightUnit"));
            });
            self.model.on('change:heightUnit', function () {
                if (self.model.get("heightUnit") === 'auto') {
                    self.model.set("heightQuantity", '');

                    $heightSlider.slider({disabled: true});
                    self.$("#widget-height").attr('disabled', 'disabled');
                } else {
                    var heightSlideConfig = self._getHeightSlideConfig();
                    $heightSlider.slider('option', 'min', heightSlideConfig.min);
                    $heightSlider.slider('option', 'max', heightSlideConfig.max);

                    if (self.model.get("heightQuantity") < heightSlideConfig.min || self.model.get("heightQuantity") > heightSlideConfig.max) {
                        self.model.set("heightQuantity", heightSlideConfig.default);
                    }

                    $heightSlider.slider({disabled: false});
                    self.$("#widget-height").removeAttr('disabled');
                }

                if (self.model.get("heightUnit") === '%') {
                    $("#widget-preview-container").css('height', '750px');
                } else {
                    $("#widget-preview-container").css('height', 'auto');
                }

                self.model.set('height', self.model.get("heightQuantity") + self.model.get("heightUnit"));
                $heightSlider.slider('value', self.model.get("heightQuantity"));
            });
        },

        _getWidthSlideConfig: function () {
            if (this.model.get("widthUnit") === '%') {
                return {
                    min: App.constants.WidgetConstants.MIN_WIDTH_SLIDER_PERCENTAGE_VALUE,
                    max: App.constants.WidgetConstants.MAX_WIDTH_SLIDER_PERCENTAGE_VALUE,
                    default: App.constants.WidgetConstants.DEFAULT_PERCENTAGE_WIDTH
                };
            } else {
                return {
                    min: App.constants.WidgetConstants.MIN_WIDTH_SLIDER_PIXEL_VALUE,
                    max: App.constants.WidgetConstants.MAX_WIDTH_SLIDER_PIXEL_VALUE,
                    default: App.constants.WidgetConstants.DEFAULT_PIXEL_WIDTH
                };
            }
        },

        _getHeightSlideConfig: function () {
            if (this.model.get("heightUnit") === 'auto') {
                return {
                    min: '',
                    max: '',
                    default: ''
                };
            } else if (this.model.get("heightUnit") === '%') {
                return {
                    min: App.constants.WidgetConstants.MIN_HEIGHT_SLIDER_PERCENTAGE_VALUE,
                    max: App.constants.WidgetConstants.MAX_HEIGHT_SLIDER_PERCENTAGE_VALUE,
                    default: App.constants.WidgetConstants.DEFAULT_PERCENTAGE_HEIGHT
                };
            } else {
                return {
                    min: App.constants.WidgetConstants.MIN_HEIGHT_SLIDER_PIXEL_VALUE,
                    max: App.constants.WidgetConstants.MAX_HEIGHT_SLIDER_PIXEL_VALUE,
                    default: App.constants.WidgetConstants.DEFAULT_PIXEL_HEIGHT
                };
            }
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
