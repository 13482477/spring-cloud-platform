require.config({
    baseUrl : 'scripts/js',
    paths : {
        'angular'   :   '/bower_components/angular/angular.min',
        'uiRouter'  :   '/bower_components/angular-ui-router/release/angular-ui-router.min',
        'jquery'    :   '/bower_components/AdminLTE/plugins/jQuery/jquery-2.2.3.min',
        'bootstrap' :   '/bower_components/AdminLTE/bootstrap/js/bootstrap',
        'adminLTE'  :   '/bower_components/AdminLTE/dist/js/app.min',
        'domReady'  :   '/bower_components/domReady/domReady',
        'app'       :   'app',
        'log'       :   'log',
        'start'     :   'start'
    },
    shim : {
        'angular'   : {
            exports :   'angular'
        },
        'uiRouter'  : {
            deps    :   ['angular'],
            exports :   'uiRouter'
        },
        'jquery'    : {
            exports :   'jquery'
        },
        'bootstrap' : {
            deps    :   ['jquery'],
        },
        'adminLTE'  : {
            deps    :   ['jquery', 'bootstrap'],
            exports :   'adminLTE'
        }
    },
    deps : [
        'start'
    ]
});