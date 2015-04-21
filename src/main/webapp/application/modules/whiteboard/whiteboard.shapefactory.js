(function(angular) {
    'use strict';

    angular
        .module('gitboard.whiteboard')
        .factory('ShapeFactory', function() {
            var BASE_SHAPE = {
                    toJson: function() {
                        return angular.toJson(this);
                    }
                },
                shapes = {
                    createPath: function (object, layer) {
                        var segments = object._segments.map(function(segment) {
                            return {
                                type: segment.type,
                                position: {
                                    x: segment.coords[0],
                                    y: segment.coords[1]
                                }
                            };
                        });

                        return angular.extend({}, BASE_SHAPE, {
                            type: '.PathShape',
                            segments: segments,
                            stroke: object._stroke,
                            layer: layer.index,
                            layerName: layer.name
                        });
                    },

                    createLine: function (object, layer) {
                        return angular.extend({}, BASE_SHAPE, {
                            type: '.LineShape',
                            start: {
                                x: object.attr('x1'),
                                y: object.attr('y1')
                            },
                            end: {
                                x: object.attr('x2'),
                                y: object.attr('y2')
                            },
                            stroke: object.attr('stroke'),
                            layer: layer.index,
                            layerName: layer.name
                        });
                    },

                    createRectangle: function (object, layer) {
                        return angular.extend({}, BASE_SHAPE, {
                            type: '.RectangleShape',
                            position: {
                                x: object.x(),
                                y: object.y()
                            },
                            dimensions: {
                                height: object.height(),
                                width: object.width()
                            },
                            fill: object.attr('fill'),
                            stroke: object.attr('stroke'),
                            layer: layer.index,
                            layerName: layer.name
                        });
                    },

                    createEllipse: function (object, layer) {
                        return angular.extend({}, BASE_SHAPE, {
                            type: '.EllipseShape',
                            position: {
                                x: object.x(),
                                y: object.y()
                            },
                            dimensions: {
                                height: object.height(),
                                width: object.width()
                            },
                            fill: object.attr('fill'),
                            stroke: object.attr('stroke'),
                            layer: layer.index,
                            layerName: layer.name
                        });
                    },

                    createText: function (object, layer) {
                        return angular.extend({}, BASE_SHAPE, {
                            type: '.TextShape',
                            text: object.attr('text'),
                            position: {
                                x: object.x(),
                                y: object.y()
                            },
                            stroke: object.attr('stroke'),
                            layer: layer.index,
                            layerName: layer.name
                        });
                    }

                };

            return shapes;
        });
}(window.angular));
