(function(angular, $) {
    'use strict';

    angular
        .module('gitboard.whiteboard')
        .service('SocketFactory', function($location, $log, CurrentBoard) {
            var atmosphere = $.atmosphere,
                Socket = function(options) {
                    options = options || {};
                    this.url = 'chat/' + CurrentBoard;
                    this.contentType = options.contentType || 'application/json';
                    this.logLevel = options.logLevel || 'debug';
                    this.transport = options.transport || 'websocket';
                    this.fallbackTransport = options.fallbackTransport || 'polling';
                    this.socket = null;
                    this.request = null;
                    this.handlers = {
                        open: [],
                        close: [],
                        message: [],
                        error: [],
                        reconnect: [],
                        messagePublished: [],
                        clientTimeout: [],
                        transportFailure: []
                    };

                    this.onOpen = function(handler) {
                        this.handlers.open.push(handler);
                        return this;
                    };

                    this.onClose = function(handler) {
                        this.handlers.close.push(handler);
                        return this;
                    };

                    this.onMessage = function(handler) {
                        this.handlers.message.push(handler);
                        return this;
                    };

                    this.onError = function(handler) {
                        this.handlers.error.push(handler);
                        return this;
                    };

                    this.onConnect = function(handler) {
                        this.handlers.reconnect.push(handler);
                        return this;
                    };

                    this.onMessagePublished = function(handler) {
                        this.handlers.messagePublished.push(handler);
                        return this;
                    };

                    this.onClientTimeout = function(handler) {
                        this.handlers.clientTimeout.push(handler);
                        return this;
                    };

                    this.onTransportFailure = function(handler) {
                        this.handlers.transportFailure.push(handler);
                        return this;
                    };

                    this.open = function() {
                        var request = {
                                url: this.url,
                                contentType: this.contentType,
                                logLevel: this.logLevel,
                                transport: this.transport,
                                fallbackTransport: this.fallbackTransport
                            },
                            socket = this;

                        request.onOpen = function(response) {
                            $log.debug('Socket opened using:', response.transport);
                            angular.forEach(socket.handlers.open, function(handler) {
                                handler(socket, response);
                            });
                        };

                        request.onClose = function(response) {
                            $log.debug('Socket closed because:', response);
                            angular.forEach(socket.handlers.close, function(handler) {
                                handler(socket, response);
                            });
                        };

                        request.onMessage = function(response) {
                            if (response.responseBody === '|') {
                                return;
                            }
                            var message = response.responseBody.split('|')[1] || response.responseBody;

                            try {
                                message = angular.fromJson(message);
                                $log.debug('Message object is', message);
                                angular.forEach(socket.handlers.message, function(handler) {
                                    handler(socket, response, message);
                                });
                            } catch (e) {
                                $log.error('Exception while processing message: ' + response.responseBody);
                                $log.error('Exception:', e);
                            }
                        };

                        request.onError = function(response) {
                            $log.error('An error occurred:', response);

                            if (response.reasonPhrase === 'maxReconnectOnClose reached') {
                                $location.path('home');
                            }

                            angular.forEach(socket.handlers.error, function(handler) {
                                handler(socket, response);
                            });
                        };

                        request.onReconnect = function(request, response) {
                            $log.debug('onReconncet', request, response);
                            angular.forEach(socket.handlers.reconnect, function(handler) {
                                handler(socket, request, response);
                            });
                        };

                        request.onMessagePublished = function(request, response) {
                            $log.debug('onMessagePublished', request, response);
                            angular.forEach(socket.handlers.messagePublished, function(handler) {
                                handler(socket, request, response);
                            });
                        };

                        request.onClientTimeout = function(request) {
                            $log.warn('Client timed out:', request);
                            angular.forEach(socket.handlers.clientTimeout, function(handler) {
                                handler(socket, request);
                            });
                        };

                        request.onTransportFailure = function(error, request) {
                            $log.warn('Transport Failure:', error);
                            angular.forEach(socket.handlers.transportFailure, function(handler) {
                                handler(socket, request);
                            });
                        };

                        this.request = request;
                        this.socket = atmosphere.subscribe(request);
                        return this;
                    };

                    this.close = function() {
                        if (!this.socket) {
                            return;
                        }
                        this.socket.unsubscribe();
                        return this;
                    };

                    this.publish = function(message) {
                        if (!this.socket) {
                            return;
                        }

                        if (typeof message !== 'string') {
                            message = angular.toJson(message);
                        }

                        this.socket.push(message);
                        return this;
                    };
                };

            return {
                createSocket: function() {
                    return new Socket();
                },
                reconnectOnErrorHandler: function(socket, response) {
                    if (response.reasonPhrase === 'No suspended connection available') {
                        $log.info('Reconnecting because of timeout');
                        socket.socket = socket.subscribe(socket.request);
                    }
                }
            };
        });
}(window.angular, window.$));
/*
 socket = SocketFactory.createSocket().onMessage(...).onError(...).open();
 */