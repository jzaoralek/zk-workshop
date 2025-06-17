/*
 * SDAT-3760
 * 
 * Tento script zaregistruje "posluchače mutací" na stromu DOM. Pokud ZK přidá do DOMu
 * okno s textem "Pracuji...", tento posluchač změnu zachytí a přidá také do DOMu
 * div, kterým překryje celý Viewport (aby uživatel nemohl na nic klikat během ajaxového
 * requestu).
 * 
 */


MutationObserver = window.MutationObserver || window.WebKitMutationObserver;

var observer = new MutationObserver(function(mutations, observer) {
    for (var i = 0; i < mutations.length; i++) {
		var mutation = mutations[i];
		iterateRecords(mutation.addedNodes, true);
		iterateRecords(mutation.removedNodes, false);
	}
});

/**
 * Projde záznamy o mutacích v DOMu
 * 
 * @param records
 * @param added
 * @returns
 */
function iterateRecords(records, added) {
	for (var j = 0; j < records.length; j++) {
		var node = records[j];
		if (node.id == "zk_proc") {
			if (added) {
				toggleBusyBackground(true);
			} else {
				toggleBusyBackground(false);
			}
		}
	}
}

/**
 * Přidá se (odebere) do DOMu div, kterým se překryje celý Viewport 
 * (aby uživatel nemohl na nic klikat během ajaxového requestu)
 * 
 * @param visible
 * @returns
 */
function toggleBusyBackground(visible) {
	if (visible) {
		$("body").append("<div id='sdat-disabled-background'> </div>");
	} else {
		$("#sdat-disabled-background").remove();
	}
}


var body = document.getElementsByTagName("BODY")[0];

//define what element should be observed by the observer
//and what types of mutations trigger the callback
observer.observe(body, {
  childList: true
});