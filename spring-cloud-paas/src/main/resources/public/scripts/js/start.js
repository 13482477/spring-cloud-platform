/**
 * Created by jhonelee on 2017/7/28.
 */
define([
        'require',
        'angular',
        'jquery',
        'bootstrap',
        'adminLTE',
        'domReady',
        'app'
    ],
    function (require, angular) {
        require(['domReady!'], function(document) {
            angular.bootstrap(document, ['app']);
        });
    }
);