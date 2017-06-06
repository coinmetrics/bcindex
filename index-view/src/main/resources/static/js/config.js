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
            }
        ],
        currency:  [
            {
                id: 'USD',
                text: 'USD',
                selected: true
            },
            {
                id: 'BTC',
                text: 'BTC'
            }
        ]
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
                        unitStepSize: 10,
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

export default Config;