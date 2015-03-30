(function(angular, SVG, gapi, $) {
    'use strict';

    var MODE_DRAWING = 'd',
        MODE_COMMAND = 'c',
        MODE_SELECT = 's',
        MODE_SELECT_LASSO = 0,
        MODE_SELECT_RECT = 1,

        MODE_DRAWING_PENCIL = 'p',
        MODE_DRAWING_LINE = 'l',
        MODE_DRAWING_RECT = 'r',
        MODE_DRAWING_OVAL = 'e',

        KEY_SHIFT = 16,
        KEY_ESCAPE = 27,
        KEY_CONTROL = 17;

    angular.module('gitboard.whiteboard')
        .controller('WhiteBoardController', function($scope,
                                                     $http,
                                                     $interval,
                                                     $location,
                                                     $log,
                                                     $timeout,
                                                     CurrentBoard,
                                                     MessageFactory,
                                                     ShapeFactory,
                                                     SocketFactory) {

            function onMessage(socket, response, message) {
                var i,
                    actions,
                    action,
                    segments;

                if (message.type === 'welcome') {
                    $scope.uuid = message.uuid;
                    actions = message.actions;
                    if (actions !== null && actions.length > 0) {
                        for (i = 0; i < actions.length; ++i) {
                            action = actions[i];
                            var actionType = action.type.substring(7);
                            window.console.log('Adding', actionType, 'from welcome: ', action);
                            switch (actionType) {
                            case 'path':
                                segments = action.object.segments.map(function(item) {
                                    return item.type + item.position.x + ' ' + item.position.y
                                }).join('');
                                window.console.log('Segments', segments);
                                $scope.canvas.path(segments).attr({
                                    fill: 'none',
                                    stroke: action.object.stroke,
                                    'stroke-width': 1
                                });
                                break;
                            case 'rect':
                                $scope.canvas.rect(action.object.dimensions.width, action.object.dimensions.height)
                                    .move(action.object.position.x, action.object.position.y)
                                    .attr({
                                        fill: action.object.fill,
                                        stroke: action.object.stroke
                                    });
                                break;
                            case 'ellipse':
                                $scope.canvas.ellipse(action.object.dimensions.width, action.object.dimensions.height)
                                    .move(action.object.position.x, action.object.position.y)
                                    .attr({
                                        fill: action.object.fill,
                                        stroke: action.object.stroke
                                    });
                                break;
                            }
                        }
                    }
                } else if (message.type.substring(0, 7) === 'action.' && message.author !== $scope.uuid) {
                    actionType = message.type.substring(7);
                    window.console.log('Adding action ', actionType, 'from ', message.author);
                    switch (actionType) {
                    case 'path':
                        segments = message.segments.map(function(item) {
                            return item.type + item.position.x + ' ' + item.position.y
                        }).join('');
                        window.console.log('Segments', segments);
                        $scope.canvas.path(segments).attr({
                            fill: 'none',
                            stroke: message.stroke,
                            'stroke-width': 1
                        });
                        break;
                    case 'rect':
                        $scope.canvas.rect(message.dimensions.width, message.dimensions.height)
                            .move(message.position.x, message.position.y)
                            .attr({
                                fill: message.fill,
                                stroke: message.stroke
                            });
                        break;
                    case 'ellipse':
                        $scope.canvas.ellipse(message.dimensions.width, message.dimensions.height)
                            .move(message.position.x, message.position.y)
                            .attr({
                                fill: message.fill,
                                stroke: message.stroke
                            });
                        break;
                    }
                }
            }

            try {
                $scope.color = {
                    foreground: 'rgba(0, 0, 0, 1)',
                    fill: 'rgba(255, 255, 255, 1)',
                    complementary: function(color) {
                        return new SVG.Color(color).complementary();
                    }
                };
                $scope.boardId = CurrentBoard;
                $scope.selected = null;
                $scope.uuid = null;
                $scope.mode = MODE_DRAWING;
                $scope.drawingMode = MODE_DRAWING_PENCIL;
                $scope.strokes = [];
                $scope.canvas = SVG('canvas').size(1920, 1080);
                $scope.toolboxSide = 'left';
                $scope.command = {
                    clear: function() {
                        if (window.confirm('Clear the drawing?')) {
                            $scope.canvas.clear();
                        }
                        $scope.mode = MODE_DRAWING;
                    },
                    switchSides: function() {
                        $scope.toolboxSide = ($scope.toolboxSide === 'left') ? 'right' : 'left';
                    }

                };

                var recognizer = new window.PDollarRecognizer(),
                    svgElement = $('#canvas').find('svg'),

                    position = svgElement.offset(),

                    shape = null,
                    path = null,
                    isDown = false,
                    points = [],
                    strokes = $scope.strokes,
                    submode = 0;

                $(window).keyup(function(e) {
                    $scope.$apply(function() {
                        $log.info('keyUp: ', e);
                        if (e.which === KEY_ESCAPE) {
                            if (shape) {
                                shape.remove();
                                shape = null;
                            }
                            if (path) {
                                path.remove();
                                path = null;
                            }
                            isDown = false;
                        }
                    });
                });

                svgElement.on('mousemove touchmove', function(e) {
                    $scope.$apply(function() {
                        var x,
                            y,
                            attr,
                            originalX,
                            originalY,
                            width,
                            height,
                            newX,
                            newY;

                        if (window.TouchEvent !== undefined && e.originalEvent instanceof window.TouchEvent) {
                            x = e.originalEvent.changedTouches[0].pageX - position.left;
                            y = e.originalEvent.changedTouches[0].pageY - position.top;
                        } else {
                            x = e.pageX;
                            y = e.pageY;
                        }
                        if (isDown) {
                            if ($scope.mode === MODE_COMMAND) {
                                path.attr({
                                    fill: 'none',
                                    stroke: '#0000ff',
                                    'stroke-width': 2
                                }).L(x, y);
                                points[points.length] = new window.Point(x, y, strokes.length);
                            } else if ($scope.mode === MODE_DRAWING && (shape || path)) {
                                if ($scope.drawingMode === MODE_DRAWING_PENCIL) {
                                    path.attr({
                                        fill: 'none',
                                        stroke: $scope.color.foreground,
                                        'stroke-width': 1
                                    }).L(x, y);
                                } else if ($scope.drawingMode === MODE_DRAWING_LINE) {
                                    shape.attr({
                                        fill: $scope.color.fill,
                                        stroke: $scope.color.foreground,
                                        x2: x,
                                        y2: y
                                    });
                                } else if ($scope.drawingMode === MODE_DRAWING_RECT) {
                                    attr = shape.attr();
                                    originalX = attr.originalX;
                                    originalY = attr.originalY;
                                    width = Math.abs(originalX - x);
                                    height = Math.abs(originalY - y);
                                    newX = originalX;
                                    newY = originalY;
                                    if (originalX > x) {
                                        newX = x;
                                    }
                                    if (originalY > y) {
                                        newY = y;
                                    }
                                    if (newX !== originalX || newY !== originalY) {
                                        shape.move(newX, newY);
                                    }
                                    shape.size(width, height);
                                    shape.attr({
                                        fill: $scope.color.fill,
                                        stroke: $scope.color.foreground
                                    });
                                } else if ($scope.drawingMode === MODE_DRAWING_OVAL) {
                                    attr = shape.attr();
                                    $log.info(attr);
                                    originalX = attr.originalX;
                                    originalY = attr.originalY;
                                    width = Math.abs(originalX - x) * 2;
                                    height = Math.abs(originalY - y) * 2;
                                    shape.size(width, height);
                                    shape.attr({
                                        fill: $scope.color.fill,
                                        stroke: $scope.color.foreground
                                    });
                                }
                            }
                        }
                    });
                });

                svgElement.on('mousedown touchstart', function(e) {
                    $scope.$apply(function() {
                        var x, y;
                        $log.info('mouseDown:', e);
                        isDown = true;
                        if (window.TouchEvent !== undefined && e.originalEvent instanceof window.TouchEvent) {
                            x = e.originalEvent.changedTouches[0].pageX - position.left;
                            y = e.originalEvent.changedTouches[0].pageY - position.top;
                        } else {
                            x = e.pageX;
                            y = e.pageY;
                        }

                        if ($scope.mode === MODE_SELECT) {
                            $log.info('Select');
                            var children = $scope.canvas.children();
                            var topChild = null;
                            var foundSelected = false;
                            var firstChild = null;
                            for (var i = 0; i < children.length; ++i) {
                                if (!children[i].attr('not-selectable') && children[i].inside(x, y)) {
                                    if (!firstChild) {
                                        firstChild = children[i];
                                    }
                                    window.console.log('Inside:', children[i]);
                                    if ($scope.selected) {
                                        window.console.log('Parent:', $scope.selected.cloneParent)
                                    }
                                    if (!$scope.selected || ($scope.selected && foundSelected)) {
                                        topChild = children[i];
                                        break;
                                    } else if (children[i] === $scope.selected.cloneParent) {
                                        window.console.log('Found selected');
                                        foundSelected = true;
                                    }
                                }
                            }
                            if ($scope.selected) {
                                $scope.selected.remove();
                                $scope.selected = null;
                                if (topChild === null) {
                                    window.console.log('No topChild, using firstChild');
                                    topChild = firstChild;
                                }
                            }
                            if (topChild !== null) {
                                window.console.log('Top child is', topChild);
                                $scope.selected = topChild.clone();
                                $scope.selected.attr({
                                    stroke: '#00F',
                                    'stroke-width': 2,
                                    'fill-opacity': 0,
                                    'not-selectable': true
                                });
                                $scope.selected.cloneParent = topChild;
                            }

                        } else if ($scope.mode === MODE_COMMAND) {
                            path = $scope.canvas.path().attr({
                                fill: 'none',
                                stroke: '#00F',
                                'stroke-width': 2
                            }).M(x, y);
                            points[points.length] = new window.Point(x, y, 1);
                        } else if ($scope.mode === MODE_DRAWING) {
                            if ($scope.drawingMode === MODE_DRAWING_PENCIL) {
                                path = $scope.canvas.path().attr({
                                    fill: 'none',
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1
                                }).M(x, y);
                            } else if ($scope.drawingMode === MODE_DRAWING_LINE) {
                                shape = $scope.canvas.line(x, y, x, y).attr({
                                    fill: $scope.color.fill,
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                });
                            } else if ($scope.drawingMode === MODE_DRAWING_RECT) {
                                shape = $scope.canvas.rect(1, 1).attr({
                                    fill: $scope.color.fill,
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                }).move(x, y);
                            } else if ($scope.drawingMode === MODE_DRAWING_OVAL) {
                                shape = $scope.canvas.ellipse(1, 1).attr({
                                    fill: $scope.color.fill,
                                    stroke: $scope.color.foreground,
                                    'stroke-width': 1,
                                    originalX: x,
                                    originalY: y
                                }).move(x, y);
                            }
                        }
                    });
                });

                svgElement.on('mouseup touchend', function(e) {
                    $scope.$apply(function() {
                        $log.info('mouseUp:', e, $scope.mode);
                        if (shape === null && path === null) {
                            return;
                        }
                        if ($scope.mode === MODE_COMMAND) {
                            strokes[strokes.length] = path;
                        }

                        $scope.addAction(shape || path);
                        path = null;
                        shape = null;
                        isDown = false;
                    });

                });

                $scope.addAction = function(action) {
                    var shape,
                        message;

                    window.console.log('Adding action:', action);
                    switch (action.type) {
                    case 'path':
                        shape = ShapeFactory.createPath(action);
                        break;
                    case 'line':
                        shape = ShapeFactory.createLine(action);
                        break;
                    case 'rect':
                        shape = ShapeFactory.createRectangle(action);
                        break;
                    case 'ellipse':
                        shape = ShapeFactory.createEllipse(action);
                        break;
                    }
                    message = MessageFactory.createAddShapeAction(shape);
                    $scope.socket.publish(message);
                };

                $interval(function() {
                    $scope.socket.publish(MessageFactory.createHeartbeat());
                }, 90000);

                $scope.socket = SocketFactory
                    .createSocket()
                    .onMessage(onMessage)
                    .onError(SocketFactory.reconnectOnErrorHandler)
                    .open();

                $timeout(function() {
                    $scope.socket.publish(MessageFactory.createQuery());
                }, 1000);
            } catch (e) {
                window.console.error(e);
            }
        });
}(window.angular, window.SVG, window.gapi, window.jQuery));
