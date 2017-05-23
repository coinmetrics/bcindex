


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
                backgroundColor: 'rgba(255,153,0,0.4)' ,
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
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
    });


});    