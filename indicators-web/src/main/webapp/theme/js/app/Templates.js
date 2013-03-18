(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['code'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression;


  buffer += "<div class=\"widget-export\">\r\n    <p>Selecciona, copia y pega este código en tu página</p>\r\n\r\n    <textarea id=\"code-container\" readonly=\"readonly\">\r\n<div id=\"istac-widget\"></div>\r\n<script src=\"";
  foundHelper = helpers.script;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.script; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\"></script>\r\n<script>\r\n    new IstacWidget(";
  foundHelper = helpers.code;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.code; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + ");\r\n</script>\r\n    </textarea>\r\n\r\n    <p>\r\n        <a target=\"_blank\" href=\"";
  foundHelper = helpers.context;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.context; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "/widgets/example?";
  foundHelper = helpers.parameters;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.parameters; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">Ejemplo de uso</a>\r\n    </p>\r\n    <p class=\"widget-import-netvibes\">\r\n        <a target=\"_blank\" href=\"";
  foundHelper = helpers.context;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.context; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "/widgets/uwa?";
  foundHelper = helpers.uwaParameters;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.uwaParameters; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\" class=\"widget-import-netvibes-link\" href=\"#\"><img src=\"";
  foundHelper = helpers.context;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.context; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "/theme/images/netvibes.png\"> Importar a Netvibes</a>\r\n    </p>\r\n\r\n</div>\r\n";
  return buffer;});

templates['data-options-lastData'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  


  return "<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Medidas</div>\r\n    <div class=\"widget-data-measures\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Sistema o Tema</div>\r\n\r\n    <label class=\"radio\">\r\n        <input type=\"radio\" name=\"groupType\" value=\"system\"> Sistema\r\n    </label>\r\n    <label class=\"radio\">\r\n        <input type=\"radio\" name=\"groupType\" value=\"subject\"> Tema\r\n    </label>\r\n\r\n\r\n    <div class=\"widget-data-system\"></div>\r\n    <div class=\"widget-data-subject\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Valores espaciales</div>\r\n    <div class=\"widget-data-geographicalGranularities\"></div>\r\n    <div class=\"widget-data-geographicalValues\"></div>\r\n</div>\r\n\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Indicadores</div>\r\n    <div class=\"widget-data-instances\"></div>\r\n    <div class=\"widget-data-indicators\"></div>\r\n</div>\r\n\r\n<button class=\"widget-update-preview\">Actualizar vista previa</button>\r\n\r\n\r\n";});

templates['data-options-recent'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  


  return "<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Medidas</div>\r\n    <div class=\"widget-data-measures\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Sistema o Tema</div>\r\n\r\n    <label class=\"radio\">\r\n        <input type=\"radio\" name=\"groupType\" value=\"system\"> Sistema\r\n    </label>\r\n    <label class=\"radio\">\r\n        <input type=\"radio\" name=\"groupType\" value=\"subject\"> Tema\r\n    </label>\r\n\r\n    <div class=\"widget-data-system\"></div>\r\n    <div class=\"widget-data-subject\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Valores espaciales</div>\r\n    <div class=\"widget-data-geographicalGranularities\"></div>\r\n    <div class=\"widget-data-geographicalValues\"></div>\r\n</div>\r\n\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Número de indicadores recientes</div>\r\n    <div class=\"widget-data-recent\">\r\n        <input type=\"text\" name=\"nrecent\">\r\n    </div>\r\n</div>\r\n\r\n<button class=\"widget-update-preview\">Actualizar vista previa</button>\r\n\r\n\r\n";});

templates['data-options-temporal'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  


  return "<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Medidas</div>\r\n    <div class=\"widget-data-measures\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Sistema</div>\r\n    <div class=\"widget-data-system\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Indicadores</div>\r\n    <div class=\"widget-data-instances\"></div>\r\n</div>\r\n\r\n<div class=\"control-group\">\r\n    <div class=\"widget-data-title\">Valores espaciales</div>\r\n    <div class=\"widget-data-geographicalValues\"></div>\r\n</div>\r\n\r\n<button class=\"widget-update-preview\">Actualizar vista previa</button>\r\n\r\n\r\n";});

