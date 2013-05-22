{
    "apiVersion" : "1.0",
    "swaggerVersion" : "1.1",
    "basePath" : "${appBaseUrl}/api/indicators/v1.0",
    "resourcePath" : "",
    "apis" : [
        {
            "path" : "/indicators/",
            "description" : "Indicadores",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Buscar indicadores",
                    "nickname" : "findIndicators",
                    "notes" : "<p>El parámetro <strong>q</strong> permite realizar consultas del tipo:</p><ul><li>q=subjectCode EQ \"EDUCACION\" AND geographicalValue EQ \"ES\"</li><li>q=id IN (\"CODE-1\", \"CODE-2\")</li><li>q=id EQ \"CODE-1\"</li></ul><br><p>El parámetro <strong>order</strong> permite orden por el campo <strong>update</strong>:</p><ul><li>order=update ASC</li></ul><br><p>El parámetro <strong>fields</strong> permite añadir más campos a la respuesta por defecto. Los campos disponibles son <strong>data</strong>, <strong>metadata</strong> y <strong>observationConf</strong></p>fields=+data,+metadata,+observationsMetadata",
                    "responseClass" : "IndicatorsPagination",
                    "parameters" : [
                        {
                            "name" : "q",
                            "description" : "Consulta",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "order",
                            "description" : "Orden",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "limit",
                            "description" : "Límite",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "offset",
                            "description" : "Desplazamiento",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "fields",
                            "description" : "Permite añadir más campos a la respuesta",
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
                    "nickname" : "findIndicator",
                    "responseClass" : "Indicator",
                    "parameters" : [
                        {
                            "name" : "indicatorCode",
                            "paramType" : "path",
                            "description" : "Código del indicador",
                            "dataType" : "string",
                            "required" : true
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicators/{indicatorCode}/data",
            "description" : "Datos de un indicador",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Datos de un indicador",
                    "nickname" : "findIndicator",
                    "notes" : "Los parámetros <strong>representation</strong> y <strong>granularity</strong> permiten filtrar los datos. El formato es el siguiente:<br><br> representation={dimension1}[{categoria1}|{categoria2}]:{dimension2}[{categoria3}|{categoria4}]<br>representation=MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]<br><br>granularity={dimension1}[{granularity1}|{granularity2}]:{dimension2}[{granularity3}]<br>granularity=TIME[YEARLY|MONTHLY]:GEOGRAPHICAL[COUNTRY]<br>",
                    "responseClass" : "Data",
                    "parameters" : [
                        {
                            "name" : "indicatorCode",
                            "paramType" : "path",
                            "description" : "Código del indicador",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "representation",
                            "description" : "Filtro de representación",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "granularity",
                            "description" : "Filtro de granularidad",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/",
            "description" : "Sistemas de indicadores",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Buscar sistemas de indicadores",
                    "nickname" : "findIndicatorsSystems",
                    "notes" : "",
                    "responseClass" : "IndicatorsSystemsPagination",
                    "parameters" : [
                        {
                            "name" : "limit",
                            "description" : "Límite",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "offset",
                            "description" : "Desplazamiento",
                            "paramType" : "query",
                            "dataType" : "int"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}",
            "description" : "Sistema de indicadores",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Sistema de indicadores",
                    "nickname" : "findIndicatorsSystem",
                    "notes" : "",
                    "responseClass" : "IndicatorSystem",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : "true"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}/indicatorInstances",
            "description" : "Buscar instancias de un sistema de indicadores",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Buscar instancias de un sistema de indicadores",
                    "nickname" : "retrieveIndicatorsInstances",
                    "notes" : "<p>El parámetro <strong>q</strong> permite realizar consultas del tipo:</p><ul><li>q=geographicalValue EQ \"ES\"</li><li>q=id EQ \"IndSys-1-v1-IInstance-1.111-CODE\"</li><li>q=id IN (\"IndSys-1-v1-IInstance-2.112-CODE\", \"IndSys-1-v1-IInstance-1.111-CODE\")</li></ul><br><p>El parámetro <strong>order</strong> permite orden por el campo <strong>update</strong>:</p><ul><li>order=update ASC</li></ul><br><p>El parámetro <strong>fields</strong> permite añadir más campos a la respuesta por defecto. Los campos disponibles son <strong>data</strong>, <strong>metadata</strong> y <strong>observationMetadata</strong></p>fields=+data,+metadata,+observationsMetadata",
                    "responseClass" : "InstancesPagination",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "q",
                            "description" : "Consulta",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "order",
                            "description" : "Orden",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "limit",
                            "description" : "Límite",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "offset",
                            "description" : "Desplazamiento",
                            "paramType" : "query",
                            "dataType" : "int"
                        },
                        {
                            "name" : "fields",
                            "description" : "Permite añadir más campos a la respuesta",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}/indicatorInstances/{indicatorInstanceCode}",
            "description" : "Instancia de un sistema de indicadores",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Instancia de un sistema de indicadores",
                    "nickname" : "retrieveIndicatorsInstance",
                    "notes" : "",
                    "responseClass" : "Instance",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "indicatorInstanceCode",
                            "description" : "Código de la instancia",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/indicatorsSystems/{indicatorSystemCode}/indicatorInstances/{indicatorInstanceCode}/data",
            "description" : "Datos de una instancias de indicadores",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Datos de una instancia",
                    "nickname" : "retrieveIndicatorsInstanceData",
                    "notes" : "Los parámetros <strong>representation</strong> y <strong>granularity</strong> permiten filtrar los datos. El formato es el siguiente:<br><br> representation={dimension1}[{categoria1}|{categoria2}]:{dimension2}[{categoria3}|{categoria4}]<br>representation=MOTIVOS_ESTANCIA[000|001|002]:ISLAS_DESTINO_PRINCIPAL[005|006]<br><br>granularity={dimension1}[{granularity1}|{granularity2}]:{dimension2}[{granularity3}]<br>granularity=TIME[YEARLY|MONTHLY]:GEOGRAPHICAL[COUNTRY]<br>",
                    "responseClass" : "Data",
                    "parameters" : [
                        {
                            "name" : "indicatorSystemCode",
                            "description" : "Código del sistema",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "indicatorInstanceCode",
                            "description" : "Código de la instancia",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        },
                        {
                            "name" : "representation",
                            "description" : "Filtro de representación",
                            "paramType" : "query",
                            "dataType" : "string"
                        },
                        {
                            "name" : "granularity",
                            "description" : "Filtro de granularidad",
                            "paramType" : "query",
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/geographicGranularities/",
            "description" : "Operaciones de granularidades geográficas",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Buscar granularidades geográficas",
                    "nickname" : "findGeographicGranularities",
                    "notes" : "",
                    "responseClass" : "List[GeographicalGranularity]",
                    "parameters" : []
                }
            ]
        },
        {
            "path" : "/geographicGranularities/{granularityCode}",
            "description" : "Operaciones de granularidade geográfica",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Granularidad geográfica",
                    "nickname" : "findGeographicGranularities",
                    "notes" : "",
                    "responseClass" : "GeographicalGranularity",
                    "parameters" : [
                        {
                            "name" : "granularityCode",
                            "description" : "Código de la granularidad",
                            "paramType" : "path",
                            "dataType" : "string",
                            "required" : true
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/geographicalValues/",
            "description" : "Operaciones de valores geográficos",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Buscar valores geográficos",
                    "notes" : "Es obligatorio especificar un <strong>subjectCode</strong> o un <strong>systemCode</strong>.",
                    "responseClass" : "List[GeographicalValue]",
                    "nickname" : "findGeographicalValues",
                    "parameters" : [
                        {
                            "name" : "subjectCode",
                            "description" : "Código del tema",
                            "paramType" : "query",
                            "required" : false,
                            "dataType" : "string"
                        },
                        {
                            "name" : "systemCode",
                            "description" : "Código del sistema de indicadores",
                            "paramType" : "query",
                            "required" : false,
                            "dataType" : "string"
                        },
                        {
                            "name" : "geographicalGranularityCode",
                            "description" : "Código de la granularidad geográfica",
                            "paramType" : "query",
                            "required" : true,
                            "dataType" : "string"
                        }
                    ]
                }
            ]
        },
        {
            "path" : "/subjects/",
            "description" : "Temas",
            "operations" : [
                {
                    "httpMethod" : "GET",
                    "summary" : "Buscar temas",
                    "nickname" : "findSubjects",
                    "notes" : "",
                    "responseClass" : "List[Subject]"
                }
            ]
        }
    ],
    "models" : {
        "InternationalString" : {
            "id" : "InternationalString",
            "properties" : {
                "{locale}" : {
                    "type" : "string"
                },
                "__default__" : {
                    "type" : "string"
                }
            }
        },
        "GeographicalValue" : {
            "id" : "GeographicalValue",
            "properties" : {
                "code" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "granularityCode" : {
                    "type" : "string"
                }
            }
        },
        "GeographicalGranularity" : {
            "id" : "GeographicalGranularity",
            "properties" : {
                "code" : {
                    "type"  : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                }
            }
        },
        "Subject" : {
            "id" : "Subject",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "code" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                }
            }
        },
        "IndicatorsPagination" : {
            "id" : "IndicatorsPagination",
            "properties" : {
                "kind" : {
                    "type" : "string"
                },
                "limit" : {
                    "type" : "string"
                },
                "offset" : {
                    "type" : "int"
                },
                "total" : {
                    "type" : "int"
                },
                "items" : {
                    "type" : "array",
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
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "code" : {
                    "type" : "string"
                },
                "version" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "acronym" : {
                    "type" : "InternationalString"
                },
                "subjectCode" : {
                    "type" : "string"
                },
                "subjectTitle" : {
                    "type" : "InternationalString"
                },
                "systemSurveyLinks" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "Link"
                    }
                },
                "quantity" : {
                    "type" : "Quantity"
                },
                "conceptDescription" : {
                    "type" : "InternationalString"
                },
                "notes" : {
                    "type" : "InternationalString"
                },
                "metadata" : {
                    "type" : "Metadata"
                },
                "data" : {
                    "type" : "Data"
                }
            }
        },
        "Link" : {
            "id" : "Link",
            "properties" : {
                "kind" : {
                    "type" : "string"
                },
                "href" : {
                    "type" : "string"
                }
            }
        },
        "Quantity" : {
            "id" : "Quantity",
            "properties" : {
                "type" : {
                    "type" : "string",
                    "allowallableValues" : {
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
                "unitSymbol" : {
                    "type" : "string"
                },
                "unitSymbolPosition" : {
                    "type" : "string",
                    "allowallableValues" : {
                        "valueType" : "List",
                        "values" : [
                            "START",
                            "END"
                        ]
                    }
                },
                "unitMultiplier" : {
                    "type" : "int"
                },
                "significantDigits" : {
                    "type" : "int"
                },
                "decimalPlaces" : {
                    "type" : "int"
                },
                "min" : {
                    "type" : "int"
                },
                "max" : {
                    "type" : "int"
                },
                "denominatorLink" : {
                    "type" : "Link"
                },
                "numeratorLink" : {
                    "type" : "Link"
                },
                "isPercentage" : {
                    "type" : "boolean"
                },
                "percentageOf" : {
                    "type" : "string"
                },
                "baseValue" : {
                    "type" : "int"
                },
                "baseTime" : {
                    "type" : "string"
                },
                "baseLocation" : {
                    "type" : "string"
                },
                "baseQuantityLink" : {
                    "type" : "Link"
                }
            }
        },
        "Metadata" : {
            "id" : "Metadata",
            "properties" : {
                "dimension" : {
                    "type" : "MetadataDimensionMap"
                },
                "attribute" : {
                    "type" : "MetadataAttributeMap"
                }
            }
        },
        "MetadataDimensionMap" : {
            "id" : "MetadataDimensionMap",
            "properties" : {
                "GEOGRAPHICAL" : {
                    "type" : "Dimension"
                },
                "TIME" : {
                    "type" : "Dimension"
                },
                "MEASURE" : {
                    "type" : "Dimension"
                }
            }
        },
        "Dimension" : {
            "id" : "Dimension",
            "properties" : {
                "code" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "granularity" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "Granularity"
                    }
                },
                "representation" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "Representation"
                    }
                }
            }
        },
        "Granularity" : {
            "id" : "Granularity",
            "properties" : {
                "code" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                }
            }
        },
        "Representation" : {
            "id" : "Representation",
            "properties" : {
                "code" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "string"
                },
                "latitude" : {
                    "type" : "double"
                },
                "longitude" : {
                    "type" : "double"
                },
                "granularityCode" : {
                    "type" : "string"
                }
            }
        },
        "MetadataAttributeMap" :{
            "id" : "MetadataAttributeMap",
            "properties" : {
                "UNIT_MEAS_DETAIL" : {
                    "type" : "Attribute"
                },
                "{attribute}" : {
                    "type" : "Attribute"
                }
            }
        },
        "Attribute" : {
            "id" : "Attribute",
            "properties" : {
                "code" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
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
                    }
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
                    }
                },
                "dimension" : {
                    "type" : "DataDimensionMap"
                },
                "observation" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "string"
                    }
                },
                "attribute" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "DataAttributeMap"
                    }
                }
            }
        },
        "DataDimensionMap" : {
            "id" : "DataDimensionMap",
            "properties" : {
                "{dimension}" : {
                    "type" : "DataDimension"
                }
            }
        },
        "DataDimension" : {
            "id" : "DataDimension",
            "properties" : {
                "representation"  : {
                    "type" : "DataDimensionRepresentation"
                }
            }
        },
        "DataDimensionRepresentation" : {
            "id" : "DataDimensionRepresentation",
             "properties" : {
                 "size" : {
                     "type" : "int"
                 },
                 "index" : {
                     "type" : "DataDimensionRepresentationIndexMap"
                 }
             }
        },
        "DataDimensionRepresentationIndexMap" : {
            "id" : "DataDimensionRepresentationIndexMap",
            "properties" : {
                "{category}" : {
                    "type" : "int"
                }
            }
        },
        "DataAttributeMap" : {
            "id" : "DataAttributeMap",
            "properties" : {
                "{attribute}" : {
                    "type" : "DataAttribute"
                }
            }
        },
        "DataAttribute" : {
            "id" : "DataAttribute",
            "properties" : {
                "code" : {
                    "type" : "string"
                },
                "value" : {
                    "type" : "InternationalString"
                }
            }
        },
        "Indicator": {
            "id" : "Indicator",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "code" : {
                    "type" : "string"
                },
                "version" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "acronym" : {
                    "type" : "InternationalString"
                },
                "subjectCode" : {
                    "type" : "string"
                },
                "subjectTitle" : {
                    "type" : "InternationalString"
                },
                "systemSurveyLinks" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "Link"
                    }
                },
                "quantity" : {
                    "type" : "Quantity"
                },
                "conceptDescription" : {
                    "type" : "InternationalString"
                },
                "notes" : {
                    "type" : "InternationalString"
                },
                "dimension" : {
                    "type" : "MetadataDimensionMap"
                },
                "attribute" : {
                    "type" : "MetadataAttributeMap"
                },
                "decimalPlaces" : {
                    "type" : "int"
                }
            }
        },
        "IndicatorsSystemsPagination" : {
            "id" : "IndicatorsSystemsPagination",
            "properties" : {
                "kind" : {
                    "type" : "string"
                },
                "limit" : {
                    "type" : "string"
                },
                "offset" : {
                    "type" : "int"
                },
                "total" : {
                    "type" : "int"
                },
                "items" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "IndicatorSystemBase"
                    }
                }
            }
        },
        "IndicatorSystemBase" : {
            "id" : "IndicatorSystemBase",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "code" : {
                    "type" : "string"
                },
                "version" : {
                    "type" : "string"
                },
                "publicationDate" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "acronym" : {
                    "type" : "InternationalString"
                },
                "statisticalOperationLink" : {
                    "type" : "Link"
                },
                "description" : {
                    "type" : "InternationalString"
                },
                "objective" : {
                    "type" : "InternationalString"
                }
            }
        },
        "IndicatorSystem" : {
            "id" : "IndicatorSystem",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "code" : {
                    "type" : "string"
                },
                "version" : {
                    "type" : "string"
                },
                "publicationDate" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "acronym" : {
                    "type" : "InternationalString"
                },
                "statisticalOperationLink" : {
                    "type" : "Link"
                },
                "description" : {
                    "type" : "InternationalString"
                },
                "objective" : {
                    "type" : "InternationalString"
                },
                "elements" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "ElementLevel"
                    }
                },
                "childLink" : {
                    "type" : "Link"
                }
            }
        },
        "ElementLevel" : {
            "id" : "ElementLevel",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "elements" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "ElementLevel"
                    }
                }
            }
        },
        "InstancesPagination" : {
            "id" : "IndicatorsSystemsPagination",
            "properties" : {
                "kind" : {
                    "type" : "string"
                },
                "limit" : {
                    "type" : "string"
                },
                "offset" : {
                    "type" : "int"
                },
                "total" : {
                    "type" : "int"
                },
                "items" : {
                    "type" : "array",
                    "items" : {
                        "$ref" : "InstanceBase"
                    }
                }
            }
        },
        "InstanceBase" : {
            "id" : "InstanceBase",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "parentLink": {
                    "type" : "Link"
                },
                "systemCode" : {
                    "type" : "string"
                },
                "title" : {
                    "type" : "InternationalString"
                },
                "conceptDescription" : {
                    "type" : "InternationalString"
                },
                "metadata" : {
                    "type" : "Metadata"
                },
                "data" : {
                    "type" : "Data"
                }
            }
        },
        "Instance" : {
            "id" : "Instance",
            "properties" : {
                "id" : {
                    "type" : "string"
                },
                "kind" : {
                    "type" : "string"
                },
                "selfLink" : {
                    "type" : "string"
                },
                "parentLink": {
                    "type" : "Link"
                },
                "systemCode" : {
                    "type" : "string",
                    "description" : "Código del sistema"
                },
                "title" : {
                    "type" : "InternationalString",
                    "description" : "Título"
                },
                "conceptDescription" : {
                    "type" : "InternationalString"
                },
                "dimension" : {
                    "type" : "MetadataDimensionMap"
                },
                "decimalPlaces" : {
                    "type" : "int"
                },
                "childLink" : {
                    "type" : "Link"
                }
            }
        }
    }
}