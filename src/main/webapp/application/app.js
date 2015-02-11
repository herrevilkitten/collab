(function(angular) {
    'use strict';

    angular.module('svb.modules', [
        'svb.frontpage',
        'svb.whiteboard',
        'svb.user'
    ]);

    angular.module('svb', [ 'ngRoute', 'svb.modules', 'colorpicker.module', 'xeditable' ])
        .config(function($routeProvider, $locationProvider) {
            $locationProvider.html5Mode(true);

            $routeProvider.when('/home', {
                templateUrl: 'application/modules/frontpage/frontpage.html',
                controller: 'FrontPageController'
            });
            $routeProvider.when('/board', {
                templateUrl: 'application/modules/whiteboard/whiteboard.html',
                controller: 'WhiteBoardController'
            });
            $routeProvider.otherwise({
                redirectTo: '/home'
            });
        }).run(function($rootScope, editableOptions) {
            $rootScope.$on('$rootChangeError', function(event, current, previous, rejection) {
                window.console.log(event, current, previous, rejection);
            });

            editableOptions.theme = 'bs3';
        });
}(window.angular));
