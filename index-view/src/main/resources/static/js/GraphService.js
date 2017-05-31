import config from './config.js';
 
export default class GraphService {
    constructor(ctxElem, chartConfig) {        
        this.ctx = document.getElementById(ctxElem);
        this.options = Object.assign({}, config.chart.defaultOptions, chartConfig);
    }
    
    init() {
        fetch(config.data)
        .then(r => r.text())
        .then(d => JSON.parse(d))
        .then((result) => {            
            this.renderLineChart(result.times, result.data);
            $('.last-price').html($('<h1>', {text: "Last Price: " + result.lastPrice})); // TODO: move this out to its own class or function
        });
    }

    update(payload) {
        fetch(config.data, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            method: "POST",
            body: JSON.stringify(payload)
        }).then(r => r.text())
        .then(d => JSON.parse(d))
        .then((result) => {
            this.renderLineChart(result.times, result.data);
            $('.last-price').html($('<h1>', {text: "Last Price: " + result.lastPrice})); // TODO: move this out to its own class or function
        });
    }

    renderLineChart(labels, data) {
        this.chart = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'The Index',
                    data: data,
                    backgroundColor: 'rgba(204, 226, 159, 0.4)' ,
                    borderColor: 'rgba(104, 140, 33, 0.4)',
                    borderWidth: 1
                }]
            },
            options: this.options
        });
    }
}