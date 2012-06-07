/**
 *
 */
var WidgetDataOptionsView = Backbone.View.extend({
    initialize : function(options){
        this.template = templateManager.get('dataOptionsTmpl');

        this.$el.html(this.template());

        var containers = {};
        containers.systems = $('.systems', this.$el);
        containers.indicators = $('.indicators', this.$el);
        containers.measures = $('.measures', this.$el);
        containers.geographicalValues = $('.geographicalValues', this.$el);


        var systemsCollection = new SystemsCollection();
        var systemsView = new SystemsView({el : containers.systems, collection : systemsCollection});
        systemsView.setMaxSelectionSize(1);

        var indicatorsCollection = new IndicatorsCollection();
        var indicatorsView = new IndicatorsView({el : containers.indicators, collection : indicatorsCollection, discriminator : 'id'});
        indicatorsView.setMaxSelectionSize(1);

        var self = this;
        systemsView.on('selectionChange', function(selection){
            if(selection.length > 0){
                var system = selection[0];
                var code = system != null ? system.get('code') : null;
                indicatorsCollection.setSystemId(code);
                indicatorsCollection.fetch();
                self.model.set('systemId', code);
            }else{
                indicatorsCollection.setSystemId(null);
                indicatorsCollection.reset([]);
                self.model.set('systemId', null);
            }
        });

        var selectedIndicatorsCollection = new IndicatorsCollection();
        indicatorsView.on('selectionChange', function(selection){
            var indicators = _.pluck(selection, 'id');
            self.model.set('indicators', indicators);
            selectedIndicatorsCollection.reset(selection);
            _.each(selection, function(indicator){
                if(!indicator.isFetched()){
                    indicator.fetch();
                }
            });
        });


        var measuresCollection = new Backbone.Collection();
        var measuresView = new SelectionView({collection : measuresCollection, el : containers.measures, discriminator : 'id'});

        measuresView.render();
        measuresCollection.reset([
            {id : 'ABSOLUTE', name : 'Dato'},
            {id : 'ANNUAL_PUNTUAL_RATE', name : 'Variación anual'},
            {id : 'INTERPERIOD_PUNTUAL_RATE', name : 'Variación interperiódica'},
            {id : 'ANNUAL_PERCENTAGE_RATE', name : 'Tasa variación anual'},
            {id : 'INTERPERIOD_PERCENTAGE_RATE', name : 'Tasa variación interperiódica'}
        ]);

        measuresView.on('selectionChange', function(selection){
            var measuresIds = _.pluck(selection, 'id');
            self.model.set('measures', measuresIds);
        });

        var geographicalCollection = new GeographicalCollection({indicators : selectedIndicatorsCollection});
        var geographicalView = new GeographicalView({collection : geographicalCollection, el : containers.geographicalValues, discriminator : 'code'});

        geographicalView.on('selectionChange', function(selection){
            var geographicalValues = _.map(selection, function(item){
                return item.get('code');
            });

            self.model.set('geographicalValues', geographicalValues);
        });

        var prepareSelectionLimits = function(){
            var type = self.model.get('type');
            if(type === "temporal"){
                measuresView.setMaxSelectionSize(1);
                indicatorsView.setMaxSelectionSize(1);
                geographicalView.setMaxSelectionSize(-1);
            }else if(type === "lastData"){
                measuresView.setMaxSelectionSize(-1);
                indicatorsView.setMaxSelectionSize(-1);
                geographicalView.setMaxSelectionSize(1);
            }
        }
        prepareSelectionLimits();

        //Sincronización de los limites en la selección
        self.model.on('change:type', prepareSelectionLimits);

        this.systemsView = systemsView;
        this.indicatorsView = indicatorsView;
        this.measuresView = measuresView;
        this.geographicalView = geographicalView;
    },

    render : function(){
        //Para no perder las selecciones se renderiza en el init.
        //Las selecciones se deberían poder actualizar

        this.systemsView.render();
        this.indicatorsView.render();
        this.measuresView.render();
        this.geographicalView.render();

        return this;
    }
});
