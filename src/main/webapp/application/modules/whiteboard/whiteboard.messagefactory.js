(function(angular) {
    'use strict';

    angular
        .module('gitboard.whiteboard')
        .factory('MessageFactory', function(CurrentBoard) {
            var MESSAGE_PACKAGE = 'org.evilkitten.gitboard.application.services.atmosphere.message',
                BASE_MESSAGE = {
                    toJson: function() {
                        return angular.toJson(this);
                    }
                },
                messages = {
                    createAddShapeAction: function(shape) {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.AddShapeActionMessage',
                            boardId: CurrentBoard,
                            shape: shape
                        });
                    },

                    createHeartbeat: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.HeartbeatMessage',
                            boardId: CurrentBoard
                        });
                    },

                    createQuery: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.QueryMessage',
                            boardId: CurrentBoard
                        });
                    },

                    createWelcome: function() {
                        return angular.extend({}, BASE_MESSAGE, {
                            type: MESSAGE_PACKAGE + '.WelcomeMessage',
                            boardId: CurrentBoard
                        });
                    }
                };

            return messages;
        });
}(window.angular));
