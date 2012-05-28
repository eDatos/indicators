module.exports = function (grunt) {
    var min = 'widget.min.js',
        minAll = 'widget.min.all.js';

    grunt.initConfig({
        concat : {
            dist : {
                src : ['libs/jquery-1.7.1.js', 'libs/raphael-min.js', 'libs/elycharts.min.js', min],
                dest : minAll,
                separator : ';'
            }
        },
        min : {
            dist : {
                src : ['widget.js'],
                dest : min
            }
        },
        watch : {
            files : 'widget.js',
            tasks : 'default'
        }
    });
    grunt.registerTask('default', 'min concat');
};