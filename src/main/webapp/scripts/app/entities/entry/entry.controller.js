'use strict';

angular.module('blogApp')
    .controller('EntryController', function ($scope, Entry, ParseLinks) {
        $scope.entrys = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Entry.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.entrys = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Entry.get({id: id}, function (result) {
                $scope.entry = result;
                $('#deleteEntryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Entry.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEntryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.entry = {name: null, content: null, date: null, id: null};
        };
    });
