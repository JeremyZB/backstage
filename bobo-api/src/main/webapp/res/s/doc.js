$(function() {
	var req = $('#req').val();
	$("#req_json").JSONView(req);


	$('#req-collapse-btn').on('click', function() {
		$('#req_json').JSONView('collapse');
	});
	$('#req-expand-btn').on('click', function() {
		$('#req_json').JSONView('expand');
	});
	
	
	var resp = $('#resp').val();
	$("#resp_json").JSONView(resp);


	$('#resp-collapse-btn').on('click', function() {
		$('#resp_json').JSONView('collapse');
	});
	$('#resp-expand-btn').on('click', function() {
		$('#resp_json').JSONView('expand');
	});
});