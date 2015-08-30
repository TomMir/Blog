'use strict';

angular.module('blogApp').controller('EntryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Entry', 'Blog', 'Tag',
        function ($scope, $stateParams, $modalInstance, entity, Entry, Blog, Tag) {

            $scope.entry = entity;
            $scope.blogs = Blog.query();
            $scope.tags = Tag.query();
            $scope.load = function (id) {
                Entry.get({id: id}, function (result) {
                    $scope.entry = result;
                });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('blogApp:entryUpdate', result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                if ($scope.entry.id != null) {
                    Entry.update($scope.entry, onSaveFinished);
                } else {
                    Entry.save($scope.entry, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
