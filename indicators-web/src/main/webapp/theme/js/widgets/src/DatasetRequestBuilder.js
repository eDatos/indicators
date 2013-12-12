(function ($) {

    var DatasetRequestBuilder = function (options) {
        this.apiUrl = options.apiUrl;
    };

    DatasetRequestBuilder.prototype = {

        _toInParameters : function (list) {
            var stringItems = _.map(list,function (item) {
                return '"' + item + '"';
            }).join(", ");
            return "(" + stringItems + ")";
        },

        _eqOrIn : function (arg) {
            if (arg.length > 1) {
                return "IN " + this._toInParameters(arg);
            } else {
                return 'EQ "' + arg[0] + '"';
            }
        },

        _validateDefined : function (arg) {
            if (_.isUndefined(arg)) {
                throw new Error("undefined");
            }
        },

        _validateOneOrMore : function (arg) {
            if (_.isUndefined(arg) || !_.isArray(arg) || arg.length === 0) {
                throw new Error("array one or more");
            }
        },

        _validateOne : function (arg) {
            this._validateDefined(arg);
            if (!_.isArray(arg) || arg.length !== 1) {
                throw new Error("array one");
            }
        },

        _fieldsParameter : function () {
            return "&fields=%2Bdata,%2Bmetadata";
        },

        _representation : function (options) {
            this._validateOneOrMore(options.geographicalValues);
            var geographicalRepresentation = options.geographicalValues.join("|");
            return "&representation=GEOGRAPHICAL[" + geographicalRepresentation + "]";
        },

        _granularity : function (options) {
            this._validateOne(options.timeGranularities);
            var timeGranularities = options.timeGranularities.join("|");
            return "&granularity=TIME[" + timeGranularities + "]";
        },

        _selectedInstancesRequest : function (options) {
            this._validateDefined(options.indicatorSystem);
            this._validateOneOrMore(options.instances);

            return this.apiUrl + "/indicatorsSystems/" +
                options.indicatorSystem +
                "/indicatorsInstances/?q=id "
                + this._eqOrIn(options.instances)
                + this._fieldsParameter()
                + this._representation(options);

        },

        _selectedIndicatorsRequest : function (options) {
            this._validateOneOrMore(options.indicators);

            return this.apiUrl + "/indicators/?q=id "
                + this._eqOrIn(options.indicators)
                + this._fieldsParameter()
                + this._representation(options);
        },

        _recentInstancesRequest : function (options) {
            this._validateDefined(options.nrecent);
            this._validateDefined(options.indicatorSystem);
            this._validateOne(options.geographicalValues);

            var geographicalValue = options.geographicalValues[0];
            return this.apiUrl + "/indicatorsSystems/"
                + options.indicatorSystem
                + '/indicatorsInstances/?q=geographicalValue EQ "'
                + geographicalValue
                + '"&order=update DESC, id DESC&limit='
                + options.nrecent
                + this._fieldsParameter()
                + this._representation(options);
        },

        _recentIndicatorsRequest : function (options) {
            this._validateDefined(options.nrecent);
            this._validateDefined(options.subjectCode);
            this._validateOne(options.geographicalValues);

            var geographicalValue = options.geographicalValues[0];
            return this.apiUrl + '/indicators/?q=subjectCode EQ "'
                + options.subjectCode
                + '" AND geographicalValue EQ "'
                + geographicalValue
                + '"&order=update DESC, id DESC&limit='
                + options.nrecent
                + this._fieldsParameter()
                + this._representation(options);
        },

        _temporalRequest : function (options) {
            this._validateDefined(options.indicatorSystem);
            this._validateOne(options.instances);

            return this.apiUrl + "/indicatorsSystems/"
                + options.indicatorSystem
                + '/indicatorsInstances/?q=id EQ "'
                + options.instances[0]
                + '"'
                + this._fieldsParameter()
                + this._representation(options)
                + this._granularity(options);
        },

        request : function (options) {
            var result;
            try {
                if (options.type === 'lastData') {
                    if (options.groupType === 'system') {
                        result = this._selectedInstancesRequest(options);
                    } else if (options.groupType === 'subject') {
                        result = this._selectedIndicatorsRequest(options);
                    }
                } else if (options.type === 'recent') {
                    if (options.groupType === 'system') {
                        result = this._recentInstancesRequest(options);
                    } else if (options.groupType === 'subject') {
                        result = this._recentIndicatorsRequest(options);
                    }
                } else if (options.type === 'temporal') {
                    result = this._temporalRequest(options);
                }
            } catch (e) {
            }
            return result;
        }

    };

    Istac.widget.DatasetRequestBuilder = DatasetRequestBuilder;

}(jQuery));