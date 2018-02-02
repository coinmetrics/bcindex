import config from './config.js';
import StockInfoService from './StockInfoService.js';
import color from 'color'

export default class GraphService {
    constructor(ctxElem, chartConfig={}) {        
        this.ctx = document.getElementById(ctxElem);
        this.options = Object.assign({}, config.chart.defaultOptions, chartConfig);
        this.stockInfoService = new StockInfoService();
    }
    
    init() {
        fetch(config.mult)
        .then(r => r.text())
        .then(d => JSON.parse(d))
        .then((result) => {         
            debugger;            
            let formatter = config.formatter[result.currency] ? 
                    config.formatter[result.currency] : 
                    config.formatter.default;
            formatter.tickerInfoLastPrice(result);
            this.options.scales.xAxes[0].time.unit=result.timeUnit;
            let graph = this.transformDataToChartJsDataSet(result);
            this.renderLineChart(graph.labels, graph.dataset);
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
            let formatter = config.formatter[result.currency] ? 
                    config.formatter[result.currency] : 
                    config.formatter.default;    
            formatter.tickerInfoLastPrice(result);
            this.options.scales.xAxes[0].time.unit=result.timeUnit;
            this.chart.destroy();
            let graph = this.transformDataToChartJsDataSet(result.data);
            this.renderLineChart(graph.labels, graph.dataset);
            this.stockInfoService.draw(result);
        });
    }

    transformDataToChartJsDataSet(model) {
        let result = new Array();
        var lineColor = color('rgb(104, 140, 33)');
        let object = (data, lineColor) => {
            return {
                label: 'Price',
                data: data,
                /*backgroundColor: config.colors.shadeRGBColor('rgb(104, 140, 33)', 5),*/
                borderColor: lineColor.lighten(0.5),
                borderWidth: 2,
                pointRadius: 0,
                pointHoverRadius: 4,
                pointHoverBorderWidth: 4,
                pointBackgroundColor: lineColor.lighten(0.5),
            }
        }
        for (let data of model.prices) {
            result.push(object(data, lineColor));
        }        
        model.times = [].concat.apply([], model.times).sort(function(a,b){
            var c = new Date(a);
            var d = new Date(b);
            return c-d;
        });
        return {
            dataset: result,
            labels: model.times
        };
    }
    
    renderLineChart(labels, data) {
        this.chart = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: data
            },
            options: this.options
        });
    }
}