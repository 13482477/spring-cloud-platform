/**
 * Created by jhonelee on 2017/7/31.
 */
define(
    ['./module'],
    function(dirApp) {
        'use strict;'
        dirApp.directive('paasNavigation', function(){
            return {
                restrict : 'E',
                templateUrl : 'views/directives/paas-navigation.html'
            }
        });
    }
);
