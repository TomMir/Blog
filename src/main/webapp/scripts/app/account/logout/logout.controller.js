'use strict';

angular.module('blogApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
