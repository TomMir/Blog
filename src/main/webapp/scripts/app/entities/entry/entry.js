'use strict';

angular.module('blogApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entry', {
                parent: 'entity',
                url: '/entrys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'blogApp.entry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/entry/entrys.html',
                        controller: 'EntryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('entry');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('entry.detail', {
                parent: 'entity',
                url: '/entry/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'blogApp.entry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/entry/entry-detail.html',
                        controller: 'EntryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('entry');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Entry', function ($stateParams, Entry) {
                        return Entry.get({id: $stateParams.id});
                    }]
                }
            })
            .state('entry.new', {
                parent: 'entry',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/entry/entry-dialog.html',
                        controller: 'EntryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, content: null, date: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                            $state.go('entry', null, {reload: true});
                        }, function () {
                            $state.go('entry');
                        })
                }]
            })
            .state('entry.edit', {
                parent: 'entry',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/entry/entry-dialog.html',
                        controller: 'EntryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Entry', function (Entry) {
                                return Entry.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function (result) {
                            $state.go('entry', null, {reload: true});
                        }, function () {
                            $state.go('^');
                        })
                }]
            });
    });
