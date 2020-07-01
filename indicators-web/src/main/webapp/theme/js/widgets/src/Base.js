(function ($) {

    var Dataset = Istac.widget.Dataset;
    var Datasets = Istac.widget.Datasets;
    var DatasetRequestBuilder = Istac.widget.DatasetRequestBuilder;

    Istac.widget.Base = function (options) {
        this.init(options);
    };

    Istac.widget.Base.prototype = {

        _defaultOptions: {
            title: 'default title',
            width: 300,
            height: 400,
            headerColor: '#0F5B95',
            borderColor: '#EBEBEB',
            textColor: '#000000',
            indicatorNameColor: "#003366",
            indicatorSystem: "",
            subjectCode: "",
            indicators: [],
            instances: [],
            groupType: 'system', // or subject
            measures: [
                "ABSOLUTE",
                "ANNUAL_PERCENTAGE_RATE",
                "ANNUAL_PUNTUAL_RATE",
                "INTERPERIOD_PERCENTAGE_RATE",
                "INTERPERIOD_PUNTUAL_RATE"
            ],
            showLabels: true,
            showLegend: true,
            sideView: false,
            sparkline_ABSOLUTE: false,
            sparklineType_ABSOLUTE: 'line',
            sparklineMax_ABSOLUTE: 0,
            sparkline_ANNUAL_PERCENTAGE_RATE: false,
            sparklineType_ANNUAL_PERCENTAGE_RATE: 'line',
            sparklineMax_ANNUAL_PUNTUAL_RATE: 0,
            sparkline_ANNUAL_PUNTUAL_RATE: false,
            sparklineType_ANNUAL_PUNTUAL_RATE: 'line',
            sparklineMax_INTERPERIOD_PUNTUAL_RATE: 0,
            sparkline_INTERPERIOD_PERCENTAGE_RATE: false,
            sparklineType_INTERPERIOD_PERCENTAGE_RATE: 'line',
            sparklineMax_ANNUAL_PERCENTAGE_RATE: 0,
            sparkline_INTERPERIOD_PUNTUAL_RATE: false,
            sparklineType_INTERPERIOD_PUNTUAL_RATE: 'line',
            sparklineMax_INTERPERIOD_PERCENTAGE_RATE: 0,
            sparklineType: 'line',
            sparklineWidth: 30,
            sparklineHeight: 20,
            sparklineBarWidth: 1,
            sparklineBarSpacing: 1,
            sparklineLineColor: "#0058B0",
            sparklineFillColor: null,
            uwa: false,
            shadow: true,
            borderRadius: true,
            scale: "natural"
        },

        _containerTemplate: Handlebars.templates.container,

        _uwaContainerTemplate: Handlebars.templates.uwaContainer,

        init: function (options) {
            this.options = _.extend({}, this._defaultOptions, options);
            this.el = $(options.el);
            this.type = options.type;
            this.measures = options.measures || this._defaultOptions.measures;
            this.geographicalValues = options.geographicalValues;

            this.afterRenderCallback = !_.isUndefined(this.options.afterRenderCallback) ? _.debounce(this.options.afterRenderCallback, 300) : null;

            // urls
            this.url = options.url || "";
            this.apiUrl = options.apiUrl || "";
            this.visualizerUrl = options.visualizerUrl || "";

            // Request builder
            this.datasetRequestBuilder = new DatasetRequestBuilder({ apiUrl: this.apiUrl });

            // locale
            this.locale = options.locale || "es";

            this.datasets = [];

            //Create containers
            var templateOptions = { widgetsTypeUrl: Istac.widget.configuration['indicators.widgets.typelist.url'] };

            if (this.options.uwa) {
                this.el.html(this._uwaContainerTemplate(templateOptions));
                this.titleText = $('');
                this.titleContainer = $('');
            } else {
                this.el.html(this._containerTemplate(templateOptions));
                this.titleText = this.el.find('.istac-widget-title-text');
                this.titleContainer = this.el.find('.istac-widget-title');
            }

            this.bodyContainer = this.el.find('.istac-widget-body');
            this.contentContainer = this.el.find('.istac-widget-content');
            this.creditsContainer = this.el.find('.istac-widget-credits');
            this.embedContainer = this.el.find('.istac-widget-body-embed');
            this.allIndicatorsContainer = this.el.find('.istac-widget-body-allIndicators');

            this.el.addClass("istac-widget");
            this.el.addClass(this.containerClass);

            this.initializeLogo();
            this.initializeEmbed();

            // Initialize style

            var realWidth = this.options.uwa ? "100%" : options.width;

            this.set('textColor', options.textColor);
            this.set('borderColor', options.borderColor);
            this.set('headerColor', options.headerColor);
            this.set('indicatorNameColor', options.indicatorNameColor);
            this.set('title', options.title);
            this.set('widgetWith', options.width);
            this.set('width', realWidth);
            this.set('showLabels', options.showLabels);
            this.set('showLegend', options.showLegend);
            this.set('indicatorSystem', options.indicatorSystem);
            this.set('borderRadius', options.borderRadius);
            this.set('shadow', options.shadow);
            this.set('style', options.style);
            this.set('gobcanStyleColor', options.gobcanStyleColor);
            this.reloadData();
        },

        set: function (property, value) {
            this[property] = value;
            this.options[property] = value;

            var setter = this[this._getSetterMethodName(property)];
            if (_.isFunction(setter)) {
                setter.call(this, value);
            }
        },

        _getSetterMethodName: function (property) {
            var upper = property[0].toUpperCase() + property.substring(1);
            return 'set' + upper;
        },

        setTextColor: function (textColor) {
            this.el.css('color', textColor);
        },

        setIndicatorNameColor: function (color) {
            this.el.find('.istac-widget-embed a').css('color', color);

            // The color on the indicator name itself is handled inside the html
        },

        _getTimeGranularities: function () {
            return $.ajax({
                url: this.apiUrl + '/timeGranularities',
                dataType: 'jsonp',
                jsonp: "_callback"
            });
        },

        _getContrast50: function (hexcolor) {
            if (hexcolor && hexcolor.length > 0) {
                if (hexcolor[0] === '#') {
                    hexcolor = hexcolor.substring(1);
                }
                return (parseInt(hexcolor, 16) > 0xffffff / 2) ? '#333' : 'white';
            }
        },

        setBorderColor: function (borderColor) {
            this.bodyContainer.css('border-color', borderColor);
        },

        _cssBorderRadius: function (el, radius) {
            el.css('border-radius', radius);
            el.css('-webkit-border-radius', radius);
            el.css('-moz-border-radius', radius);
        },

        setBorderRadius: function (enable) {
            if (enable) {
                this._cssBorderRadius(this.titleContainer, "4px 4px 0 0");
                this._cssBorderRadius(this.bodyContainer, "0 0 12px 12px");
            } else {
                this._cssBorderRadius(this.titleContainer, "0");
                this._cssBorderRadius(this.bodyContainer, "0");
            }
        },

        setShadow: function (enable) {
            if (enable) {
                this.bodyContainer.css('box-shadow', 'rgba(0, 0, 0, 0.18) 0px 3px 5px -2px');
            } else {
                this.bodyContainer.css('box-shadow', 'none');
            }
        },

        setHeaderColor: function (color) {
            this.titleContainer.css('background-color', color);
            var contrastColor = this._getContrast50(color);

            this.titleContainer.css('color', contrastColor);
            this.titleContainer.find("a").css('color', contrastColor);
        },

        setWidth: function (width) {
            this.width = width;
            this.el.css('width', width);
        },

        updateTitle: function () {
            this.setTitle(this.options.title);
        },

        _getDefaultTitle: function () {
            var title;
            if (this.options.type === "lastData") {
                title = "Últimos datos";

                if (this.datasets && this.datasets.length > 0) {
                    var geographicalValue = this.options.geographicalValues[0];
                    title += ". " + this.datasets[0].getGeographicalValuesTitles()[geographicalValue];
                }
            } else if (this.options.type === "recent") {
                title = "Últimos indicadores actualizados";

                if (this.datasets && this.datasets.length > 0) {
                    var geographicalValue = this.options.geographicalValues[0];
                    title += ". " + this.datasets[0].getGeographicalValuesTitles()[geographicalValue];
                }
            } else if (this.options.type === "temporal") {
                if (this.datasets && this.datasets.length > 0) {
                    title = this.datasets[0].getTitle();
                } else {
                    title = "Serie temporal";
                }
            }
            return title;
        },

        setTitle: function (title) {
            if (_.isUndefined(title) || title.length === 0) {
                title = this._getDefaultTitle();
            }

            var $link = this.titleText.find('a');
            if ($link.length > 0) {
                $link.text(title);
            } else {
                this.titleText.text(title);
            }
            this.title = title;
        },

        setIndicatorSystem: function () {
            this._updateLinks();
        },

        setGroupType: function () {
            this._updateLinks();
        },

        _updateLinks: function () {
            var systemId = this.options.indicatorSystem;
            var url;
            var hasSystemId = this.options.type === 'temporal' || (this.options.groupType === 'system');
            if (hasSystemId) {
                var systemIdStr = systemId || "";
                url = this.url + "/indicatorsSystems/" + systemIdStr;
            }


            if (url) {
                this.titleText.html('<a href="' + url + '" target="_blank"></a>');
                this.setTitle(this.title);
                this.setHeaderColor(this.headerColor);
            } else {
                this.titleText.html('');
                this.setTitle(this.title);
                this.setHeaderColor(this.headerColor);
            }


            this.allIndicatorsContainer.find('a').attr('href', url);
            this.el.find('.istac-widget-body-allIndicators-text').toggle(hasSystemId);
        },

        setStyle: function (style) {
            var isGobcan = style === "gobcan";
            this.el.toggleClass("gobcan-style", isGobcan);
            this.set('gobcanStyleColor', this.gobcanStyleColor);
            if (isGobcan) {
                this.set('textColor', '#000000');

                var width = this.options.sideView ? 151 : 423;
                this.set('width', width);
            }
        },

        setGobcanStyleColor: function (color) {
            if (this.options.style === "gobcan") {
                this.el.toggleClass("blue", color === "blue");
                this.el.toggleClass("lightBlue", color === "lightBlue");
                if (color === "blue") {
                    this.set('headerColor', "#0F5B95");
                } else if (color === "lightBlue") {
                    this.set('headerColor', "#C4D0DC");
                }
            }
        },

        includeLogo: function () {
            if (this.options.uwa) {
                return true
            } else if (window.location.href.indexOf(this.url) > -1) {
                // Si estoy mostrando el widget en la misma página donde está la API
                // se muestra el logo para que el usuario lo vea igual que cuando
                // lo va a incrustar
                return true;
            } else {
                return !Istac.widget.helper.isIstacPage(window.location);
            }
        },

        initializeLogo: function () {
            this.creditsContainer.toggle(this.includeLogo());
        },

        initializeEmbed: function () {
            this.hideEmbed();
            var self = this;
            this.embedContainer.find('.hideEmbed').click(function () {
                self.hideEmbed();
                return false;
            });
            this.el.find('.istac-widget-embed a').click(function () {
                self.showEmbed();
                return false;
            });
        },

        openTag: function (tag, parameters) {
            var result = '<' + tag;

            _.each(parameters, function (value, key) {
                result += " " + key + "='" + value + "'";
            });

            result += '>';
            return result;
        },

        closeTag: function (tag) {
            return '</' + tag + '>';
        },

        showEmbed: function () {
            var filteredOptions = _.omit(this.options, ["el", "uwa", "afterRenderCallback", "width", "widgetWith"]);

            var closeScript = this.closeTag('script');

            var el = 'istac-widget';
            var code = '<div id="' + el + '"></div>';
            filteredOptions.el = '#' + el;

            filteredOptions.visualizerUrl = this._getVisualizerUrl();
            filteredOptions.width = this.options.widgetWith;

            code += this.openTag('script', { src: this.url + "/theme/js/widgets/widget.min.all.js" }) + closeScript;
            code += this.openTag('script') + "new IstacWidget(" + JSON.stringify(filteredOptions, null, 4) + ")" + closeScript;

            this.embedContainer.find('textarea').val(code);
            this.embedContainer.show();
        },

        _getVisualizerUrl: function () {
            if (typeof visualizerUrl !== "undefined") return visualizerUrl;

            return this.options.visualizerUrl;
        },

        hideEmbed: function () {
            this.embedContainer.hide();
        },

        reloadData: function () {
            var self = this;
            var requestUrl = this.datasetRequestBuilder.request(this.options);
            if (requestUrl) {
                var req = $.ajax({
                    url: requestUrl,
                    dataType: 'jsonp',
                    jsonp: "_callback"
                });

                this._getTimeGranularities().success(function (response) {
                    self.timeGranularities = response.items;

                    req.success(function (response) {
                        var datasets = _.map(response.items, function (item) {
                            return Dataset.fromRequest(item, self.timeGranularities);
                        }, self);
                        self.datasets = datasets;
                        self.render();
                    });
                });

            } else {
                self.datasets = [];
                self.render();
            }
        },

        onAfterRender: function () {
            if (this.afterRenderCallback) {
                this.afterRenderCallback(this);
            }
        }


    };

}(jQuery));