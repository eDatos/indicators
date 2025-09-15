(function () {
    'use strict';

    EDatos.common.translations = EDatos.common.translations || {};
    EDatos.common.translations['es'] = {
        'INDICATOR': {
            'TITLE': 'Indicador'
        },
        'COMMON': {
            'CLOSE': 'Cerrar',
        },
        'OPTIONS': {
            'OLD_BROWSER_WARNING': 'Este estilo no se visualiza correctamente en algunos navegadores',
            'DATA': {
                'MEASURES': 'Medidas',
                'SYSTEM': 'Sistema',
                'SYSTEM_OR_SUBJECT': 'Modo de selección de datos',
                'SUBJECT': 'Tema',
                'ALL_SUBJECT': 'Todos los temas',
                'INDICATORS': 'Indicadores',
                'GEOGRAPHICAL_VALUES': 'Valores espaciales',
                'TIME_GRANULARITIES': 'Granularidad temporal',
                'UPDATE_PREVIEW': 'Actualizar vista',
                'RECENT': 'Número de indicadores recientes',
                'ALL_INDICATORS': 'Todos los indicadores',
                'ONLY_MAIN_INDICATORS': 'Sólo los principales'
            },
            'STYLE': {
                'TITLE': 'Estilo',
                'TITLE_FIELD': 'Título',
                'CUSTOM': 'Personalizado',
                'PREDETERMINED': 'Predeterminado',
                'TEXT_COLOR': 'Color del texto',
                'HEADER_COLOR': 'Color de la cabecera',
                'TITLE_COLOR': 'Color del título',
                'BORDER_COLOR': 'Color del borde',
                'INDICATOR_NAME_COLOR': 'Color del nombre del indicador',
                'WIDGET_WIDTH': 'Ancho del widget',
                'WIDGET_HEIGHT': 'Altura del widget',
                'WIDGET_AUTO_HEIGHT': 'Automático',
                'BORDER_RADIUS': 'Bordes redondeados',
                'SHADOW': 'Sombra',
                'COLORS': {
                    'TITLE': 'Colores',
                    'BLUE': 'Azul',
                    'LIGHT_BLUE': 'Azul claro'
                },
                'SCALE': {
                    'TITLE': 'Escalamiento',
                    'NATURAL_LIB': {
                        'TITLE': 'Natural equilibrado',
                        'DESCRIPTION': 'El escalamiento utiliza incrementos que las personas reconocen como naturales a la hora de contar. Además, en la representación conjunta de números positivos y negativos las líneas de escala coinciden en valor absoluto.',
                    },
                    'NATURAL': {
                        'TITLE': 'Natural no equilibrado',
                        'DESCRIPTION': 'El escalamiento utiliza incrementos que las personas reconocen como naturales a la hora de contar. La visualización se ajusta al valor mínimo y máximo de la serie.'
                    },
                    'MINMAX': {
                        'TITLE': 'No natural, no equilibrado',
                        'DESCRIPTION': 'El escalamiento utiliza incrementos mejor ajustados a la serie de datos, pero sin respetar los que las personas reconocen como naturales a la hora de contar. La visualización se ajusta al valor mínimo y máximo de la serie.'
                    }
                },
                'VIEW': {
                    'TITLE': 'Visualización',
                    'SIDE_VIEW': 'Visualización lateral',
                    'SHOW_LABELS': 'Mostrar etiquetas en el eje x',
                    'SHOW_LEGEND': 'Mostrar leyenda'
                },
                'SPARKLINES': {
                    'TITLE': 'Sparklines',
                    'MAX': {
                        'TITLE': 'Número de datos a representar',
                        'DESCRIPTION': 'Si no indica un valor, o es superior al máximo, se limitará el número de puntos del sparkline al máximo permitido.',
                    },
                    'TYPE': {
                        'LINE': 'Líneas',
                        'BAR': 'Barras'
                    }
                },
                'LANGUAGE': {
                    'TITLE' : 'Idioma',
                    'DROPDOWN': {
                        'TITLE': 'Añadir selector de idioma'
                    },
                    'PREFERENCE': {
                        'TITLE': 'Idioma a utilizar',
                        'NAVIGATOR': 'Idioma del navegador'
                    }
                },
                'MORE_INDICATORS': {
                    'TITLE': 'Más indicadores',
                    'LINK': {
                        'TITLE': 'Añadir enlace a más indicadores'
                    }
                }
            },
        },
        'EMBED': {
            'TITLE': 'Incrustar widget',
            'MORE': 'más indicadores',
            'CREDITS': 'widget facilitado por',
            'HELP': 'Selecciona, copia y pega este código en tu página',
            'EXAMPLE': 'Ejemplo de uso',
            'ADD_TO_NETVIBES': 'Añadir a Netvibes'
        },
        'MEASURE': {
            'ABSOLUTE': 'Dato',
            'ANNUAL_PUNTUAL_RATE': 'Variación anual',
            'INTERPERIOD_PUNTUAL_RATE': 'Variación interperiódica',
            'ANNUAL_PERCENTAGE_RATE': 'Tasa variación anual',
            'INTERPERIOD_PERCENTAGE_RATE': 'Tasa variación interperiódica'
        },
        'LAST_DATA': {
            'TITLE': 'Últimos datos',
            'DESCRIPTION': 'Tabla que muestra los últimos datos disponibles'
        },
        'TEMPORAL': {
            'TITLE': 'Serie temporal',
            'DESCRIPTION': 'Gráfica que muestra la evolución temporal de un indicador para diferentes valores geográficos'
        },
        'RECENT': {
            'TITLE': 'Últimos indicadores actualizados'
        },
        'ERROR': {
            'INVALID_WIDGET_TYPE': 'Tipo de widget no soportado',
            'URL_NOT_PROVIDED': 'Error, no se ha especificado la url del servicio web',
            'LEGACY_WIDGET_NAME': 'Se ha detectado un widget que sigue haciendo uso del nombre "IstacWidget". Por favor, cambie al nuevo nombre: "EdatosIndicatorsWidget".'
        },
        'SELECT2': {
            'NO_MATCHES': 'No hay resultados',
            'LOADING': 'Cargando...'
        },
        'PICKLIST': {
            'FILTER_PLACEHOLDER': 'Escribe para filtrar indicadores',
            'MOVE_SELECTED_TO_FIRST_LIST': 'Quitar los indicadores seleccionados del widget',
            'MOVE_ALL_TO_FIRST_LIST': 'Quitar todos los indicadores del widget',
            'MOVE_SELECTED_TO_SECOND_LIST': 'Añadir los indicadores seleccionado al widget'
        },
        'ECHARTS': {
            'time': {
                'month': [
                    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
                    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
                ],
                'monthAbbr': [
                    'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'
                ],
                'dayOfWeek': [
                    'Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'
                ],
                'dayOfWeekAbbr': [
                    'Dom', 'Lun', 'Mar', 'Miérc', 'Juev', 'Vier', 'Sáb'
                ]
            }
        },
        'CAPTCHA': {
            'LABEL': "Escriba el valor de la imagen mostrada encima"
        },
        'CONNECTOR': {
            'OF': ' de ',
            'CONTRACTED_OF': ' de '
        },
        'MAIN_INDICATORS': {
            'ONLY_MAIN': {
                'TOOLTIP': 'Los indicadores principales son un subconjunto de los indicadores disponibles que han sido preseleccionados por considerarse más relevantes.'
            }
        }
    };
}());
