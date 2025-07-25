(function (_) {
    "use strict";

    App.views.WidgetDataOptionsLastDataView = Backbone.View.extend({

        template: App.loadTemplate('data-options-lastData'),

        initialize: function () {
            this._modelBinder = new Backbone.ModelBinder();

            this.measures = new App.collections.Measures();
            this.geographicalGranularities = new App.collections.GeographicalGranularities();
            this.geographicalValues = new App.collections.GeographicalValues();
            this.systems = new App.collections.IndicatorSystems();
            this.subjects = new App.collections.Subjects();
            this.instances = new App.collections.IndicatorsInstances();
            this.indicators = new App.collections.Indicators();
            this.allSubjects = new App.collections.AllSubjects();

            this.model.on('change:groupType', function (model, value) {

                this.geographicalGranularities.reset([]);
                this.geographicalValues.reset([]);
                this.instances.reset([]);
                this.indicators.reset([]);

                switch (value) {
                    case 'allValues':
                        this._fetchGeographicalGranularities();
                        this._fetchIndicators();
                        break;
                    default:
                        this._fetchGeographicalValuesAndTimeGranularities();
                        break;
                }

            }, this);
            this.model.on('change:subjectCode', this._fetchGeographicalGranularities, this);
            this.model.on('change:indicatorSystem', this._fetchGeographicalGranularities, this);
            this.model.on('change:geographicalGranularityCode', this._fetchGeographicalValuesAndTimeGranularities, this);

            this.model.on('change:indicatorSystemCode', this._fetchIndicatorInstances, this);
            this.model.on('change:geographicalValues', this._fetchIndicatorInstances, this);

            this.model.on('change:subjectCode', this._fetchIndicators, this);
            this.model.on('change:geographicalValues', this._fetchIndicators, this);
            this.model.on('change:indicatorsMain', this._fetchIndicators, this);

            this.model.on('change:instances', this.updatePreview, this);
            this.model.on('change:measures', this.updatePreview, this);
            this.model.on('change:indicators', this.updatePreview, this);

            this.measures.resetDefaults();
            this.fetchSystems();

        },

        events: {
            "click .widget-update-preview": "updatePreview"
        },

        updatePreview: function () {
            this.trigger("updatePreviewData");
            return false;
        },

        fetchSystems: function() {
            var self = this;
            this.systems.fetchWithoutLimit().done(function () {
                if (self.model.get('groupType') !== 'system' && self.systems.length > 0) {
                    $("#system").show();
                } 
            });
        },

        _fetchGeographicalGranularities: function () {
            this.geographicalGranularities.reset([]);
            this.model.set('geographicalGranularityCode', undefined);

            var groupType = this.model.get('groupType');
            var indicatorSystemCode = this.model.get('indicatorSystem');
            var subjectCode = this.model.get('subjectCode');

            if (groupType === 'system' && indicatorSystemCode) {
                this.geographicalGranularities.fetchByIndicatorSystemCode(indicatorSystemCode);
            } else if (groupType === 'subject' && subjectCode) {
                this.geographicalGranularities.fetchBySubjectCode(subjectCode);
            } else if (groupType === 'allValues') {
                this.geographicalGranularities.fetchAll();
            }
        },

        _fetchGeographicalValuesAndTimeGranularities: function () {
            this.geographicalValues.reset([]);
            this.model.set('instances', undefined);
            var geographicalGranularityCode = this.model.get('geographicalGranularityCode');
            var groupType = this.model.get('groupType');
            var indicatorSystemCode = this.model.get('indicatorSystem');
            var subjectCode = this.model.get('subjectCode');

            if (geographicalGranularityCode && groupType === 'system' && indicatorSystemCode) {
                this.geographicalValues.fetchByIndicatorSystemCodeAndGeographicalGranularityCode(indicatorSystemCode, geographicalGranularityCode);
            } else if (geographicalGranularityCode && groupType === 'subject' && subjectCode) {
                this.geographicalValues.fetchBySubjectCodeAndGeographicalGranularityCode(subjectCode, geographicalGranularityCode);
            } else if (geographicalGranularityCode && groupType === 'allValues') {
                this.geographicalValues.fetchAllAndGeographicalGranularityCode(geographicalGranularityCode);
            }

        },

        _fetchIndicatorInstances: function () {
            this.instances.reset([]);

            var groupType = this.model.get('groupType');
            var geographicalValues = this.model.get('geographicalValues');
            var geographicalValue = geographicalValues[0];
            var indicatorSystemCode = this.model.get('indicatorSystem');

            if (groupType === "system" && indicatorSystemCode && geographicalValue) {
                this.instances.fetchBySystemCodeAndGeographicalValueCode(indicatorSystemCode, geographicalValue);
            }
        },

        _fetchIndicators: function () {
            this.indicators.reset([]);
            var groupType = this.model.get('groupType');
            var indicatorsMain = this.model.get('indicatorsMain');
            var subjectCode = this.model.get('subjectCode');
            var geographicalValues = this.model.get('geographicalValues');
            var geographicalValue = geographicalValues[0];
            if (groupType === 'subject' && subjectCode && geographicalValue) {
                this._disableIndicatorsMainRadioButtons();
                this.indicators.fetchBySubjectCodeAndGeographicalValueCode(subjectCode, geographicalValue, indicatorsMain)
                    .then(this._enableIndicatorsMainRadioButtons, this._enableIndicatorsMainRadioButtons);
            } else if (groupType === 'allValues' && geographicalValue) {
                this._disableIndicatorsMainRadioButtons();
                this.indicators.fetchAllByGeographicalValueCode(geographicalValue, indicatorsMain)
                    .then(this._enableIndicatorsMainRadioButtons, this._enableIndicatorsMainRadioButtons);
            }
        },

        _enableIndicatorsMainRadioButtons: function () {
            $(".widget-data-main input").each((i, radioButton) => {
                radioButton.disabled = false;
            });
        },

        _disableIndicatorsMainRadioButtons: function () {
            $(".widget-data-main input").each((i, radioButton) => {
                radioButton.disabled = true;
            });
        },

        _renderMeasures: function () {
            // Measures
            var measuresView = new App.views.Select2View({
                el: this.$('.widget-data-measures'),
                collection: this.measures,
                idAttribute: 'id',
                textAttribute: 'text',
                multiple: true,
                width: "600px"
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

        _renderSystems: function () {
            // Systems
            var indicatorSystemView = new App.views.Select2View({
                el: this.$('.widget-data-system'),
                collection: this.systems,
                idAttribute: 'code',
                textAttribute: 'title',
                multiple: false,
                width: "600px"
            });

            indicatorSystemView.on('change', function (indicatorSystem) {
                var value = indicatorSystem ? indicatorSystem.code : "";
                this.model.set('indicatorSystem', value);
            }, this);
        },

        _renderSubjects: function () {
            // Subjects
            var subjectSystemView = new App.views.Select2View({
                el: this.$('.widget-data-subject'),
                collection: this.subjects,
                idAttribute: 'code',
                textAttribute: 'title',
                multiple: false,
                width: "600px"
            });

            subjectSystemView.on('change', function (subject) {
                var value = subject ? subject.code : "";
                this.model.set('subjectCode', value);
            }, this);
        },

        _renderGranularities: function () {
            // Granularities
            var geographicalGranularitiesView = new App.views.Select2View({
                el: this.$(".widget-data-geographicalGranularities"),
                collection: this.geographicalGranularities,
                idAttribute: 'code',
                textAttribute: 'title',
                multiple: false,
                width: "600px"
            });

            geographicalGranularitiesView.on('change', function (granularity) {
                var value = granularity ? granularity.code : "";
                this.model.set('geographicalGranularityCode', value);
            }, this);
        },

        _renderGeographicalValues: function () {
            // GeographicalValues
            var geographicalValuesView = new App.views.Select2View({
                el: this.$(".widget-data-geographicalValues"),
                collection: this.geographicalValues,
                idAttribute: 'code',
                textAttribute: 'title',
                multiple: false,
                width: "600px",
                sort: false
            });

            geographicalValuesView.on('change', function (geographicalValue) {
                var value = geographicalValue ? [geographicalValue.code] : [];
                this.model.set('geographicalValues', value);
            }, this);
        },
        
        _renderGroupType: function () {
            // Group type
            var toggleGroupType = function () {
                var groupType = this.model.get('groupType');
                var toggleSystem = groupType === 'system';
                var toggleSubject = groupType === 'subject';
                var toggleAllValues = groupType === 'allValues';
                this.$('.widget-data-system').toggle(toggleSystem);
                this.$('.widget-data-subject').toggle(toggleSubject);
                this.$(".widget-data-instances").toggle(toggleSystem);
                this.$(".widget-data-indicators").toggle(toggleSubject);
                this.$(".widget-data-all-indicators").toggle(toggleAllValues);
                this.$(".widget-data-main").toggle(toggleSubject || toggleAllValues);
                this.$(".main-indicators-help").toggle(toggleSubject || toggleAllValues);
            };
            toggleGroupType = _.bind(toggleGroupType, this);
            this.model.on('change:groupType', toggleGroupType);
            toggleGroupType();
        },

        _renderIndicators: function () {
            // Indicators
            var indicatorsView = new App.views.Select2View({
                el: this.$(".widget-data-indicators"),
                collection: this.indicators,
                idAttribute: 'code',
                textAttribute: 'title',
                multiple: true,
                width: "600px"
            });
            indicatorsView.on('change', function (indicators) {
                var value = indicators ? _.pluck(indicators, "id") : [];
                this.model.set('indicators', value);
            }, this);
        },

        _renderAllIndicators: function () {
            // Indicators
            var self = this;
            var allIndicatorsWidget = new App.views.IndicatorPicklist({
                el: self.$(".widget-data-all-indicators"),
                collection: this.indicators,
                subjects: this.allSubjects
            });
            allIndicatorsWidget.on('change', function (indicators) {
                var value = indicators ? _.pluck(indicators, "id") : [];
                this.model.set('indicators', value);
            }, this);
        },

        _renderInstances: function () {
            // Instances
            var instancesView = new App.views.Select2View({
                el: this.$(".widget-data-instances"),
                collection: this.instances,
                idAttribute: 'id',
                textAttribute: 'title',
                multiple: true,
                width: "600px"
            });
            instancesView.on('change', function (instances) {
                var value = instances ? _.pluck(instances, "id") : [];
                this.model.set('instances', value);
            }, this);
        },

        render: function () {
            this.$el.html(this.template());
            
            this.$('.main-indicators-help').qtip({
                content: EDatos.common.I18n.translate('MAIN_INDICATORS.ONLY_MAIN.TOOLTIP'),
                show: 'mouseover',
                hide: 'mouseout'
            });

            // Bind radio button
            this._modelBinder.bind(this.model, this.el);

            // Bind select elements
            this._renderMeasures();
            this._renderSystems();
            this._renderSubjects();
            this._renderGranularities();
            this._renderGeographicalValues();
            this._renderIndicators();
            this._renderAllIndicators();
            this._renderInstances();

            // Visible zones
            this._renderGroupType();

            this.subjects.fetch();
            this.allSubjects.fetch();

            return this;
        }


    });
}(window._));

