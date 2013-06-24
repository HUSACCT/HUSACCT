$(document).ready(function() {
	show_page("home");
});

function show_page(page_name){
	to_show_div = "#content .page_content." + page_name;
	if($(to_show_div).length < 1){
		$.when(create_page_content_container(page_name))
		.then(load_page_content_into_div(page_name))
		.then(hide_all_page_content())
		.then($(to_show_div).fadeIn(250));
	}
}

function create_page_content_container(page_name){
	$("#content").html('<div class="page_content ' + page_name + '"></div>');
}

function load_page_content_into_div(page_name){
	$("#content .page_content." + page_name).load("page_content/" + page_name + ".html");
}

function hide_all_page_content(){
	$("#content .page_content").fadeOut(250);
}