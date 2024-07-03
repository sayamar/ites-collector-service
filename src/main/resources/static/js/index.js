var popupWindow = null;
var bodyElement = null;
function openPopup(event) {
	event.preventDefault(); // prevent the default behavior of the link
	const url = event.target.getAttribute("href"); // get the href attribute of
													// the link
	
	var w = 900; // Width of the popup window
	var h = 380; // Height of the popup window
	var left = (screen.width / 2) - (w / 2);
	var top = (screen.height / 2) - (h / 2);
	
	let params = 'scrollbars=no,resizable=no,status=no,location=no,toolbar=no,menubar=no,'+
		'width=' + w +',height='+h+',left='+left +',top='+top+'';
		popupWindow = window.open("", "job popup",params);

  fetch(url)
    .then(response => response.text())
    .then(html => {
      popupWindow.document.body.innerHTML = html;
      activateCss(event.target);
    });
}

function activateCss(tdElement) {
//	event.target.classList.add("layer");
	var element = document.querySelector("div.center");
	const parentElement = element.parentElement;
	bodyElement = parentElement.closest("body");
	
	if (parentElement.classList.contains("col") && parentElement.classList.contains("s12")) {
		  console.log("Parent has col s12 class");
		  //bodyElement.classList.add("layer");
	}
	
	if(popupWindow.closed) {
		bodyElement.classList.remove("layer");
	}
}

function parent_disable() {
	if(popupWindow && !popupWindow.closed){
		popupWindow.focus();
	}
	
}

function popupClose() {
		bodyElement.classList.remove("layer");
}
