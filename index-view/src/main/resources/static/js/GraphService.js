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
            debugger;            
            let formatter = config.formatter[result.currency] ? 
                    config.formatter[result.currency] : 
                    config.formatter.default;    
            formatter.tickerInfoLastPrice(result);
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

     formatter: {        
        default: {
            toolTip: function (text) {
                text = text.toFixed(2);
                var parts = text.toString().split(".");
                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");                                
                return text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            },
            yAxes: function (text) {
                var parts = text.toString().split(".");
                parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                return parts.join(".");                
            },
            tickerInfoLastPrice: function (data) {
                function formatPrice(text) {
                    var parts = text.toString().split(".");
                    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");                                
                    return text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }
                data.change = formatPrice(data.change.toFixed(2));
                data.prevClose = formatPrice(data.prevClose.toFixed(2));
                data.high = formatPrice(data.high.toFixed(2));
                data.lastPrice = formatPrice(data.lastPrice.toFixed(2));
                data.low = formatPrice(data.low.toFixed(2));
                data.percentChange = formatPrice(data.percentChange.toFixed(2));
            }
        },
        BTC: {
            toolTip: function(text) {                
                return text.toFixed(5);
            },
            yAxes: function (text) {                
                return text.toFixed(3);
            },
            tickerInfoLastPrice: function (data) {
                function formatPrice(text) {
                    var parts = text.toString().split(".");
                    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");                                
                    return text.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                }
                data.change = formatPrice(data.change.toFixed(5));    
                data.prevClose = formatPrice(data.prevClose.toFixed(5));
                data.high = formatPrice(data.high.toFixed(5));
                data.lastPrice = formatPrice(data.lastPrice.toFixed(5));
                data.low = formatPrice(data.low.toFixed(5));
                data.percentChange = formatPrice(data.percentChange.toFixed(2));
            }
        }
    },

    renderLineChart(labels, data) {
        this.chart = new Chart(this.ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Price',
                    data: data,
                    backgroundColor: 'rgba(104, 140, 33, 0.0)' ,
                    borderColor: 'rgba(104, 140, 33, 1)',
                    borderWidth: 2,
                    pointRadius: 0,
                    pointHoverRadius: 4,
                    pointHoverBorderWidth: 4,
                    pointBackgroundColor: 'rgba(104, 140, 33, 1)',
                }]
            },
            options: this.options
        });
    }
}