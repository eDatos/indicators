[#ftl]
[#macro messageEscape code, escape=false]
  [#assign args = [] /]
  ${springMacroRequestContext.getMessage(code, args, '', escape)}[/#macro]
{
   "swagger":"2.0",
   "info":{
      "description":"[@messageEscape 'api.doc.swagger.description'/]",
      "version":"4.5.2-SNAPSHOT",
      "title":"[@messageEscape 'api.doc.swagger.title'/]"
   },
  "host": "${indicatorsExternalApiUrlBaseSwagger}",
   "schemes":[

   ],
   "tags" : [
    {
      "name" : "[@messageEscape 'api.doc.swagger.tags.indicators' /]",
      "description" : ""
    },
    {
      "name" : "[@messageEscape 'api.doc.swagger.tags.indicatorssistem' /]",
      "description" : ""
    },
    {
      "name" : "[@messageEscape 'api.doc.swagger.tags.tablevalues' /]",
      "description" : ""
    }
  ],
  "definitions": {
    "Attribute": {
      "properties": {
        "attachmentLevel": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.attribute.properties.attachmentLevel' /]",
          "type": "string"
        },
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.attribute.properties.code' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.attribute.properties.title' /]"
        }
      }
    },
    "Data": {
      "properties": {
        "attribute": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.attribute' /]",
          "items": {
            "$ref": "#/definitions/DataAttributeMap"
          },
          "type": "array"
        },
        "dimension": {
          "$ref": "#/definitions/DataDimensionMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.dimension' /]"
        },
        "format": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.format' /]",
          "items": {
            "type": "string"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.kind' /]",
          "type": "string"
        },
        "observation": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.observation' /]",
          "items": {
            "type": "string"
          },
          "type": "array"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.parentlink' /]"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.data.properties.selflink' /]",
          "type": "string"
        }
      }
    },
    "DataAttribute": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.dataattribute.properties.code' /]",
          "type": "string"
        },
        "value": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.dataattribute.properties.value' /]"
        }
      }
    },
    "DataAttributeMap": {
      "properties": {
        "{attribute}": {
          "$ref": "#/definitions/DataAttribute",
          "description": "[@messageEscape 'api.doc.swagger.definitions.dataattributemap.properties.attribute' /]"
        }
      }
    },
    "DataDimension": {
      "properties": {
        "representation": {
          "$ref": "#/definitions/DataDimensionRepresentation",
          "description": "[@messageEscape 'api.doc.swagger.definitions.datadimension.properties.representation' /]"
        }
      }
    },
    "DataDimensionMap": {
      "properties": {
        "{dimension}": {
          "$ref": "#/definitions/DataDimension",
          "description": "[@messageEscape 'api.doc.swagger.definitions.datadimensionmap.properties.dimension' /]"
        }
      }
    },
    "DataDimensionRepresentation": {
      "properties": {
        "index": {
          "$ref": "#/definitions/DataDimensionRepresentationIndexMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.datadimensionrepresentation.properties.index' /]"
        },
        "size": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.datadimensionrepresentation.properties.size' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "DataDimensionRepresentationIndexMap": {
      "properties": {
        "{category}": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.datadimensionrepresentationindexmap.properties.category' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "ElementLevel": {
      "properties": {
        "elements": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.elementlevel.properties.elements' /]",
          "items": {
            "$ref": "#/definitions/ElementLevel"
          },
          "type": "array"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.elementlevel.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.elementlevel.properties.kind' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.elementlevel.properties.selflink' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.elementlevel.properties.title' /]"
        }
      }
    },
    "GeographicalDimension": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicaldimension.properties.code' /]",
          "type": "string"
        },
        "granularity": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicaldimension.properties.granularity' /]",
          "items": {
            "$ref": "#/definitions/Granularity"
          },
          "type": "array"
        },
        "representation": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicaldimension.properties.representation' /]",
          "items": {
            "$ref": "#/definitions/GeographicalRepresentation"
          },
          "type": "array"
        },
        "showCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.dimensions.properties.showCode' /]",
          "type": "boolean"
        }
      }
    },
    "GeographicalGranularity": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalgranularity.properties.code' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalgranularity.properties.title' /]"
        }
      }
    },
    "GeographicalGranularityList": {
      "properties": {
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalgranularitylist.properties.items' /]",
          "items": {
            "$ref": "#/definitions/GeographicalGranularity"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalgranularitylist.properties.kind' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalgranularitylist.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalgranularitylist.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "GeographicalRepresentation": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalrepresentation.properties.code' /]",
          "type": "string"
        },
        "granularityCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalrepresentation.properties.granularitycode' /]",
          "type": "string"
        },
        "latitude": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalrepresentation.properties.latitude' /]",
          "format": "double",
          "type": "number"
        },
        "longitude": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalrepresentation.properties.longitude' /]",
          "format": "double",
          "type": "number"
        },
        "title": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalrepresentation.properties.title' /]",
          "type": "string"
        }
      }
    },
    "GeographicalValue": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvalue.properties.code' /]",
          "type": "string"
        },
        "granularityCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvalue.properties.granularitycode' /]",
          "type": "string"
        },
        "latitude": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvalue.properties.latitude' /]",
          "format": "double",
          "type": "number"
        },
        "longitude": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvalue.properties.longitude' /]",
          "format": "double",
          "type": "number"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvalue.properties.title' /]"
        }
      }
    },
    "GeographicalValueList": {
      "properties": {
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvaluelist.properties.items' /]",
          "items": {
            "$ref": "#/definitions/GeographicalValue"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvaluelist.properties.kind' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvaluelist.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.geographicalvaluelist.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "Granularity": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.granularity.properties.code' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.granularity.properties.title' /]"
        }
      }
    },
    "Indicator": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.acronym' /]"
        },
        "attribute": {
          "$ref": "#/definitions/MetadataAttributeMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.attribute' /]"
        },
        "childLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.childlink' /]"
        },
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.code' /]",
          "type": "string"
        },
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.conceptdescription' /]"
        },
        "decimalPlaces": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.decimalplaces' /]",
          "format": "int32",
          "type": "integer"
        },
        "dimension": {
          "$ref": "#/definitions/MetadataDimensionMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.dimension' /]"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.kind' /]",
          "type": "string"
        },
        "notes": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.notes' /]"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.parentlink' /]"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.selflink' /]",
          "type": "string"
        },
        "subjectCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.subjectcode' /]",
          "type": "string"
        },
        "subjectTitle": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.subjecttitle' /]"
        },
        "systemSurveyLinks": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.systemsurveylinks' /]",
          "items": {
            "$ref": "#/definitions/Link"
          },
          "type": "array"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.title' /]"
        },
        "version": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicator.properties.version' /]",
          "type": "string"
        },
        "isMainIndicator": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.mainindicator' /]",
          "type": "boolean"
        }
      }
    },
    "IndicatorBase": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.acronym' /]"
        },
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.code' /]",
          "type": "string"
        },
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.conceptdescription' /]"
        },
        "data": {
          "$ref": "#/definitions/Data",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.data' /]"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.kind' /]",
          "type": "string"
        },
        "metadata": {
          "$ref": "#/definitions/Metadata",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.metadata' /]"
        },
        "notes": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.notes' /]"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.selflink' /]",
          "type": "string"
        },
        "subjectCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.subjectcode' /]",
          "type": "string"
        },
        "subjectTitle": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.subjecttitle' /]"
        },
        "systemSurveyLinks": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.systemsurveylinks' /]",
          "items": {
            "$ref": "#/definitions/Link"
          },
          "type": "array"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.title' /]"
        },
        "version": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.version' /]",
          "type": "string"
        },
        "isMainIndicator": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorbase.properties.mainindicator' /]",
          "type": "boolean"
        }
      }
    },
    "IndicatorSystem": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.acronym' /]"
        },
        "childLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.childlink' /]"
        },
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.code' /]",
          "type": "string"
        },
        "description": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.description' /]"
        },
        "operational": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.operational' /]",
          "type": "boolean"
        },
        "elements": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.elements' /]",
          "items": {
            "$ref": "#/definitions/ElementLevel"
          },
          "type": "array"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.kind' /]",
          "type": "string"
        },
        "objective": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.objective' /]"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.parentlink' /]"
        },
        "publicationDate": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.publicationdate' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.selflink' /]",
          "type": "string"
        },
        "statisticalOperationLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.statisticaloperationlink' /]"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.title' /]"
        },
        "version": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystem.properties.version' /]",
          "type": "string"
        }
      }
    },
    "IndicatorSystemBase": {
      "properties": {
        "acronym": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.acronym' /]"
        },
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.code' /]",
          "type": "string"
        },
        "description": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.description' /]"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.id' /]",
          "type": "string"
        },
        "operational": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.operational' /]",
          "type": "boolean"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.kind' /]",
          "type": "string"
        },
        "objective": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.objective' /]"
        },
        "publicationDate": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.publicationdate' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.selflink' /]",
          "type": "string"
        },
        "statisticalOperationLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.statisticaloperationlink' /]"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.title' /]"
        },
        "version": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorsystembase.properties.version' /]",
          "type": "string"
        }
      }
    },
    "IndicatorsPagination": {
      "properties": {
        "firstLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.firstlink' /]",
          "type": "string"
        },
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.items' /]",
          "items": {
            "$ref": "#/definitions/IndicatorBase"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.kind' /]",
          "type": "string"
        },
        "lastLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.lastlink' /]",
          "type": "string"
        },
        "limit": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.limit' /]",
          "type": "string"
        },
        "nextLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.nextlink' /]",
          "type": "string"
        },
        "offset": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.offset' /]",
          "format": "int32",
          "type": "integer"
        },
        "previousLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.previouslink' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorspagination.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "IndicatorsSystemsPagination": {
      "properties": {
        "firstLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.firstlink' /]",
          "type": "string"
        },
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.items' /]",
          "items": {
            "$ref": "#/definitions/IndicatorSystemBase"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.kind' /]",
          "type": "string"
        },
        "lastLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.lastlink' /]",
          "type": "string"
        },
        "limit": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.limit' /]",
          "type": "string"
        },
        "nextLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.nextlink' /]",
          "type": "string"
        },
        "offset": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.offset' /]",
          "format": "int32",
          "type": "integer"
        },
        "previousLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.previouslink' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.indicatorssystemspagination.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "Instance": {
      "properties": {
        "childLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.childlink' /]"
        },
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.conceptdesctiption' /]"
        },
        "decimalPlaces": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.decimalplaces' /]",
          "format": "int32",
          "type": "integer"
        },
        "dimension": {
          "$ref": "#/definitions/MetadataDimensionMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.dimension' /]"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.kind' /]",
          "type": "string"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.parentlink' /]"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.selflink' /]",
          "type": "string"
        },
        "subjectCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.subjectcode' /]",
          "type": "string"
        },
        "subjectTitle": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.subjecttitle' /]"
        },
        "systemCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.systemcode' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instance.properties.title' /]"
        }
      }
    },
    "InstanceBase": {
      "properties": {
        "conceptDescription": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.conceptdescription' /]"
        },
        "data": {
          "$ref": "#/definitions/Data",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.data' /]"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.kind' /]",
          "type": "string"
        },
        "metadata": {
          "$ref": "#/definitions/Metadata",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.metadata' /]"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.parentlink' /]"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.selflink' /]",
          "type": "string"
        },
        "systemCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.systemcode' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancebase.properties.title' /]"
        }
      }
    },
    "InstancesPagination": {
      "properties": {
        "firstLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.firstlink' /]",
          "type": "string"
        },
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.items' /]",
          "items": {
            "$ref": "#/definitions/InstanceBase"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.kind' /]",
          "type": "string"
        },
        "lastLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.lastlink' /]",
          "type": "string"
        },
        "limit": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.limit' /]",
          "type": "string"
        },
        "nextLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.nextlink' /]",
          "type": "string"
        },
        "offset": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.offset' /]",
          "format": "int32",
          "type": "integer"
        },
        "parentLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.parentlink' /]"
        },
        "previousLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.previouslink' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.instancepagination.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "InternationalString": {
      "properties": {
        "__default__": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.internationalstring.properties.default' /]",
          "type": "string"
        },
        "{locale}": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.internationalstring.properties.locale' /]",
          "type": "string"
        }
      }
    },
    "Link": {
      "properties": {
        "href": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.link.properties.href' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.link.properties.kind' /]",
          "type": "string"
        }
      }
    },
    "MeasureDimension": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.measuredimension.properties.code' /]",
          "type": "string"
        },
        "representation": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.measuredimension.properties.representation' /]",
          "items": {
            "$ref": "#/definitions/MeasureRepresentation"
          },
          "type": "array"
        },
        "showCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.dimensions.properties.showCode' /]",
          "type": "boolean"
        }
      }
    },
    "MeasureRepresentation": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.measurerepresentation.properties.code' /]",
          "type": "string"
        },
        "quantity": {
          "$ref": "#/definitions/Quantity",
          "description": "[@messageEscape 'api.doc.swagger.definitions.measurerepresentation.properties.quantity' /]"
        },
        "title": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.measurerepresentation.properties.title' /]",
          "type": "string"
        }
      }
    },
    "Metadata": {
      "properties": {
        "attribute": {
          "$ref": "#/definitions/MetadataAttributeMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.metadata.properties.attribute' /]"
        },
        "dimension": {
          "$ref": "#/definitions/MetadataDimensionMap",
          "description": "[@messageEscape 'api.doc.swagger.definitions.metadata.properties.dimension' /]"
        }
      }
    },
    "MetadataAttributeMap": {
      "properties": {
        "{attribute}": {
          "$ref": "#/definitions/Attribute",
          "description": "[@messageEscape 'api.doc.swagger.definitions.metadataattributemap.properties.attribute' /]"
        }
      }
    },
    "MetadataDimensionMap": {
      "properties": {
        "GEOGRAPHICAL": {
          "$ref": "#/definitions/GeographicalDimension",
          "description": "[@messageEscape 'api.doc.swagger.definitions.metadatadimensionmap.properties.geographical' /]"
        },
        "MEASURE": {
          "$ref": "#/definitions/MeasureDimension",
          "description": "[@messageEscape 'api.doc.swagger.definitions.metadatadimensionmap.properties.measure' /]"
        },
        "TIME": {
          "$ref": "#/definitions/TimeDimension",
          "description": "[@messageEscape 'api.doc.swagger.definitions.metadatadimensionmap.properties.time' /]"
        }
      }
    },
    "Quantity": {
      "properties": {
        "baseLocation": {
          "$ref": "#/definitions/GeographicalRepresentation",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.baselocation' /]"
        },
        "baseQuantityLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.basequantitylink' /]"
        },
        "baseTime": {
          "$ref": "#/definitions/TimeRepresentation",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.basetime' /]"
        },
        "baseValue": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.basevalue' /]",
          "format": "int32",
          "type": "integer"
        },
        "decimalPlaces": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.decimalplaces' /]",
          "format": "int32",
          "type": "integer"
        },
        "denominatorLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.denominatorlink' /]"
        },
        "isPercentage": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.ispercentege' /]",
          "type": "boolean"
        },
        "max": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.max' /]",
          "format": "int32",
          "type": "integer"
        },
        "min": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.min' /]",
          "format": "int32",
          "type": "integer"
        },
        "numeratorLink": {
          "$ref": "#/definitions/Link",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.numeratorlink' /]"
        },
        "percentageOf": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.percentegeof' /]"
        },
        "significantDigits": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.significantdigits' /]",
          "format": "int32",
          "type": "integer"
        },
        "type": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.type' /]",
          "type": "string"
        },
        "unit": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.unit' /]"
        },
        "unitMultiplier": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.unitmultiplier' /]"
        },
        "unitSymbol": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.unitsymbol' /]",
          "type": "string"
        },
        "unitSymbolPosition": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.quantity.properties.unitsymbolposition' /]",
          "type": "string"
        }
      }
    },
    "Subject": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subject.properties.code' /]",
          "type": "string"
        },
        "id": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subject.properties.id' /]",
          "type": "string"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subject.properties.kind' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.subject.properties.title' /]"
        }
      }
    },
    "SubjectList": {
      "properties": {
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subjectlist.properties.items' /]",
          "items": {
            "$ref": "#/definitions/Subject"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subjectlist.properties.kind' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subjectlist.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.subjectlist.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "TimeDimension": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timedimension.properties.code' /]",
          "type": "string"
        },
        "granularity": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timedimension.properties.granularity' /]",
          "items": {
            "$ref": "#/definitions/Granularity"
          },
          "type": "array"
        },
        "representation": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timedimension.properties.representation' /]",
          "items": {
            "$ref": "#/definitions/TimeRepresentation"
          },
          "type": "array"
        },
        "showCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.dimensions.properties.showCode' /]",
          "type": "boolean"
        }
      }
    },
    "TimeGranularitiesList": {
      "properties": {
        "items": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timegranularitieslist.properties.items' /]",
          "items": {
            "$ref": "#/definitions/TimeGranularity"
          },
          "type": "array"
        },
        "kind": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timegranularitieslist.properties.kind' /]",
          "type": "string"
        },
        "selfLink": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timegranularitieslist.properties.selflink' /]",
          "type": "string"
        },
        "total": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timegranularitieslist.properties.total' /]",
          "format": "int32",
          "type": "integer"
        }
      }
    },
    "TimeGranularity": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timegranularity.properties.code' /]",
          "type": "string"
        },
        "title": {
          "$ref": "#/definitions/InternationalString",
          "description": "[@messageEscape 'api.doc.swagger.definitions.timegranularity.properties.title' /]"
        }
      }
    },
    "TimeRepresentation": {
      "properties": {
        "code": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timerepresentation.properties.code' /]",
          "type": "string"
        },
        "granularityCode": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timerepresentation.properties.granularitycode' /]",
          "type": "string"
        },
        "title": {
          "description": "[@messageEscape 'api.doc.swagger.definitions.timerepresentation.properties.title' /]",
          "type": "string"
        }
      }
    }
  },
  "paths": {
    "/v1.0/geographicGranularities": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.geographicgranuarities.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.geographicgranuarities.get.description' /]",
        "operationId": "findGeographicGranularities",
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/GeographicalGranularityList"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.geographicgranuarities.get.summary' /]"
      }
    },
    "/v1.0/geographicalValues": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.geographicalvalues.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.geographicalvalues.get.description' /]",
        "operationId": "findGeographicalValues",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.geographicalvalues.get.parameters.subjectCode' /]",
            "in": "query",
            "name": "subjectCode",
            "required": false,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.geographicalvalues.get.parameters.systemcode' /]",
            "in": "query",
            "name": "systemCode",
            "required": false,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.geographicalvalues.get.parameters.geographicalgranularitycode' /]",
            "in": "query",
            "name": "geographicalGranularityCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/GeographicalValueList"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.geographicalvalues.get.summary' /]"
      }
    },
    "/v1.0/indicators": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicators.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.description' /]",
        "operationId": "findIndicators",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.parameters.q' /]",
            "in": "query",
            "name": "q",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.parameters.order' /]",
            "in": "query",
            "name": "order",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.parameters.limit' /]",
            "format": "int32",
            "in": "query",
            "name": "limit",
            "type": "integer"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.parameters.offset' /]",
            "format": "int32",
            "in": "query",
            "name": "offset",
            "type": "integer"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.parameters.fields' /]",
            "in": "query",
            "name": "fields",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.get.parameters.representation' /]",
            "in": "query",
            "name": "representation",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/IndicatorsPagination"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicators.get.summary' /]"
      }
    },
    "/v1.0/indicators/{indicatorCode}": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicators.code.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.get.description' /]",
        "operationId": "findIndicator",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.get.parameters.indicatorcode' /]",
            "in": "path",
            "name": "indicatorCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/Indicator"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicators.code.get.summary' /]"
      }
    },
    "/v1.0/indicators/{indicatorCode}/data": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.description' /]",
        "operationId": "findIndicator",
        "produces":[
           "application/json",
           "application/jsonstat+json"
        ],
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.parameters.indicatorcode' /]",
            "in": "path",
            "name": "indicatorCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.parameters.representation' /]",
            "in": "query",
            "name": "representation",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.parameters.granularity' /]",
            "in": "query",
            "name": "granularity",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.parameters.fields' /]",
            "in": "query",
            "name": "fields",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/Data"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicators.code.data.get.summary' /]"
      }
    },
    "/v1.0/indicatorsSystems": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.description' /]",
        "operationId": "findIndicatorsSystems",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.parameters.q' /]",
            "in": "query",
            "name": "q",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.parameters.order' /]",
            "in": "query",
            "name": "order",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.parameters.limit' /]",
            "format": "int32",
            "in": "query",
            "name": "limit",
            "type": "integer"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.parameters.offset' /]",
            "format": "int32",
            "in": "query",
            "name": "offset",
            "type": "integer"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/IndicatorsSystemsPagination"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.get.summary' /]"
      }
    },
    "/v1.0/indicatorsSystems/{indicatorSystemCode}": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.get.description' /]",
        "operationId": "findIndicatorsSystem",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.get.parameters.indicatorsystemcode' /]",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/IndicatorSystem"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.get.summary' /]"
      }
    },
    "/v1.0/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.description' /]",
        "operationId": "retrieveIndicatorsInstances",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.indicatorsystemcode' /]",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.q' /]",
            "in": "query",
            "name": "q",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.order' /]",
            "in": "query",
            "name": "order",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.limit' /]",
            "format": "int32",
            "in": "query",
            "name": "limit",
            "type": "integer"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.offset' /]",
            "format": "int32",
            "in": "query",
            "name": "offset",
            "type": "integer"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.fields' /]",
            "in": "query",
            "name": "fields",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.representation' /]",
            "in": "query",
            "name": "representation",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.parameters.granularity' /]",
            "in": "query",
            "name": "granularity",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/InstancesPagination"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.get.summary' /]"
      }
    },
    "/v1.0/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances/{indicatorInstanceCode}": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.get.description' /]",
        "operationId": "retrieveIndicatorsInstance",
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.get.parameters.indicatorsystemcode' /]",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.get.parameters.indicatorinstancecode' /]",
            "in": "path",
            "name": "indicatorInstanceCode",
            "required": true,
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/Instance"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.get.summary' /]"
      }
    },
    "/v1.0/indicatorsSystems/{indicatorSystemCode}/indicatorsInstances/{indicatorInstanceCode}/data": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.description' /]",
        "operationId": "retrieveIndicatorsInstanceData",
        "produces":[
           "application/json",
           "application/jsonstat+json"
        ],
        "parameters": [
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.parameters.indicatorsystemcode' /]",
            "in": "path",
            "name": "indicatorSystemCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.parameters.indicatorinstancecode' /]",
            "in": "path",
            "name": "indicatorInstanceCode",
            "required": true,
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.parameters.representation' /]",
            "in": "query",
            "name": "representation",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.parameters.granularity' /]",
            "in": "query",
            "name": "granularity",
            "type": "string"
          },
          {
            "description": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.parameters.fields' /]",
            "in": "query",
            "name": "fields",
            "type": "string"
          }
        ],
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/Data"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.indicatorssystems.code.indicatorsinstance.code.data.get.summary' /]"
      }
    },
    "/v1.0/subjects": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.subjects.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.subjects.get.description' /]",
        "operationId": "findSubjects",
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/SubjectList"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.subjects.get.summary' /]"
      }
    },
    "/v1.0/timeGranularities": {
      "get": {
        "tags": [ "[@messageEscape 'api.doc.swagger.paths.timegranularities.get.tags' /]" ],
        "description": "[@messageEscape 'api.doc.swagger.paths.timegranularities.get.description' /]",
        "operationId": "retrieveTimeGranularities",
        "responses": {
          "200": {
            "description": "[@messageEscape 'api.doc.swagger.paths.responses.200' /]",
            "schema": {
              "$ref": "#/definitions/TimeGranularitiesList"
            }
          }
        },
        "summary": "[@messageEscape 'api.doc.swagger.paths.timegranularities.get.summary' /]"
      }
    }
  }
}
