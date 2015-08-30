'use strict';

angular.module('blogApp')
    .controller('BlogController', function ($scope, Blog, ParseLinks) {
        $scope.blogs = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Blog.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.blogs = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Blog.get({id: id}, function (result) {
                $scope.blog = result;
                $('#deleteBlogConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Blog.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBlogConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.blog = {name: null, handle: null, id: null};
        };
    });
