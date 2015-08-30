'use strict';

angular.module('blogApp')
    .controller('EntryDetailController', function ($scope, $rootScope, $stateParams, entity, Entry, Blog, Tag) {
        $scope.entry = entity;
        $scope.load = function (id) {
            Entry.get({id: id}, function (result) {
                $scope.entry = result;
            });
        };
        $rootScope.$on('blogApp:entryUpdate', function (event, result) {
            $scope.entry = result;
        });
    });
