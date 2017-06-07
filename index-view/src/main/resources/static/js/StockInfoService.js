import Config from './config.js';

/*
    Responsible for draw stock information about the class.
*/
export default class StockInfoService {
    draw(model) {
        let template =  
`<div class="ticker-info-box">
    <ul>
        <li>
            <dt>Previous Close </dt>
            <dd>${model.prevClose}</dd>
        </li>                            
        <li>
            <dt>High </dt>
            <dd>${model.high}</dd>
        </li>
        <li>
            <dt>Low </dt>
            <dd>${model.low}</dd>
        </li>
        <li>
            <dt>Change </dt>
            <dd>${model.change}</dd>
        </li>
        <li>
            <dt>Precent Change </dt>
            <dd>${model.percentChange}</dd>                            
        </li>
    </ul>
</div>`;
        $('.last-price').html($('<h1>', {text: "Last Price: " + model.lastPrice})); // TODO: move this out to its own class or function
        $("#stock-info").html(template);
    }
}