templates['pagination'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, self=this, functionType="function", escapeExpression=this.escapeExpression;

function program1(depth0,data) {
  
  
  return "\r\n            <li class=\"disabled\"><a href=\"#\" class=\"pagination-previous\">&larr;</a></li>\r\n        ";}

function program3(depth0,data) {
  
  
  return "\r\n            <li><a href=\"#\" class=\"pagination-previous\">&larr;</a></li>\r\n        ";}

function program5(depth0,data) {
  
  
  return "\r\n            <li class=\"disabled\"><a href=\"#\" class=\"pagination-next\">&rarr;</a></li>\r\n        ";}

function program7(depth0,data) {
  
  
  return "\r\n            <li><a href=\"#\" class=\"pagination-next\">&rarr;</a></li>\r\n        ";}

  buffer += "<div class=\"pagination\">\r\n    <ul>\r\n        ";
  stack1 = depth0.isFirstPage;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.program(3, program3, data),fn:self.program(1, program1, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n\r\n        <li class=\"disabled\"><a href=\"#\" class=\"pagination-page\">";
  foundHelper = helpers.page;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.page; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + " / ";
  foundHelper = helpers.totalPages;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.totalPages; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</a></li>\r\n\r\n        ";
  stack1 = depth0.isLastPage;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.program(7, program7, data),fn:self.program(5, program5, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n\r\n    </ul>\r\n</div>\r\n";
  return buffer;});

templates['style-options'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, self=this;

function program1(depth0,data) {
  
  
  return "\r\n            <div>\r\n                <label>Color del texto:</label>\r\n                <input type=\"text\" name=\"textColor\" id=\"textColor\" class=\"small\"/>\r\n            </div>\r\n\r\n            <div>\r\n                <label>Color de la cabecera:</label>\r\n                <input type=\"text\" name=\"headerColor\" id=\"headerColor\" class=\"small\">\r\n            </div>\r\n\r\n            <div>\r\n                <label>Color del borde:</label>\r\n                <input type=\"text\" name=\"borderColor\" id=\"borderColor\" class=\"small\">\r\n            </div>\r\n\r\n            <div>\r\n                <label>Ancho del widget<small>(px)</small>:</label>\r\n                <input type=\"text\" name=\"width\" id=\"widget-width\" class=\"small\"/>\r\n            </div>\r\n            <div class=\"width-slider\"></div>\r\n\r\n            <div>\r\n                <label>\r\n                    Bordes redondeados\r\n                </label>\r\n                <input type=\"checkbox\" name=\"borderRadius\">\r\n            </div>\r\n\r\n            <div>\r\n                <label>\r\n                    Sombra\r\n                </label>\r\n                <input type=\"checkbox\" name=\"shadow\">\r\n            </div>\r\n        ";}

function program3(depth0,data) {
  
  
  return "\r\n        <div>\r\n            <label class=\"radio\">\r\n                <input type=\"radio\" name=\"gobcanStyleColor\" value=\"blue\"> Azul\r\n            </label>\r\n            <label class=\"radio\">\r\n                <input type=\"radio\" name=\"gobcanStyleColor\" value=\"green\"> Verde\r\n            </label>\r\n        </div>\r\n        ";}

function program5(depth0,data) {
  
  
  return "\r\n            <div>\r\n                <label>Visualización lateral</label>\r\n                <input type=\"checkbox\" name=\"sideView\">\r\n            </div>\r\n        ";}

function program7(depth0,data) {
  
  
  return "\r\n            <div>\r\n                <label>Mostrar etiquetas en el eje x</label>\r\n                <input type=\"checkbox\" name=\"showLabels\">\r\n            </div>\r\n            <div>\r\n                <label>Mostrar leyenda</label>\r\n                <input type=\"checkbox\" name=\"showLegend\">\r\n            </div>\r\n        ";}

function program9(depth0,data) {
  
  
  return "\r\n        <fieldset>\r\n            <legend>Sparklines</legend>\r\n            <label class=\"checkbox\">\r\n                <input type=\"checkbox\" name=\"sparkline_ABSOLUTE\">Dato\r\n            </label>\r\n            <label class=\"checkbox\">\r\n                <input type=\"checkbox\" name=\"sparkline_ANNUAL_PERCENTAGE_RATE\">Variación anual\r\n            </label>\r\n            <label class=\"checkbox\">\r\n                <input type=\"checkbox\" name=\"sparkline_ANNUAL_PUNTUAL_RATE\">Variación interperiódica\r\n            </label>\r\n            <label class=\"checkbox\">\r\n                <input type=\"checkbox\" name=\"sparkline_INTERPERIOD_PERCENTAGE_RATE\">Tasa variación anual\r\n            </label>\r\n            <label class=\"checkbox\">\r\n                <input type=\"checkbox\" name=\"sparkline_INTERPERIOD_PUNTUAL_RATE\">Variación interperiódica\r\n            </label>\r\n        </fieldset>\r\n    ";}

  buffer += "<div>\r\n    <div>\r\n        <label>Título:</label>\r\n        <input type=\"text\" name=\"title\"/>\r\n    </div>\r\n\r\n    <fieldset>\r\n        <legend>Estilo</legend>\r\n\r\n        <label class=\"radio\">\r\n            <input type=\"radio\" name=\"style\" value=\"custom\"> Personalizado\r\n        </label>\r\n        <label class=\"radio\">\r\n            <input type=\"radio\" name=\"style\" value=\"gobcan\"> Gobierno de Canarias\r\n        </label>\r\n\r\n        ";
  stack1 = depth0.showCustomStyle;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.program(3, program3, data),fn:self.program(1, program1, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n    </fieldset>\r\n\r\n\r\n\r\n    <fieldset>\r\n        <legend>Visualización</legend>\r\n        ";
  stack1 = depth0.showSideView;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(5, program5, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n        ";
  stack1 = depth0.showAxisAndLegend;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(7, program7, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n    </fieldset>\r\n\r\n    ";
  stack1 = depth0.showSparkLines;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(9, program9, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n\r\n</div>\r\n";
  return buffer;});

templates['systems-item'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression, helperMissing=helpers.helperMissing;


  buffer += "<a href=\"#\" class=\"selectable-item item ";
  foundHelper = helpers.selectedClass;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.selectedClass; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  stack1 = depth0.item;
  stack1 = stack1 == null || stack1 === false ? stack1 : stack1.title;
  foundHelper = helpers.getLabel;
  stack1 = foundHelper ? foundHelper.call(depth0, stack1, {hash:{}}) : helperMissing.call(depth0, "getLabel", stack1, {hash:{}});
  buffer += escapeExpression(stack1) + "</a>\r\n";
  return buffer;});

templates['type-options'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression;


  buffer += "<div class=\"widget-type-container\">\r\n\r\n    <div class=\"col2 widget-type ";
  foundHelper = helpers.lastDataClass;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.lastDataClass; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">\r\n        <a href=\"#\" data-type=\"lastData\">\r\n            <div class=\"widget-type-title\">Últimos datos</div>\r\n            <p>Tabla que muestra los últimos datos disponibles</p>\r\n        </a>\r\n    </div>\r\n\r\n\r\n    <div class=\"col2 widget-type ";
  foundHelper = helpers.temporalClass;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.temporalClass; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">\r\n        <a href=\"#\" data-type=\"temporal\">\r\n            <div class=\"widget-type-title\">Serie temporal</div>\r\n            <p>Gráfica que muestra la evolución temporal de un indicador para diferentes valores geográficos</p>\r\n        </a>\r\n    </div>\r\n    <div class=\"clearfix\"></div>\r\n</div>\r\n";
  return buffer;});
})();