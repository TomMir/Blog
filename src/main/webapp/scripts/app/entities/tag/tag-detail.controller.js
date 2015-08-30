'use strict';

angular.module('blogApp')
    .controller('TagDetailController', function ($scope, $rootScope, $stateParams, entity, Tag) {
        $scope.tag = entity;
        $scope.load = function (id) {
            Tag.get({id: id}, function (result) {
                $scope.tag = result;
            });
        };
        $rootScope.$on('blogApp:tagUpdate', function (event, result) {
            $scope.tag = result;
        });
    });
