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
                    label: 'The Index',
                    data: data,
                    backgroundColor: 'rgba(0, 0, 0, 0.0)' ,
                    borderColor: 'rgba(104, 140, 33, 1)',
                    borderWidth: 3,
                    scaleShowVerticalLines: false,
                    pointRadius: 0,
                }]
            },
            options: this.options
        });
    }
}