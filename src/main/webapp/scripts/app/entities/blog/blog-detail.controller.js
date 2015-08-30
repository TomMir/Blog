'use strict';

angular.module('blogApp')
    .controller('BlogDetailController', function ($scope, $rootScope, $stateParams, entity, Blog, User) {
        $scope.blog = entity;
        $scope.load = function (id) {
            Blog.get({id: id}, function (result) {
                $scope.blog = result;
            });
        };
        $rootScope.$on('blogApp:blogUpdate', function (event, result) {
            $scope.blog = result;
        });
    });
