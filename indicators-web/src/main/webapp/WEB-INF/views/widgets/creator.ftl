[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="widget-options">
    <div class="widget-options-type">
        <h3>Selección del tipo de widget</h3>
        <div>
            <label>
                Tipo de widget:
                <select id="widget-options-data-type">
                    <option value="lastData" selected>Últimos datos</option>
                    <option value="temporal">Serie temporal</option>
                </select>
            </label>
        </div>
    </div>

    <div class="widget-options-style">
        <h3>Opciones de estilo</h3>

        <div><label>Color del texto: <input type="text" id="widget-options-style-text-color"/></label></div>
        <div><label>Color del marco del widget: <input type="text" id="widget-options-style-border-color"></label>
        </div>
        <div><label>Ancho del widget
            <small>(px)</small>
            : <input type="text" id="widget-options-style-width"/></label></div>
        <div id="widget-options-style-width-slider"></div>

        <div><label>Título del widget: <input type="text" id="widget-options-style-title"/></label></div>
    </div>

    <div class="widget-options-data">
        <h3>Opciones de datos</h3>

        <div>Selección del sistema de indicadores</div>
        <div>Selección del indicador (que está en el sistema) que se desea visualizar</div>
        <div>Selección de la granularidad temporal a mostrar</div>
        <div>
            <h4>Selección del único dato a visualizar. A elegir entre: absoluto, variación interanual o variación
                interperiódica.</h4>
            <label>
                <input type="checkbox" id="widget-options-data-visibleDataAbsolute" /> Absoluto
            </label>
            <label>
                <input type="checkbox" id="widget-options-data-visibleDataInteranual" /> Variación interanual
            </label>
            <label>
                <input type="checkbox" id="widget-options-data-visibleDataInterperiodic" /> Variación interperiódica
            </label>
        </div>
    </div>
</div>

<div id="widget-preview">
    <h3>Vista previa</h3>
    <div id="widget-preview-content" class="widget"></div>
    <h3>Exportar</h3>
    <div class="widget-export">
        <textarea id="code-container" readonly="readonly"></textarea>
        <p>Copia este código HTML en tu página</p>
    </div>
</div>

<script src="[@spring.url "/theme/js/widgets/libs/Class.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/libs/raphael-min.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/libs/elycharts.js"/]"></script>

<script src="[@spring.url "/theme/js/widgets/Base.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/LastData.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/Temporal.js"/]"></script>
<script src="[@spring.url "/theme/js/widgets/Factory.js"/]"></script>

<script src="[@spring.url "/theme/js/modules/widgets/creator.js"/]"></script>

<script>

    var mockIndicators = [];
    for (var i = 0; i < 10; i++) {
        mockIndicators[i] = {
            name : 'indicator ' + i,
            data : {
                absolute : i * 10,
                interanual : i * 20,
                interperiodic : i * 30
            }
        };
    }

    var model = new WidgetOptionsModel({indicators : mockIndicators});
    var optionsView = new WidgetOptionsView({el : $('#widget-options'), model : model});
    var widgetView = new WidgetView({el : $('#widget-preview-content'), model : model});
    var codeView = new WidgetCodeView({el : $('#code-container'), model : model});

</script>


[/@template.base]