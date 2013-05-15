module.exports = function (grunt) {
    var _ = require('underscore');
    grunt.loadTasks('tasks');
    grunt.loadNpmTasks('grunt-less');

    var templatesPath = 'src/main/webapp/theme/templates';
    var lessPath = 'src/main/webapp/theme/css/';
    var jsPath = 'src/main/webapp/theme/js';
    var widgetsPath = 'src/main/webapp/theme/js/widgets';


    var widgetsSrc = [
        widgetsPath + '/libs/underscore.js',
        widgetsPath + '/libs/jquery.js',
        widgetsPath + '/libs/handlebars.runtime-1.0.0.beta.6.js',
        widgetsPath + '/libs/jquery.sparkline.js',
        widgetsPath + '/libs/raphael-min.js',
        widgetsPath + '/libs/elycharts.min.js',
        widgetsPath + '/src/analytics.js',
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
        widgetsPath + '/src/Factory.js'
    ];


    grunt.initConfig({
        handlebars : {
            all : {
                root : templatesPath,
                src : templatesPath + "/**/*.html",
                dest : jsPath + '/app/Templates.js'
            },
            widgets : {
                root : widgetsPath + '/templates',
                src : widgetsPath + '/templates/*.html',
                dest : widgetsPath + '/src/Templates.js'
            }
        },
        less : {
            all : {
                src : lessPath + '/main.less',
                dest : lessPath + '/main.css',
                options : {
                    compress : true
                }
            }
        },
        min : {
            widgets : {
                src : widgetsSrc,
                dest : widgetsPath + '/widget.min.all.js',
                separator : ';'
            }
        },
        concat : {
            widgets : {
                src : widgetsSrc,
                dest : widgetsPath + '/widget.min.all.js'
            }
        },
        watch : {
            less : {
                files : lessPath + "/**/*.less",
                tasks : 'less'
            },
            handlebarsWeb : {
                files : templatesPath + "/**/*.html",
                tasks : 'handlebars:all'
            },
            handlebarsWidgets : {
                files : widgetsPath + '/templates/*.html',
                tasks : 'handlebars:widgets'
            },
            widgets : {
               files : [widgetsPath + '/templates/*.html', widgetsPath + '/src/*.js'],
               tasks : ['handlebars:widgets', 'concat:widgets']
	        }
        }
    });

    grunt.registerTask('widgets', 'handlebars:widgets min:widgets');
    grunt.registerTask('widgets:dev', 'handlebars:widgets concat:widgets');

    grunt.registerTask('default', 'handlebars less min');

};
