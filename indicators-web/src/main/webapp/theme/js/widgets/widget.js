/**
 * Istac Widget
 */
;
(function(undefined){

    var $tooltip = $('<p class="istact-widget-tooltip"></p>');
    $("body").append($tooltip);

    function tooltip($el, text){
        var xOffset = 10;
        var yOffset = 20;

        if(text){
            $tooltip.text(text);
            $el.hover(function(e){
                    $tooltip
                        .css("top",(e.pageY - xOffset) + "px")
                        .css("left",(e.pageX + yOffset) + "px")
                        .fadeIn("fast");
                },
                function(){
                    $tooltip.fadeOut("fast");
                });
            $el.mousemove(function(e){
                $tooltip
                    .css("top",(e.pageY - xOffset) + "px")
                    .css("left",(e.pageX + yOffset) + "px");
            });
        }
    }


    function addThousandSeparator(nStr){
        nStr += '';
        var x = nStr.split(',');
        var x1 = x[0];
        var x2 = x.length > 1 ? ',' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
            x1 = x1.replace(rgx, '$1' + '.' + '$2');
        }
        return x1 + x2;
    }


    function getHost(url){
        var l = document.createElement("a"),
            host;
        l.href = url;
        return l.host;
    }

    var _getKeys = function(hash){
        var keys = [];
        for (var i in hash) {
            if (hash.hasOwnProperty(i)) {
                keys.push(i);
            }
        }
        return keys;
    };

    var _firstKey = function(hash){
        var key;
        for (var i in hash) {
            if (hash.hasOwnProperty(i)) {
                key = i;
                break;
            }
        }
        return key;
    };

    var _firstValue = function(hash){
        var key = _firstKey(hash);
        var result;
        if(key){
            result = hash[key];
        }
        return result;
    }

    /* Simple JavaScript Inheritance
     * By John Resig http://ejohn.org/
     * MIT Licensed.
     */
    // Inspired by base2 and Prototype
    var initializing = false, fnTest = /xyz/.test(function(){
        xyz;
    }) ? /\b_super\b/ : /.*/;
    // The base Class implementation (does nothing)
    var Class = function(){
    };

    // Create a new Class that inherits from this class
    Class.extend = function(prop){
        var _super = this.prototype;

        // Instantiate a base class (but only create the instance,
        // don't run the init constructor)
        initializing = true;
        var prototype = new this();
        initializing = false;

        // Copy the properties over onto the new prototype
        for (var name in prop) {
            // Check if we're overwriting an existing function
            prototype[name] = typeof prop[name] == "function" &&
                typeof _super[name] == "function" && fnTest.test(prop[name]) ?
                (function(name, fn){
                    return function(){
                        var tmp = this._super;

                        // Add a new ._super() method that is the same method
                        // but on the super-class
                        this._super = _super[name];

                        // The method only need to be bound temporarily, so we
                        // remove it when we're done executing
                        var ret = fn.apply(this, arguments);
                        this._super = tmp;

                        return ret;
                    };
                })(name, prop[name]) :
                prop[name];
        }

        // The dummy class constructor
        function Class(){
            // All construction is actually done in the init method
            if (!initializing && this.init)
                this.init.apply(this, arguments);
        }

        // Populate our constructed prototype object
        Class.prototype = prototype;

        // Enforce the constructor to be what we expect
        Class.prototype.constructor = Class;

        // And make this class extendable
        Class.extend = arguments.callee;

        return Class;
    };

    var Dataset = function(apiUrl){
        this.data = {}
        this.structure = {};
        this.apiUrl = apiUrl;
    };

    Dataset._cache = {};
    $.extend(Dataset.prototype, {
        fetch : function(systemid, indicatorid, callback){
            var self = this;
            this.systemid = systemid;
            this.indicatorid = indicatorid;
            this.data = {};
            this.structure = {};

            var deferred = $.Deferred();

            var resolveFetch = function(){
                if(callback !== undefined){
                    callback(self);
                };

                deferred.resolve(self);
            }

            if(systemid == undefined || indicatorid == undefined){
                resolveFetch();
            }else{
                //Uso de cache interna, en producción se podría utilizar la cache de la petición?
                if(this._getDatasetFromCache(systemid, indicatorid)){
                    resolveFetch();
                }else{
                    this._getDatasetFromApi(systemid, indicatorid, resolveFetch);
                }
            }

            return deferred.promise();
        },

        getObservationIndex : function(geo, time, measure){
            var data = this.data;

            var geoIdIndex = $.inArray("GEOGRAPHICAL", data.format);
            var timeIdIndex = $.inArray("TIME", data.format);
            var measureIdIndex = $.inArray("MEASURE", data.format);

            var geoIndex = data.dimension.GEOGRAPHICAL.representation.index[geo];
            var timeIndex = data.dimension.TIME.representation.index[time];
            var measureIndex = data.dimension.MEASURE.representation.index[measure];

            //Calculo del multiplicador
            var m = 1;
            var mult = [];
            var dimensionsOrder = data.format;
            for (var i = 0; i < dimensionsOrder.length; i++) {
                if (i > 0) {
                    var dimension = dimensionsOrder[dimensionsOrder.length - i];
                    m *= data.dimension[dimension].representation.size;
                }
                mult.unshift(m);
            }

            var res = mult[geoIdIndex] * geoIndex +
                mult[timeIdIndex] * timeIndex +
                mult[measureIdIndex] * measureIndex;
            return res;
        },

        _observationToNumber : function(observation){
            if (observation) {
                return parseFloat(observation);
            }
        },

        getObservation : function(geo, time, measure){
            if(this.data.observation){
                var index = this.getObservationIndex(geo, time, measure);
                var observation = this.data.observation[index];
                var res = this._observationToNumber(observation);
                return res;
            }
            return null;
        },

        getUnit : function (geo, time, measure) {
            if(this.data.attribute) {
                var index = this.getObservationIndex(geo, time, measure);
                var attribute = this.data.attribute[index];

                if(attribute){
                    var result = "";
                    if(attribute.UNIT_MULT !== undefined && attribute.UNIT_MULT.value.__default__ !== "Unidades"){
                        result = result + attribute.UNIT_MULT.value.__default__ + " de ";
                    }
                    result = result + attribute.UNIT_MEAS_DETAIL.value.__default__;
                    return result;
                }
            }
            return "";
        },

        getObservationStr : function(geo, time, measure){
            if(this.data.observation){
                var index = this.getObservationIndex(geo, time, measure);
                var decimalPlaces = this.structure.decimalPlaces;
                var observation = this.data.observation[index];
                var res;
                if (observation) {
                    res = observation;
                    // No need to be fixed anymore, the api return the correct value
                    //res = parseFloat(observation).toFixed(decimalPlaces);

                    res = res.replace("\.", ",");
                    res = addThousandSeparator(res);
                } else {
                    res = "-";
                }
                return res;
            }
            return null;
        },

        _getTitles : function(data, locale){
            var result = {};
            if(data){
                for(var i = 0; i < data.length; i++){
                    var valor = data[i];
                    if(valor.title[locale]){
                        result[valor.code] = valor.title[locale];
                    }else{
                        result[valor.code] = _firstValue(valor.title);
                    }
                }
            }
            return result;
        },

        getDescription : function () {
            if(this.structure.conceptDescription){
                return this.structure.conceptDescription.__default__;
            }
        },

        getTimeValues : function(){
            var timeValues;
            if(this.data.dimension){
                timeValues = _getKeys(this.data.dimension.TIME.representation.index);
                return timeValues.sort();
            }
            return [];
        },

        getTimeValuesTitles : function(locale){
            if(this.structure.dimension){
                return this._getTitles(this.structure.dimension.TIME.representation, locale);
            }else{
                return [];
            }
        },

        getGeographicalValues : function(){
            if(this.data.dimension){
                return _getKeys(this.data.dimension.GEOGRAPHICAL.representation.index);
            }
            return [];
        },

        getGeographicalValuesTitles : function(locale){
            if(this.structure.dimension){
                return this._getTitles(this.structure.dimension.GEOGRAPHICAL.representation, locale);
            }else{
                return [];
            }
        },

        getLastTimeValue : function(){
            var timeValues = this.getTimeValues();
            if(timeValues.length > 0){
                return timeValues[timeValues.length - 1];
            }else{
                return null;
            }
        },

        _getCacheKey : function(systemid, indicatorid){
            if(systemid != undefined && indicatorid != undefined){
                return systemid + '#' + indicatorid;
            }
        },

        _getDatasetFromCache : function(systemid, indicatorid){
            var key = this._getCacheKey(systemid, indicatorid);
            if(key){
                var dataset = Dataset._cache[key];
                if(dataset){
                    this.data = dataset.data;
                    this.structure = dataset.structure;
                    return true;
                }
            }
            return false;
        },

        _saveCache : function(systemid, indicatorid){
            var self = this;
            var key = self._getCacheKey(systemid, indicatorid);
            Dataset._cache[key] = {
                data : self.data,
                structure : self.structure
            };
        },

        _getDatasetFromApi : function(systemid, indicatorid, callback){
            var self = this;

            var indicatorUrl = this.apiUrl + "/indicatorsSystems/" + systemid + "/indicatorsInstances/" + indicatorid;
            var indicatorDataUrl = indicatorUrl + "/data";

            var requestStructure = $.ajax({
                url : indicatorUrl,
                dataType : 'jsonp',
                jsonp : "_callback"
            });

            var requestData = $.ajax({
                url : indicatorDataUrl,
                dataType : 'jsonp',
                jsonp : "_callback"
            });

            requestStructure.success(function(response){
                self.structure = response;
            });

            requestData.success(function(response){
                self.data = response;
            });

            $.when(requestStructure, requestData).then(function(){
                self._saveCache(systemid, indicatorid);
                callback();
            });
        }
    });

    /*
     * IstacWidget Code
     */
    var Base = Class.extend({
        _defaultOptions : {
            title : 'default title',
            width : 300,
            height : 400,
            borderColor : '#3478B0',
            textColor : '#000000',
            indicators : [],
            measures : ["ABSOLUTE", "ANNUAL_PERCENTAGE_RATE", "ANNUAL_PUNTUAL_RATE", "INTERPERIOD_PERCENTAGE_RATE", "INTERPERIOD_PUNTUAL_RATE"],
            showLabels : true,
            showLegend : true
        },

        init : function(options){
            options = options || defaultOptions;
            this.el = $(options.el);

            this.type = options.type;

            this.systemId = options.systemId;



            this.indicators = options.indicators || [];
            this.measures = options.measures || this._defaultOptions.measures;
            this.geographicalValues = options.geographicalValues;

            // urls
            this.url = options.url || "";
            this.apiUrl = this.url + "/api/indicators/v1.0";
            this.jaxiUrl = options.jaxiUrl || "";

            // locale
            this.locale = options.locale || "es";

            //Create containers
            //TODO empty the container
            this.titleLink = $('<a href="#" target="_blank"></a>');
            this.titleContainer = $('<div class="istac-widget-title"></div>').append(this.titleLink);
            this.contentContainer = $('<div class="istac-widget-content"></div>');
            this.el.html(this.titleContainer).append(this.contentContainer);
            this.el.addClass("istac-widget");
            this.el.addClass(this.containerClass);

            if(!this.isIstacDomain()){
                this.includeLogo();
            }

            // Initialize style
            this.setTextColor(options.textColor);
            this.setBorderColor(options.borderColor);
            this.setTitle(options.title);
            this.setWidth(options.width);
            this.setShowLabels(options.showLabels);
            this.setShowLegend(options.showLegend);
            this.setSystemId(options.systemId);
        },

        setShowLabels : function(showLabels){
            this.showLabels = showLabels;
        },

        setShowLegend : function(showLegend){
            this.showLegend = showLegend;
        },

        setTextColor : function(textColor){
            this.textColor = textColor;
            this.el.css('color', textColor);
        },

        _getContrast50 : function(hexcolor){
            if (hexcolor && hexcolor.length > 0) {
                if (hexcolor[0] === '#') {
                    hexcolor = hexcolor.substring(1);
                }
                return (parseInt(hexcolor, 16) > 0xffffff / 2) ? '#333' : 'white';
            }
        },

        setBorderColor : function(borderColor){
            this.borderColor = borderColor;

            this.titleContainer.css('background-color', borderColor);
            var contrastColor = this._getContrast50(borderColor);
            this.titleLink.css('color', contrastColor);
            this.el.css('border-color', borderColor);
        },

        setWidth : function(width){
            this.width = width;
            this.el.css('width', width);
        },

        setTitle : function(title){
            this.title = title;
            this.titleLink.text(title);
        },

        setSystemId : function(systemId){
            this.systemId = systemId;
            this.titleLink.attr('href', this.url + "/indicatorsSystems/" + systemId);
        },

        setIndicators : function(indicators){
            this.indicators = indicators;
        },

        setMeasures : function(measures){
            this.measures = measures;
        },

        setGeographicalValues : function(geographicalValues){
            this.geographicalValues = geographicalValues;
        },

        isIstacDomain : function(){
            var result = window.location.host === getHost(this.url);
            return result;
        },

        includeLogo : function(){
            this.el.append('<div class="istact-widget-footer"><a href="' + this.url + '/widgets/creator">ISTAC</a></div>');
        }
    });

    var Temporal = Base.extend({
        init : function(options){
            this._super(options);
        },

        setWidth : function(width){
            this._super(width);
            //this.render();
        },

        parse : function(dataset){
            var self = this;

            var locale = this.locale;

            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();
            var timeValuesTitles = dataset.getTimeValuesTitles(locale);
            var geographicalValues = this.geographicalValues;
            var geographicalValuesTitles = dataset.getGeographicalValuesTitles(locale);


            var legend = {};
            var values = {};
            var tooltips = {};
            for (var i = 0; i < geographicalValues.length; i++) {
                var serie = 'serie' + i;
                var geoValue = geographicalValues[i];
                var geoValueTitle = geographicalValuesTitles[geoValue];

                var data = [];
                var tooltip = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
                    var valueStr = dataset.getObservationStr(geoValue, timeValue, measureValue);
                    data.push(value);
                    tooltip.push('<div>' + geoValueTitle + '</div><div>' + timeValuesTitles[timeValue] + '</div><div>' + valueStr + '</div>');
                }

                legend[serie] = geoValueTitle;
                values[serie] = data;
                tooltips[serie] = tooltip;
            };

            //limit the number of labels (eje x)
            var totalLabels = 5;
            var division = Math.floor(timeValues.length / totalLabels);
            var labels = [];
            for(var i = 0; i < timeValues.length; i++){
                if(i % division === 0){
                    var timeValue = timeValues[i];
                    labels.push(timeValuesTitles[timeValue]);
                }else{
                    labels.push('');
                }
            }

            var chartData = {
                labels : labels,
                values : values,
                legend : legend,
                tooltips : tooltips
            };
            return chartData;
        },

        render : function(){
            var self = this;
            var dataset = new Dataset(this.apiUrl);


            var indicatorId = self.indicators[0];
            var systemId = self.systemId;

            dataset.fetch(systemId, indicatorId,function(){
                var chartData = self.parse(dataset);
                self.renderChart(chartData);
            });
        },

        chartColors : function(chartData){
            var seriesLength = chartData.tooltips.length;

            var result = {
                serie0 : {
                    color : "#4F81BD"
                },
                serie1 : {
                    color : "#FFC000"
                },
                serie2 : {
                    color : "#92D050"
                },
                serie3 : {
                    color : "#F79646"
                },
                serie4 : {
                    color : "#C00000"
                },
                serie5 : {
                    color : "#8064A2"
                },
                serie6 : {
                    color : "#808080"
                },
                serie7 :{
                    color : "#00B0F0"
                }
            };
            return result;
        },

        renderChart : function(chartData){
            var $chartContainer = $('<div id="chart"></div>');
            $chartContainer.css('width', this.width);
            $chartContainer.css('height', this.height);
            this.contentContainer.html($chartContainer);

            var renderData = chartData;
            var colors = this.chartColors(chartData);
            var legendElements = _getKeys(chartData.legend);

            var chartWidth = this.width;
            var chartHeight = 200;

            var legendWidth = this.width;
            var legendHeight = this.showLegend? legendElements.length * 20 : 0;

            var chartTopMargin = 20;
            var labelHeight = this.showLabels? 40 : 10;
            var legendTop = chartTopMargin + chartHeight + labelHeight;

            var chartBottomMargin = legendHeight + labelHeight;

            var containerHeight = chartTopMargin + chartHeight + chartBottomMargin;

            var chartOptions = {
                type : "line",
                margins : [chartTopMargin, 20, chartBottomMargin, 50],
                defaultSeries : {
                    plotProps : {
                        "stroke-width" : 1
                    },
                    dot : false,
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
                    },
                    tooltip : {
                        active : true,
                        width: 200,
                        height: 60,
                        roundedCorners: 5,
                        padding: [6, 6] /* y, x */,
                        offset: [20, 0] /* y, x */,
                        frameProps : { fill: "white", "stroke-width": 1 },
                        contentStyle : { "font-family": "Arial", "font-size": "12px", "line-height": "16px", color: "black" }
                    }
                },
                series : colors,
                defaultAxis : {
                    labels : true,
                    labelsDistance: 20
                },
                features : {
                    grid : {
                        draw : [true, false],
                        forceBorder : true,
                        props : {
                            "stroke-dasharray" : "-"
                        }
                    }
                },
                width : chartWidth,
                height : containerHeight,
                labels : renderData.labels,
                values : renderData.values,
                tooltips : renderData.tooltips
            };

            if(this.showLegend){
                chartOptions.features.legend = {
                    width : legendWidth,
                    height : legendHeight,
                    x : 0,
                    y : legendTop,
                    dotType : "circle",
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
                    },
                    borderProps : {
                        opacity : 0.3,
                        fill : "#c0c0c0",
                        "stroke-width" : 0,
                        "stroke-opacity" : 0
                    }
                }
                chartOptions.legend = renderData.legend;
            }

            if(!this.showLabels){
                chartOptions.axis = {
                    x : {
                        labels : false
                    }
                }
            }

            $chartContainer.chart(chartOptions);

        }
    });

    var measuresLabels = {
        'ABSOLUTE' : 'Dato',
        'ANNUAL_PERCENTAGE_RATE' : 'Tasa variación anual',
        'ANNUAL_PUNTUAL_RATE' :  'Variación anual',
        'INTERPERIOD_PERCENTAGE_RATE' : 'Tasa variación interperiódica',
        'INTERPERIOD_PUNTUAL_RATE' : 'Variación interperiódica'
    }

    var LastData = Base.extend({
        containerClass : "istac-widget-lastData",

        init : function(options){
            this._super(options);
        },

        _renderTableColumn : function(content){
            return '<td>' + content + '</td>'
        },

        parse : function(dataset){
            var temporalValue = dataset.getLastTimeValue();
            var geographicalValue = this.geographicalValues[0];

            var observations = [];
            var units = [];
            for (var i = 0; i < this.measures.length; i++) {
                var measure = this.measures[i];
                var value = dataset.getObservationStr(geographicalValue, temporalValue, measure);

                if(typeof(value) === 'undefined'){
                    value = '-';
                }

                var unit = dataset.getUnit(geographicalValue, temporalValue, measure);

                observations.push({value : value, unit : unit});
            }

            return {
                temporalValue : temporalValue,
                observations : observations,
                units : units
            }
        },

        render : function(){
            var self = this;
            var datasets = [];
            var requests = [];
            var systemId = self.systemId;

            var indicators = self.indicators;
            for(var i = 0; i < indicators.length; i++){
                var dataset = new Dataset(this.apiUrl);
                var request = dataset.fetch(systemId, indicators[i]);
                datasets.push(dataset);
                requests.push(request);
            }

            if(requests.length > 0){
                $.when.apply(this, requests).then(function(){
                    self.renderTable(datasets);
                });
            }else{
                self.renderTable(datasets);
            }
        },

        renderTable : function(datasets){
            if (datasets) {
                var head = '<thead>';
                head += '<tr><th></th>';

                for (var i = 0; i < this.measures.length; i++) {
                    var measure = this.measures[i];
                    head += '<th>' + measuresLabels[measure] + '</th>';
                }

                head += '</thead>';

                var $tbody = $('<tbody></tbody>');

                for (var i = 0; i < datasets.length; i++) {
                    var dataset = datasets[i];
                    var timeValuesTitles = dataset.getTimeValuesTitles('es');

                    var indicator = this.indicators[i];
                    var renderValues = this.parse(dataset);


                    //TODO LOCALIZE title!

                    var row = '<tr>' +
                    '<th>' +
                        '<a href="'+ this.jaxiUrl +'/tabla.do?uuidInstanciaIndicador=' + dataset.indicatorid + '&codigoSistemaIndicadores=' + dataset.systemid + '&accion=html">' +
                            dataset.structure.title.__default__ + ' (' + timeValuesTitles[renderValues.temporalValue] + ')' +
                        '</a>' +
                    '</th>';

                    var observations = renderValues.observations;
                    for (var j = 0; j < observations.length; j++) {
                        var observation = observations[j];
                        row += this._renderTableColumn('<p class="istact-widget-observation">' + observation.value + '</p><p class="istact-widget-unit">' + observation.unit + '</p>');
                    }

                    row += '</tr>';

                    var $row = $(row);
                    tooltip($row, dataset.getDescription());

                    $tbody.append($row);
                };

                var $table = $('<table>' + head + '</table>').append($tbody);
                this.contentContainer.html($table);
            }
        }
    });

    var loadCSS = function(cssId, url){
        var doc = document;
        if (!doc.getElementById(cssId)) {
            var head = doc.getElementsByTagName('head')[0];
            var link = doc.createElement('link');
            link.id = cssId;
            link.rel = 'stylesheet';
            link.type = 'text/css';
            link.href = url;
            link.media = 'all';
            head.appendChild(link);
        }
    };

    var loadJS = function(condition, url){
        if (condition) {
            var head = document.getElementsByTagName('head')[0],
                script = document.createElement('script');
            script.src = url;
            head.appendChild(script);
        }
    };

    var loadResources = function(url){
        loadCSS('istac-widget-css', url + '/theme/css/widgets.css');
        loadJS(!window.jQuery, url + '/theme/js/widgets/libs/jquery-1.7.1.js');
        loadJS(!window.Raphael, url + '/theme/js/widgets/libs/raphael-min.js');
        loadJS(!(window.jQuery && window.jQuery.elycharts), url + '/theme/js/widgets/libs/elycharts.min.js');
    }

    var Factory = function(options){
        options = options || {};

        if(options.url){
            loadResources(options.url);
            var widget;
            if (options.type === 'temporal') {
                widget = new Temporal(options);

            } else {
                widget = new LastData(options);
            }
            widget.render();
            return widget;
        }else{
            $(options.el).text("Error, no se ha especificado la url del servicio web");
        }
    };


    //Export ClosureCompiler style
    window['IstacWidgetTemporal'] = Temporal;
    window['IstacWidgetLastData'] = LastData;
    window['IstacWidget'] = Factory;
    window['IstacDataset'] = Dataset;
})();