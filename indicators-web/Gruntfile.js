module.exports = function (grunt) {
    grunt.loadTasks('tasks');

    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-webfont');

    var templatesPath = 'src/main/webapp/theme/templates';
    var lessPath = 'src/main/webapp/theme/css';
    var jsPath = 'src/main/webapp/theme/js';
    var widgetsPath = 'src/main/webapp/theme/js/widgets';

    var vendorSrc = [
        jsPath + '/libs/indicators-utils.js',
        jsPath + '/libs/jquery-1.7.1.js',
        jsPath + '/libs/jquery-ui-1.8.17.custom.js',
        jsPath + '/libs/jquery.json-2.3.min.js',
        jsPath + '/libs/jquery-disable-text-selection-1.0.0.js',
        jsPath + '/libs/underscore-min-1.3.1.js',
        jsPath + '/libs/underscore.string.min-v.2.0.0-57.js',
        jsPath + '/libs/backbone-min-0.9.2.js',
        jsPath + '/libs/i18n.js',
        jsPath + '/libs/colorpicker/js/colorpicker.js'
    ]

    var widgetsSrc = [
        widgetsPath + '/libs/underscore.js',
        widgetsPath + '/libs/jquery.js',
        widgetsPath + '/libs/handlebars.runtime-1.0.0.beta.6.js',
        widgetsPath + '/libs/jquery.sparkline.js',
        widgetsPath + '/libs/highcharts.js',
        widgetsPath + '/libs/moment.js',
        widgetsPath + '/src/Templates.js',
        widgetsPath + '/src/Istac.js',
        widgetsPath + '/src/Helper.js',
        widgetsPath + '/src/Dataset.js',
        widgetsPath + '/src/DatasetRequestBuilder.js',
        widgetsPath + '/src/Base.js',
        widgetsPath + '/src/LastData.js',
        widgetsPath + '/src/NaturalScale.js',
        widgetsPath + '/src/Temporal.js',
        widgetsPath + '/src/Loader.js',
        widgetsPath + '/src/Factory.js',
        widgetsPath + '/src/DateParser.js',
        widgetsPath + '/src/analytics.js',
    ];

    var appsSrc = [
        // lib
        jsPath + '/libs/underscore.string.min-v.2.0.0-57.js',
        jsPath + '/libs/handlebars-1.0.0.beta.6.js',
        jsPath + '/libs/Backbone.ModelBinder.js',
        jsPath + '/libs/select2/select2.js',
        jsPath + '/libs/jquery.qtip-1.0.0-rc3.min.js',
        // App
        jsPath + '/app/App.js',
        jsPath + '/app/Templates.js',

        // helpers
        jsPath + '/app/helpers/I18n.js',
        jsPath + '/app/helpers/HandlebarsHelpers.js',

        // Mixin
        jsPath + '/app/mixins/JsonpSync.js',


        // Model
        jsPath + '/app/models/Subject.js',
        jsPath + '/app/models/WidgetOptions.js',
        jsPath + '/app/models/IndicatorSystem.js',
        jsPath + '/app/models/GeographicalGranularity.js',
        jsPath + '/app/models/GeographicalValue.js',
        jsPath + '/app/models/IndicatorBase.js',
        jsPath + '/app/models/Indicator.js',
        jsPath + '/app/models/IndicatorInstance.js',
        jsPath + '/app/models/TimeGranularity.js',

        jsPath + '/app/collections/Measures.js',
        jsPath + '/app/collections/IndicatorsBase.js',
        jsPath + '/app/collections/IndicatorsInstances.js',
        jsPath + '/app/collections/IndicatorSystems.js',
        jsPath + '/app/collections/ToggleSelection.js',
        jsPath + '/app/collections/Subjects.js',
        jsPath + '/app/collections/Indicators.js',
        jsPath + '/app/collections/GeographicalGranularities.js',
        jsPath + '/app/collections/GeographicalValues.js',
        jsPath + '/app/collections/TimeGranularities.js',


        // Widget
        jsPath + '/widgets/libs/Class.js',
        jsPath + '/widgets/libs/raphael-min.js',
        jsPath + '/widgets/libs/highcharts.js',
        jsPath + '/widgets/libs/jquery.sparkline.js',
        jsPath + '/widgets/libs/moment.js',


        jsPath + '/widgets/src/Templates.js',
        jsPath + '/widgets/src/Istac.js',
        jsPath + '/widgets/src/Helper.js',
        jsPath + '/widgets/src/Dataset.js',
        jsPath + '/widgets/src/DatasetRequestBuilder.js',
        jsPath + '/widgets/src/Base.js',
        jsPath + '/widgets/src/LastData.js',
        jsPath + '/widgets/src/NaturalScale.js',
        jsPath + '/widgets/src/Temporal.js',
        jsPath + '/widgets/src/Loader.js',
        jsPath + '/widgets/src/Factory.js',
        jsPath + '/widgets/src/DateParser.js',

        // Vistas
        jsPath + '/app/views/TabView.js',

        jsPath + '/app/views/Select2View.js',
        jsPath + '/app/views/WidgetCodeView.js',
        jsPath + '/app/views/WidgetDataOptionsLastDataView.js',
        jsPath + '/app/views/WidgetDataOptionsRecentView.js',
        jsPath + '/app/views/WidgetDataOptionsTemporalView.js',
        jsPath + '/app/views/WidgetPreviewView.js',
        jsPath + '/app/views/WidgetStyleOptionsView.js',
        jsPath + '/app/views/WidgetView.js'
    ]

    grunt.initConfig({
        handlebars: {
            app: {
                root: templatesPath,
                src: templatesPath + "/**/*.html",
                dest: jsPath + '/app/Templates.js'
            },
            widgets: {
                root: widgetsPath + '/templates',
                src: widgetsPath + '/templates/*.html',
                dest: widgetsPath + '/src/Templates.js'
            }
        },
        less: {
            app: {
                src: lessPath + '/main.less',
                dest: lessPath + '/main.css',
                options: {
                    compress: true
                }
            }
        },
        uglify: {
            app: {
                src: appsSrc,
                dest: jsPath + '/app.min.js',
                separator: ';'
            },
            widgets: {
                src: widgetsSrc,
                dest: widgetsPath + '/widget.min.all.js',
                separator: ';'
            }
        },
        concat: {
            vendor: {
                src: vendorSrc,
                dest: jsPath + '/vendor.min.js',
            },
            widgets: {
                src: widgetsSrc,
                dest: widgetsPath + '/widget.min.all.js'
            }
        },
        watch: {
            less: {
                files: lessPath + "/**/*.less",
                tasks: 'less'
            },
            handlebarsWeb: {
                files: templatesPath + "/**/*.html",
                tasks: 'handlebars:app'
            },
            handlebarsWidgets: {
                files: widgetsPath + '/templates/*.html',
                tasks: 'handlebars:widgets'
            },
            widgets: {
                files: [widgetsPath + '/templates/*.html', widgetsPath + '/src/*.js', widgetsPath + "/libs/*.js"],
                tasks: ['handlebars:widgets', 'concat:widgets']
            }
        },
        webfont: {
            icons: {
                src: 'src/main/webapp/theme/icons/*.svg',
                dest: 'src/main/webapp/theme/fonts',
                destCss: 'src/main/webapp/theme/css',
                options: {
                    relativeFontPath: "../fonts/",
                    destHtml: 'src/main/webapp/theme/fonts',
                    stylesheet: "less",
                    hashes: false,
                    syntax: "bootstrap"
                }
            },
            css: {
                src: 'src/main/webapp/theme/icons/*.svg',
                dest: 'src/main/webapp/theme/fonts',
                destCss: 'src/main/webapp/theme/css',
                options: {
                    relativeFontPath: "../fonts/",
                    destHtml: 'src/main/webapp/theme/fonts',
                    hashes: false,
                    syntax: "bootstrap"
                }
            }
        }
    });

    grunt.registerTask('widgets', ["handlebars:widgets", "uglify:widgets"]);
    grunt.registerTask('widgets:dev', ["handlebars:widgets", "concat:widgets"]);
    grunt.registerTask("app", ["handlebars:app", "less:app", "uglify:app", "concat:vendor"]);
    grunt.registerTask('default:dev', ["app", "widgets:dev"]);
    grunt.registerTask('default', ["app", "widgets"]);

};
