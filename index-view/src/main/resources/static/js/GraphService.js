import config from './config.js';
import StockInfoService from './StockInfoService.js';

export default class GraphService {
    constructor(ctxElem, chartConfig={}) {        
        this.ctx = document.getElementById(ctxElem);
        this.options = Object.assign({}, config.chart.defaultOptions, chartConfig);
        this.stockInfoService = new StockInfoService();
    }
    
    init() {
        fetch(config.data)
        .then(r => r.text())
        .then(d => JSON.parse(d))
        .then((result) => {            
            this.options.scales.xAxes[0].time.unit=result.timeUnit;
            this.renderLineChart(result.times, result.data);
            this.stockInfoService.draw(result);
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
            this.options.scales.xAxes[0].time.unit=result.timeUnit;
            this.chart.destroy();
            this.renderLineChart(result.times, result.data);
            this.stockInfoService.draw(result);
        });
    }

    renderLineChart(labels, data) {
        this.chart = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
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