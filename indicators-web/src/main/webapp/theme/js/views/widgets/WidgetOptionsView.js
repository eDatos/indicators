/**
 *
 */
var WidgetOptionsView = Backbone.View.extend({
    initialize : function(options){
        var options = options || {};

        this.cacheInputSelectors();
        this.setInputsValFromModel();
        this.bindColorPickers();
        this.configureSlider();

        var systemsCollection = new SystemsCollection();
        var systemsView = new SystemsView({el : '#widget-options-data-systems',
            collection : systemsCollection});
        systemsView.setMaxSelectionSize(1);

        var indicatorsCollection = new IndicatorsCollection();
        var indicatorsView = new IndicatorsView({el : '#widget-options-data-indicators', collection : indicatorsCollection, discriminator : 'id'});
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
        var measuresView = new SelectionView({collection : measuresCollection, el : '#widget-options-measures', discriminator : 'id'});

        measuresView.render();
        measuresCollection.reset([
            {id : 'ABSOLUTE', name : 'ABSOLUTE'},
            {id : 'ANNUAL_PERCENTAGE_RATE', name : 'ANNUAL PERCENTAGE RATE'},
            {id : 'ANNUAL_PUNTUAL_RATE', name : 'ANNUAL PUNTUAL RATE'},
            {id : 'INTERPERIOD_PERCENTAGE_RATE', name : 'INTERPERIOD PERCENTAGE RATE'},
            {id : 'INTERPERIOD_PUNTUAL_RATE', name : 'INTERPERIOD PUNTUAL RATE'}
        ]);

        measuresView.on('selectionChange', function(selection){
            var measuresIds = _.pluck(selection, 'id');
            self.model.set('measures', measuresIds);
        });

        var geographicalCollection = new GeographicalCollection({indicators : selectedIndicatorsCollection});
        var geographicalView = new GeographicalView({collection : geographicalCollection, el : '#widget-options-data-geographical-value', discriminator : 'code'});

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
    },

    configureSlider : function(){
        var self = this;

        self.inputs.widthSlider.slider({
            min : 100,
            max : 500,
            value : self.model.get('width'),
            slide : function(event, ui){
                self.model.set('width', ui.value);
                self.inputs.width.val(ui.value);
            }
        });
    },

    bindColorPicker : function(input, property){
        var self = this;
        var defaultVal = input.val();
        input.ColorPicker({
            color : defaultVal,
            onChange : function(hsb, hex, rgb){
                var value = '#' + hex;
                input.val(value);
                self.model.set(property, value);
            }
        }).bind('keyup', function(){
                $(this).ColorPickerSetColor(this.value);
                self.model.set(property, value);
            });
    },

    bindColorPickers : function(){
        this.bindColorPicker(this.inputs.borderColor, 'borderColor');
        this.bindColorPicker(this.inputs.textColor, 'textColor');
    },

    cacheInputSelectors : function(){
        this.inputs = {};
        this.inputs.title = $('#widget-options-style-title');
        this.inputs.width = $('#widget-options-style-width');
        this.inputs.widthSlider = $('#widget-options-style-width-slider');
        this.inputs.borderColor = $('#widget-options-style-border-color');
        this.inputs.textColor = $('#widget-options-style-text-color');

        this.inputs.type = $('#widget-options-data-type');
    },

    setInputsValFromModel : function(){
        this.inputs.title.val(this.model.get('title'));
        this.inputs.width.val(this.model.get('width'));
        this.inputs.borderColor.val(this.model.get('borderColor'));
        this.inputs.textColor.val(this.model.get('textColor'));

        // TODO checks measures
    },

    events : {
        'keyup #widget-options-style-title' : 'setTitle',
        'keyup #widget-options-style-width' : 'setWidth',
        'change .measure' : 'setMeasures',
        'change #widget-options-data-type' : 'setType'
    },

    setTitle : function(){
        var value = this.inputs.title.val();
        this.model.set('title', value);
    },

    setWidth : function(){
        var value = this.inputs.width.val();
        this.model.set('width', value);
        this.inputs.widthSlider.slider('value', value);
    },

    setMeasures : function(){
        var measures = [];
        var $measuresChecks = $('.measure', this.$el);
        $.each($measuresChecks, function(i, check){
            var $check = $(check);
            if ($check.prop("checked")) {
                measures.push($check.attr('name'));
            }
        });
        this.model.set('measures', measures);
    },

    setType : function(){
        var type = $("option:selected", this.inputs.type).val();
        this.model.set('type', type);
    }
});
