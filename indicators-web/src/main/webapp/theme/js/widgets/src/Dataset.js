(function ($) {

    var Dataset = function () {
        this.data = {};
        this.metadata = {};
    };

    Dataset.fromRequest = function (request) {
        var dataset = new Dataset();
        dataset.request = request;
        dataset.metadata = request.metadata;
        dataset.data = request.data;
        return dataset;
    };

    Dataset.prototype = {

        getObservationIndex : function (geo, time, measure) {
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

        _observationToNumber : function (observation) {
            if (observation) {
                return parseFloat(observation);
            }
        },

        getObservation : function (geo, time, measure) {
            if (this.data.observation) {
                var index = this.getObservationIndex(geo, time, measure);
                var observation = this.data.observation[index];
                var res = this._observationToNumber(observation);
                return res;
            }
            return null;
        },

        getUnit : function (geo, time, measure) {
            if (this.data.attribute) {
                var index = this.getObservationIndex(geo, time, measure);
                var attribute = this.data.attribute[index];

                if (attribute) {
                    var result = "";
                    if (attribute.UNIT_MULT !== undefined && attribute.UNIT_MULT.value.__default__ !== "Unidades") {
                        result = result + attribute.UNIT_MULT.value.__default__ + " de ";
                    }

                    if(attribute.UNIT_MEASURE) {
                        result = result + attribute.UNIT_MEASURE.value.__default__;
                    } else {
                        result = result + attribute.UNIT_MEAS_DETAIL.value.__default__;
                    }

                    return result;
                }
            }
            return "";
        },

        getObservationStr : function (geo, time, measure) {
            if (this.data.observation) {
                var index = this.getObservationIndex(geo, time, measure);
                var observation = this.data.observation[index];
                var attributes = this.data.attribute[index];
                
                var res = null;
                if (attributes) {
	                if (observation) {
	                    res = observation;
	                    // No need to be fixed anymore, the api return the correct value
	                    //res = parseFloat(observation).toFixed(decimalPlaces);
	
	                    res = res.replace("\.", ",");
	                    res = Istac.widget.helper.addThousandSeparator(res);
	                } else {
	                    res = "-";
	                }
                }
                return res;
            }
            return null;
        },

        getObservationsByGeoAndMeasure : function (geo, measure) {
            var timeValues = this.getTimeValues();
            var result = _.map(timeValues, function (time) {
                return this.getObservation(geo, time, measure)
            }, this);
            return result;
        },

        _getTitles : function (data, locale) {
            var result = {};
            if (data) {
                for (var i = 0; i < data.length; i++) {
                    var valor = data[i];
                    if (valor.title[locale]) {
                        result[valor.code] = valor.title[locale];
                    } else {
                        result[valor.code] = valor.title.__default__;
                    }
                }
            }
            return result;
        },

        getDescription : function (locale) {
            return this._getLabel(this.request.conceptDescription, locale);
        },

        getTimeValues : function () {
            var timeValues = [];
            if (this.data.dimension.TIME.representation.index) {
            	timeValues = _.chain(this.data.dimension.TIME.representation.index)
	            	.map(function (value, key) {
	            		return {key : key, value : value};
	            	}).sortBy(function (dimension) {
	            		return dimension.value;
	            	}).map(function (dimension) {
	            		return dimension.key;
	            	}).value().reverse();            
            }
            return timeValues;
        },

        getTimeValuesTitles : function (locale) {
            if (this.metadata.dimension) {
                var titles = this._getTitles(this.metadata.dimension.TIME.representation, locale);
                return titles;
            } else {
                return [];
            }
        },

        getGeographicalValues : function () {
            if (this.data.dimension) {
                return Istac.widget.helper.getKeys(this.data.dimension.GEOGRAPHICAL.representation.index);
            }
            return [];
        },

        getGeographicalValuesTitles : function (locale) {
            if (this.metadata.dimension) {
                return this._getTitles(this.metadata.dimension.GEOGRAPHICAL.representation, locale);
            } else {
                return [];
            }
        },

        getLastTimeValue : function () {
            var timeValues = this.getTimeValues();
            if (timeValues.length > 0) {
                return timeValues[timeValues.length - 1];
            } else {
                return null;
            }
        },

        _getLabel : function (iString, locale) {
            if (iString) {
                return iString[locale] || iString["__default__"];
            }
        },

        getTitle : function (locale) {
            return this._getLabel(this.request.title, locale);
        },

        _emptyIsUndefined : function (value) {
            return value || "";
        }

    };

    Istac.widget.Dataset = Dataset;

}(jQuery));