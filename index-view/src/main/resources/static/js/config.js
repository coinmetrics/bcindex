let Config = {
    data: '/api/index',
    initalState: {"index":"ODD","currency":"USD","timeFrame":"HOURLY"} ,
    dropdown: {
        index: [
            {
                id: 'ODD',
                text: 'odd',
                selected: true
            },
            {
                id: 'EVEN',
                text: 'even'
            }
        ],
        currency:  [
            {
                id: 'USD',
                text: 'usd',
                selected: true
            },
            {
                id: 'BTC',
                text: 'bit coin'
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
                        padding: 20
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