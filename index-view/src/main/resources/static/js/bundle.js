(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _config = require('./config.js');

var _config2 = _interopRequireDefault(_config);

var _StockInfoService = require('./StockInfoService.js');

var _StockInfoService2 = _interopRequireDefault(_StockInfoService);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var GraphService = function () {
    function GraphService(ctxElem) {
        var chartConfig = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

        _classCallCheck(this, GraphService);

        this.ctx = document.getElementById(ctxElem);
        this.options = Object.assign({}, _config2.default.chart.defaultOptions, chartConfig);
        this.stockInfoService = new _StockInfoService2.default();
    }

    _createClass(GraphService, [{
        key: 'init',
        value: function init() {
            var _this = this;

            fetch(_config2.default.data).then(function (r) {
                return r.text();
            }).then(function (d) {
                return JSON.parse(d);
            }).then(function (result) {
                _this.options.scales.xAxes[0].time.unit = result.timeUnit;
                _this.renderLineChart(result.times, result.data);
                _this.stockInfoService.draw(result);
            });
        }
    }, {
        key: 'update',
        value: function update(payload) {
            var _this2 = this;

            fetch(_config2.default.data, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                method: "POST",
                body: JSON.stringify(payload)
            }).then(function (r) {
                return r.text();
            }).then(function (d) {
                return JSON.parse(d);
            }).then(function (result) {
                _this2.options.scales.xAxes[0].time.unit = result.timeUnit;
                _this2.chart.destroy();
                _this2.renderLineChart(result.times, result.data);
                _this2.stockInfoService.draw(result);
            });
        }
    }, {
        key: 'renderLineChart',
        value: function renderLineChart(labels, data) {
            this.chart = new Chart(this.ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'The Index',
                        data: data,
                        backgroundColor: 'rgba(204, 226, 159, 0.4)',
                        borderColor: 'rgba(104, 140, 33, 0.4)',
                        borderWidth: 1
                    }]
                },
                options: this.options
            });
        }
    }]);

    return GraphService;
}();

exports.default = GraphService;

},{"./StockInfoService.js":2,"./config.js":4}],2:[function(require,module,exports){
'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _config = require('./config.js');

var _config2 = _interopRequireDefault(_config);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/*
    Responsible for draw stock information about the class.
*/
var StockInfoService = function () {
    function StockInfoService() {
        _classCallCheck(this, StockInfoService);
    }

    _createClass(StockInfoService, [{
        key: 'draw',
        value: function draw(model) {
            var template = '<div class="ticker-info-box">\n    <ul>\n        <li>\n            <dt>Previous Close: </dt>\n            <dd>' + model.prevClose + '</dd>\n        </li>                            \n        <li>\n            <dt>High: </dt>\n            <dd>' + model.high + '</dd>\n        </li>\n        <li>\n            <dt>Low: </dt>\n            <dd>' + model.low + '</dd>\n        </li>\n        <li>\n            <dt>Change: </dt>\n            <dd>' + model.change + '</dd>\n        </li>\n        <li>\n            <dt>Precent Change: </dt>\n            <dd>' + model.percentChange + '</dd>                            \n        </li>\n    </ul>\n</div>';
            $('.last-price').html($('<h1>', { text: "Last Price: " + model.lastPrice })); // TODO: move this out to its own class or function
            $("#stock-info").html(template);
        }
    }]);

    return StockInfoService;
}();

exports.default = StockInfoService;

},{"./config.js":4}],3:[function(require,module,exports){
'use strict';

var _GraphService = require('./GraphService.js');

var _GraphService2 = _interopRequireDefault(_GraphService);

var _config = require('./config.js');

var _config2 = _interopRequireDefault(_config);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

(function () {
    var state = _config2.default.initalState;

    var graphService = new _GraphService2.default("myChart");
    var result = graphService.init();

    var select2DropDown = function select2DropDown(elem, data, key) {
        var $elem = $(elem);
        $elem.select2({
            data: data,
            minimumResultsForSearch: Infinity
        });

        $elem.on("select2:select", function (e) {
            console.log("SELECT");
            console.log(this);
            console.log(e);
            state = Object.assign({}, state, _defineProperty({}, key, e.params.data.id));
            graphService.update(state);
            console.log("SELECT");
        });
    };

    select2DropDown('.index', _config2.default.dropdown.index, "index");
    select2DropDown('.currency', _config2.default.dropdown.currency, "currency");

    $('.btn').on('click', function (e) {
        if ($(this).hasClass('selected')) return;
        $('.selected').removeClass('selected');
        $(this).addClass('selected');
        state = Object.assign({}, state, { "timeFrame": $(this).data('timeframe').toUpperCase() });
        graphService.update(state);
    });
})();

},{"./GraphService.js":1,"./config.js":4}],4:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
    value: true
});
var Config = {
    data: '/api/index',
    initalState: { "index": "ODD", "currency": "USD", "timeFrame": "HOURLY" },
    dropdown: {
        index: [{
            id: 'ODD',
            text: 'odd',
            selected: true
        }, {
            id: 'EVEN',
            text: 'even'
        }],
        currency: [{
            id: 'USD',
            text: 'usd',
            selected: true
        }, {
            id: 'BTC',
            text: 'bit coin'
        }]
    },
    stock: {
        template: ""
    },
    chart: {
        defaultOptions: {
            responsive: true,
            maintainAspectRatio: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: false
                    },
                    scaleLabel: {
                        display: true,
                        labelString: 'Price'
                    }
                }],
                xAxes: [{
                    type: 'time',
                    ticks: {
                        maxRotation: 0,
                        autoSkipPadding: 10,
                        padding: 20,
                        autoSkip: true,
                        maxTicksLimit: 5
                    },
                    time: {
                        unit: 'minute',
                        unitStepSize: 10
                    },
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

},{}]},{},[3]);
