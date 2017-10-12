/**
 * Created by jhonelee on 2017/7/31.
 */
define(
    ['./module'],
    function(dirApp) {
        'use strict;'
        dirApp.directive('paasHeader', function(){
            return {
                restrict : 'E',
                templateUrl : 'views/directives/paas-header.html'
            }
        });
    }
);
