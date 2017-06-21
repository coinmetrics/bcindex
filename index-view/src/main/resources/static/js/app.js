import GraphService from './GraphService.js';
import Config from './config.js';

(function () {
    window.state = Config.initalState;

    let graphService = new GraphService("myChart");
    let result = graphService.init();
    
    let select2DropDown = (elem, data, key) => {
        let $elem = $(elem);
        $elem.select2({
            data: data,
            minimumResultsForSearch: Infinity
        });

        $elem.on("select2:select", function(e) {
            console.log("SELECT")
            console.log(this);
            console.log(e);
            state = Object.assign({}, state, {[key]: e.params.data.id});
            graphService.update(state);
            console.log("SELECT")
        });
    }

    select2DropDown('.index', Config.dropdown.index, "index");
    select2DropDown('.currency', Config.dropdown.currency, "currency");
 
    $('.btn').on('click', function(e) {
        if($(this).hasClass('selected')) return;
        $('.selected').removeClass('selected');
        $(this).addClass('selected');
        state = Object.assign({}, state, {"timeFrame": $(this).data('timeframe').toUpperCase()});
        graphService.update(state);
    });
})();