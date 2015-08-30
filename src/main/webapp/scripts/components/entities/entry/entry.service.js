'use strict';

angular.module('blogApp')
    .factory('Entry', function ($resource, DateUtils) {
        return $resource('api/entrys/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
