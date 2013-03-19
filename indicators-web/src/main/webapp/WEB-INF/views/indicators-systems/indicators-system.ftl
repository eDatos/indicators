[#ftl]
[#include "/inc/includes.ftl"]
[@template.base migas='<li><strong>Sistema de indicadores</strong></li>']



<div class="listadoTablas">
    <div id="indicators-system-view"></div>
    <div>
        <ul class="subcaps" style="margin: 0px; padding-left: 4px;">
            <div id="elements-view"></div>
        </ul>
    </div>
</div>
<div id="indicators-system-not-found-view"></div>

<script type="text/html" id="indicatorsSystemTemplate">
	<div class="h2roundbox">
		<div class="h2top"></div>
		<div class="h2content">
			[@apph.messageEscape 'menu.indicators-systems'/]: <%= getLabel(title) %>
			<a href="<%= context %>/indicatorsSystems/<%=code%>/atom.xml?language=${.lang}" style="float: right; font-weight: normal; margin-right: 5px;" ><img height="14" width="14" style="margin-right:5px; vertical-align: text-bottom; float: none;" src="[@spring.url "/theme/images/atom.png"/]" title="RSS" alt="RSS" /></a>
		</div>
	</div>

	<% if (description) { %>
		<div>
			<p><%= getLabel(description) %></p>
		</div>
	<% } %>

    <% if (objective) { %>
        <p><%= getLabel(objective) %></p>
    <% } %>

</script>

<script type="text/html" id="elementTemplate">

	<% if (kind == 'indicators#dimension') { %>
		<div style="clear: both;" ></div>
		<% if (level == 1) { %>
			<br />
			<h3 id="cab_1"><%= getLabel(title) %></a></h3>
		<% } else if (level == 2) { %>
			<li>
				<span class="itemSubcapitulo"><%= getLabel(title) %></span>
			</li>
		<% } else { %>
			<li style="font-weight: bold;">
				<span class="itemSubSubcapitulo" style="margin-left:<%= (level - 2)* 20 %>px"></span><%= getLabel(title) %></span>
			</li>
		<% } %>
		<ul class="dimensionSubElement" style="margin-left: 0px;">
		</ul>	
	<% } else if (kind == 'indicators#indicatorInstance') { %>
		<li>		
	  		<div style="clear: both;">
				<div class="itemControlInfo">
					<img id="<%= id %>" style="padding-right:3px" border="0" src="[@spring.url "/theme/images/tabla.gif"/]">
				</div>
				<div class="itemTabla">
					<%= numeration %>
					<a class="nouline" href="<%= '${jaxiUrlBase}' %>/tabla.do?instanciaIndicador=<%= id %>&sistemaIndicadores=<%= indicatorsSystemCode %>&accion=html"><%= getLabel(title) %></a>
				</div>
				<div class="clearfix"></div>
				<div class="indicatorInstanceDetail"></div>
			</div>								
		</li>
	<% } %>		
</script>

<script type="text/html" id="indicatorInstanceTemplate">

    <table class="popupInfoDescarga">
        <tbody>
            <% if (dimension.GEOGRAPHICAL.granularity != null) {
            %>
            <tr>
                <td><strong>[@apph.messageEscape 'entity.indicator-instance.geographical-granularities'/]: </strong>
                </td>
                <td>
                    <%= geographicalGranularities %>
                </td>
            </tr>
            <% } %>

            <% if (dimension.TIME.granularity != null) {
            %>
            <tr>
                <td>
                    <strong>[@apph.messageEscape 'entity.indicator-instance.time-granularities'/]: </strong>
                </td>
                <td>
                    <%= timeGranularities %>
                </td>
            </tr>
            <% } %>
            <tr>
                <td><strong>Indicador: </strong></td>
                <td><a href="<%= selfLink %>" target="_blank"><img src="[@spring.url "/theme/images/download_json.png"/]"></a></td>
            </tr>
            <tr>
                <td><strong>Datos del indicador: </strong></td>
                <td><a href="<%= selfLink %>/data" target="_blank"><img src="[@spring.url "/theme/images/download_json.png"/]"></a></td>
            </tr>
        </tbody>
    </table>
</script>

<script type="text/html" id="indicatorsSystemNotFoundTemplate">
    <div>[@apph.messageEscape 'page.indicators-system.error.not-found'/]</div>
</script>

<script type="text/javascript" src="[@spring.url "/theme/js/libs/jquery.qtip-1.0.0-rc3.min.js"/]"></script>

<script>

var IndicatorsSystemModel = Backbone.Model.extend({

    "url" : function () {
        return apiContext + '/indicatorsSystems/' + this.get("code");
    },

    initialize : function () {
        this.fetch();
    }
});

var IndicatorsSystemView = Backbone.View.extend({
    template : _.template($('#indicatorsSystemTemplate').html()),

    initialize : function () {
        this.model.bind("change", this.render, this);
    },

    render : function () {
        // render is called by fetch method of IndicatorsSystemModel
        var json = this.model.toJSON();
        if (!this.model.description) {
            json.description = null;
        }
        $(this.el).html(this.template(json));
        return this;
    }
});

var IndicatorInstanceDetail = Backbone.View.extend({
    template : _.template($('#indicatorInstanceTemplate').html()),

    render : function () {
        var context = this.model;
        context.geographicalGranularities = _.map(this.model.dimension.GEOGRAPHICAL.granularity, getIndicatorInstanceSubelementDimension).join(", ");
        context.timeGranularities = _.map(this.model.dimension.TIME.granularity, getIndicatorInstanceSubelementDimension).join(", ");

        $(this.el).html(this.template(this.model));
        return this;
    }
});

var ElementView = Backbone.View.extend({
    template : _.template($('#elementTemplate').html()),

    showIndicatorInstanceDetail : function () {
        var self = this;
        console.log("making api call", this.model.get('selfLink'));
        $.get(this.model.get('selfLink'))
                .success(function (indicatorInstance) {
                    console.log(indicatorInstance);


                    //var $detail = $('.indicatorInstanceDetail', self.el);
                    var view = new IndicatorInstanceDetail({model : indicatorInstance});
                    view.render();
                    self.$tip.qtip("api").updateContent(view.el);
                })
                .error(function () {
                    console.log('Error!');
                });
    },

    bindTip : function () {
        if (this.$el.find("img").length) {

            this.$tip = this.$el.find("img").qtip(
                    {
                        content : {
                            text : '<img class="throbber" src="[@spring.url "/theme/images/loading.gif" /]" alt="Cargando..." />',
                            title : {
                                text : 'Información de descarga',
                                button : 'Cerrar'
                            }
                        },
                        position : {
                            corner : {
                                target : 'bottomMiddle', // Position the tooltip above the link
                                tooltip : 'topMiddle'
                            },
                            adjust : {
                                screen : true // Keep the tooltip on-screen at all times
                            }
                        },
                        show : {
                            when : 'click',
                            solo : true // Only show one tooltip at a time
                        },
                        hide : 'unfocus',
                        style : {
                            tip : true, // Apply a speech bubble tip to the tooltip at the designated tooltip corner
                            border : {
                                width : 0,
                                radius : 4
                            },
                            name : 'light', // Use the default light style
                            width : 390 // Set the tooltip width
                        }
                    });


            var self = this;
            this.$tip.qtip("api").onRender = function () {
                self.showIndicatorInstanceDetail();
            };
        }
    },

    render : function () {

        // Numeration
        var numerationBase = '';
        if (this.options.numerationBase != '') {
            numerationBase = this.options.numerationBase + '.';
        }
        numerationBase += _.string.lpad(this.options.numeration, 2, '0');

        // Element
        var json = this.model.toJSON();
        json.level = this.options.level;
        json.indicatorsSystemCode = this.options.indicatorsSystemCode;
        json.numeration = numerationBase;

        var html = this.template(json);
        this.$el.html(html);

        var self = this;


        this.bindTip();

        // Subelements
        var kind = this.model.get('kind');
        if (kind == 'indicators#dimension') {
            var subelementsNumeration = 1;
            var subelements = this.model.get('elements');
            if (subelements != '') {
                var subelementsCollection = new ElementsCollection(subelements);
                var htmlUbication = $('.dimensionSubElement', this.$el);
                var subelementsCollectionView = new ElementsCollectionView({el : htmlUbication,
                    collection : subelementsCollection,
                    level : this.options.level + 1,
                    numerationBase : numerationBase,
                    numeration : subelementsNumeration,
                    indicatorsSystemCode : this.options.indicatorsSystemCode});

                subelementsCollectionView.render();

                subelementsNumeration += 1;
            }
        }
        return html;
    }
});

var ElementsCollection = Backbone.Collection.extend({
});

var ElementsCollectionView = Backbone.View.extend({

    initialize : function () {
        this.collection.bind("reset", this.render, this);
    },

    render : function () {
        var self = this;
        var numeration = self.options.numeration;
        this.collection.forEach(function (model) {
            var elementView = new ElementView({ model : model,
                level : self.options.level,
                numerationBase : self.options.numerationBase,
                numeration : numeration,
                indicatorsSystemCode : self.options.indicatorsSystemCode});
            elementView.render();

            self.$el.append(elementView.el);

            numeration += 1;
        });
        return this;
    }
});

var IndicatorsSystemNotFoundView = Backbone.View.extend({
    template : _.template($('#indicatorsSystemNotFoundTemplate').html()),

    render : function () {
        $(this.el).html(this.template(this.model));
        return this;
    }
});

var ElementsModel = Backbone.Model.extend({

    "url" : function () {
        return apiContext + '/indicatorsSystems/' + this.get("code");
    },

    initialize : function () {
        // the following line forces 'this' to refer to the Package instance in the function `fetch_success`
        _.bindAll(this, 'fetchSuccess');
        this.bind('change', this.fetchSuccess);
        this.fetch();

    },

    fetchSuccess : function () {
        var elementsCollection = new ElementsCollection(this.get('elements'));
        var elementsCollectionView = new ElementsCollectionView({el : '#elements-view', collection : elementsCollection, level : 1, numerationBase : '', numeration : 1, indicatorsSystemCode : this.get('code')});
        elementsCollectionView.render();
    }
});


$(function () {
    var indicatorsSystemCode = '${indicatorsSystemCode}';
    var indicatorsSystemModel = new IndicatorsSystemModel({code : indicatorsSystemCode});
    new IndicatorsSystemView({el : '#indicators-system-view', model : indicatorsSystemModel});

    var elementsCollection = new ElementsCollection();
    indicatorsSystemModel.on("change", function () {
        elementsCollection.reset(indicatorsSystemModel.get('elements'));
    });

    var elementsCollectionView = new ElementsCollectionView({el : '#elements-view', collection : elementsCollection, level : 1, numerationBase : '', numeration : 1, indicatorsSystemCode : indicatorsSystemCode});
});

</script>
[/@template.base]