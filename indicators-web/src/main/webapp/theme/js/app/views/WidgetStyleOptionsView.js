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
            $widthSlider.slider({
                min: self._getWidthSlideMin(),
                max: self._getWidthSlideMax(),
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
                var slideMin = self._getWidthSlideMin();
                var slideMax = self._getWidthSlideMax();
                $widthSlider.slider('option', 'min', slideMin);
                $widthSlider.slider('option', 'max', slideMax);

                if (self.model.get("widthQuantity") < slideMin || self.model.get("widthQuantity") > slideMax) {
                    self.model.set("widthQuantity", self._getWidthSlideDefaultValue());
                }
                self.model.set('width', self.model.get("widthQuantity") + self.model.get("widthUnit"));
                $widthSlider.slider('value', self.model.get("widthQuantity"));
            });
        },

        _bindHeightSlider: function () {
            var self = this;
            var $heightSlider = this.$(".height-slider");
            $heightSlider.slider({
                min: self._getHeightSlideMin(),
                max: self._getHeightSlideMax(),
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
                    var slideMin = self._getHeightSlideMin();
                    var slideMax = self._getHeightSlideMax();
                    $heightSlider.slider('option', 'min', slideMin);
                    $heightSlider.slider('option', 'max', slideMax);

                    if (self.model.get("heightQuantity") < slideMin || self.model.get("heightQuantity") > slideMax) {
                        self.model.set("heightQuantity", self._getHeightSlideDefaultValue());
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

        _getWidthSlideMin: function () {
            return this.model.get("widthUnit") === '%'
                ? App.constants.WidgetConstants.MIN_WIDTH_SLIDER_PERCENTAGE_VALUE
                : App.constants.WidgetConstants.MIN_WIDTH_SLIDER_PIXEL_VALUE;
        },

        _getWidthSlideMax: function () {
            return this.model.get("widthUnit") === '%'
                ? App.constants.WidgetConstants.MAX_WIDTH_SLIDER_PERCENTAGE_VALUE
                : App.constants.WidgetConstants.MAX_WIDTH_SLIDER_PIXEL_VALUE;
        },

        _getWidthSlideDefaultValue: function () {
            return this.model.get("widthUnit") === '%'
                ? App.constants.WidgetConstants.DEFAULT_PERCENTAGE_WIDTH
                : App.constants.WidgetConstants.DEFAULT_PIXEL_WIDTH;
        },

        _getHeightSlideMin: function () {
            if (this.model.get("heightUnit") === 'auto') {
                return '';
            } else if (this.model.get("heightUnit") === '%') {
                return App.constants.WidgetConstants.MIN_HEIGHT_SLIDER_PERCENTAGE_VALUE;
            } else {
                return App.constants.WidgetConstants.MIN_HEIGHT_SLIDER_PIXEL_VALUE;
            }
        },

        _getHeightSlideMax: function () {
            if (this.model.get("heightUnit") === 'auto') {
                return '';
            } else if (this.model.get("heightUnit") === '%') {
                return App.constants.WidgetConstants.MAX_HEIGHT_SLIDER_PERCENTAGE_VALUE;
            } else {
                return App.constants.WidgetConstants.MAX_HEIGHT_SLIDER_PIXEL_VALUE;
            }
        },

        _getHeightSlideDefaultValue: function () {
            if (this.model.get("heightUnit") === 'auto') {
                return '';
            } else if (this.model.get("heightUnit") === '%') {
                return App.constants.WidgetConstants.DEFAULT_PERCENTAGE_HEIGHT;
            } else {
                return App.constants.WidgetConstants.DEFAULT_PIXEL_HEIGHT;
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
