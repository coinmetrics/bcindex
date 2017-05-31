(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});
exports.GraphService = undefined;

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _config = require('./config.js');

var _config2 = _interopRequireDefault(_config);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var GraphService = exports.GraphService = function () {
    function GraphService(ctxElem, chartConfig) {
        _classCallCheck(this, GraphService);

        this.ctx = document.getElementById(ctxElem);
        this.options = Object.assign({}, _config2.default.chart.defaultOptions, chartConfig);
    }

    _createClass(GraphService, [{
        key: 'renderLineChart',
        value: function renderLineChart(data, label) {
            this.chart = new Chart(this.ctx, {
                type: 'line',
                data: {
                    labels: times,
                    datasets: [{
                        label: 'The Index',
                        data: data,
                        backgroundColor: 'rgba(255,153,0,0.4)',
                        borderColor: 'rgba(255, 159, 64, 1)',
                        borderWidth: 1
                    }]
                },
                options: this.options
            });
        }
    }]);

    return GraphService;
}();

},{"./config.js":3}],2:[function(require,module,exports){
'use strict';

var _GraphService = require('./GraphService.js');

var _GraphService2 = _interopRequireDefault(_GraphService);

var _config = require('./config.js');

var _config2 = _interopRequireDefault(_config);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

(function () {
    fetch('/api/index').then(function (r) {
        return r.text();
    }).then(function (d) {
        return JSON.parse(d);
    }).then(function (result) {
        var graphService = new _GraphService2.default("myChart");
        graphService.renderLineChart(result.times, result.data);
    });

    $('.index').select2({
        data: [{
            id: 0,
            text: 'odd',
            selected: true
        }, {
            id: 1,
            text: 'even'
        }],
        minimumResultsForSearch: Infinity
    });
    $('.currency').select2({
        data: [{
            id: 0,
            text: 'usd',
            selected: true
        }, {
            id: 1,
            text: 'bit coin'
        }],
        minimumResultsForSearch: Infinity
    });
})();

},{"./GraphService.js":1,"./config.js":3}],3:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});
var Config = {
    data: '/api/index',
    dropdown: {
        index: [{
            id: 0,
            text: 'odd',
            selected: true
        }, {
            id: 1,
            text: 'even'
        }],
        currency: [{
            id: 0,
            text: 'usd',
            selected: true
        }, {
            id: 1,
            text: 'bit coin'
        }]
    },
    chart: {
        defaultOptions: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Price'
                    }
                }],
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Time'
                    }
                }]
            }
        }
    }
};

exports.default = Config;

},{}]},{},[2]);
