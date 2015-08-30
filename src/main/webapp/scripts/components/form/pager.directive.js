/* globals $ */
'use strict';

angular.module('blogApp')
    .directive('blogAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
