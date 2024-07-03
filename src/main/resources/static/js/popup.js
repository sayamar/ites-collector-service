import { popupClose } from "./js/index.js";	

function closeWindow(event) {
	console.log("popup and inline function together Asndkasdaodhahdoha");
	event.preventDefault();
	event.view.close();
	window.close();
	popupClose();		
}
		  