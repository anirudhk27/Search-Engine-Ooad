var search_btn = document.getElementById("search-btn");
search_btn.addEventListener('click', function () {
	search();
});

function search() {

	var q = document.getElementById("search").value;
	var k = document.getElementById("k").value;
	if (isNaN(k)){
		alert("K has to be a number. Please check its value and try again");
		return
	}
	var res = document.getElementById("results");
	res.innerHTML = "";
	// Set up our HTTP request
	var xhr = new XMLHttpRequest();
	// Setup our listener to process completed requests
	xhr.onreadystatechange = function () {
		// Only run if the request is complete
		if (xhr.readyState !== 4) return;
		// Process our return data
		if (xhr.status >= 200 && xhr.status < 300) {
			// What do when the request is successful
			var data = JSON.parse(xhr.responseText);
			for (i = 0; i < data.results.length; i++) {
				res.innerHTML += '<div class="card mt-2"> <div class="card-header"><a href=" '+data.results[i]+'">Result '+(i+1)+'</a></div> <div class="card-body"><blockquote class="blockquote mb-0"><p><a href="' + data.results[i] + '">' + data.results[i] + '</a></p></blockquote></div></div>';
			}
			console.log(data);
		} else {
			// What to do when the request has failed
			console.log('error', xhr);
		}
	};

	console.log(q);
	console.log(k);
	xhr.open('GET', 'http://localhost:8080/search?query=' + q + '&k=' + k);
	xhr.send();
}
