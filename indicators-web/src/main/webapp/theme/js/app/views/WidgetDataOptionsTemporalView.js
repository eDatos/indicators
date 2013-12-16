(function () {
    "use strict";

    App.views.WidgetDataOptionsTemporalView = Backbone.View.extend({

        template : App.loadTemplate('data-options-temporal'),

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
            this.systems = new App.collections.IndicatorSystems();
            this.instances = new App.collections.IndicatorsInstances();
            this.geographicalValues = new App.collections.GeographicalValues();
            this.timeGranularities = new App.collections.TimeGranularities();

            this.model.on('change:indicatorSystem', this._fetchIndicatorInstances, this);
            this.model.on('change:instances', this._fetchGeographicalValuesAndTimeGranularities, this);

            this.measures.resetDefaults();
            this.systems.fetch();
        },

        _fetchIndicatorInstances : function () {
            this.instances.reset([]);

            var systemCode = this.model.get('indicatorSystem');
            if (systemCode) {
                this.instances.fetchBySystemCode(systemCode);
            }
        },

        _fetchGeographicalValuesAndTimeGranularities : function () {
            var self = this;

            this.geographicalValues.reset([]);
            this.timeGranularities.reset([]);

            var instances = this.model.get('instances');
            if (instances.length > 0) {
                var instanceId = instances[0];
                var instanceModel = this.instances.get(instanceId);
                var req = instanceModel.fetch();
                this.geographicalValues.trigger('syncStart', this.geographicalValues);
                this.timeGranularities.trigger("syncStart", this.getTimeGranularities);
                req.success(function () {
                    self.geographicalValues.reset(instanceModel.getGeographicalValues());
                    self.timeGranularities.reset(instanceModel.getTimeGranularities(), {parse : true});
                });
            }
        },

        _renderMeasures : function () {
            // Measures
            var measuresView = new App.views.Select2View({
                el : this.$('.widget-data-measures'),
                collection : this.measures,
                idAttribute : 'id',
                textAttribute : 'text',
                multiple : false,
                width : "600px"
            });

            measuresView.on('change', function (measure) {
                this.model.set('measures', [measure.id]);
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

        _renderInstances : function () {
            // Instances
            var instancesView = new App.views.Select2View({
                el : this.$(".widget-data-instances"),
                collection : this.instances,
                idAttribute : 'id',
                textAttribute : 'title',
                multiple : false,
                width : "600px"
            });

            instancesView.on('change', function (instance) {
                var value = instance ? [instance.id] : [];
                this.model.set('instances', value);
            }, this);
        },

        _renderGeographicalValues : function () {
            // GeographicalValues
            var geographicalValuesView = new App.views.Select2View({
                el : this.$(".widget-data-geographicalValues"),
                collection : this.geographicalValues,
                idAttribute : 'code',
                textAttribute : 'title',
                multiple : true,
                width : "600px",
                sort : false
            });

            geographicalValuesView.on('change', function (geographicalValues) {
                var value = geographicalValues ? _.pluck(geographicalValues, 'code') : [];
                this.model.set('geographicalValues', value);
            }, this);
        },

        _renderTimeGranularities : function () {
            var timeGranularitiesView = new App.views.Select2View({
                el : this.$(".widget-data-timeGranularities"),
                collection : this.timeGranularities,
                idAttribute : 'code',
                textAttribute : 'title',
                multiple : false,
                width : "600px"
            });

            timeGranularitiesView.on('change', function (timeGranularities) {
                var value = timeGranularities ? [timeGranularities.code] : [];
                this.model.set('timeGranularities', value);
            }, this);
        },

        render : function () {
            this.$el.html(this.template());

            // Bind select elements
            this._renderMeasures();
            this._renderSystems();
            this._renderInstances();
            this._renderGeographicalValues();
            this._renderTimeGranularities();

            return this;
        }

    });
}());

