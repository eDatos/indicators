{
    "apiVersion" : "1.0",
    "swaggerVersion" : "1.1",
    "basePath" : "${appBaseUrl}/v1.0",
    "resourcePath" : "",
    "apis" : [
        {
            "path" : "/indicators",
            "description" : "Listado de indicadores.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Listado de indicadores",
                    "nickname" : "findIndicators",
                    "notes" : "<p>Esta petición aporta la lista de indicadores publicados en el banco de datos ISTAC-indicadores. Un indicador es una medida utilizada para conocer la intensidad de un fenómeno en el espacio-tiempo. Esa medida se puede referir a distintas granularidades espaciales o temporales. Por ejemplo \"Tasa de paro\" es un indicador con diversas granularidades espaciales (insular, provincial y autonómica) y con el trimestre como una única granulariad temporal.</p><br>",
                    "responseClass" : "IndicatorsPagination",
                    "parameters" : [
                        {
                            "name" : "q",
                            "description" : "Consulta. Los metadatos sobre los que se pueden construir las búsquedas son: \"id\", \"subjectCode\" y \"geographicalValue\".<br> Ejemplos: <br>q=id EQ \"PARO_REGISTRADO\" <br> q=subjectCode EQ \"EDUCACION\" AND geographicalValue EQ \"ES\" <br> q=id IN (\"CODE-1\", \"CODE-2\").",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "order",
                            "description" : "Orden. Los posibles valores son \"update\" e \"id\" y los criterios de orden \"asc\" y \"desc\". <br> Ejemplo: order=update asc.",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "limit",
                            "description" : "Límite de resultados (número máximo). <br> Ejemplo: limit=10.",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "offset",
                            "description" : "Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. <br> Ejemplo: offset=2.",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "fields",
                            "description" : "Permite personalizar la respuesta mediante la adición de nuevos campos. Los posibles valores son: \"+metadata\", \"+data\" y \"+observationsMetadata\". <br> Ejemplo: fields=+metadata.",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "representation",
                            "description" : "Permite filtrar las observaciones mediante el valor de las mismas. Su uso sólo tiene sentido cuando se ha incluído \"+data\" y/o \"+observationsMetadata\". <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicators/{indicatorCode}",
            "description" : "Indicador",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Indicador",
                    "notes" : "<p>Un indicador es una medida utilizada para conocer la intensidad de un fenómeno en el espacio-tiempo. Esa medida se puede referir a distintas granularidades espaciales, p.e. islas y municipios, o temporales, p.e. años y meses. A través de esta petición se ofrecen los metadatos que describen las características de un indicador específico, pemitiendo la compresión del hecho medido; asimismo a través de la petición data se aportan los datos completos (para todos los espacio-tiempo) del indicador.</p><br>",
                    "nickname" : "findIndicator",
                    "responseClass" : "Indicator",
                    "parameters" : [
                        {
                            "name" : "indicatorCode",
                            "paramType" : "path",
                            "description" : "Código del indicador a obtener.",
                            "dataType" : "string",
                            "required" : true
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicators/{indicatorCode}/data",
            "description" : "Observaciones de un indicador.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Observaciones de un indicador",
                    "nickname" : "findIndicator",
                    "notes" : "<p>Un indicador es una medida utilizada para conocer la intensidad de un fenómeno en el espacio-tiempo. Esa medida se puede referir a distintas granularidades espaciales, p.e. islas y municipios, o temporales, p.e. años y meses.  A través de la petición data se aportan los datos completos (para todos los espacio-tiempo) del indicador. Por otra parte a través de la petición metadata se ofrecen los metadatos que describen las características de un indicador específico, pemitiendo la compresión del hecho medido.</p><br>",
                    "responseClass" : "Data",
                    "parameters" : [
                        {
                            "name" : "indicatorCode",
                            "paramType" : "path",
                            "description" : "Código del indicador a obtener.",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "representation",
                            "description" : "Permite filtrar las observaciones mediante el valor de las mismas. <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "granularity",
                            "description" : "Permite filtrar las observaciones mediante las granularidades de las mismas. <br> Ejemplo: granularirty=GEOGRAPHICAL[MUNICIPALITIES|PROVINCES],TIME[MONTHLY].",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "fields",
                            "description" : "Permite personalizar la respuesta mediante la exclusión de campos. Los posibles valores son: \"-observationsMetadata\". <br> Ejemplo: fields=-observationsMetadata.",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems",
            "description" : "Listado de sistemas de indicadores.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Listado de sistemas de indicadores",
                    "nickname" : "findIndicatorsSystems",
                    "notes" : "<p>Esta petición aporta la lista de sistemas de indicadores publicados en el banco de datos ISTAC-indicadores. Los indicadores son estadísticas simples o compuestas, sin embargo un solo indicador rara vez puede proporcionar información útil acerca de fenómenos complejos tales como la coyuntura económica, las condiciones de vida, la escolarización u otros. Los sistemas de indicadores generalmente están diseñados para generar más y más precisa información acerca de las condiciones de un fenómeno; y para ello se organizan en dimensiones o áreas de análisis, bajo las cuáles se integran los indicadores. En el ISTAC un sistema de indicadores se trata como una operación estadística, por lo que se puede consultar más información del sistema en la API de operaciones.</p><br>",
                    "responseClass" : "IndicatorsSystemsPagination",
                    "parameters" : [
                        {
                            "name" : "limit",
                            "description" : "Límite de resultados (número máximo). <br> Ejemplo: limit=10.",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "offset",
                            "description" : "Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. <br> Ejemplo: offset=2.",
                            "paramType" : "query",
                            "dataType" : "int"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}",
            "description" : "Sistema de indicadores.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Sistema de indicadores",
                    "nickname" : "findIndicatorsSystem",
                    "notes" : "<p>Esta petición ofrece los metadatos de un sistema de indicadores publicado en el banco de datos ISTAC-indicadores. Los indicadores son estadísticas simples o compuestas, sin embargo un solo indicador rara vez puede proporcionar información útil acerca de fenómenos complejos tales como la coyuntura económica, las condiciones de vida, la escolarización u otros. Los sistemas de indicadores generalmente están diseñados para generar más y más precisa información acerca de las condiciones de un fenómeno; y para ello se organizan en dimensiones o áreas de análisis, bajo las cuales se integran los indicadores.  En el ISTAC un sistema de indicadores se trata como una operación estadística, por lo que se puede consultar más información del sistema en la API de operaciones.</p><br>",
                    "responseClass" : "IndicatorSystem",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema a obtener.",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : "true"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances",
            "description" : "Instancias de sistema de indicadores.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Instancias de sistema de indicadores",
                    "nickname" : "retrieveIndicatorsInstances",
                    "notes" : "<p>Esta petición devuelve las instacias de indicadores asociadas a un sistema de indicadores especifico. Una instancia de un indicador no es más que una consulta espacio-temporal de un indicador a la hora de incorporarlo a un sistema de indicadores concreto. Por ejemplo, el indicador Paro registrado se incorpora al sistema Anuario de Indicadores Municipales como una consulta (instancia de indicador) a través de la cual se seleccionan los datos municipales y anuales de dicho indicador. </p><br>",
                    "responseClass" : "InstancesPagination",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema a obtener.",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "q",
                            "description" : "Consulta. Los metadatos sobre los que se pueden construir las búsquedas son: \"id\" y \"geographicalValue\".<br> Ejemplos: <br>q=id EQ \"INDICADORES_MUNICIPALES\" <br> q=geographicalValue EQ \"ES\" <br> q=id IN (\"CODE-1\", \"CODE-2\").",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "order",
                            "description" : "Orden. Los posibles valores son \"update\" e \"id\" y los criterios de orden \"asc\" y \"desc\". <br> Ejemplo: order=update asc.",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "limit",
                            "description" : "Límite de resultados (número máximo). <br> Ejemplo: limit=10.",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "offset",
                            "description" : "Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. <br> Ejemplo: offset=2.",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "fields",
                            "description" : "Permite personalizar la respuesta mediante la adición de nuevos campos. Los posibles valores son: \"+metadata\", \"+data\" y \"+observationsMetadata\". <br> Ejemplo: fields=+metadata.",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "representation",
                            "description" : "Permite filtrar las observaciones mediante el valor de las mismas. Su uso sólo tiene sentido cuando se ha incluído \"+data\" y/o \"+observationsMetadata\". <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "granularity",
                            "description" : "Permite filtrar las observaciones mediante las granularidades de las mismas. Su uso sólo tiene sentido cuando se ha incluído \"+data\" y/o \"+observationsMetadata\". <br> Ejemplo: granularirty=GEOGRAPHICAL[MUNICIPALITIES|PROVINCES],TIME[MONTHLY].",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances/{indicatorInstanceCode}",
            "description" : "Instancia de un sistema de indicadores.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Instancia de un sistema de indicadores",
                    "nickname" : "retrieveIndicatorsInstance",
                    "notes" : "<p>Esta petición devuelve los meteadatos de una instacia de indicadores asociada a un sistema de indicadores especifico. Una instancia de un indicador no es más que una consulta espacio-temporal de un indicador a la hora de incorporarlo a un sistema de indicadores concreto. Por ejemplo, el indicador Paro registrado se incorpora al sistema Anuario de Indicadores Municipales como una consulta (instancia de indicador) a través de la cual se seleccionan los datos municipales y anuales de dicho indicador. </p><br>",
                    "responseClass" : "Instance",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema a obtener.",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "indicatorInstanceCode",
                            "description" : "Código de la instancia a obtener.",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances/{indicatorInstanceCode}/data",
            "description" : "Observaciones de instancia de indicador",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Observaciones instancia de indicador",
                    "nickname" : "retrieveIndicatorsInstanceData",
                    "notes" : "<p>Esta petición devuelve los datos de una instacia de indicadores asociada a un sistema de indicadores especifico. Una instancia de un indicador no es más que una consulta espacio-temporal de un indicador a la hora de incorporarlo a un sistema de indicadores concreto. Por ejemplo, el indicador Paro registrado se incorpora al sistema Anuario de Indicadores Municipales como una consulta (instancia de indicador) a través de la cual se seleccionan los datos municipales y anuales de dicho indicador. </p><br>",
                    "responseClass" : "Data",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema a obtener.",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "indicatorInstanceCode",
                            "description" : "Código de la instancia a obtener.",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "representation",
                            "description" : "Permite filtrar las observaciones mediante el valor de las mismas. <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "granularity",
                            "description" : "Permite filtrar las observaciones mediante las granularidades de las mismas. <br> Ejemplo: granularirty=GEOGRAPHICAL[MUNICIPALITIES|PROVINCES],TIME[MONTHLY].",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "fields",
                            "description" : "Permite personalizar la respuesta mediante la exclusión de campos. Los posibles valores son: \"-observationsMetadata\". <br> Ejemplo: fields=-observationsMetadata.",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/timeGranularities",
            "description" : "Granularidades temporales",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Granularidades temporales",
                    "nickname" : "retrieveTimeGranularities",
                    "notes" : "<p>Esta petición devuelve la lista de granularidades temporales tratadas en el banco de datos ISTAC-indicadores ordenadas de mayor a menor granularidad. Por ejemplo granularidad anual, trimestral o mensual.</p><br>",
                    "responseClass" : "TimeGranularitiesList"
                }
            ]
        },
        {
            "path" : "/geographicGranularities",
            "description" : "Granularidades geográficas.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Granularidades geográficas",
                    "nickname" : "findGeographicGranularities",
                    "notes" : "<p>Esta petición devuelve la lista de granularidades geográficas tratadas en el banco de datos ISTAC-indicadores. Por ejemplo granularidad provincial, insular o municipal.</p><br>",
                    "responseClass" : "GeographicalGranularityList"
                }
            ]
        },
        {
            "path" : "/geographicalValues",
            "description" : "Valores geográficos.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Valores geográficos",
                    "notes" : "<p>Esta petición devuelve los valores de una granularidad geográfica que a su vez forman parte de una un tema o un sistema de indicadores específicos.</p><br><p>Es importante tener en cuenta que de los parátemeros opcionales (<strong>subjectCode</strong> y <strong>systemCode</strong>) podrá estar cumplimentado uno de ellos o ninguno. En caso de que estén cumplimentados ambos, sólo se tendrá en cuenta el último.</p> <br>",
                    "responseClass" : "GeographicalValueList",
                    "nickname" : "findGeographicalValues",
                    "parameters" : [
                        {
                            "name" : "subjectCode",
                            "description" : "Código del tema del que se desean obtener los valores geográficos.",
                            "paramType" : "query",
                            "required" : false,
                            "dataType" : "string"
                        },
                        {
                            "name" : "systemCode",
                            "description" : "Código del sistema de indicadores del que se desean obtener los valores geográficos.",
                            "paramType" : "query",
                            "required" : false,
                            "dataType" : "string"
                        },
                        {
                            "name" : "geographicalGranularityCode",
                            "description" : "Código de la granularidad geográfica.",
                            "paramType" : "query",
                            "required" : true,
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/subjects",
            "description" : "Temas estadísticos.",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Temas estadísticos",
                    "nickname" : "findSubjects",
                    "notes" : "<p>Esta petición devuelve los temas en los que el ISTAC clasifica sus operaciones estadísticas.</p><br>",
                    "responseClass" : "SubjectList"
                }
            ]
        }
    ],
    "models" : {
        "InternationalString" : {
            "id" : "InternationalString",
            "properties" : {
                "{locale}" : {
                    "type" : "string",
                    "description" : "Traducción en el locale especificado."
                },
                "__default__" : {
                    "type" : "string",
                    "description" : "Traducción en el idioma por defecto."
                }
            }
        },
        "GeographicalValueList" : {
            "id" : "GeographicalValueList",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "total" : {
                    "type" : "int",
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "items" : {
                    "type" : "array",
                    "description" : "Listado de recursos.",
                    "items" : {
                        "$ref" : "GeographicalValue"
                    }
                }
            }
        },
        "GeographicalValue" : {
            "id" : "GeographicalValue",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código del valor geográfico."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del valor geográfico."
                },
                "granularityCode" : {
                    "type" : "string",
                    "description" : "Granularidad del valor geográfico."
                },
                "latitude" : {
                    "type" : "double",
                    "description" : "Latitud del recinto geográfico."
                },
                "longitude" : {
                    "type" : "double",
                    "description" : "Longitud del recinto geográfico."
                }
            }
        },
        "TimeGranularitiesList" : {
            "id" : "TimeGranularitiesList",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "total" : {
                    "type" : "int",
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "items" : {
                    "type" : "array",
                    "description" : "Listado de recursos.",
                    "items" : {
                        "$ref" : "TimeGranularity"
                    }
                }
            }
        },
        "TimeGranularity" : {
            "id" : "TimeGranularity",
            "properties" : {
                "code" : {
                    "type"  : "string",
                    "description" : "Código de la granularidad temporal"
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre la granularidad temporal"
                }
            }
        },
        "GeographicalGranularityList" : {
            "id" : "GeographicalGranularityList",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "total" : {
                    "type" : "int",
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "items" : {
                    "type" : "array",
                    "description" : "Listado de recursos.",
                    "items" : {
                        "$ref" : "GeographicalGranularity"
                    }
                }
            }
        },
        "GeographicalGranularity" : {
            "id" : "GeographicalGranularity",
            "properties" : {
                "code" : {
                    "type"  : "string",
                    "description" : "Código de la granularidad geográfica."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre la granularidad geográfica."
                }
            }
        },
        "SubjectList" : {
            "id" : "SubjectList",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "total" : {
                    "type" : "int",
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "items" : {
                    "type" : "array",
                    "description" : "Listado de recursos.",
                    "items" : {
                        "$ref" : "Subject"
                    }
                }
            }
        },
        "Subject" : {
            "id" : "Subject",
            "properties" : {
                "id" : {
                    "type" : "string",
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "code" : {
                    "type" : "string",
                    "description" : "Código semántico del recurso."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del tema estadístico."
                }
            }
        },
        "IndicatorsPagination" : {
            "id" : "IndicatorsPagination",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "limit" : {
                    "type" : "string",
                    "description" : "Número máximo de resultados a obtener."
                },
                "offset" : {
                    "type" : "int",
                    "description" : "Desplazamiento. Número a partir del cual se comienzan a obtener los resultados."
                },
                "total" : {
                    "type" : "int",
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso. Dado un resultado nos permite saber cómo realizar la petición a la API para volver a obtenerlo"
                },
                "firstLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la primera página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última."
                },
                "previousLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página anterior a la que nos encontramos. Si no se muestra es porque no existe siguiente."
                },
                "nextLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página siguiente a la que nos encontramos. Si no se muestra es porque no existe siguiente."
                },
                "lastLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la última página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última."
                },
                "items" : {
                    "type" : "array",
                    "description" : "Listado de recursos.",
                    "items" : {
                        "$ref" : "IndicatorBase"
                    }
                }
            }
        },
        "IndicatorBase" : {
            "id" : "IndicatorBase",
            "properties" : {
                "id" : {
                    "type" : "string",
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "code" : {
                    "type" : "string",
                    "description" : "Código semántico del recurso."
                },
                "version" : {
                    "type" : "string",
                    "description" : "Versión del recurso."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del recurso."
                },
                "acronym" : {
                    "type" : "InternationalString",
                    "description" : "Acrónimo del indicador, por ejemplo para el  Índice de Precios Industriales sería (IPRI)."
                },
                "subjectCode" : {
                    "type" : "string",
                    "description" : "Código del área temática, según clasificación ISTAC, en el que se cataloga el indicador."
                },
                "subjectTitle" : {
                    "type" : "InternationalString",
                    "description" : "Etiqueta del área temática, según clasificación ISTAC, en el que se cataloga el indicador."
                },
                "systemSurveyLinks" : {
                    "type" : "array",
                    "description" : "Sistemas de indicadores relacionados con el indicador.",
                    "items" : {
                        "$ref" : "Link"
                    }
                },
                "conceptDescription" : {
                    "type" : "InternationalString",
                    "description" : "Descripción del indicador."
                },
                "notes" : {
                    "type" : "InternationalString",
                    "description" : "Notas explicativas sobre el indicador."
                },
                "metadata" : {
                    "type" : "Metadata",
                    "description" : "Metadatos de un indicador."
                },
                "data" : {
                    "type" : "Data",
                    "description" : "Datos (observaciones) de un indicador."
                }
            }
        },
        "Link" : {
            "id" : "Link",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "href" : {
                    "type" : "string",
                    "description" : "Enlace al recurso."
                }
            }
        },
        "Metadata" : {
            "id" : "Metadata",
            "properties" : {
                "dimension" : {
                    "type" : "MetadataDimensionMap",
                    "description" : "Dimensiones de los datos."
                },
                "attribute" : {
                    "type" : "MetadataAttributeMap",
                    "description" : "Atributos de los datos."
                }
            }
        },
        "MetadataDimensionMap" : {
            "id" : "MetadataDimensionMap",
            "properties" : {
                "GEOGRAPHICAL" : {
                    "type" : "GeographicalDimension",
                    "description" : "Dimensión geográfica de los datos."
                },
                "TIME" : {
                    "type" : "TimeDimension",
                    "description" : "Dimensión temporal de los datos."
                },
                "MEASURE" : {
                    "type" : "MeasureDimension",
                    "description" : "Dimensión de medida de los datos."
                }
            }
        },

        "GeographicalDimension" : {
            "id" : "GeographicalDimension",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código de la dimensión."
                },
                "granularity" : {
                    "type" : "array",
                    "description" : "Granularidades que intervienen en la dimensión.",                   
                    "items" : {
                        "$ref" : "Granularity"
                    }
                },
                "representation" : {
                    "type" : "array",
                    "description" : "Representación de la dimensión. Indica como se codifica la dimensión.",
                    "items" : {
                        "$ref" : "GeographicalRepresentation"
                    }
                }
            }
        },
        "TimeDimension" : {
                "id" : "TimeDimension",
                "properties" : {
                    "code" : {
                        "type" : "string",
                        "description" : "Código de la dimensión."
                    },
                    "granularity" : {
                        "type" : "array",
                        "description" : "Granularidades que intervienen en la dimensión.",
                        "items" : {
                            "$ref" : "Granularity"
                        }
                    },
                    "representation" : {
                        "type" : "array",
                        "description" : "Representación de la dimensión. Indica como se codifica la dimensión.",
                        "items" : {
                            "$ref" : "TimeRepresentation"
                        }
                    }
                }
        },
        "MeasureDimension" : {
            "id" : "MeasureDimension",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código de la dimensión."
                },
                "representation" : {
                    "type" : "array",
                    "description" : "Representación de la dimensión. Indica como se codifica la dimensión.",
                    "items" : {
                        "$ref" : "MeasureRepresentation"
                    }
                }
            }
        },
        "Granularity" : {
            "id" : "Granularity",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código de la granularidad."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre de la granularidad."
                }
            }
        },
        "GeographicalRepresentation" : {
            "id" : "GeographicalRepresentation",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código del recinto geográfico."
                },
                "title" : {
                    "type" : "string",
                    "description" : "Nombre del recinto geográfico."
                },
                "latitude" : {
                    "type" : "double",
                    "description" : "Latitud del recinto geográfico."
                },
                "longitude" : {
                    "type" : "double",
                    "description" : "Longitud del recinto geográfico."
                },
                "granularityCode" : {
                    "type" : "string",
                    "description" : "Granularidad del recinto geográfico."
                }
            }
        },
        "TimeRepresentation" : {
            "id" : "TimeRepresentation",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código del momento de tiempo."
                },
                "title" : {
                    "type" : "string",
                    "description" : "Nombre del momento de tiempo."
                },
                "granularityCode" : {
                    "type" : "string",
                    "description" : "Granularidad del momento de tiempo."
                }
            }
        },
        "MeasureRepresentation" : {
            "id" : "MeasureRepresentation",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código de la medida."
                },
                "title" : {
                    "type" : "string",
                    "description" : "Nombre de la medida."
                },
                "quantity" : {
                    "type" : "Quantity",
                    "description" : "Metadatos relacionados con la medida."
                }
            }
        },
        "Quantity" : {
            "id" : "Quantity",
            "properties" : {
                "type" : {
                    "type" : "string",
                    "description" : "Existen diferentes tipos de medición (type) y asociados a cada una de ellos, diferentes metadatos que especifican mejor esa medida: medición (quantity), cantidad (amount), magnitud (magnitude), fracción (fraction),  ratio (ratio), tasa (rate), indice (index), tasa de variación (changerate). Se ha usado la clasificación utilizada por Google, en su estándar Dataset Publishing Language (DSPL).",
                    "allowableValues" : {
                        "valueType" : "List",
                        "values" : [
                            "QUANTITY",
                            "AMOUNT",
                            "MAGNITUDE",
                            "FRACTION",
                            "RATIO",
                            "RATE",
                            "INDEX",
                            "CHANGE_RATE"
                        ]
                    }
                },
                "unit" : {
                    "type" : "InternationalString",
                    "description" : "Define la unidad de medida de un indicador. Una unidad de medida es una cantidad estandarizada de una determinada magnitud, definida y adoptada por convención o por ley, por ejemplo: unidades física (kg, mm, °C, °F) o unidades monetarias (euros, pesetas, dólares). Cualquier valor de una cantidad física puede expresarse como un múltiplo de la unidad de medida. Una unidad de medida toma su valor a partir de un patrón o de una composición de otras unidades definidas previamente. Las primeras unidades se conocen como unidades básicas o de base (fundamentales), mientras que las segundas se llaman unidades derivadas. <br>También consideramos como unidades de medida las especificaciones de simples recuentos (por ejemplo personas, vehículos, viviendas)."
                },
                "unitSymbol" : {
                    "type" : "string",
                    "description" : "Símbolo asociado a la unidad de medida, por ejemplo \"km\" para kilómetros. <br>Metadato asociado a todos los tipos de medida."
                },
                "unitSymbolPosition" : {
                    "type" : "string",
                    "description" : "Posición del símbolo de la unidad de medida, que puede ser delante o detrás de la cifra. <br>Metadato asociado a todos los tipos de medida.",
                    "allowableValues" : {
                        "valueType" : "List",
                        "values" : [
                            "START",
                            "END"
                        ]
                    }
                },
                "unitMultiplier" : {
                    "type" : "InternationalString",
                    "description" : "Literal que especifica el multiplicador de la unidad de medida. Por ejemplo \"Miles de personas\". Metadato asociado a todos los tipos de medida."
                },
                "significantDigits" : {
                    "type" : "int",
                    "description" : "Especifica el número de dígitos significativos para utilizar cuando se muestran los valores de un concepto métrico."
                },
                "decimalPlaces" : {
                    "type" : "int",
                    "description" : "Especifica el número de decimales a utilizar para la visualización de los valores de un concepto métrico."
                },
                "min" : {
                    "type" : "int",
                    "description" : "Especifica el valor mínimo que puede tomar la magnitud. En el el caso de una magnitud referenciada en una escala de intervalo (o de intensidad) indica el valor inferior de la escala. Por ejemplo, para el Indice de Desarrollo Humano; min = 0. Metadato asociado a los siguientes tipos de medida: magnitude, fraction, ratio, rate, index y changerate."
                },
                "max" : {
                    "type" : "int",
                    "description" : "Especifica el valor máximo que puede tomar la magnitud. En el el caso de una magnitud referenciada en una escala de intervalo (o de intensidad) indica el valor superior de la escala. Por ejemplo, para el Indice de Desarrollo Humano; max = 1. Metadato asociado a los siguientes tipos de medida: magnitude, fraction, ratio, rate, index y changerate."
                },
                "denominatorLink" : {
                    "type" : "Link",
                    "description" : "Otro indicador del sistema que es el denominador del indicador tipo proporción. Por ejemplo, para el indicador \"tasa de paro\" sería el indicador \"población activa\". Metadato asociado a los siguientes tipos de medida: fraction, ratio, rate, index y changerate."
                },
                "numeratorLink" : {
                    "type" : "Link",
                    "description" : "Otro indicador del sistema que es el numerador del indicador tipo proporción. Por ejemplo, para el indicador \"tasa de paro\" sería el indicador \"población parada\". Metadato asociado a los siguientes tipos de medida: fraction, ratio, rate, index y changerate."
                },
                "isPercentage" : {
                    "type" : "boolean",
                    "description" : "Atributo booleano que toma el valor TRUE si una fracción es un porcentaje y FALSE en caso contrario. Metadato asociado a los siguientes tipos de medida: ratio, rate, index y changerate."
                },
                "percentageOf" : {
                    "type" : "InternationalString",
                    "description" : "Si estamos ante un porcentaje, este atributo ofrece un texto descriptivo  que puede utilizarse para mostrarse junto al número y especifica de qué se trata dicho porcentaje. Por ejemplo, la métrica \"tasa de desempleo\" puede tener un atributo PercentageOf  \"% de la población activa\" para que el valor se lea como \"10,5% de la población activa\". Metadato asociado a los siguientes tipos de medida: ratio, rate, index y changerate."
                },
                "baseValue" : {
                    "type" : "int",
                    "description" : "Valor de referencia de un índice (index), generalmente baseValue = 100. Metadato asociado al tipo de medida index."
                },
                "baseTime" : {
                    "type" : "TimeRepresentation",
                    "description" : "Base temporal de un indicador tipo índice (index) temporal. Por ejemplo, 2010. Metadato asociado al tipo de medida index."
                },
                "baseLocation" : {
                    "type" : "GeographicalRepresentation",
                    "description" : "Base espacial para un indicador tipo índice (index) espacial o temporal. El ejemplo más característico de índices espaciales son las Paridades de Poder Adquisitivo (PPA) que comparan los niveles de precios entre territorios, siendo el territorio base la referencia 100. Por ejemplo las PPA de Canarias comparan los niveles de precios entre islas, siendo el territorio base Canarias. Metadato asociado al tipo de medida index."
                },
                "baseQuantityLink" : {
                    "type" : "Link",
                    "description" : "Referencia al indicador sobre el que se calcula una tasa de variación. Metadato asociado al tipo de medida changerate."
                }

            }
        },
        "MetadataAttributeMap" :{
            "id" : "MetadataAttributeMap",
            "properties" : {
                "{attribute}" : {
                    "type" : "Attribute",
                    "description" : "Atributo de los datos."
                }
            }
        },
        "Attribute" : {
            "id" : "Attribute",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código del atributo."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del atributo."
                },
                "attachmentLevel" : {
                    "type" : "string",
                    "allowableValues":{
                        "valueType":"LIST",
                        "values":[
                            "DATASET",
                            "DIMENSION",
                            "OBSERVATION"
                        ]
                    },
                    "description" : "Nivel de los datos al que afecta el atributo."
                }
            }
        },
        "Data" : {
            "id" : "Data",
            "properties" : {
                "format" : {
                    "type": "array",
                    "items":{
                        "$ref":"string"
                    },
                    "description" : "Orden en el que se deuvelven las dimensiones."
                },
                "dimension" : {
                    "type" : "DataDimensionMap",
                    "description" : "Dimensiones que identifican los datos."
                },
                "observation" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "string"
                    },
                    "description" : "Array de observaciones. Para obtener una observación específica lo que se debe hacer es multiplicar el índice de cada código de dimensión deseado y obtendremos la posición del array de observaciones en la que se encuentra la observación deseada. <br> Ejemplo: En un dataset de tres dimensiones con los siguientes valores SEXO(M,F), LUGAR(A,B) y EDAD(1,2). Para obtener el valor de la observación(M,B,2) tendremos que hacer la operación 1*2*2=4. Luego la observación deseada será la que ocupa la cuarta posición en el array de observaciones."
                },
                "attribute" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "DataAttributeMap"
                    },
                    "description" : "Atributos que describen los datos."
                }
            }
        },
        "DataDimensionMap" : {
            "id" : "DataDimensionMap",
            "properties" : {
                "{dimension}" : {
                    "type" : "DataDimension",
                    "description" : "Dimensión que identifica a los datos."
                }
            }
        },
        "DataDimension" : {
            "id" : "DataDimension",
            "properties" : {
                "representation"  : {
                    "type" : "DataDimensionRepresentation",
                    "description" : "Representación de la dimensión. Incluye los posibles valores que toma la dimensión."
                }
            }
        },
        "DataDimensionRepresentation" : {
            "id" : "DataDimensionRepresentation",
            "properties" : {
                "size" : {
                    "type" : "int",
                    "description" : "Número de posibles valores de la dimensión."
                },
                "index" : {
                    "type" : "DataDimensionRepresentationIndexMap",
                    "description" : "Índice que se le asigna a cada una de las dimensiones."
                }
            }
        },
        "DataDimensionRepresentationIndexMap" : {
            "id" : "DataDimensionRepresentationIndexMap",
            "properties" : {
                "{category}" : {
                    "type" : "int",
                    "description" : "Índice del código de la dimensión indicado."
                }
            }
        },
        "DataAttributeMap" : {
            "id" : "DataAttributeMap",
            "properties" : {
                "{attribute}" : {
                    "type" : "DataAttribute",
                    "description" : "Atributo que describe a los datos."
                }
            }
        },
        "DataAttribute" : {
            "id" : "DataAttribute",
            "properties" : {
                "code" : {
                    "type" : "string",
                    "description" : "Código del atributo."
                },
                "value" : {
                    "type" : "InternationalString",
                    "description" : "Valor que toma el atributo."
                }
            }
        },

        "Indicator": {
            "id" : "Indicator",
            "properties" : {
                "id" : {
                    "type" : "string",
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "code" : {
                    "type" : "string",
                    "description" : "Código semántico del recurso."
                },
                "version" : {
                    "type" : "string",
                    "description" : "Versión del recurso."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del recurso."
                },
                "acronym" : {
                    "type" : "InternationalString",
                    "description" : "Acrónimo del indicador, por ejemplo para el  Índice de Precios Industriales sería (IPRI)."
                },
                "subjectCode" : {
                    "type" : "string",
                    "description" : "Código del área temática, según clasificación ISTAC, en el que se cataloga el indicador."
                },
                "subjectTitle" : {
                    "type" : "InternationalString",
                    "description" : "Etiqueta del área temática, según clasificación ISTAC, en el que se cataloga el indicador."
                },
                "systemSurveyLinks" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "Link"
                    },
                    "description" : "Sistemas de indicadores relacionados con el indicador."
                },
                "conceptDescription" : {
                    "type" : "InternationalString",
                    "description" : "Descripción del indicador."
                },
                "notes" : {
                    "type" : "InternationalString",
                    "description" : "Notas explicativas sobre el indicador."
                },
                "dimension" : {
                    "type" : "MetadataDimensionMap",
                    "description" : "Dimensiones que identifican los datos del indicador."
                },
                "attribute" : {
                    "type" : "MetadataAttributeMap",
                    "description" : "Atributos que describen los datos del indicador."
                },
                "decimalPlaces" : {
                    "type" : "int",
                    "description" : "Especifica el número de decimales a utilizar para la visualización de los valores de un concepto métrico."
                },
                "childLink" : {
                    "type" : "Link",
                    "description" : "Recurso de la API al que se puede acceder desde el recurso actual."
                }
                
            }
        },

        "IndicatorsSystemsPagination" : {
            "id" : "IndicatorsSystemsPagination",
            "properties" : {
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "limit" : {
                    "type" : "string",
                    "description" : "Número máximo de resultados a obtener."
                },
                "offset" : {
                    "type" : "int",
                    "description" : "Desplazamiento. Número a partir del cual se comienzan a obtener los resultados."
                },
                "total" : {
                    "type" : "int",
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso. Dado un resultado nos permite saber cómo realizar la petición a la API para volver a obtenerlo"
                },
                "firstLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la primera página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última."
                },
                "previousLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página anterior a la que nos encontramos. Si no se muestra es porque no existe siguiente."
                },
                "nextLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página siguiente a la que nos encontramos. Si no se muestra es porque no existe siguiente."
                },
                "lastLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la última página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última."
                },
                "items" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "IndicatorSystemBase"
                    },
                    "description" : "Listado de recursos."
                }
            }
        },
        "IndicatorSystemBase" : {
            "id" : "IndicatorSystemBase",
            "properties" : {
                "id" : {
                    "type" : "string",
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "code" : {
                    "type" : "string",
                    "description" : "Código semántico del recurso."
                },
                "version" : {
                    "type" : "string",
                    "description" : "Versión del recurso."
                },
                "publicationDate" : {
                    "type" : "string",
                    "description" : "Fecha de publicación del sistema de indicadores."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del recurso."
                },
                "acronym" : {
                    "type" : "InternationalString",
                    "description" : "Acrónimo del sistema de indicadores."
                },
                "statisticalOperationLink" : {
                    "type" : "Link",
                    "description" : "Enlace a la documentación del sistema de indicadores en la API de operaciones estadísticas."
                },
                "description" : {
                    "type" : "InternationalString",
                    "description" : "Descripción del sistema de indicadores."
                },
                "objective" : {
                    "type" : "InternationalString",
                    "description" : "Objetivo del sistema de indicadores."
                }
            }
        },
        "IndicatorSystem" : {
            "id" : "IndicatorSystem",
            "properties" : {
                "id" : {
                    "type" : "string",
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "code" : {
                    "type" : "string",
                    "description" : "Código semántico del recurso."
                },
                "version" : {
                    "type" : "string",
                    "description" : "Versión del recurso."
                },
                "publicationDate" : {
                    "type" : "string",
                    "description" : "Fecha de publicación del sistema de indicadores."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del recurso."
                },
                "acronym" : {
                    "type" : "InternationalString",
                    "description" : "Acrónimo del sistema de indicadores."
                },
                "statisticalOperationLink" : {
                    "type" : "Link",
                    "description" : "Enlace a la documentación del sistema de indicadores en la API de operaciones estadísticas."
                },
                "description" : {
                    "type" : "InternationalString",
                    "description" : "Descripción del sistema de indicadores."
                },
                "objective" : {
                    "type" : "InternationalString",
                    "description" : "Objetivo del sistema de indicadores."
                },
                "elements" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "ElementLevel"
                    },
                    "description" : "Estructura del sistema de indicadores. La estructura se compone de elementos de tipo \"dimensión\" y elementos de tipo \"instancias de indicador\". "
                },
                "childLink" : {
                    "type" : "Link",
                    "description" : "Recurso de la API al que se puede acceder desde el recurso actual."
                }
            }
        },

        "ElementLevel" : {
            "id" : "ElementLevel",
            "properties" : {
                "id" : {
                    "type" : "string",
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string",
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso."
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Nombre del recurso."
                },
                "elements" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "ElementLevel"
                    },
                    "description" : "En caso de tratarse de un elemento de tipo \"dimensión\", este elemento podrá contener otros sub-elementos."
                }
            }
        },
        "InstancesPagination" : {
            "id" : "IndicatorsSystemsPagination",
            "properties" : {
                "kind" : {
                    "type" : "string", 
                    "description" : "Tipo del recurso."
                },
                "limit" : {
                    "type" : "string", 
                    "description" : "Número máximo de resultados a obtener."
                },
                "offset" : {
                    "type" : "int", 
                    "description" : "Desplazamiento. Número a partir del cual se comienzan a obtener los resultados."
                },
                "total" : {
                    "type" : "int", 
                    "description" : "Número total de resultados existentes."
                },
                "selfLink" : {
                    "type" : "string",
                    "description" : "Enlace al propio recurso. Dado un resultado nos permite saber cómo realizar la petición a la API para volver a obtenerlo"
                },
                "firstLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la primera página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última."
                },
                "previousLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página anterior a la que nos encontramos. Si no se muestra es porque no existe siguiente."
                },
                "nextLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página siguiente a la que nos encontramos. Si no se muestra es porque no existe siguiente."
                },
                "lastLink" : {
                    "type" : "string",
                    "description" : "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la última página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última."
                },
                "items" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "InstanceBase"
                    }, 
                    "description" : "Listado de recursos."
                }
            }
        },
        "InstanceBase" : {
            "id" : "InstanceBase",
            "properties" : {
                "id" : {
                    "type" : "string", 
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string", 
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string", 
                    "description" : "Enlace al propio recurso."
                },
                "parentLink": {
                    "type" : "Link", 
                    "description" : "Enlace al recurso padre de la API."
                },
                "systemCode" : {
                    "type" : "string", 
                    "description" : "Código del sistema de indicadores del que forma parte la instacia de un indicador."
                },
                "title" : {
                    "type" : "InternationalString", 
                    "description" : "Nombre del recurso."
                },
                "conceptDescription" : {
                    "type" : "InternationalString", 
                    "description" : "Descripción del indicador con el que se asocia la instancia de indicador."
                },
                "metadata" : {
                    "type" : "Metadata", 
                    "description" : "Metadatos de la instanacia de indicador."
                },
                "data" : {
                    "type" : "Data", 
                    "description" : "Datos de la instancia de indicador."
                }
            }
        },
        "Instance" : {
            "id" : "Instance",
            "properties" : {
                "id" : {
                    "type" : "string", 
                    "description" : "Identificador del recurso."
                },
                "kind" : {
                    "type" : "string", 
                    "description" : "Tipo del recurso."
                },
                "selfLink" : {
                    "type" : "string", 
                    "description" : "Enlace al propio recurso."
                },
                "parentLink": {
                    "type" : "Link", 
                    "description" : "Enlace al recurso padre de la API."
                },
                "systemCode" : {
                    "type" : "string", 
                    "description" : "Código del sistema de indicadores del que forma parte la instacia de un indicador."
                },
                "title" : {
                    "type" : "InternationalString", 
                    "description" : "Nombre del recurso."
                },
                "conceptDescription" : {
                    "type" : "InternationalString", 
                    "description" : "Descripción de la instancia de indicador."
                },
                "subjectCode" : {
                    "type" : "string", 
                    "description" : "Código del área temática, según clasificación ISTAC, en el que se cataloga la instancia de indicador."
                },
                "subjectTitle" : {
                    "type" : "InternationalString", 
                    "description" : "Etiqueta del área temática, según clasificación ISTAC, en el que se cataloga la instancia de indicador."
                },
                "dimension" : {
                    "type" : "MetadataDimensionMap", 
                    "description" : "Dimensiones de identifican a la instancia de indicador."
                },
                "decimalPlaces" : {
                    "type" : "int", 
                    "description" : "Especifica el número de decimales a utilizar para la visualización de los valores de un concepto métrico."
                },
                "childLink" : {
                    "type" : "Link", 
                    "description" : "Recurso de la API al que se puede acceder desde el recurso actual."
                }
            }
        }
    }
}