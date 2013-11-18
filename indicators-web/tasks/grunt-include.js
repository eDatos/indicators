module.exports = function (grunt) {

    var _ = require('underscore');
    var path = require('path');

    function generateHtml (prefix, file) {
        var extension = path.extname(file);
        if (extension === ".js") {
            return "<script src='" + prefix + file + "'></script>\n";
        } else if (extension === ".css") {
            return "<link rel='stylesheet' href='" + prefix + file + "'>\n";
        } else {
            grunt.log.writeln('error in file ' + file);
        }
    }

    grunt.registerMultiTask('include', '', function () {
        var env = this.target;
        var destPath = this.data.dest;
        var pathToAdd = this.data.pathToAdd;
        var pathToRemove = this.data.pathToRemove;

        var prop;
        if (this.data.dev) {
            prop = 'src';
        } else {
            prop = 'dest';
        }

        if (prop) {
            var modules = this.data.modules;
            _.each(modules, function (moduleContent, moduleName) {
                var content = "";
                var files = grunt.file.expandFiles(moduleContent[prop]);
                _.each(files, function (moduleFile) {
                    var relativePath = path.relative(pathToRemove, moduleFile);

                    //force unix-style path separator
                    relativePath = relativePath.split(path.sep).join('/');

                    content += generateHtml(pathToAdd, relativePath);
                });

                var includeFile = destPath + "/" + moduleName + ".html";
                grunt.file.write(includeFile, content);
            });
        }

    });

};
