module.exports = function (grunt) {

    grunt.registerMultiTask("handlebars", "Compile handlebars templates", function () {
        var path = require('path');
        var src = this.file.src;
        var root = this.data.root;
        var dest = this.file.dest;
        var options = this.data.options || {};
        var srcFiles = grunt.file.expandFiles(src);

        var templatesSrc = srcFiles.map(function (srcFile) {
            var rootRelative = root ? srcFile.substring(root.length + 1) : srcFile;
            var ext = path.extname(rootRelative);
            var dir = path.dirname(rootRelative);
            var baseName = path.basename(rootRelative, ext);

            var parts = [];
            if (dir !== ".") {
                parts.push(dir);
            }
            parts.push(baseName);
            var name = parts.join("/");
            return {
                src : srcFile,
                name : name
            };
        });

        var done = this.async();
        grunt.helper('handlebars', templatesSrc, options, function (errors, templates) {
            if (errors.length > 0) {
                grunt.warn(errors.join('\n'));
                done(false);
                return;
            }
            grunt.file.write(dest, templates);
            done();
        });
    });

    grunt.registerHelper("handlebars", function (templates, options, callback) {
        var Handlebars = require("handlebars");
        var contents = "(function() {\n  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};\n";

        var errors = [];

        contents += templates.map(
            function (template) {
                var templateFunction = "";
                try {
                    grunt.verbose.writeln('Precompiling ' + template.name + " << " + template.src);
                    templateFunction = 'templates[\'' + template.name + '\'] = template(' + Handlebars.precompile(grunt.file.read(template.src)) + ');\n';
                    return templateFunction;
                } catch (err) {
                    errors.push("\n" + template.src + "\n------------------------------------------------\n" + err);
                }
            }).join("\n");

        contents += "})();";
        callback(errors, contents);
    });

}
