[#ftl]
[#include "/inc/includes.ftl"]
[@template.base]

<div id="widget-creator">


    <div id="widget-preview-content" class="widget"></div>

    <div class="tabbable" id="widget-options-tabs">
        <ul class="nav-tabs">
            <li class="active" ><a href="#type" >Tipo</a></li>
            <li><a href="#data">Datos</a></li>
            <li><a href="#style">Estilos</a></li>
            <li><a href="#export">Exportar</a></li>
        </ul>

        <div class="clearfix"></div>
        <div class="tab-content"></div>
        <div class="clearfix"></div>
    </div>

    <!-- Templates -->

    <script id="typeOptionsTmpl" type="text/html">
        <div class="widget-type-container">

            <div class="col2 widget-type <% if(type === 'lastData'){ %>widget-type-active<% }%>">
                <a href="#" data-type="lastData">
                    <div class="widget-type-title">Últimos datos</div>
                    <p>Tabla que muestra los últimos datos disponibles</p>
                </a>
            </div>


            <div class="col2 widget-type <% if(type === 'temporal'){ %>widget-type-active<% }%>">
                <a href="#" data-type="temporal">
                    <div class="widget-type-title">Serie temporal</div>
                    <p>Gráfica que muestra la evolución temporal de un indicador para diferentes valores geográficos</p>
                </a>
            </div>
            <div class="clearfix"></div>
        </div>
    </script>

    <script id="dataOptionsTmpl" type="text/html">
        <div class="clearfix">
            <div class="widget-data-title">Medidas</div>
            <div class="measures"></div>
        </div>

        <div class="clearfix">
            <div class="col3">
                <div>
                    <div class="widget-data-title">Sistema de indicadores</div>
                    <div class="selectionable-items clearfix">Puede seleccionar varios elementos</div>
                    <div class="systems"></div>
                </div>
            </div>

            <div class="col3">
                <div>
                    <div class="widget-data-title">Indicadores</div>
                    <div class="indicators"></div>
                </div>
            </div>

            <div class="col3">
                <div>
                    <div class="widget-data-title">Valores espaciales</div>
                    <div class="geographicalValues"></div>
                </div>
            </div>
        </div>
     </script>

    <script id="styleOptionsTmpl" type="text/html">
        <div>
            <div>
                <label>Color del texto:</label>
                <input type="text" name="text-color" id="text-color"/>
            </div>
            <div>
                <label>Color del marco del widget:</label>
                <input type="text" name="border-color" id="border-color">
            </div>
            <div>
                <label>Ancho del widget<small>(px)</small>:</label>
                <input type="text" name="width" id="widget-width"/>
            </div>
            <div class="width-slider"></div>
            <div>
                <label>Título del widget:</label>
                <input type="text" name="title"/>
            </div>
            <% if(isTemporal) { %>
                <div>
                    <label>
                        Mostrar etiquetas en el eje x
                    </label>
                    <input type="checkbox" name="showLabels">
                </div>
                <div>
                    <label>
                        Mostrar leyenda
                    </label>
                    <input type="checkbox" name="showLegend">
                </div>
            <% } %>
        </div>
    </script>

    <script id="codeTmpl" type="text/html">
        <div class="widget-export">
            <p>Copia este código HTML en tu página</p>
            <textarea id="code-container" readonly="readonly"><%= code %></textarea>
        </div>
    </script>

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

        var apiBaseUrl = "[@spring.url ""/]";
        var apiContext = apiBaseUrl + "/api/indicators/v1.0/";
        var jaxiUrl = "${jaxiUrlBase}";
    </script>

    <script type="text/javascript" src="[@spring.url "/theme/js/libs/underscore.string.min-v.2.0.0-57.js"/]"></script>
    
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
    <script src="[@spring.url "/theme/js/views/widgets/WidgetPreviewView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/TabView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetStyleOptionsView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetDataOptionsView.js"/]"></script>
    <script src="[@spring.url "/theme/js/views/widgets/WidgetTypeOptionsView.js"/]"></script>

    <!-- Widget -->
    <script src="[@spring.url "/theme/js/widgets/widget.js"/]"></script>

    <script>
        var widgetView = new WidgetView();
        widgetView.render();
    </script>


</div>
[/@template.base]
