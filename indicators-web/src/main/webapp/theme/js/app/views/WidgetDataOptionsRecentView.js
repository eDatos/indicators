(function () {
    "use strict";

    var integerConverter = function (direction, val) {
        if (direction === 'ModelToView') {
            return val.toString();
        } else {
            return parseInt(val, 10);
        }
    };

    App.views.WidgetDataOptionsRecentView = Backbone.View.extend({

        template : App.loadTemplate('data-options-recent'),

        events : {
            "click .widget-update-preview" : "updatePreview"
        },

        updatePreview : function () {
            this.trigger("updatePreviewData");
            return false;
        },

        initialize : function () {
            this._modelBinder = new Backbone.ModelBinder();

            this.measures = new App.collections.Measures();
            this.geographicalGranularities = new App.collections.GeographicalGranularities();
            this.geographicalValues = new App.collections.GeographicalValues();
            this.systems = new App.collections.IndicatorSystems();
            this.subjects = new App.collections.Subjects();
            this.instances = new App.collections.IndicatorsInstances();
            this.indicators = new App.collections.Indicators();

            this.model.on('change:groupType', this._fetchGeographicalGranularities, this);
            this.model.on('change:subjectCode', this._fetchGeographicalGranularities, this);
            this.model.on('change:indicatorSystem', this._fetchGeographicalGranularities, this);
            this.model.on('change:geographicalGranularityCode', this._fetchGeographicalValuesAndTimeGranularities, this);
            
            this.model.on('change:geographicalValues', this.updatePreview, this);            
            this.model.on('change:indicators', this.updatePreview, this);
            this.model.on('change:measures', this.updatePreview, this);
            this.model.on('change:nrecent', this.updatePreview, this);
        },

        _fetchGeographicalGranularities : function () {
            this.geographicalGranularities.reset([]);
            this.model.set('geographicalGranularityCode', undefined);

            var groupType = this.model.get('groupType');
            var indicatorSystemCode = this.model.get('indicatorSystem');
            var subjectCode = this.model.get('subjectCode');

            if (groupType === 'system' && indicatorSystemCode) {
                this.geographicalGranularities.fetchByIndicatorSystemCode(indicatorSystemCode);
            } else if (groupType === 'subject' && subjectCode) {
                this.geographicalGranularities.fetchBySubjectCode(subjectCode);
            }
        },

        _fetchGeographicalValuesAndTimeGranularities : function () {
            this.geographicalValues.reset([]);
            this.model.set('indicators', undefined);

            var geographicalGranularityCode = this.model.get('geographicalGranularityCode');
            var groupType = this.model.get('groupType');
            var indicatorSystemCode = this.model.get('indicatorSystem');
            var subjectCode = this.model.get('subjectCode');

            if (geographicalGranularityCode && groupType === 'system' && indicatorSystemCode) {
                this.geographicalValues.fetchByIndicatorSystemCodeAndGeographicalGranularityCode(indicatorSystemCode, geographicalGranularityCode);
            } else if (geographicalGranularityCode && groupType === 'subject' && subjectCode) {
                this.geographicalValues.fetchBySubjectCodeAndGeographicalGranularityCode(subjectCode, geographicalGranularityCode);
            }
        },

        _renderMeasures : function () {
            // Measures
            var measuresView = new App.views.Select2View({
                el : this.$('.widget-data-measures'),
                collection : this.measures,
                idAttribute : 'id',
                textAttribute : 'text',
                multiple : true,
                width : "600px"
            });

            measuresView.on('change', function (measures) {
                if (measures) {
                    var ids = _.pluck(measures, "id");
                    this.model.set('measures', ids);
                } else {
                    this.model.set('measures', []);
                }
            }, this);
        },

        _renderSystems : function () {
            // Systems
            var indicatorSystemView = new App.views.Select2View({
                el : this.$('.widget-data-system'),
                collection : this.systems,
                idAttribute : 'code',
                textAttribute : 'title',
                multiple : false,
                width : "600px"
            });

            indicatorSystemView.on('change', function (indicatorSystem) {
                var value = indicatorSystem ? indicatorSystem.code : "";
                this.model.set('indicatorSystem', value);
            }, this);
        },

        _renderSubjects : function () {
            // Subjects
            var subjectSystemView = new App.views.Select2View({
                el : this.$('.widget-data-subject'),
                collection : this.subjects,
                idAttribute : 'code',
                textAttribute : 'title',
                multiple : false,
                width : "600px"
            });

            subjectSystemView.on('change', function (subject) {
                var value = subject ? subject.code : "";
                this.model.set('subjectCode', value);
            }, this);
        },

        _renderGranularities : function () {
            // Granularities
            var geographicalGranularitiesView = new App.views.Select2View({
                el : this.$(".widget-data-geographicalGranularities"),
                collection : this.geographicalGranularities,
                idAttribute : 'code',
                textAttribute : 'title',
                multiple : false,
                width : "600px"
            });

            geographicalGranularitiesView.on('change', function (granularity) {
                var value = granularity ? granularity.code : "";
                this.model.set('geographicalGranularityCode', value);
            }, this);
        },

        _renderGeographicalValues : function () {
            // GeographicalValues
            var geographicalValuesView = new App.views.Select2View({
                el : this.$(".widget-data-geographicalValues"),
                collection : this.geographicalValues,
                idAttribute : 'code',
                textAttribute : 'title',
                multiple : false,
                width : "600px",
                sort : false
            });

            geographicalValuesView.on('change', function (geographicalValue) {
                var value = geographicalValue ? [geographicalValue.code] : [];
                this.model.set('geographicalValues', value);
            }, this);
        },

        _renderGroupType : function () {
            // Group type
            var toggleGroupType = function () {
                var groupType = this.model.get('groupType');
                var toggleSystem = groupType === 'system';
                var toggleSubject = groupType === 'subject';
                this.$('.widget-data-system').toggle(toggleSystem);
                this.$('.widget-data-subject').toggle(toggleSubject);
            };
            toggleGroupType = _.bind(toggleGroupType, this);
            this.model.on('change:groupType', toggleGroupType);
            toggleGroupType();
        },

        render : function () {
            this.$el.html(this.template());

            // Bind radio button and nrecent input
            var bindings = {
                groupType: '[name="groupType"]',
                nrecent : {
                    selector : '[name="nrecent"]',
                    converter : integerConverter
                }
            };
            this._modelBinder.bind(this.model, this.el, bindings);

            // Bind select elements
            this._renderMeasures();
            this._renderSystems();
            this._renderSubjects();
            this._renderGranularities();
            this._renderGeographicalValues();

            // Visible zones
            this._renderGroupType();

            this.measures.resetDefaults();
            this.systems.fetchWithoutLimit();
            this.subjects.fetch();

            return this;
        }

    });
}());

