/**
 * Created by jhonelee on 2017/7/31.
 */
define([],
    function() {
        var log = {};
        log.log = function(str) {
            document.write(str);
        };
        return log;
    }
);