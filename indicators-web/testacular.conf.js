// Testacular configuration
// Generated on Fri Nov 02 2012 09:24:47 GMT+0000 (Hora estándar GMT)


// base path, that will be used to resolve files and exclude
basePath = './';



// list of files / patterns to load in the browser
files = [
    JASMINE,
    JASMINE_ADAPTER,

    'src/main/webapp/theme/js/libs/jquery-1.7.1.js',
    'src/main/webapp/theme/js/libs/underscore-min-1.3.1.js',
    'src/main/webapp/theme/js/libs/backbone-min-0.9.2.js',
    'src/main/webapp/theme/js/libs/handlebars-1.0.0.beta.6.js',
    'src/main/webapp/theme/js/libs/Backbone.ModelBinder.js',
    'src/main/webapp/theme/js/libs/*',
    'src/main/webapp/theme/js/app/App.js',
    'src/main/webapp/theme/js/app/Templates.js',

    'src/main/webapp/theme/js/app/mixins/*',

    'src/main/webapp/theme/js/app/models/*',
    'src/main/webapp/theme/js/app/collections/IndicatorInstances.js',
    'src/main/webapp/theme/js/app/collections/Geographicals.js',
    'src/main/webapp/theme/js/app/collections/IndicatorSystems.js',
    'src/main/webapp/theme/js/app/views/WidgetStyleOptionsView.js',


    'src/main/webapp/theme/js/widgets/src/Istac.js',
    'src/main/webapp/theme/js/widgets/src/Helper.js',
    'src/main/webapp/theme/js/widgets/src/Templates.js',
    'src/main/webapp/theme/js/widgets/src/Dataset.js',
    'src/main/webapp/theme/js/widgets/src/Base.js',
    'src/main/webapp/theme/js/widgets/src/LastData.js',
    'src/main/webapp/theme/js/widgets/src/Temporal.js',
    'src/main/webapp/theme/js/widgets/src/Loader.js',
    'src/main/webapp/theme/js/widgets/src/Factory.js',

    'src/test/javascript/spec/*.js',
    'src/test/javascript/spec/collections/*.js',
    'src/test/javascript/spec/models/*.js',
    'src/test/javascript/spec/views/*.js',
    'src/test/javascript/spec/widgets/*.js'
];

// list of files to exclude
exclude = [

];

// test results reporter to use
// possible values: 'dots', 'progress', 'junit'
reporters = ['progress'];

// web server port
port = 8080;

// cli runner port
runnerPort = 9100;

// enable / disable colors in the output (reporters and logs)
colors = true;

// level of logging
// possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
logLevel = LOG_INFO;

// enable / disable watching file and executing tests whenever any file changes
autoWatch = true;

// Start these browsers, currently available:
// - Chrome
// - ChromeCanary
// - Firefox
// - Opera
// - Safari (only Mac)
// - PhantomJS
// - IE (only Windows)
browsers = [];

// If browser does not capture in given timeout [ms], kill it
captureTimeout = 5000;

// Continuous Integration mode
// if true, it capture browsers, run tests and exit
singleRun = false;
