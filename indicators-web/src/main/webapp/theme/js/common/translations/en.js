(function () {
    "use strict";

    EDatos.common.translations = EDatos.common.translations || {};
    EDatos.common.translations["en"] = {
        'INDICATOR': {
            'TITLE': 'Indicator'
        },
        'COMMON': {
            'CLOSE': 'Close',
        },
        'OPTIONS': {
            'OLD_BROWSER_WARNING': 'This style will not display correctly on some browsers',
            'DATA': {
                'MEASURES': 'Measures',
                'SYSTEM': 'System',
                'SYSTEM_OR_SUBJECT': 'System or subject',
                'SUBJECT': 'Subject',
                'INDICATORS': 'Indicators',
                'GEOGRAPHICAL_VALUES': 'Geographical values',
                'TIME_GRANULARITIES': 'Time granularities',
                'UPDATE_PREVIEW': 'Update preview',
                'RECENT': 'Number of recent indicators'
            },
            'STYLE': {
                'TITLE': 'Style',
                'TITLE_FIELD': 'Title',
                'CUSTOM': 'Custom',
                'PREDETERMINED': 'Predetermined',
                'TEXT_COLOR': 'Text color',
                'HEADER_COLOR': 'Header color',
                'TITLE_COLOR': 'Title color',
                'BORDER_COLOR': 'Border color',
                'INDICATOR_NAME_COLOR': 'Indicator name color',
                'WIDGET_WIDTH': 'Widget width',
                'BORDER_RADIUS': 'Border radius',
                'SHADOW': 'Shadow',
                'COLORS': {
                    'TITLE': 'Colors',
                    'BLUE': 'Blue',
                    'LIGHT_BLUE': 'Light blue'
                },
                'SCALE': {
                    'TITLE': 'Scale',
                    'NATURAL_LIB': {
                        'TITLE': 'Natural, balanced',
                        'DESCRIPTION': 'Scaling uses increments that people recognize as natural when counting. In addition, when positive and negative numbers are represented, scale lines coincide in absolute values.'
                    },
                    'NATURAL': {
                        'TITLE': 'Natural, not balanced',
                        'DESCRIPTION': 'Scaling uses increments that people recognize as natural when counting. Visualization is set to the max and min serie values.'
                    },
                    'MINMAX': {
                        'TITLE': 'Not natural, not balanced',
                        'DESCRIPTION': 'Scaling uses increments better adjusted to the data serie values, respecting those that people recognize as natural when counting. Visualization is set to the max and min serie values.'
                    }
                },
                'VIEW': {
                    'TITLE': 'View',
                    'SIDE_VIEW': 'Side view',
                    'SHOW_LABELS': 'Show labels on axis x',
                    'SHOW_LEGEND': 'Show legend'
                },
                'SPARKLINES': {
                    'TITLE': 'Sparklines',
                    'MAX': {
                        'TITLE': 'Number of data points to show',
                        'DESCRIPTION': 'If no value is provided, or if its above the maximum, it will be set to the maximum allowed.',
                    },
                    'TYPE': {
                        'LINE': 'Lines',
                        'BAR': 'Bars'
                    }
                }
            },
        },
        'EMBED': {
            'TITLE': 'Embed widget',
            'MORE': 'more indicators',
            'CREDITS': 'widget provided by',
            'HELP': 'Select, copy and paste this code on your webpage',
            'EXAMPLE': 'Use example',
            'ADD_TO_NETVIBES': 'Add to Netvibes'
        },
        'MEASURE': {
            'ABSOLUTE': 'Absolute',
            'ANNUAL_PUNTUAL_RATE': 'Annual puntual rate',
            'INTERPERIOD_PUNTUAL_RATE': 'Interperiod puntual rate',
            'ANNUAL_PERCENTAGE_RATE': 'Annual percentage rate',
            'INTERPERIOD_PERCENTAGE_RATE': 'Interperiod percentage rate'
        },
        'LAST_DATA': {
            'TITLE': 'Last data',
            'DESCRIPTION': 'Table showing the latest available data'
        },
        'TEMPORAL': {
            'TITLE': 'Temporal serie',
            'DESCRIPTION': 'Chart showing the time evolution of an indicator for different geographical values'
        },
        'ERROR': {
            'INVALID_WIDGET_TYPE': 'Unsupported widget type',
            'URL_NOT_PROVIDED': 'Error, web service url not provided'
        },
        'SELECT2': {
            'NO_MATCHES': 'No matches',
            'LOADING': 'Loading...'
        },
        'HIGHCHARTS': {
            'months': ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            'weekdays': ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            'shortMonths': ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            // A priori no cambiamos esto para ser coherentes con ale existencia de addThousandSeparator
            'thousandsSep': '.',
            'decimalPoint': ','
        },
        'CAPTCHA': {
            'LABEL': "Write the value of the image shown above"
        }
    };
}());