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
                        maxTicksLimit: 4
                    },
                    scaleLabel: {
                        display: false,
                    },
                    tooltipFormat: '$' 
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
                        tooltipFormat: 'MMM DD HH:MM',
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