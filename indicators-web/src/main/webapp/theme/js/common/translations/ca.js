(function () {
    'use strict';

    EDatos.common.translations = EDatos.common.translations || {};
    EDatos.common.translations['ca'] = {
        'INDICATOR': {
            'TITLE': 'Indicador'
        },
        'COMMON': {
            'CLOSE': 'Tancar',
        },
        'OPTIONS': {
            'OLD_BROWSER_WARNING': 'Aquest estil no es visualitza correctament en alguns navegadors',
            'DATA': {
                'MEASURES': 'Mesures',
                'SYSTEM': 'Sistema',
                'SYSTEM_OR_SUBJECT': 'Mode de selecció de dades',
                'INDICATORS': 'Indicadors',
                'GEOGRAPHICAL_VALUES': 'Valors espacials',
                'TIME_GRANULARITIES': 'Granularitat temporal',
                'UPDATE_PREVIEW': 'Actualitzar vista',
                'RECENT': "Nombre d'indicadors recents",
                'ALL_INDICATORS': 'Tots els indicadors',
                'ONLY_MAIN_INDICATORS': 'Només els principals'
            },
            'STYLE': {
                'TITLE': 'Estil',
                'TITLE_FIELD': 'Títol',
                'CUSTOM': 'Personalitzat',
                'PREDETERMINED': 'Predeterminat',
                'TEXT_COLOR': 'Color del text',
                'HEADER_COLOR': 'Color de la capçalera',
                'TITLE_COLOR': 'Color del títol',
                'BORDER_COLOR': 'Color de la vora',
                'INDICATOR_NAME_COLOR': "Color del nombre de l'indicador",
                'WIDGET_WIDTH': 'Ample del widget',
                'WIDGET_HEIGHT': 'Altura del widget',
                'WIDGET_AUTO_HEIGHT': 'Automàtic',
                'BORDER_RADIUS': 'Vores arrodonides',
                'SHADOW': 'Ombra',
                'COLORS': {
                    'TITLE': 'Colors',
                    'BLUE': 'Blau',
                    'LIGHT_BLUE': 'Blau clar'
                },
                'SCALE': {
                    'TITLE': 'Escalament',
                    'NATURAL_LIB': {
                        'TITLE': 'Natural equilibrat',
                        'DESCRIPTION': "L'escalament utilitza increments que les persones reconeixen com a naturals a l'hora de comptar. A més, en la representació conjunta de números positius i negatius les línies d'escala coincideixen en valor absolut."
                    },
                    'NATURAL': {
                        'TITLE': 'Natural no equilibrado',
                        'DESCRIPTION': "L'escalament utilitza increments que les persones reconeixen com a naturals a l'hora de comptar. La visualització s'ajusta al valor mínim i màxim de la sèrie."
                    },
                    'MINMAX': {
                        'TITLE': 'No natural, no equilibrado',
                        'DESCRIPTION': "L'escalament utilitza increments millor ajustats a la sèrie de dades, però sense respectar els que les persones reconeixen com a naturals a l'hora de comptar. La visualització s'ajusta al valor mínim i màxim de la sèrie."
                    }
                },
                'VIEW': {
                    'TITLE': 'Visualització',
                    'SIDE_VIEW': 'Visualització lateral',
                    'SHOW_LABELS': "Mostrar etiquetes en l'eix x",
                    'SHOW_LEGEND': 'Mostrar llegenda'
                },
                'SPARKLINES': {
                    'TITLE': 'Sparklines',
                    'MAX': {
                        'TITLE': 'Nombre de dades a representar',
                        'DESCRIPTION': 'Si no indica un valor, o és superior al màxim, es limitarà el nombre de punts del sparkline al màxim permès.',
                    },
                    'TYPE': {
                        'LINE': 'Línies',
                        'BAR': 'Barres'
                    }
                },
                'LANGUAGE': {
                    'TITLE' : 'Idioma',
                    'DROPDOWN': {
                        'TITLE': 'Afegir selector d\'idioma'
                    },
                    'PREFERENCE': {
                        'TITLE': 'Idioma a utilitzar',
                        'NAVIGATOR': 'Idioma del navegador'
                    }
                },
                'MORE_INDICATORS': {
                    'TITLE': 'Més indicadors',
                    'LINK': {
                        'TITLE': 'Afegir enllaç a més indicadors'
                    }
                }
            },
        },
        'EMBED': {
            'TITLE': 'Incrustar widget',
            'MORE': 'més indicadors',
            'CREDITS': 'widget facilitat per',
            'HELP': 'Selecciona, còpia i pega aquest codi en la teva pàgina',
            'EXAMPLE': "Exemple d'ús",
            'ADD_TO_NETVIBES': 'Afegir a Netvibes'
        },
        'MEASURE': {
            'ABSOLUTE': 'Dada',
            'ANNUAL_PUNTUAL_RATE': 'Variació anual',
            'INTERPERIOD_PUNTUAL_RATE': 'Variació interperiòdica',
            'ANNUAL_PERCENTAGE_RATE': 'Taxa variació anual',
            'INTERPERIOD_PERCENTAGE_RATE': 'Taxa variació interperiòdica'
        },
        'LAST_DATA': {
            'TITLE': 'Darreres dades',
            'DESCRIPTION': 'Taula que mostra les darreres dades disponibles'
        },
        'TEMPORAL': {
            'TITLE': 'Sèrie temporal',
            'DESCRIPTION': "Gràfica que mostra l'evolució temporal d'un indicador per a diferents valors geogràfics"
        },
        'RECENT': {
            'TITLE': 'Darrers indicadors actualitzats'
        },
        'ERROR': {
            'INVALID_WIDGET_TYPE': 'Tipus de widget no suportat',
            'URL_NOT_PROVIDED': "Error, no s'ha especificat la url del servei web",
            'LEGACY_WIDGET_NAME': 'S\'ha detectat un widget que segueix fent ús del nom "IstacWidget". Si us plau, canvieu al nou nom: "EdatsIndicatorsWidget".'
        },
        'SELECT2': {
            'NO_MATCHES': 'No hi ha resultats',
            'LOADING': 'Carregant...'
        },
        'ECHARTS': {
            'time': {
                'month': [
                    'Gener', 'Febrer', 'Març', 'Abril', 'Maig', 'Juny',
                    'Juliol', 'Agost', 'Setembre', 'Octubre', 'Novembre', 'Desembre'
                ],
                'monthAbbr': [
                    'Gen', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Set', 'Oct', 'Nov', 'Des'
                ],
                'dayOfWeek': [
                    'Diumenge', 'Dilluns', 'Dimarts', 'Dimecres', 'Dijous', 'Divendres', 'Dissabte'
                ],
                'dayOfWeekAbbr': [
                    'Dg', 'Dl', 'Dt', 'Dc', 'Dj', 'Dv', 'Ds'
                ]
            }
        },
        'CAPTCHA': {
            'LABEL': "Escriu el valor de la imatge que es mostra a dalt"
        },
        'CONNECTOR': {
            'OF': ' de ',
            'CONTRACTED_OF': ' d\''
        },
        'MAIN_INDICATORS': {
            'ONLY_MAIN': {
                'TOOLTIP': 'Els indicadors principals són un subconjunt dels indicadors disponibles que han estat preseleccionats per considerar-se més rellevants.'
            }
        }
    };
}());
