(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['container'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression;


  buffer += "<div class=\"istac-widget-title\">\r\n    <a class=\"istac-widget-title-text\" href=\"#\" target=\"_blank\"></a>\r\n</div>\r\n<div class=\"istac-widget-body\">\r\n    <div class=\"istac-widget-content\">\r\n    </div>\r\n    <div class=\"istac-widget-body-embed\">\r\n        <textarea rows=\"10\"></textarea>\r\n        <a href=\"#\" class=\"hideEmbed\">Cerrar</a>\r\n    </div>\r\n    <div class=\"istac-widget-body-allIndicators\" style=\"display:none\">\r\n        <p class=\"istac-widget-embed\"><a href=\"#\" title=\"Incrustar widget\">&lt;/&gt;</a></p>\r\n        <img src=\"http://www.gobiernodecanarias.org/opencms8/export/system/modules/es.gobcan.istac.web/resources/images/maslight.gif\"><a target=\"_blank\" href=\"#\">más indicadores</a>\r\n    </div>\r\n    <div class=\"istac-widget-footer\">\r\n        <p class=\"istac-widget-credits\"><a target=\"_blank\" href=\"";
  foundHelper = helpers.widgetsTypeUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.widgetsTypeUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">Widget facilitado por ISTAC</a></p>\r\n    </div>\r\n</div>\r\n";
  return buffer;});

templates['embed'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  


  return "<div id=\"istac-widget\"></div>\r\n";});

templates['last-data-lateral'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, functionType="function", escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n        <div class=\"istac-widget-lateral-dataset\">\r\n            <div class=\"istac-widget-lateral-title\"><a href=\"";
  foundHelper = helpers.jaxiUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.jaxiUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.title;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.title; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</a></div>\r\n            ";
  stack1 = depth0.observations;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(2, program2, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n        </div>\r\n    ";
  return buffer;}
function program2(depth0,data) {
  
  var buffer = "", stack1;
  buffer += "\r\n                ";
  stack1 = depth0.hasValue;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.programWithDepth(program3, data, depth0)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n            ";
  return buffer;}
function program3(depth0,data,depth1) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n                    <div class=\"istac-widget-lateral-subtitle\">";
  stack1 = depth1.temporalLabel;
  stack1 = typeof stack1 === functionType ? stack1() : stack1;
  buffer += escapeExpression(stack1) + " - ";
  foundHelper = helpers.measure;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.measure; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + " (";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + ")</div>\r\n                    <div class=\"istac-widget-lateral-value\">";
  stack1 = depth0.showSparkline;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(4, program4, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += " ";
  foundHelper = helpers.value;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.value; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                ";
  return buffer;}
function program4(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "<span class=\"inlinesparkline\" data-time=\"";
  foundHelper = helpers.timeTitles;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.timeTitles; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\" data-unit=\"";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.values;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.values; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</span>";
  return buffer;}

  buffer += "<div class=\"istac-widget-lateral\">\r\n    ";
  stack1 = depth0.datasets;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(1, program1, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n</div>";
  return buffer;});

templates['last-data-table'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, functionType="function", escapeExpression=this.escapeExpression, self=this;

function program1(depth0,data) {
  
  var buffer = "";
  buffer += "\r\n                <th>";
  depth0 = typeof depth0 === functionType ? depth0() : depth0;
  buffer += escapeExpression(depth0) + "</th>\r\n            ";
  return buffer;}

function program3(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n            <tr data-tooltip=\"";
  foundHelper = helpers.description;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.description; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">\r\n                <td class=\"istac-widget-last-data-indicator-column\">\r\n                    <div class=\"istac-widget-lastData-title\"><a href=\"";
  foundHelper = helpers.jaxiUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.jaxiUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.title;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.title; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</a></div>\r\n                    <div class=\"istac-widget-unit\">";
  foundHelper = helpers.temporalLabel;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.temporalLabel; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                </td>\r\n                ";
  stack1 = depth0.observations;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(4, program4, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n            </tr>\r\n        ";
  return buffer;}
function program4(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "\r\n                <td>\r\n                    <div>\r\n                        ";
  stack1 = depth0.showSparkline;
  stack1 = helpers['if'].call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(5, program5, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n                        <span class=\"istact-widget-observation\">";
  foundHelper = helpers.value;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.value; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + " </span>\r\n                    </div>\r\n                    <div class=\"istact-widget-unit\"> ";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</div>\r\n                </td>\r\n                ";
  return buffer;}
function program5(depth0,data) {
  
  var buffer = "", stack1, foundHelper;
  buffer += "<span class=\"inlinesparkline\" data-time=\"";
  foundHelper = helpers.timeTitles;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.timeTitles; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\" data-unit=\"";
  foundHelper = helpers.unit;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.unit; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">";
  foundHelper = helpers.values;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.values; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "</span>";
  return buffer;}

  buffer += "<table>\r\n    <thead>\r\n        <tr>\r\n            <th class=\"istac-widget-last-data-indicator-column istac-widget-lastData-title\">Indicador</th>\r\n            ";
  stack1 = depth0.measures;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(1, program1, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n        </tr>\r\n    </thead>\r\n    <tbody>\r\n        ";
  stack1 = depth0.datasets;
  stack1 = helpers.each.call(depth0, stack1, {hash:{},inverse:self.noop,fn:self.program(3, program3, data)});
  if(stack1 || stack1 === 0) { buffer += stack1; }
  buffer += "\r\n    </tbody>\r\n</table>";
  return buffer;});

templates['uwaContainer'] = template(function (Handlebars,depth0,helpers,partials,data) {
  helpers = helpers || Handlebars.helpers;
  var buffer = "", stack1, foundHelper, functionType="function", escapeExpression=this.escapeExpression;


  buffer += "<div class=\"istac-widget-uwa-body\">\r\n    <div class=\"istac-widget-content\">\r\n    </div>\r\n    <div class=\"istac-widget-body-embed\">\r\n        <textarea rows=\"10\"></textarea>\r\n        <a href=\"#\" class=\"hideEmbed\">Cerrar</a>\r\n    </div>\r\n    <div class=\"istac-widget-footer\">\r\n        <p class=\"istac-widget-embed\"><a href=\"#\" title=\"Incrustar widget\">&lt;/&gt;</a></p>\r\n        <p class=\"istac-widget-credits\"><a href=\"";
  foundHelper = helpers.widgetsTypeUrl;
  if (foundHelper) { stack1 = foundHelper.call(depth0, {hash:{}}); }
  else { stack1 = depth0.widgetsTypeUrl; stack1 = typeof stack1 === functionType ? stack1() : stack1; }
  buffer += escapeExpression(stack1) + "\">Widget facilitado por ISTAC</a></p>\r\n    </div>\r\n</div>";
  return buffer;});
})();