'use strict';

angular.module('blogApp').controller('BlogDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Blog', 'User',
        function ($scope, $stateParams, $modalInstance, entity, Blog, User) {

            $scope.blog = entity;
            $scope.users = User.query();
            $scope.load = function (id) {
                Blog.get({id: id}, function (result) {
                    $scope.blog = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('blogApp:blogUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.blog.id != null) {
                    Blog.update($scope.blog, onSaveFinished);
                } else {
                    Blog.save($scope.blog, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
