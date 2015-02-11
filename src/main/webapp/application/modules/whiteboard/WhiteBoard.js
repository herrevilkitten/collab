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

    angular.module('svb.whiteboard', [ 'ui.bootstrap', 'colorpicker.module' ])
        .controller('WhiteBoardController', [ '$scope', '$log', '$http', '$interval', 'Helper',
            function($scope, $log, $http, $interval, Helper) {
                try {
                    $scope.color = {
                        foreground: 'rgba(0, 0, 0, 1)',
                        fill: 'rgba(255, 255, 255, 1)',
                        complementary: function(color) {
                            return new SVG.Color(color).complementary();
                        }
                    };
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
                                        if ( $scope.selected) {
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
                        var data = {
                            author: $scope.uuid,
                            type: 'action.' + action.type
                        };

                        window.console.log('Adding action:', action);
                        switch (action.type) {
                        case 'path':
                            console.log(action._segments);
                            var segments = [];
                            for (var i = 0; i < action._segments.length; ++i) {
                                var segment = action._segments[i];
                                segments.push({
                                    type: segment.type,
                                    position: {
                                        x: segment.coords[0],
                                        y: segment.coords[1]
                                    }
                                });
                                //segment.type + segment.coords[0] + ' ' + segment.coords[1]
                            }
                            data.segments = segments;
                            data.stroke = action._stroke;
                            break;
                        case 'rect':
                            data.position = {
                                x: action.x(),
                                y: action.y()
                            };
                            data.dimensions = {
                                height: action.height(),
                                width: action.width()
                            };
                            data.fill = action.attr('fill');
                            data.stroke = action.attr('stroke');
                            break;
                        case 'ellipse':
                            data.position = {
                                x: action.x(),
                                y: action.y()
                            };
                            data.dimensions = {
                                height: action.height(),
                                width: action.width()
                            };
                            data.fill = action.attr('fill');
                            data.stroke = action.attr('stroke');
                            break;
                        }
                        window.console.log('Sending ', data);
                        $scope.subSocket.push(JSON.stringify(data));
                    };

                    $interval(function() {
                        window.console.log('Heartbeat');
                        $scope.subSocket.push(JSON.stringify({
                            author: $scope.uuid,
                            message: 'heartbeat',
                            type: 'heartbeat'
                        }));
                    }, 90000);

                    (function() {
                        var socket = $.atmosphere,
                            request = {
                                url: 'chat',
                                contentType: 'application/json',
                                logLevel: 'debug',
                                transport: 'websocket',
                                fallbackTransport: 'long-polling'
                            };
                        window.console.log('$', $);
                        window.console.log('atmosphere', $.atmosphere);

                        request.onOpen = function(response) {
                            window.console.log('Atmosphere connected using ' + response.transport);
                        };

                        request.onClose = function(response) {
                            window.console.log('Connection closed:', response);
                        };

                        request.onMessage = function(response) {
                            var message = response.responseBody.split('|')[1] || response.responseBody,
                                json,
                                i,
                                actions,
                                action,
                                segments;
                            window.console.log('Received:', response);
                            window.console.log('ResponseBody', response.responseBody);
                            try {
                                json = JSON.parse(message);
                                window.console.log('json', json);
                                if (json.type === 'welcome') {
                                    $scope.uuid = json.uuid;
                                    actions = json.actions;
                                    try {
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
                                    } catch (e) {
                                        window.console.log('Exception', e);
                                    }
                                } else if (json.type.substring(0, 7) === 'action.' && json.author !== $scope.uuid) {
                                    actionType = json.type.substring(7);
                                    window.console.log('Adding action ', actionType, 'from ', json.author);
                                    switch (actionType) {
                                    case 'path':
                                        segments = json.segments.map(function(item) {
                                            return item.type + item.position.x + ' ' + item.position.y
                                        }).join('');
                                        window.console.log('Segments', segments);
                                        $scope.canvas.path(segments).attr({
                                            fill: 'none',
                                            stroke: json.stroke,
                                            'stroke-width': 1
                                        });
                                        break;
                                    case 'rect':
                                        $scope.canvas.rect(json.dimensions.width, json.dimensions.height)
                                            .move(json.position.x, json.position.y)
                                            .attr({
                                                fill: json.fill,
                                                stroke: json.stroke
                                            });
                                        break;
                                    case 'ellipse':
                                        $scope.canvas.ellipse(json.dimensions.width, json.dimensions.height)
                                            .move(json.position.x, json.position.y)
                                            .attr({
                                                fill: json.fill,
                                                stroke: json.stroke
                                            });
                                        break;
                                    }
                                }
                            }
                            catch
                                (e) {
                                window.console.log('Exception', e);
                                window.console.log('This does not look like valid JSON: ', message);
                                return;
                            }
                            window.console.log('ResponseObject:', json);
                        }
                        ;

                        request.onError = function(response) {
                            window.console.log('An error occurred:', response);

                            if (response.reasonPhrase === 'No suspended connection available') {
                                window.console.log('Reconnecting because of timeout');
                                $scope.subSocket = socket.subscribe(request);
                            }
                        };

                        $scope.subSocket = socket.subscribe(request);
                    }()
                        )
                    ;

                }
                catch
                    (e) {
                    window.console.error(e);
                }
            }
        ])
    ;
}(window.angular, window.SVG, window.gapi, window.jQuery));
