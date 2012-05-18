/**
 * Istac Widget
 */
;
(function(undefined){

    var istacHost;

    if(typeof(istacUrl) === 'undefined'){
        throw  "istacUrl not defined";
    }else{
        var getLocation = function(href) {
            var l = document.createElement("a");
            l.href = href;
            return l;
        };
        istacHost = getLocation(istacUrl).host;
    }

    if(typeof(apiContext) === 'undefined'){
        apiContext = istacUrl + "/api/indicators/v1.0";
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

    var Dataset = Class.extend({

        _cache : [],

        init : function(){
            this.data = {}
            this.structure = {};
        },

        fetch : function(systemid, indicatorid, callback){
            var self = this;
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
                observation = observation.replace('\.', '');
                observation = observation.replace(',', '.');
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

        getTimeValues : function(){
            var timeValues;
            if(this.data.dimension){
                timeValues = _getKeys(this.data.dimension.TIME.representation.index);
                return timeValues.sort();
            }
            return [];
        },

        getGeographicalValues : function(){
            if(this.data.dimension){
                return _getKeys(this.data.dimension.GEOGRAPHICAL.representation.index);
            }
            return [];
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
                var dataset = this._cache[key];
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
            self._cache[key] = {
                data : self.data,
                structure : self.structure
            };
        },

        _getDatasetFromApi : function(systemid, indicatorid, callback){
            var self = this;
            //TODO esto van a tener que ser peticiones JSONP?
            var indicatorUrl = apiContext + "/indicatorsSystems/" + systemid + "/indicatorsInstances/" + indicatorid;
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
            measures : ["ABSOLUTE", "ANNUAL_PERCENTAGE_RATE", "ANNUAL_PUNTUAL_RATE", "INTERPERIOD_PERCENTAGE_RATE", "INTERPERIOD_PUNTUAL_RATE"]
        },

        init : function(options){
            options = options || defaultOptions;
            this.el = $(options.el);

            //TODO indicator data from webservice
            this.systemId = options.systemId;
            this.indicators = options.indicators || [];
            this.measures = options.measures || this._defaultOptions.measures;
            this.geographicalValues = options.geographicalValues;

            //Create containers
            //TODO empty the container
            this.titleContainer = $('<div class="istac-widget-title"></div>');
            this.contentContainer = $('<div class="istac-widget-content"></div>');
            this.el.html(this.titleContainer).append(this.contentContainer);
            this.el.addClass("istac-widget");
            this.el.addClass(this.containerClass);

            if(!this.isIstacDomain()){
                this.includeLogo();
            }

            //Initialize style
            this.setTextColor(options.textColor);
            this.setBorderColor(options.borderColor);
            this.setTitle(options.title);
            this.setWidth(options.width);

            //Render the table content
            this.render();
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
            this.titleContainer.css('color', contrastColor);
            this.el.css('border-color', borderColor);
        },

        setWidth : function(width){
            this.width = width;
            this.el.css('width', width);
        },

        setTitle : function(title){
            this.title = title;
            this.titleContainer.text(title);
        },

        setSystemId : function(systemId){
            this.systemId = systemId;
            this.render();
        },

        setIndicators : function(indicators){
            this.indicators = indicators;
            this.render();
        },

        setMeasures : function(measures){
            this.measures = measures;
            this.render();
        },

        setGeographicalValues : function(geographicalValues){
            this.geographicalValues = geographicalValues;
            this.render();
        },

        isIstacDomain : function(){
            return window.location.host === istacHost;
        },

        includeLogo : function(){
            this.el.append('<div class="istact-widget-footer"><a href="' + istacUrl + '">Widget facilitado por el ISTAC</a></div>');
        }
    });

    var Temporal = Base.extend({
        init : function(options){
            this._super(options);
        },

        setWidth : function(width){
            this._super(width);
            this.render();
        },

        parse : function(dataset){
            var self = this;
            var measureValue = this.measures[0];
            var timeValues = dataset.getTimeValues();

            var geographicalValues = this.geographicalValues;

            var legend = {};
            var values = {};
            var tooltips = {};
            for (var i = 0; i < geographicalValues.length; i++) {
                var serie = 'serie' + i;
                var geoValue = geographicalValues[i];
                legend[serie] = geoValue;
                tooltips[serie] = timeValues;

                var data = [];
                for (var j = 0; j < timeValues.length; j++) {
                    var timeValue = timeValues[j];
                    var value = dataset.getObservation(geoValue, timeValue, measureValue);
                    data.push(value);
                }
                values[serie] = data;
            }
            ;

            self.renderChart({
                labels : timeValues,
                values : values,
                legend : legend,
                tooltips : tooltips
            });
        },

        render : function(){
            var self = this;
            var dataset = new Dataset();


            var indicatorId = self.indicators[0];
            var systemId = self.systemId;

            dataset.fetch(systemId, indicatorId,function(){
                self.parse(dataset);
            });
        },

        renderChart : function(chartData){
            var $chartContainer = $('<div id="chart"></div>');
            $chartContainer.css('width', this.width);
            $chartContainer.css('height', this.height);
            this.contentContainer.html($chartContainer);

            var width = this.width;
            var legendWith = 80;
            var legendMargin = 20;

            var renderData = chartData;

            $chartContainer.chart({
                type : "line",
                margins : [10, 10, 20, 50],
                defaultSeries : {
                    plotProps : {
                        "stroke-width" : 4
                    },
                    dot : true,
                    dotProps : {
                        stroke : "white",
                        "stroke-width" : 2
                    }
                },
                series : {
                    serie0 : {
                        color : "#3478B0"
                    },
                    serie1 : {
                        color : "#EBCC5C"
                    }
                },
                defaultAxis : {
                    labels : true
                },
                features : {
                    grid : {
                        draw : [true, false],
                        props : {
                            "stroke-dasharray" : "-"
                        }
                    },
                    legend : {
                        horizontal : false,
                        width : legendWith,
                        height : 50,
                        x : width - legendWith - legendMargin,
                        y : 220,
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
                },
                width : this.width,
                height : 400,
                labels : renderData.labels,
                values : renderData.values,
                legend : renderData.legend,
                tooltips : renderData.tooltips
            });
        }
    });


    var measuresLabels = {
        'ABSOLUTE' : 'Absoluto',
        'ANNUAL_PERCENTAGE_RATE' : 'Tasa porcentual interanual',
        'ANNUAL_PUNTUAL_RATE' :  'Tasa puntual interanual',
        'INTERPERIOD_PERCENTAGE_RATE' : 'Tasa porcentual interperiódica',
        'INTERPERIOD_PUNTUAL_RATE' : 'Tasa puntual interperiódica'
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
            for (var i = 0; i < this.measures.length; i++) {
                var measure = this.measures[i];
                var observation = dataset.getObservation(geographicalValue, temporalValue, measure);
                observations.push(observation);
            }

            return {
                temporalValue : temporalValue,
                observations : observations
            }
        },

        render : function(){
            var self = this;
            var datasets = [];
            var requests = [];
            var systemId = self.systemId;

            var indicators = self.indicators;
            for(var i = 0; i < indicators.length; i++){
                var dataset = new Dataset();
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

                var rows = '';

                for (var i = 0; i < datasets.length; i++) {
                    var dataset = datasets[i];

                    var indicator = this.indicators[i];
                    var renderValues = this.parse(dataset);

                    //TODO LOCALIZE title!
                    var row = '<tr><th>' + dataset.structure.title.es + ' (' + renderValues.temporalValue + ')</th>';

                    var observations = renderValues.observations;
                    for (var j = 0; j < observations.length; j++) {

                        var value = observations[j];
                        value = (typeof(value) === 'undefined') ? '-' : value;
                        row += this._renderTableColumn(value);
                    }

                    row += '</tr>';
                    rows += (row);
                }
                this.contentContainer.html('<table>' + head + '<tbody>' + rows + '</tbody></table>');
            }
        }
    });

    var Factory = function(options){
        options = options || {};
        if (options.type === 'temporal') {
            return new Temporal(options);
        } else {
            return new LastData(options);
        }
    };

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
    }

    var loadJS = function(condition, url){
        if (condition) {
            var head = document.getElementsByTagName('head')[0],
                script = document.createElement('script');
            script.src = url;
            head.appendChild(script);
        }
    }

    loadCSS('istac-widget-css', istacUrl + '/theme/css/widgets.css');
    loadJS(!window.jQuery, istacUrl + '/theme/js/widgets/libs/jquery-1.7.1.js');
    loadJS(!window.Raphael, istacUrl + '/theme/js/widgets/libs/raphael-min.js');
    loadJS(!(window.jQuery && window.jQuery.elycharts), istacUrl + '/theme/js/widgets/libs/elycharts.min.js');

    //Export ClosureCompiler style
    window['IstacWidget'] = Factory;
    window['IstacDataset'] = Dataset;
})();