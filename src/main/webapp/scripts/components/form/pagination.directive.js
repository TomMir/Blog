/* globals $ */
'use strict';

angular.module('blogApp')
    .directive('blogAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
