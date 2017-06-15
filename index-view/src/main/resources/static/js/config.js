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
    chart: {
        defaultOptions: {
            responsive: true,
            maintainAspectRatio: true,
            scaleShowVerticalLines: false,
            pointRadius: 0,
            borderWidth: 10,
            legend: {
                display: false
            },
            tooltips: {
                mode: 'index',
                displayColors: false,
                intersect: false
            },
            scales: {
                yAxes: [{
                    gridLines: {
                        color: "rgba(0, 100, 100, 0.3)"
                    },
                    ticks: {
                        beginAtZero: false,
                        maxTicksLimit: 4
                    },
                    scaleLabel: {
                        display: false,
                    }  
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