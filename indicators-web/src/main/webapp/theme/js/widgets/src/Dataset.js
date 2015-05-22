(function ($) {

    var Dataset = function () {
        this.data = {};
        this.metadata = {};
    };

    Dataset.fromRequest = function (request, timeGranularities) {
        var dataset = new Dataset();
        dataset.request = request;
        dataset.metadata = request.metadata;
        dataset.data = request.data;

        dataset.allTimeGranularities = timeGranularities;
        
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
            if (this._isValidObservation(observation)) {
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

        getUnit : function (measure) {
            var result = "";

            if (this.metadata) {
                var measureRepresentation = _.find(this.metadata.dimension.MEASURE.representation, function (representation) {return representation.code === measure;});
                if (measureRepresentation) {
                    var quantity = measureRepresentation.quantity;

                    if (!_.isUndefined(quantity.unitMultiplier)) {
                        var unitMultiplierTitle = quantity.unitMultiplier.__default__;
                        if (unitMultiplierTitle !== "Unidades") {
                            result = result + unitMultiplierTitle + " de ";
                        }
                    }

                    if (quantity.unitSymbol) {
                        result = result + quantity.unitSymbol;
                    } else {
                        result = result + quantity.unit.__default__;
                    }
                }
            }
            return result;
        },

        _isValidObservation : function (observation) {
            var result = (!_.isUndefined(observation) && !_.isNull(observation) && observation !== ".");
            return result;
        },

        getObservationStr : function (geo, time, measure) {
            var res = null;
            if (this.data.observation) {
                var index = this.getObservationIndex(geo, time, measure);
                var observation = this.data.observation[index];

                if (this._isValidObservation(observation)) {
                    res = observation;
                    res = res.replace("\.", ",");
                    res = Istac.widget.helper.addThousandSeparator(res);
                }
            }
            return res;
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
            self = this;
            self.smallestTimeGranularity = this._getSmallestTimeGranularity();
            if (this.data.dimension.TIME.representation.index) {
                timeValues = _.chain(this.data.dimension.TIME.representation.index)
                    .map(function (value, key) {
                        return {key : key, value : value};
                    }).sortBy(function (dimension) {
                        return dimension.value;
                    }).filter(function (time) { 
                    	return this.metadata.dimension.TIME.representation[time.value].granularityCode == self.smallestTimeGranularity }, 
                    	self
                    ).map(function (dimension) {
                        return dimension.key;
                    }).value().reverse();
            }
            return timeValues;
        },
        
        _getSmallestTimeGranularity : function() {

        	this.timeGranularityCodes = _.pluck(this.metadata.dimension.TIME.granularity, 'code');
        	var sortedTimeGranularities = _.filter(this.allTimeGranularities, function(timeGranularity) {
        		return _.contains(this.timeGranularityCodes, timeGranularity.code); 
        	}, this);
        	
        	return sortedTimeGranularities[sortedTimeGranularities.length - 1].code;                     
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