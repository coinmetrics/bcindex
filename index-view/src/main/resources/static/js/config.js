let Config = {
    data: '/api/index',
    initalState: {"index":"ODD","currency":"USD","timeFrame":"HOURLY"} ,
    dropdown: {
        index: [
            {
                id: 'ODD',
                text: 'Bletchley 10 Index',
                selected: true
            },
            {
                id: 'EVEN',
                text: 'Bletchley 10 Even Index'
            },
            {
                id: 'INDEX_TWENTY',
                text: 'Bletchley 20 Index'
            },
            {
                id: 'EVEN_TWENTY',
                text: 'Bletchley 20 Even Index'
            }

        ],
        currency:  [
            {
                id: 'USD',
                text: 'US Dollar',
                selected: true
            },
            {
                id: 'BTC',
                text: 'Bitcoin'
            }
        ]
    },
    stock: {
        template: ""
    },
    formatter: {        
        default: {
            toolTip: function (text) {
                text = text.toFixed(2);
                var parts = text.toString().split(".");
                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");                                
                return text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            },
            yAxes: function (text) {
                var parts = text.toString().split(".");
                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                return parts.join(".");                
            },
            tickerInfoLastPrice: function (data) {
                function formatPrice(text) {
                    var parts = text.toString().split(".");
                    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");                                
                    return text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }                
                data.prevClose = formatPrice(data.prevClose.toFixed(2));
                data.high = formatPrice(data.high.toFixed(2));
                data.lastPrice = formatPrice(data.lastPrice.toFixed(2));
                data.low = formatPrice(data.low.toFixed(2));
                data.percentChange = data.percentChange.toFixed(2);
            }
        },
        BTC: {
            toolTip: function(text) {                
                return text.toFixed(5);
            },
            yAxes: function (text) {                
                return text.toFixed(3);
            },
            tickerInfoLastPrice: function (data) {
                function formatPrice(text) {
                    var parts = text.toString().split(".");
                    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");                                
                    return text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }                
                data.prevClose = formatPrice(data.prevClose.toFixed(5));
                data.high = formatPrice(data.high.toFixed(5));
                data.lastPrice = formatPrice(data.lastPrice.toFixed(5));
                data.low = formatPrice(data.low.toFixed(5));
                data.percentChange = data.percentChange.toFixed(5);
            }
        }
    },
    chart: {
        defaultOptions: {
            responsive: true,
            maintainAspectRatio: true,
            scaleShowVerticalLines: false,
            hover: {
                mode: "x-axis"
            },
            legend: {
                display: false
            },
            tooltips: {
                mode: 'index',
                displayColors: false,
                intersect: false,
                backgroundColor: 'rgba(66, 73, 73, 0.6)',
                bodyFontSize: 12,
                callbacks: {
                    label: function(tooltipItem, data) {
                         let formatter = Config.formatter[state.currency] ? 
                                    Config.formatter[state.currency] : 
                                    Config.formatter.default;    
                        return formatter.toolTip(tooltipItem.yLabel);
                        return tooltipItem.yLabel.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    },
                },
                cornerRadius: 6,
                position: 'nearest',
            },
            scales: {
                yAxes: [{
                    gridLines: {
                        color: "rgba(0, 100, 100, 0.3)"
                    },
                    ticks: {
                        beginAtZero: false,
                        maxTicksLimit: 4,
                        userCallback: function(value, index, values) {
                            let formatter = Config.formatter[state.currency] ? 
                                    Config.formatter[state.currency] : 
                                    Config.formatter.default;    
                            return formatter.yAxes(value);
                        },
                    },
                    scaleLabel: {
                        display: false,
                    },
                    tooltipFormat: '#,###.#0' 
                }],


                xAxes: [{
                    type: 'time',
                    gridLines: {
                        color: "rgba(0, 0, 0, 0)"
                    },
                    ticks: {
                        maxRotation: 0,
                        autoSkipPadding: 10,
                        padding: 20,
                        autoSkip: true,
                        maxTicksLimit: 4
                    },                    
                    time: {
                        unit: 'minute',
                        unitStepSize: 10,
                        tooltipFormat: 'MMM DD HH:mm',
                        displayFormats: {
                            'minute': 'HH:mm', 
                            'hour': 'HH:mm',
                            'day' : 'MMM DD HH',
                            'week' : 'MMM DD',
                            'month' : 'MMM DD',
                            'year' : 'MMM DD'
                        },
                    },
                    scaleLabel: {
                        display: false,
                    }
                }]
            }
        }
    }
};

export default Config;
