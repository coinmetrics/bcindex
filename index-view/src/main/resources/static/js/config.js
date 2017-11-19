let Config = {
    data: '/blet/index',
    initalState: {"index":"ODD_INDEX","currency":"USD","timeFrame":"DAILY"} ,
    dropdown: {
        index: [
            {
                id: 'ODD_INDEX',
                text: 'Bletchley 10 Index',
                selected: true
            },
            {
                id: 'EVEN_INDEX',
                text: 'Bletchley 10 Even Index'
            },
            {
                id: 'INDEX_ETH',
                text: 'Bletchley Ethereum Token Index'
            },
            {
                id: 'EVEN_ETH',
                text: 'Bletchley Ethereum Token Even Index'
            },
            {
                id: 'INDEX_TWENTY',
                text: 'Bletchley 20 Index'
            },
            {
                id: 'EVEN_TWENTY',
                text: 'Bletchley 20 Even Index'
            },
            {
                id: 'FORTY_INDEX',
                text: 'Bletchley 40 Index'
            },
            {
                id: 'FORTY_EVEN_INDEX',
                text: 'Bletchley 40 Even Index'
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
                    return parts.join(".");    
                }
                data.prevClose = formatPrice(data.prevClose.toFixed(2));
                data.high = formatPrice(data.high.toFixed(2));
                data.lastPrice = formatPrice(data.lastPrice.toFixed(2));
                data.low = formatPrice(data.low.toFixed(2));
                data.change = formatPrice(data.change.toFixed(2));
                data.percentChange = formatPrice(data.percentChange.toFixed(2));
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
                    return parts.join(".");   
                }
                data.prevClose = formatPrice(data.prevClose.toFixed(5));
                data.high = formatPrice(data.high.toFixed(5));
                data.lastPrice = formatPrice(data.lastPrice.toFixed(5));
                data.low = formatPrice(data.low.toFixed(5));
                data.change = formatPrice(data.change.toFixed(5));
                data.percentChange = formatPrice(data.percentChange.toFixed(2));
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
                        bodyFontSize: 12,
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
                        maxTicksLimit: 4,
                        bodyFontSize: 12
                    },                    
                    time: {
                        unit: 'minute',
                        tooltipFormat: 'MMM DD HH:mm',
                        displayFormats: {
                            'minute': 'H:mm', 
                            'hour': 'MMM D',
                            'day' : 'MMM D',
                            'week' : 'MMM',
                            'month' : 'MMM',
                            'year' : 'MMM'
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
