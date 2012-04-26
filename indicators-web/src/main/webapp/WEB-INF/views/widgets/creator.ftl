[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="widget-creator">
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

            <div class="block">
                <h2>Sistema de indicadores</h2>
                <div id="widget-options-data-systems" class="block-content"></div>
            </div>

            <div class="block">
                <h2>Indicadores</h2>

                <div id="widget-options-data-indicators" class="block-content"></div>
            </div>

            <div class="block">
                <h2>Measures</h2>
                <div id="widget-options-measures" class="block-content"></div>
            </div>

            <div class="block">
                <h2>Valores espaciales</h2>
                <div id="widget-options-data-geographical-value" class="block-content"></div>
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

    <!-- Templates -->
    <script id="pagination" type="text/html">
        <div class="pagination">
            <ul>
                <% if (page > firstPage) { %>
                <li><a href="#" class="pagination-previous">&larr;</a></li>
                <% } else { %>
                <li class="disabled"><a href="#" class="pagination-previous">&larr;</a></li>
                <% }%>

                <li class="disabled"><a href="#" class="pagination-page"><%= page %> / <%= totalPages %></a></li>

                <% if (page < totalPages) { %>
                <li><a href="#" class="pagination-next">&rarr;</a></li>
                <% } else { %>
                <li class="disabled"><a href="#" class="pagination-next">&rarr;</a></li>
                <% } %>

            </ul>
        </div>
    </script>

    <script id="systemsItem" type="text/html">
        <a href="#" class="selectable-item item <%= selectedClass %>"><%= getLabel(item.title) %></a>
    </script>

    <!-- Template manager -->
    <script src="[@spring.url "/theme/js/TemplateManager.js"/]"></script>
    <script>
        var templateManager = new TemplateManager();
        templateManager.loadTemplatesInPage();

        //API CONTEXT TODO change to local API
        var apiContext = context + "/api/indicators/v1.0";
        //var apiContext = "http://localhost:8080/indicators-web/api/indicators/v1.0";
    </script>

    <!-- Modelos -->
    <script src="[@spring.url "/theme/js/models/widgets/IndicatorInstanceModel.js"/]"></script>
    <script src="[@spring.url "/theme/js/models/widgets/WidgetOptionsModel.js"/]"></script>

    <!-- Colecciones -->
    <script src="[@spring.url "/theme/js/collections/PaginatedCollection.js"/]"></script>
    <script src="[@spring.url "/theme/js/collections/widgets/GeographicalCollection.js"/]"></script>
    <script src="[@spring.url "/theme/js/collections/widgets/IndicatorsCollection.js"/]"></script>
    <script src="[@spring.url "/theme/js/collections/widgets/SystemsCollection.js"/]"></script>
    <script src="[@spring.url "/theme/js/collections/widgets/ToggleSelection.js"/]"></script>

    <!-- Vistas -->
    <script src="[@spring.url "/theme/js/views/PaginatedView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/PaginatedItemViewWithSelection.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/PaginatedViewWithSelection.js"/]"></script>


    <script src="[@spring.url "/theme/js/views/SelectionView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/GeographicalView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/IndicatorsView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/SystemsView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetCodeView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetOptionsView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetPreviewView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetView.js"/]"></script>


    <!-- Widget -->
    <script src="[@spring.url "/theme/js/widgets/widget.js"/]"></script>

    <script>
        var widgetView = new WidgetView();
        widgetView.render();
    </script>


</div>
[/@template.base]
