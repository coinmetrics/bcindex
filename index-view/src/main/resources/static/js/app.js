(function () {
    fetch('/api/index')
    .then(r => r.text())
    .then(d => JSON.parse(d))
    .then(function(result) { 

        let ctx = document.getElementById("myChart");

        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: result.times,
                datasets: [{
                    label: 'The Index',
                    data: result.data,
                    backgroundColor: 'rgba(204, 226, 159, 0.4)' ,
                    borderColor: 'rgba(104, 140, 33, 0.4)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                elements: { point: { radius: 1, hitRadius: 1, hoverRadius: 4 } },
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
                        scaleLabel: {
                            display: true,
                            labelString: 'Time'
                        }
                    }]
                }
            }
        });
    });
    
    $('.index').select2({
        data: [
            {
                id: 0,
                text: 'odd',
                selected: true
            },
            {
                id: 1,
                text: 'even'
            }
        ],
        minimumResultsForSearch: Infinity
    });
    $('.currency').select2({
        data: [
            {
                id: 0,
                text: 'usd',
                selected: true
            },
            {
                id: 1,
                text: 'bit coin'
            }
        ],
        minimumResultsForSearch: Infinity
    });

})();