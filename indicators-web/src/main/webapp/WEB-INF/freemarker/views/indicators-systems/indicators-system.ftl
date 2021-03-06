[#ftl]
[#include "/includes.ftl"]
[@template.base]

    [#global idx = 1]
    [#global depth = 1]

    [#macro renderElementChildren element]
        [#global depth = depth + 1]
    <ul class="capitulos">
        [#if element.elements?? ]
            [@renderElementList element.elements/]
        [/#if]
    </ul>
        [#global depth = depth - 1]
    [/#macro]

    [#macro renderElement element]
        [#if element.kind == "indicators#dimension"]
            [#if depth <= 2]
                [#assign openCssClass="open"/]
            [#else]
                [#assign openCssClass="close"/]
            [/#if]
        <li class="dimension dimension-depth-${depth} ${openCssClass}">
            <span class="dimension-title">
                [#if depth > 2]<a href="#" class="tree-icon"></a>[/#if]
                [@localizeTitle element.title/]
            </span>
            [@renderElementChildren element /]
        </li>
        [#elseif element.kind == "indicators#indicatorInstance"]
        <li>
            <i class="icon-table" data-self-link="${element.selfLink?replace("^https:","","r")?replace("^http:","","r")}"></i>
            <span class="item-numeration">${idx?string?left_pad(numberOfFixedDigitsInNumeration, "0")}</span>
            <a class="nouline"
               href="${statisticalVisualizerUtil.buildIndicatorInstanceUrl(visualizerApplicationExternalUrlBase, element.id, indicatorsSystemCode)}">[@localizeTitle element.title/]</a>
               
        </li>
            [#global idx = idx + 1]
        [/#if]

    [/#macro]

    [#macro renderElementList elementList]
        [#list elementList as element]
            [@renderElement element/]
        [/#list]
    [/#macro]

    [#macro localizeTitle title][#if title.es??]${title.es}[#else]#{title.__default__}[/#if][/#macro]

<div class="listadoTablas">

    <div class="h2roundbox">
        <div class="h2top"></div>
        <div class="h2content" style="min-height: 15px; margin-top: 3px;">
            [@localizeTitle indicator.title/]
            <a href="${indicatorsExternalApiUrlBase}/v1.0/indicatorsSystems/${indicator.code}" target="_blank"
               class="metadata" title="[@apph.messageEscape 'entity.indicators.modal.detail.metadata'/]">
                <i class="icon-metadata"></i>
            </a>
        </div>
    </div>

    [#if indicator.objective??]
        <div>
            <p>[@localizeTitle indicator.objective/]</p>
        </div>
    [/#if]

    <div>
        <ul class="capitulos">
            [@renderElementList indicator.elements/]
        </ul>
    </div>
</div>

<script type="text/javascript" src="${serverURL}/theme/js/libs/jquery.qtip-1.0.0-rc3.min.js"></script>
<script>
    // Tree behaviour
    $(".tree-icon").click(function (e) {
        e.preventDefault();
        var $icon = $(this);
        var $parentDimension = $icon.closest(".dimension");
        $parentDimension.toggleClass("open");
        $parentDimension.toggleClass("close");
    });

    // Tooltips for table icons
    _.each($(".icon-table"), function (icon) {
        var $icon = $(icon);

        var $tooltip = $icon.qtip({
            content : {
                text : '<img class="throbber" src="${serverURL}/theme/images/loading.gif" alt="[@apph.messageEscape 'app.loading'/]" />',
                title : {
                    text : '[@apph.messageEscape 'entity.indicators.modal.detail.title'/]',
                    button : '[@apph.messageEscape 'app.close'/]'
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

        $tooltip.qtip("api").onRender = function () {

            var selfLink = $icon.data("self-link");

            var instanceRequest = $.get(selfLink);
            instanceRequest.success(function (indicatorInstance) {
                var view = new IndicatorInstanceDetail({model : indicatorInstance});
                view.render();
                $tooltip.qtip("api").updateContent(view.$el.html());
            });
            instanceRequest.error(function () {
                $tooltip.qtip("api").updateContent("Error");
            });
        };
    });

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
            <td><strong>[@apph.messageEscape 'entity.indicators.modal.detail.download'/]</strong></td>
            <td>
                <a href="<%= selfLink.replace("https:","").replace("http:","") %>" target="_blank" title="[@apph.messageEscape 'entity.indicators.modal.detail.metadata'/]" class="popup-metadata">
                    <i class="icon-metadata"></i>
                    [@apph.messageEscape 'entity.indicators.modal.detail.metadata'/]
                </a>
                |
                <a href="<%= selfLink.replace("https:","").replace("http:","") %>/data" target="_blank" title="[@apph.messageEscape 'entity.indicators.modal.detail.data'/]" class="popup-data">
                    <i class="icon-table"></i>
                    [@apph.messageEscape 'entity.indicators.modal.detail.data'/]
                </a>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <strong>
                    <a href="${statisticalVisualizerUtil.buildIndicatorInstanceUrl(visualizerApplicationExternalUrlBase, id, systemCode)}">
                        [@apph.messageEscape 'entity.indicators.modal.detail.query'/]
                    </a>
                </strong>
            </td>
        </tr>
        </tbody>
    </table>
</script>
<script>

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

</script>
[/@template.base]