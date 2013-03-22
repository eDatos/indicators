(function ($) {

    var Dataset = Istac.widget.Dataset;
    var Datasets = Istac.widget.Datasets;
    var DatasetRequestBuilder = Istac.widget.DatasetRequestBuilder;

    Istac.widget.Base = function (options) {
        this.init(options);
    };

    Istac.widget.Base.prototype = {

        _defaultOptions : {
            title : 'default title',
            width : 300,
            height : 400,
            headerColor : '#0F5B95',
            borderColor : '#EBEBEB',
            textColor : '#000000',
            indicatorSystem : "",
            subjectCode : "",
            indicators : [],
            instances : [],
            groupType : 'system', // or subject
            measures : [
                "ABSOLUTE",
                "ANNUAL_PERCENTAGE_RATE",
                "ANNUAL_PUNTUAL_RATE",
                "INTERPERIOD_PERCENTAGE_RATE",
                "INTERPERIOD_PUNTUAL_RATE"
            ],
            showLabels : true,
            showLegend : true,
            sideView : false,
            sparkline_ABSOLUTE : false,
            sparkline_ANNUAL_PERCENTAGE_RATE : false,
            sparkline_ANNUAL_PUNTUAL_RATE : false,
            sparkline_INTERPERIOD_PERCENTAGE_RATE : false,
            sparkline_INTERPERIOD_PUNTUAL_RATE : false,
            sparklineType : 'line',
            sparklineWidth : 30,
            sparklineHeight : 20,
            sparklineLineColor : "#0058B0",
            sparklineFillColor : null,
            uwa : false,
            shadow : true,
            borderRadius : true
        },

        _containerTemplate : Handlebars.templates.container,

        _uwaContainerTemplate : Handlebars.templates.uwaContainer,

        init : function (options) {
            this.options = _.extend({}, this._defaultOptions, options);
            this.el = $(options.el);
            this.type = options.type;
            this.measures = options.measures || this._defaultOptions.measures;
            this.geographicalValues = options.geographicalValues;

            // urls
            this.url = options.url || "";
            this.apiUrl = this.url + "/api/indicators/v1.0";
            this.jaxiUrl = options.jaxiUrl || "";

            // Request builder
            this.datasetRequestBuilder = new DatasetRequestBuilder({apiUrl : this.apiUrl});

            // locale
            this.locale = options.locale || "es";

            this.datasets = [];

            //Create containers
            var templateOptions = { widgetsTypeUrl : Istac.widget.configuration['indicators.widgets.typelist.url'] };

            if (this.options.uwa) {
                this.el.html(this._uwaContainerTemplate(templateOptions));
                this.titleLink = $('');
                this.titleContainer = $('');
            } else {
                this.el.html(this._containerTemplate(templateOptions));
                this.titleLink = this.el.find('.istac-widget-title-text');
                this.titleContainer = this.el.find('.istac-widget-title');
            }

            this.bodyContainer = this.el.find('.istac-widget-body');
            this.contentContainer = this.el.find('.istac-widget-content');
            this.creditsContainer = this.el.find('.istac-widget-credits');
            this.embedContainer = this.el.find('.istac-widget-body-embed');
            this.allIndicatorsContainer = this.el.find('.istac-widget-body-allIndicators');

            this.el.addClass("istac-widget");
            this.el.addClass(this.containerClass);

            var includeLogo = this.options.uwa ? true : !this.isIstacDomain();
            this.includeLogo(includeLogo);
            this.initializeEmbed();

            // Initialize style

            var realWidth = this.options.uwa ? "100%" : options.width;

            this.set('textColor', options.textColor);
            this.set('borderColor', options.borderColor);
            this.set('headerColor', options.headerColor);
            this.set('title', options.title);
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

        set : function (property, value) {
            this[property] = value;
            this.options[property] = value;

            var setter = this[this._getSetterMethodName(property)];
            if (_.isFunction(setter)) {
                setter.call(this, value);
            }
        },

        _getSetterMethodName : function (property) {
            var upper = property[0].toUpperCase() + property.substring(1);
            return 'set' + upper;
        },

        setTextColor : function (textColor) {
            this.el.css('color', textColor);
        },

        _getContrast50 : function (hexcolor) {
            if (hexcolor && hexcolor.length > 0) {
                if (hexcolor[0] === '#') {
                    hexcolor = hexcolor.substring(1);
                }
                return (parseInt(hexcolor, 16) > 0xffffff / 2) ? '#333' : 'white';
            }
        },

        setBorderColor : function (borderColor) {
            this.bodyContainer.css('border-color', borderColor);
        },

        _cssBorderRadius : function (el, radius) {
            el.css('border-radius', radius);
            el.css('-webkit-border-radius', radius);
            el.css('-moz-border-radius', radius);
        },

        setBorderRadius : function (enable) {
            if (enable) {
                this._cssBorderRadius(this.titleContainer, "4px 4px 0 0");
                this._cssBorderRadius(this.bodyContainer, "0 0 12px 12px");
            } else {
                this._cssBorderRadius(this.titleContainer, "0");
                this._cssBorderRadius(this.bodyContainer, "0");
            }
        },

        setShadow : function (enable) {
            if (enable) {
                this.bodyContainer.css('box-shadow', 'rgba(0, 0, 0, 0.18) 0px 3px 5px -2px');
            } else {
                this.bodyContainer.css('box-shadow', 'none');
            }
        },

        setHeaderColor : function (color) {
            this.titleContainer.css('background-color', color);
            var contrastColor = this._getContrast50(color);
            this.titleLink.css('color', contrastColor);
        },

        setWidth : function (width) {
            this.width = width;
            this.el.css('width', width);
        },

        updateTitle : function () {
            this.setTitle(this.options.title);
        },

        _getDefaultTitle : function () {
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

        setTitle : function (title) {
            if (_.isUndefined(title) || title.length === 0) {
                title = this._getDefaultTitle();
            }
            this.titleLink.text(title);
        },

        setIndicatorSystem : function () {
            this._updateLinks();
        },

        setGroupType : function () {
            this._updateLinks();
        },

        _updateLinks : function () {
            var systemId = this.options.indicatorSystem;
            var url;
            var hasSystemId = this.options.type === 'temporal' || (this.options.groupType === 'system');
            if (hasSystemId) {
                var systemIdStr = systemId || "";
                url = this.url + "/indicatorsSystems/" + systemIdStr;
            } else {
                url = this.url;
            }

            this.titleLink.attr('href', url);
            this.allIndicatorsContainer.find('a').attr('href', url);
            this.allIndicatorsContainer.toggle(hasSystemId);
        },

        setStyle : function (style) {
            var isGobcan = style === "gobcan";
            this.el.toggleClass("gobcan-style", isGobcan);
            this.set('gobcanStyleColor', this.gobcanStyleColor);
            if (isGobcan) {
                this.set('textColor', '#000000');
            }
        },

        setGobcanStyleColor : function (color) {
            this.el.toggleClass("blue", color === "blue");
            this.el.toggleClass("green", color === "green");
            if (color === "blue") {
                this.set('headerColor', "#0F5B95");
            } else if (color === "green") {
                this.set('headerColor', "#457A0E");
            }
        },

        isIstacDomain : function () {
            var result;
            if (window.location.href.substring(0, this.url.length) === this.url) {
                // Si estoy mostrando el widget en la misma página dodne está la API
                // se muestra el footer para que el usuario lo vea igual que cuando
                // lo va a incrustar
                result = false;
            } else {
                result = window.location.host === Istac.widget.helper.getHost(this.url);
            }
            return result;
        },

        includeLogo : function (include) {
            this.creditsContainer.toggle(include);
        },

        initializeEmbed : function () {
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

        openTag : function (tag, parameters) {
            var result = '<' + tag;

            _.each(parameters, function (value, key) {
                result += " " + key + "='" + value + "'";
            });

            result += '>';
            return result;
        },

        closeTag : function (tag) {
            return '</' + tag + '>';
        },

        showEmbed : function () {
            var filteredOptions = _.omit(this.options, "el");

            var closeScript = this.closeTag('script');
            var code = '<div id="istac-widget"></div>';

            code += this.openTag('script', {src : this.url + "/theme/js/widgets/widget.min.all.js" }) + closeScript;
            code += this.openTag('script') + "new IstacWidget(" + JSON.stringify(filteredOptions, null, 4) + ")" + closeScript;

            this.embedContainer.find('textarea').val(code);
            this.embedContainer.show();
        },

        hideEmbed : function () {
            this.embedContainer.hide();
        },

        reloadData : function () {
            var self = this;
            var requestUrl = this.datasetRequestBuilder.request(this.options);
            if (requestUrl) {
                var req = $.ajax({
                    url : requestUrl,
                    dataType : 'jsonp',
                    jsonp : "_callback"
                });
                req.success(function (response) {
                    var datasets = _.map(response.items, function (item) {
                        return Dataset.fromRequest(item);
                    });
                    self.datasets = datasets;
                    self.render();
                });
            } else {
                self.datasets = [];
                self.render();
            }
        }


    };

}(jQuery));