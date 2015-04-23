(function(angular, SVG, gapi, $) {
    'use strict';

    angular.module('gitboard.user', [])
        .factory('CurrentUser', function() {
            return window.user;
        })
        .controller('UserController', function($scope, $log, $http, $location) {
            $scope.signOut = function() {
                $log.info('Signing out');
                $http.get('api/user/signout').then(function(r) {
                    window.console.log('r', r);
                    window.location = 'signin';
//                        $location.path('/signin');
//                        gapi.auth.signOut();
                });
            };
        }
    );
}(window.angular, window.SVG, window.gapi, window.jQuery));
