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
            <dt-a>Previous<br/>Close</dt-a>
            <dt>Previous Close </dt>
            <dd>${model.prevClose}</dd>
        </li>                            
        <li>
            <dt-a>&nbsp;<br/>High</dt-a>
            <dt>High </dt>
            <dd>${model.high}</dd>
        </li>
        <li>
            <dt-a>&nbsp;<br/>Low</dt-a>
            <dt>Low </dt>
            <dd>${model.low}</dd>
        </li>

        <li>
            <dt-a>&nbsp;<br/>Change</dt-a>
            <dt>Change </dt>
            <dd>${model.change}</dd>
        </li>
        <li>
            <dt-a>Percent<br/>Change</dt-a>
            <dt>Percent Change </dt>
            <dd>${model.percentChange}%</dd>                            
        </li>
    </ul>
</div>`;
        $('.last-price').html($('<h4>', {text: "Last: " + model.lastPrice})); // TODO: move this out to its own class or function
        $("#stock-info").html(template);
    }
}