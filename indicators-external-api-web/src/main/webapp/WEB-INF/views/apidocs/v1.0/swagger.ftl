{
  "basePath" : "${appBaseUrl}/v1.0/docs",
  "definitions": {
    "Attribute": {
      "properties": {
        "attachmentLevel": {
          "description": "Nivel de los datos al que afecta el atributo.",
          "type": "string"
        },
        "code": {
          "description": "Código del atributo.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del atributo."
        }
      }
    },
    "Data": {
      "properties": {
        "attribute": {
          "description": "Atributos que describen los datos.",
          "items": {
            "$ref": "#/definitions/DataAttributeMap"
          },
          "type": "array"
        },
        "dimension": {
          "$ref": "#/definitions/DataDimensionMap",
          "description": "Dimensiones que identifican los datos."
        },
        "format": {
          "description": "Orden en el que se deuvelven las dimensiones.",
          "items": {
            "type": "string"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "observation": {
          "description": "Array de observaciones. Las observaciones se encuentran ordenadas por la combinación de las categorías manteniendo fijada siempre la primera categoría de la primera dimensión e iterando sobre las categorías de la última dimensión del array. Por ejemplo, dadas las dimensiones A, B y C con 3, 2, y 4 categorías respectivamente, los valores estarán ordenados de tal manera que primero se itere sobre las 4 categorías de C, posteriormente sobre las dos de B y por último sobre las 3 de A. En dicho ejemplo, el resultado sería el siguiente: A1B1C1, A1B1C2, A1B1C3, A1B1C4, A1B2C1, A1B2C2, A1B2C3, A1B2C4, A2B1C1, A2B1C2, A2B1C3, A1B1C4, A2B2C1, A2B2C2, A2B2C3, A2B2C4, A3B1C1, A3B1C2, A3B1C3, A3B1C4, A3B2C1, A3B2C2, A3B2C3, A3B2C4",
          "items": {
            "type": "string"
          },
          "type": "array"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace al recurso padre de la API."
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        }
      }
    },
    "DataAttribute": {
      "properties": {
        "code": {
          "description": "Código del atributo.",
          "type": "string"
        },
        "value": {
          "$ref": "#/definitions/InternationalString",
          "description": "Valor que toma el atributo."
        }
      }
    },
    "DataAttributeMap": {
      "properties": {
        "{attribute}": {
          "$ref": "#/definitions/DataAttribute",
          "description": "Atributo que describe a los datos."
        }
      }
    },
    "DataDimension": {
      "properties": {
        "representation": {
          "$ref": "#/definitions/DataDimensionRepresentation",
          "description": "Representación de la dimensión. Incluye los posibles valores que toma la dimensión."
        }
      }
    },
    "DataDimensionMap": {
      "properties": {
        "{dimension}": {
          "$ref": "#/definitions/DataDimension",
          "description": "Dimensión que identifica a los datos."
        }
      }
    },
    "DataDimensionRepresentation": {
      "properties": {
        "index": {
          "$ref": "#/definitions/DataDimensionRepresentationIndexMap",
          "description": "Índice que se le asigna a cada una de las dimensiones."
        },
        "size": {
          "description": "Número de posibles valores de la dimensión.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "DataDimensionRepresentationIndexMap": {
      "properties": {
        "{category}": {
          "description": "Índice del código de la dimensión indicado.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "ElementLevel": {
      "properties": {
        "elements": {
          "description": "En caso de tratarse de un elemento de tipo \"dimensión\", este elemento podrá contener otros sub-elementos.",
          "items": {
            "$ref": "#/definitions/ElementLevel"
          },
          "type": "array"
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        }
      }
    },
    "GeographicalDimension": {
      "properties": {
        "code": {
          "description": "Código de la dimensión.",
          "type": "string"
        },
        "granularity": {
          "description": "Granularidades que intervienen en la dimensión.",
          "items": {
            "$ref": "#/definitions/Granularity"
          },
          "type": "array"
        },
        "representation": {
          "description": "Representación de la dimensión. Indica como se codifica la dimensión.",
          "items": {
            "$ref": "#/definitions/GeographicalRepresentation"
          },
          "type": "array"
        }
      }
    },
    "GeographicalGranularity": {
      "properties": {
        "code": {
          "description": "Código de la granularidad geográfica.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre la granularidad geográfica."
        }
      }
    },
    "GeographicalGranularityList": {
      "properties": {
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/GeographicalGranularity"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "GeographicalRepresentation": {
      "properties": {
        "code": {
          "description": "Código del recinto geográfico.",
          "type": "string"
        },
        "granularityCode": {
          "description": "Granularidad del recinto geográfico.",
          "type": "string"
        },
        "latitude": {
          "description": "Latitud del recinto geográfico.",
          "format": "double",
          "type": "number"
        },
        "longitude": {
          "description": "Longitud del recinto geográfico.",
          "format": "double",
          "type": "number"
        },
        "title": {
          "description": "Nombre del recinto geográfico.",
          "type": "string"
        }
      }
    },
    "GeographicalValue": {
      "properties": {
        "code": {
          "description": "Código del valor geográfico.",
          "type": "string"
        },
        "granularityCode": {
          "description": "Granularidad del valor geográfico.",
          "type": "string"
        },
        "latitude": {
          "description": "Latitud del recinto geográfico.",
          "format": "double",
          "type": "number"
        },
        "longitude": {
          "description": "Longitud del recinto geográfico.",
          "format": "double",
          "type": "number"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del valor geográfico."
        }
      }
    },
    "GeographicalValueList": {
      "properties": {
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/GeographicalValue"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "Granularity": {
      "properties": {
        "code": {
          "description": "Código de la granularidad.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre de la granularidad."
        }
      }
    },
    "Indicator": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "Acrónimo del indicador, por ejemplo para el  Índice de Precios Industriales sería (IPRI)."
        },
        "attribute": {
          "$ref": "#/definitions/MetadataAttributeMap",
          "description": "Atributos que describen los datos del indicador."
        },
        "childLink": {
          "$ref": "#/definitions/Link",
          "description": "Recurso de la API al que se puede acceder desde el recurso actual."
        },
        "code": {
          "description": "Código semántico del recurso.",
          "type": "string"
        },
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "Descripción del indicador."
        },
        "decimalPlaces": {
          "description": "Especifica el número de decimales a utilizar para la visualización de los valores de un concepto métrico.",
          "format": "int32",
          "type": "integer"
        },
        "dimension": {
          "$ref": "#/definitions/MetadataDimensionMap",
          "description": "Dimensiones que identifican los datos del indicador."
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "notes": {
          "$ref": "#/definitions/InternationalString",
          "description": "Notas explicativas sobre el indicador."
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace al recurso padre de la API."
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "subjectCode": {
          "description": "Código del área temática, según clasificación ISTAC, en el que se cataloga el indicador.",
          "type": "string"
        },
        "subjectTitle": {
          "$ref": "#/definitions/InternationalString",
          "description": "Etiqueta del área temática, según clasificación ISTAC, en el que se cataloga el indicador."
        },
        "systemSurveyLinks": {
          "description": "Sistemas de indicadores relacionados con el indicador.",
          "items": {
            "$ref": "#/definitions/Link"
          },
          "type": "array"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        },
        "version": {
          "description": "Versión del recurso.",
          "type": "string"
        }
      }
    },
    "IndicatorBase": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "Acrónimo del indicador, por ejemplo para el  Índice de Precios Industriales sería (IPRI)."
        },
        "code": {
          "description": "Código semántico del recurso.",
          "type": "string"
        },
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "Descripción del indicador."
        },
        "data": {
          "$ref": "#/definitions/Data",
          "description": "Datos (observaciones) de un indicador."
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "metadata": {
          "$ref": "#/definitions/Metadata",
          "description": "Metadatos de un indicador."
        },
        "notes": {
          "$ref": "#/definitions/InternationalString",
          "description": "Notas explicativas sobre el indicador."
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "subjectCode": {
          "description": "Código del área temática, según clasificación ISTAC, en el que se cataloga el indicador.",
          "type": "string"
        },
        "subjectTitle": {
          "$ref": "#/definitions/InternationalString",
          "description": "Etiqueta del área temática, según clasificación ISTAC, en el que se cataloga el indicador."
        },
        "systemSurveyLinks": {
          "description": "Sistemas de indicadores relacionados con el indicador.",
          "items": {
            "$ref": "#/definitions/Link"
          },
          "type": "array"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        },
        "version": {
          "description": "Versión del recurso.",
          "type": "string"
        }
      }
    },
    "IndicatorSystem": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "Acrónimo del sistema de indicadores."
        },
        "childLink": {
          "$ref": "#/definitions/Link",
          "description": "Recurso de la API al que se puede acceder desde el recurso actual."
        },
        "code": {
          "description": "Código semántico del recurso.",
          "type": "string"
        },
        "description": {
          "$ref": "#/definitions/InternationalString",
          "description": "Descripción del sistema de indicadores."
        },
        "elements": {
          "description": "Estructura del sistema de indicadores. La estructura se compone de elementos de tipo \"dimensión\" y elementos de tipo \"instancias de indicador\". ",
          "items": {
            "$ref": "#/definitions/ElementLevel"
          },
          "type": "array"
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "objective": {
          "$ref": "#/definitions/InternationalString",
          "description": "Objetivo del sistema de indicadores."
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace al recurso padre de la API."
        },
        "publicationDate": {
          "description": "Fecha de publicación del sistema de indicadores.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "statisticalOperationLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace a la documentación del sistema de indicadores en la API de operaciones estadísticas."
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        },
        "version": {
          "description": "Versión del recurso.",
          "type": "string"
        }
      }
    },
    "IndicatorSystemBase": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "Acrónimo del sistema de indicadores."
        },
        "code": {
          "description": "Código semántico del recurso.",
          "type": "string"
        },
        "description": {
          "$ref": "#/definitions/InternationalString",
          "description": "Descripción del sistema de indicadores."
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "objective": {
          "$ref": "#/definitions/InternationalString",
          "description": "Objetivo del sistema de indicadores."
        },
        "publicationDate": {
          "description": "Fecha de publicación del sistema de indicadores.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "statisticalOperationLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace a la documentación del sistema de indicadores en la API de operaciones estadísticas."
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        },
        "version": {
          "description": "Versión del recurso.",
          "type": "string"
        }
      }
    },
    "IndicatorsPagination": {
      "properties": {
        "firstLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la primera página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última.",
          "type": "string"
        },
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/IndicatorBase"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "lastLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la última página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última.",
          "type": "string"
        },
        "limit": {
          "description": "Número máximo de resultados a obtener.",
          "type": "string"
        },
        "nextLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página siguiente a la que nos encontramos. Si no se muestra es porque no existe siguiente.",
          "type": "string"
        },
        "offset": {
          "description": "Desplazamiento. Número a partir del cual se comienzan a obtener los resultados.",
          "format": "int32",
          "type": "integer"
        },
        "previousLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página anterior a la que nos encontramos. Si no se muestra es porque no existe siguiente.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso. Dado un resultado nos permite saber cómo realizar la petición a la API para volver a obtenerlo",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "IndicatorsSystemsPagination": {
      "properties": {
        "firstLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la primera página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última.",
          "type": "string"
        },
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/IndicatorSystemBase"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "lastLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la última página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última.",
          "type": "string"
        },
        "limit": {
          "description": "Número máximo de resultados a obtener.",
          "type": "string"
        },
        "nextLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página siguiente a la que nos encontramos. Si no se muestra es porque no existe siguiente.",
          "type": "string"
        },
        "offset": {
          "description": "Desplazamiento. Número a partir del cual se comienzan a obtener los resultados.",
          "format": "int32",
          "type": "integer"
        },
        "previousLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página anterior a la que nos encontramos. Si no se muestra es porque no existe siguiente.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso. Dado un resultado nos permite saber cómo realizar la petición a la API para volver a obtenerlo",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "Instance": {
      "properties": {
        "childLink": {
          "$ref": "#/definitions/Link",
          "description": "Recurso de la API al que se puede acceder desde el recurso actual."
        },
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "Descripción de la instancia de indicador."
        },
        "decimalPlaces": {
          "description": "Especifica el número de decimales a utilizar para la visualización de los valores de un concepto métrico.",
          "format": "int32",
          "type": "integer"
        },
        "dimension": {
          "$ref": "#/definitions/MetadataDimensionMap",
          "description": "Dimensiones de identifican a la instancia de indicador."
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace al recurso padre de la API."
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "subjectCode": {
          "description": "Código del área temática, según clasificación ISTAC, en el que se cataloga la instancia de indicador.",
          "type": "string"
        },
        "subjectTitle": {
          "$ref": "#/definitions/InternationalString",
          "description": "Etiqueta del área temática, según clasificación ISTAC, en el que se cataloga la instancia de indicador."
        },
        "systemCode": {
          "description": "Código del sistema de indicadores del que forma parte la instacia de un indicador.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        }
      }
    },
    "InstanceBase": {
      "properties": {
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "Descripción del indicador con el que se asocia la instancia de indicador."
        },
        "data": {
          "$ref": "#/definitions/Data",
          "description": "Datos de la instancia de indicador."
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "metadata": {
          "$ref": "#/definitions/Metadata",
          "description": "Metadatos de la instanacia de indicador."
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace al recurso padre de la API."
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "systemCode": {
          "description": "Código del sistema de indicadores del que forma parte la instacia de un indicador.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del recurso."
        }
      }
    },
    "InstancesPagination": {
      "properties": {
        "firstLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la primera página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última.",
          "type": "string"
        },
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/InstanceBase"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "lastLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la última página. Si no se muestra es porque ya estamos en ella. Tener en cuenta que cuando sólo existe una página, no existirá ni primera ni última.",
          "type": "string"
        },
        "limit": {
          "description": "Número máximo de resultados a obtener.",
          "type": "string"
        },
        "nextLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página siguiente a la que nos encontramos. Si no se muestra es porque no existe siguiente.",
          "type": "string"
        },
        "offset": {
          "description": "Desplazamiento. Número a partir del cual se comienzan a obtener los resultados.",
          "format": "int32",
          "type": "integer"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "Enlace al recurso padre de la API."
        },
        "previousLink": {
          "description": "Dado que se trata de un resultado páginado, este enlace nos permite desplazarnos a la página anterior a la que nos encontramos. Si no se muestra es porque no existe siguiente.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso. Dado un resultado nos permite saber cómo realizar la petición a la API para volver a obtenerlo",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "InternationalString": {
      "properties": {
        "__default__": {
          "description": "Traducción en el idioma por defecto.",
          "type": "string"
        },
        "{locale}": {
          "description": "Traducción en el locale especificado.",
          "type": "string"
        }
      }
    },
    "Link": {
      "properties": {
        "href": {
          "description": "Enlace al recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        }
      }
    },
    "MeasureDimension": {
      "properties": {
        "code": {
          "description": "Código de la dimensión.",
          "type": "string"
        },
        "representation": {
          "description": "Representación de la dimensión. Indica como se codifica la dimensión.",
          "items": {
            "$ref": "#/definitions/MeasureRepresentation"
          },
          "type": "array"
        }
      }
    },
    "MeasureRepresentation": {
      "properties": {
        "code": {
          "description": "Código de la medida.",
          "type": "string"
        },
        "quantity": {
          "$ref": "#/definitions/Quantity",
          "description": "Metadatos relacionados con la medida."
        },
        "title": {
          "description": "Nombre de la medida.",
          "type": "string"
        }
      }
    },
    "Metadata": {
      "properties": {
        "attribute": {
          "$ref": "#/definitions/MetadataAttributeMap",
          "description": "Atributos de los datos."
        },
        "dimension": {
          "$ref": "#/definitions/MetadataDimensionMap",
          "description": "Dimensiones de los datos."
        }
      }
    },
    "MetadataAttributeMap": {
      "properties": {
        "{attribute}": {
          "$ref": "#/definitions/Attribute",
          "description": "Atributo de los datos."
        }
      }
    },
    "MetadataDimensionMap": {
      "properties": {
        "GEOGRAPHICAL": {
          "$ref": "#/definitions/GeographicalDimension",
          "description": "Dimensión geográfica de los datos."
        },
        "MEASURE": {
          "$ref": "#/definitions/MeasureDimension",
          "description": "Dimensión de medida de los datos."
        },
        "TIME": {
          "$ref": "#/definitions/TimeDimension",
          "description": "Dimensión temporal de los datos."
        }
      }
    },
    "Quantity": {
      "properties": {
        "baseLocation": {
          "$ref": "#/definitions/GeographicalRepresentation",
          "description": "Base espacial para un indicador tipo índice (index) espacial o temporal. El ejemplo más característico de índices espaciales son las Paridades de Poder Adquisitivo (PPA) que comparan los niveles de precios entre territorios, siendo el territorio base la referencia 100. Por ejemplo las PPA de Canarias comparan los niveles de precios entre islas, siendo el territorio base Canarias. Metadato asociado al tipo de medida index."
        },
        "baseQuantityLink": {
          "$ref": "#/definitions/Link",
          "description": "Referencia al indicador sobre el que se calcula una tasa de variación. Metadato asociado al tipo de medida changerate."
        },
        "baseTime": {
          "$ref": "#/definitions/TimeRepresentation",
          "description": "Base temporal de un indicador tipo índice (index) temporal. Por ejemplo, 2010. Metadato asociado al tipo de medida index."
        },
        "baseValue": {
          "description": "Valor de referencia de un índice (index), generalmente baseValue = 100. Metadato asociado al tipo de medida index.",
          "format": "int32",
          "type": "integer"
        },
        "decimalPlaces": {
          "description": "Especifica el número de decimales a utilizar para la visualización de los valores de un concepto métrico.",
          "format": "int32",
          "type": "integer"
        },
        "denominatorLink": {
          "$ref": "#/definitions/Link",
          "description": "Otro indicador del sistema que es el denominador del indicador tipo proporción. Por ejemplo, para el indicador \"tasa de paro\" sería el indicador \"población activa\". Metadato asociado a los siguientes tipos de medida: fraction, ratio, rate, index y changerate."
        },
        "isPercentage": {
          "description": "Atributo booleano que toma el valor TRUE si una fracción es un porcentaje y FALSE en caso contrario. Metadato asociado a los siguientes tipos de medida: ratio, rate, index y changerate.",
          "type": "boolean"
        },
        "max": {
          "description": "Especifica el valor máximo que puede tomar la magnitud. En el el caso de una magnitud referenciada en una escala de intervalo (o de intensidad) indica el valor superior de la escala. Por ejemplo, para el Indice de Desarrollo Humano; max = 1. Metadato asociado a los siguientes tipos de medida: magnitude, fraction, ratio, rate, index y changerate.",
          "format": "int32",
          "type": "integer"
        },
        "min": {
          "description": "Especifica el valor mínimo que puede tomar la magnitud. En el el caso de una magnitud referenciada en una escala de intervalo (o de intensidad) indica el valor inferior de la escala. Por ejemplo, para el Indice de Desarrollo Humano; min = 0. Metadato asociado a los siguientes tipos de medida: magnitude, fraction, ratio, rate, index y changerate.",
          "format": "int32",
          "type": "integer"
        },
        "numeratorLink": {
          "$ref": "#/definitions/Link",
          "description": "Otro indicador del sistema que es el numerador del indicador tipo proporción. Por ejemplo, para el indicador \"tasa de paro\" sería el indicador \"población parada\". Metadato asociado a los siguientes tipos de medida: fraction, ratio, rate, index y changerate."
        },
        "percentageOf": {
          "$ref": "#/definitions/InternationalString",
          "description": "Si estamos ante un porcentaje, este atributo ofrece un texto descriptivo  que puede utilizarse para mostrarse junto al número y especifica de qué se trata dicho porcentaje. Por ejemplo, la métrica \"tasa de desempleo\" puede tener un atributo PercentageOf  \"% de la población activa\" para que el valor se lea como \"10,5% de la población activa\". Metadato asociado a los siguientes tipos de medida: ratio, rate, index y changerate."
        },
        "significantDigits": {
          "description": "Especifica el número de dígitos significativos para utilizar cuando se muestran los valores de un concepto métrico.",
          "format": "int32",
          "type": "integer"
        },
        "type": {
          "description": "Existen diferentes tipos de medición (type) y asociados a cada una de ellos, diferentes metadatos que especifican mejor esa medida: medición (quantity), cantidad (amount), magnitud (magnitude), fracción (fraction),  ratio (ratio), tasa (rate), indice (index), tasa de variación (changerate). Se ha usado la clasificación utilizada por Google, en su estándar Dataset Publishing Language (DSPL).",
          "type": "string"
        },
        "unit": {
          "$ref": "#/definitions/InternationalString",
          "description": "Define la unidad de medida de un indicador. Una unidad de medida es una cantidad estandarizada de una determinada magnitud, definida y adoptada por convención o por ley, por ejemplo: unidades física (kg, mm, °C, °F) o unidades monetarias (euros, pesetas, dólares). Cualquier valor de una cantidad física puede expresarse como un múltiplo de la unidad de medida. Una unidad de medida toma su valor a partir de un patrón o de una composición de otras unidades definidas previamente. Las primeras unidades se conocen como unidades básicas o de base (fundamentales), mientras que las segundas se llaman unidades derivadas. <br>También consideramos como unidades de medida las especificaciones de simples recuentos (por ejemplo personas, vehículos, viviendas)."
        },
        "unitMultiplier": {
          "$ref": "#/definitions/InternationalString",
          "description": "Literal que especifica el multiplicador de la unidad de medida. Por ejemplo \"Miles de personas\". Metadato asociado a todos los tipos de medida."
        },
        "unitSymbol": {
          "description": "Símbolo asociado a la unidad de medida, por ejemplo \"km\" para kilómetros. <br>Metadato asociado a todos los tipos de medida.",
          "type": "string"
        },
        "unitSymbolPosition": {
          "description": "Posición del símbolo de la unidad de medida, que puede ser delante o detrás de la cifra. <br>Metadato asociado a todos los tipos de medida.",
          "type": "string"
        }
      }
    },
    "Subject": {
      "properties": {
        "code": {
          "description": "Código semántico del recurso.",
          "type": "string"
        },
        "id": {
          "description": "Identificador del recurso.",
          "type": "string"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre del tema estadístico."
        }
      }
    },
    "SubjectList": {
      "properties": {
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/Subject"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "TimeDimension": {
      "properties": {
        "code": {
          "description": "Código de la dimensión.",
          "type": "string"
        },
        "granularity": {
          "description": "Granularidades que intervienen en la dimensión.",
          "items": {
            "$ref": "#/definitions/Granularity"
          },
          "type": "array"
        },
        "representation": {
          "description": "Representación de la dimensión. Indica como se codifica la dimensión.",
          "items": {
            "$ref": "#/definitions/TimeRepresentation"
          },
          "type": "array"
        }
      }
    },
    "TimeGranularitiesList": {
      "properties": {
        "items": {
          "description": "Listado de recursos.",
          "items": {
            "$ref": "#/definitions/TimeGranularity"
          },
          "type": "array"
        },
        "kind": {
          "description": "Tipo del recurso.",
          "type": "string"
        },
        "selfLink": {
          "description": "Enlace al propio recurso.",
          "type": "string"
        },
        "total": {
          "description": "Número total de resultados existentes.",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "TimeGranularity": {
      "properties": {
        "code": {
          "description": "Código de la granularidad temporal",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "Nombre la granularidad temporal"
        }
      }
    },
    "TimeRepresentation": {
      "properties": {
        "code": {
          "description": "Código del momento de tiempo.",
          "type": "string"
        },
        "granularityCode": {
          "description": "Granularidad del momento de tiempo.",
          "type": "string"
        },
        "title": {
          "description": "Nombre del momento de tiempo.",
          "type": "string"
        }
      }
    }
  },
  "info": {
    "version": "1.0"
  },
  "paths": {
    "/geographicGranularities": {
      "get": {
        "description": "<p>Esta petición devuelve la lista de granularidades geográficas tratadas en el banco de datos ISTAC-indicadores. Por ejemplo granularidad provincial, insular o municipal.</p><br>",
        "operationId": "findGeographicGranularities",
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/GeographicalGranularityList"
            }
          }
        },
        "summary": "Granularidades geográficas"
      }
    },
    "/geographicalValues": {
      "get": {
        "description": "<p>Esta petición devuelve los valores de una granularidad geográfica que a su vez forman parte de una un tema o un sistema de indicadores específicos.</p><br><p>Es importante tener en cuenta que de los parátemeros opcionales (<strong>subjectCode</strong> y <strong>systemCode</strong>) podrá estar cumplimentado uno de ellos o ninguno. En caso de que estén cumplimentados ambos, sólo se tendrá en cuenta el último.</p> <br>",
        "operationId": "findGeographicalValues",
        "parameters": [
          {
            "description": "Código del tema del que se desean obtener los valores geográficos.",
            "in": "query",
            "name": "subjectCode",
            "required": false,
            "type": "string"
          },
          {
            "description": "Código del sistema de indicadores del que se desean obtener los valores geográficos.",
            "in": "query",
            "name": "systemCode",
            "required": false,
            "type": "string"
          },
          {
            "description": "Código de la granularidad geográfica.",
            "in": "query",
            "name": "geographicalGranularityCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/GeographicalValueList"
            }
          }
        },
        "summary": "Valores geográficos"
      }
    },
    "/indicators": {
      "get": {
        "description": "<p>Esta petición aporta la lista de indicadores publicados en el banco de datos ISTAC-indicadores. Un indicador es una medida utilizada para conocer la intensidad de un fenómeno en el espacio-tiempo. Esa medida se puede referir a distintas granularidades espaciales o temporales. Por ejemplo \"Tasa de paro\" es un indicador con diversas granularidades espaciales (insular, provincial y autonómica) y con el trimestre como una única granulariad temporal.</p><br>",
        "operationId": "findIndicators",
        "parameters": [
          {
            "description": "Consulta. Los metadatos sobre los que se pueden construir las búsquedas son: \"id\", \"subjectCode\" y \"geographicalValue\".<br> Ejemplos: <br>q=id EQ \"PARO_REGISTRADO\" <br> q=subjectCode EQ \"EDUCACION\" AND geographicalValue EQ \"ES\" <br> q=id IN (\"CODE-1\", \"CODE-2\").",
            "in": "query",
            "name": "q",
            "type": "string"
          },
          {
            "description": "Orden. Los posibles valores son \"update\" e \"id\" y los criterios de orden \"asc\" y \"desc\". <br> Ejemplo: order=update asc.",
            "in": "query",
            "name": "order",
            "type": "string"
          },
          {
            "description": "Límite de resultados (número máximo). <br> Ejemplo: limit=10.",
            "format": "int32",
            "in": "query",
            "name": "limit",
            "type": "integer"
          },
          {
            "description": "Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. <br> Ejemplo: offset=2.",
            "format": "int32",
            "in": "query",
            "name": "offset",
            "type": "integer"
          },
          {
            "description": "Permite personalizar la respuesta mediante la adición de nuevos campos. Los posibles valores son: \"+metadata\", \"+data\" y \"+observationsMetadata\". <br> Ejemplo: fields=+metadata.",
            "in": "query",
            "name": "fields",
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante el valor de las mismas. Su uso sólo tiene sentido cuando se ha incluído \"+data\" y/o \"+observationsMetadata\". <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
            "in": "query",
            "name": "representation",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/IndicatorsPagination"
            }
          }
        },
        "summary": "Listado de indicadores"
      }
    },
    "/indicators/{indicatorCode}": {
      "get": {
        "description": "<p>Un indicador es una medida utilizada para conocer la intensidad de un fenómeno en el espacio-tiempo. Esa medida se puede referir a distintas granularidades espaciales, p.e. islas y municipios, o temporales, p.e. años y meses. A través de esta petición se ofrecen los metadatos que describen las características de un indicador específico, pemitiendo la compresión del hecho medido; asimismo a través de la petición data se aportan los datos completos (para todos los espacio-tiempo) del indicador.</p><br>",
        "operationId": "findIndicator",
        "parameters": [
          {
            "description": "Código del indicador a obtener.",
            "in": "path",
            "name": "indicatorCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/Indicator"
            }
          }
        },
        "summary": "Indicador"
      }
    },
    "/indicators/{indicatorCode}/data": {
      "get": {
        "description": "<p>Un indicador es una medida utilizada para conocer la intensidad de un fenómeno en el espacio-tiempo. Esa medida se puede referir a distintas granularidades espaciales, p.e. islas y municipios, o temporales, p.e. años y meses.  A través de la petición data se aportan los datos completos (para todos los espacio-tiempo) del indicador. Por otra parte a través de la petición metadata se ofrecen los metadatos que describen las características de un indicador específico, pemitiendo la compresión del hecho medido.</p><br>",
        "operationId": "findIndicator",
        "parameters": [
          {
            "description": "Código del indicador a obtener.",
            "in": "path",
            "name": "indicatorCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante el valor de las mismas. <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
            "in": "query",
            "name": "representation",
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante las granularidades de las mismas. <br> Ejemplo: granularirty=GEOGRAPHICAL[MUNICIPALITIES|PROVINCES],TIME[MONTHLY].",
            "in": "query",
            "name": "granularity",
            "type": "string"
          },
          {
            "description": "Permite personalizar la respuesta mediante la exclusión de campos. Los posibles valores son: \"-observationsMetadata\". <br> Ejemplo: fields=-observationsMetadata.",
            "in": "query",
            "name": "fields",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/Data"
            }
          }
        },
        "summary": "Observaciones de un indicador"
      }
    },
    "/indicatorsSystems": {
      "get": {
        "description": "<p>Esta petición aporta la lista de sistemas de indicadores publicados en el banco de datos ISTAC-indicadores. Los indicadores son estadísticas simples o compuestas, sin embargo un solo indicador rara vez puede proporcionar información útil acerca de fenómenos complejos tales como la coyuntura económica, las condiciones de vida, la escolarización u otros. Los sistemas de indicadores generalmente están diseñados para generar más y más precisa información acerca de las condiciones de un fenómeno; y para ello se organizan en dimensiones o áreas de análisis, bajo las cuáles se integran los indicadores. En el ISTAC un sistema de indicadores se trata como una operación estadística, por lo que se puede consultar más información del sistema en la API de operaciones.</p><br>",
        "operationId": "findIndicatorsSystems",
        "parameters": [
          {
            "description": "Límite de resultados (número máximo). <br> Ejemplo: limit=10.",
            "format": "int32",
            "in": "query",
            "name": "limit",
            "type": "integer"
          },
          {
            "description": "Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. <br> Ejemplo: offset=2.",
            "format": "int32",
            "in": "query",
            "name": "offset",
            "type": "integer"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/IndicatorsSystemsPagination"
            }
          }
        },
        "summary": "Listado de sistemas de indicadores"
      }
    },
    "/indicatorsSystems/{indicatorSystemCode}": {
      "get": {
        "description": "<p>Esta petición ofrece los metadatos de un sistema de indicadores publicado en el banco de datos ISTAC-indicadores. Los indicadores son estadísticas simples o compuestas, sin embargo un solo indicador rara vez puede proporcionar información útil acerca de fenómenos complejos tales como la coyuntura económica, las condiciones de vida, la escolarización u otros. Los sistemas de indicadores generalmente están diseñados para generar más y más precisa información acerca de las condiciones de un fenómeno; y para ello se organizan en dimensiones o áreas de análisis, bajo las cuales se integran los indicadores.  En el ISTAC un sistema de indicadores se trata como una operación estadística, por lo que se puede consultar más información del sistema en la API de operaciones.</p><br>",
        "operationId": "findIndicatorsSystem",
        "parameters": [
          {
            "description": "Código del sistema a obtener.",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/IndicatorSystem"
            }
          }
        },
        "summary": "Sistema de indicadores"
      }
    },
    "/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances": {
      "get": {
        "description": "<p>Esta petición devuelve las instacias de indicadores asociadas a un sistema de indicadores especifico. Una instancia de un indicador no es más que una consulta espacio-temporal de un indicador a la hora de incorporarlo a un sistema de indicadores concreto. Por ejemplo, el indicador Paro registrado se incorpora al sistema Anuario de Indicadores Municipales como una consulta (instancia de indicador) a través de la cual se seleccionan los datos municipales y anuales de dicho indicador. </p><br>",
        "operationId": "retrieveIndicatorsInstances",
        "parameters": [
          {
            "description": "Código del sistema a obtener.",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "Consulta. Los metadatos sobre los que se pueden construir las búsquedas son: \"id\" y \"geographicalValue\".<br> Ejemplos: <br>q=id EQ \"INDICADORES_MUNICIPALES\" <br> q=geographicalValue EQ \"ES\" <br> q=id IN (\"CODE-1\", \"CODE-2\").",
            "in": "query",
            "name": "q",
            "type": "string"
          },
          {
            "description": "Orden. Los posibles valores son \"update\" e \"id\" y los criterios de orden \"asc\" y \"desc\". <br> Ejemplo: order=update asc.",
            "in": "query",
            "name": "order",
            "type": "string"
          },
          {
            "description": "Límite de resultados (número máximo). <br> Ejemplo: limit=10.",
            "format": "int32",
            "in": "query",
            "name": "limit",
            "type": "integer"
          },
          {
            "description": "Desplazamiento. Resultado a partir del que se devuelve. El valor por defecto es 0. <br> Ejemplo: offset=2.",
            "format": "int32",
            "in": "query",
            "name": "offset",
            "type": "integer"
          },
          {
            "description": "Permite personalizar la respuesta mediante la adición de nuevos campos. Los posibles valores son: \"+metadata\", \"+data\" y \"+observationsMetadata\". <br> Ejemplo: fields=+metadata.",
            "in": "query",
            "name": "fields",
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante el valor de las mismas. Su uso sólo tiene sentido cuando se ha incluído \"+data\" y/o \"+observationsMetadata\". <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
            "in": "query",
            "name": "representation",
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante las granularidades de las mismas. Su uso sólo tiene sentido cuando se ha incluído \"+data\" y/o \"+observationsMetadata\". <br> Ejemplo: granularirty=GEOGRAPHICAL[MUNICIPALITIES|PROVINCES],TIME[MONTHLY].",
            "in": "query",
            "name": "granularity",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/InstancesPagination"
            }
          }
        },
        "summary": "Instancias de sistema de indicadores"
      }
    },
    "/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances/{indicatorInstanceCode}": {
      "get": {
        "description": "<p>Esta petición devuelve los meteadatos de una instacia de indicadores asociada a un sistema de indicadores especifico. Una instancia de un indicador no es más que una consulta espacio-temporal de un indicador a la hora de incorporarlo a un sistema de indicadores concreto. Por ejemplo, el indicador Paro registrado se incorpora al sistema Anuario de Indicadores Municipales como una consulta (instancia de indicador) a través de la cual se seleccionan los datos municipales y anuales de dicho indicador. </p><br>",
        "operationId": "retrieveIndicatorsInstance",
        "parameters": [
          {
            "description": "Código del sistema a obtener.",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "Código de la instancia a obtener.",
            "in": "path",
            "name": "indicatorInstanceCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/Instance"
            }
          }
        },
        "summary": "Instancia de un sistema de indicadores"
      }
    },
    "/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances/{indicatorInstanceCode}/data": {
      "get": {
        "description": "<p>Esta petición devuelve los datos de una instacia de indicadores asociada a un sistema de indicadores especifico. Una instancia de un indicador no es más que una consulta espacio-temporal de un indicador a la hora de incorporarlo a un sistema de indicadores concreto. Por ejemplo, el indicador Paro registrado se incorpora al sistema Anuario de Indicadores Municipales como una consulta (instancia de indicador) a través de la cual se seleccionan los datos municipales y anuales de dicho indicador. </p><br>",
        "operationId": "retrieveIndicatorsInstanceData",
        "parameters": [
          {
            "description": "Código del sistema a obtener.",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "Código de la instancia a obtener.",
            "in": "path",
            "name": "indicatorInstanceCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante el valor de las mismas. <br> Ejemplo: representation=GEOGRAPHICAL[35003|35005],MEASURE[ABSOLUTE].",
            "in": "query",
            "name": "representation",
            "type": "string"
          },
          {
            "description": "Permite filtrar las observaciones mediante las granularidades de las mismas. <br> Ejemplo: granularirty=GEOGRAPHICAL[MUNICIPALITIES|PROVINCES],TIME[MONTHLY].",
            "in": "query",
            "name": "granularity",
            "type": "string"
          },
          {
            "description": "Permite personalizar la respuesta mediante la exclusión de campos. Los posibles valores son: \"-observationsMetadata\". <br> Ejemplo: fields=-observationsMetadata.",
            "in": "query",
            "name": "fields",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/Data"
            }
          }
        },
        "summary": "Observaciones instancia de indicador"
      }
    },
    "/subjects": {
      "get": {
        "description": "<p>Esta petición devuelve los temas en los que el ISTAC clasifica sus operaciones estadísticas.</p><br>",
        "operationId": "findSubjects",
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/SubjectList"
            }
          }
        },
        "summary": "Temas estadísticos"
      }
    },
    "/timeGranularities": {
      "get": {
        "description": "<p>Esta petición devuelve la lista de granularidades temporales tratadas en el banco de datos ISTAC-indicadores ordenadas de mayor a menor granularidad. Por ejemplo granularidad anual, trimestral o mensual.</p><br>",
        "operationId": "retrieveTimeGranularities",
        "responses": {
          "200": {
            "description": "No response was specified",
            "schema": {
              "$ref": "#/definitions/TimeGranularitiesList"
            }
          }
        },
        "summary": "Granularidades temporales"
      }
    }
  },
  "swagger": "2.0"
}
