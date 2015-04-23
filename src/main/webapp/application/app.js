(function(angular) {
    'use strict';

    console.log('Setting up application modules');
    angular.module('gitboard.modules', [
        'gitboard.frontpage',
        'gitboard.user',
        'gitboard.whiteboard'
    ]);

    console.log('Setting up angular application');
    angular.module('gitboard', [ 'ngRoute', 'gitboard.modules', 'colorpicker.module', 'xeditable', 'toastr' ])
        .config(function($routeProvider, $locationProvider) {
            $locationProvider.html5Mode(true);

            console.log('Configuring routes');
            $routeProvider.when('/home', {
                templateUrl: 'application/modules/frontpage/frontpage.html',
                controller: 'FrontPageController'
            });
            $routeProvider.when('/board/:boardId', {
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
