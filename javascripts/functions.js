$(document).ready(function() {
	var anchor = window.location.hash.substring(1);
	if(anchor == ""){
		anchor = "home";
	}
	
	$(document).on("click", "#pages_menu a", function(){
		var page_name = $(this).attr("href").replace("#", "");
		show_page(page_name);
	});
	
	show_page(anchor);
	
});

function show_page(page_name){
	add_active_class(page_name);
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
	var domain = window.location.hostname;
	if($.contains(domain, 'husacct.nl')){
		$("#content .page_content." + page_name).load("http://husacct.github.io/HUSACCT/page_content/" + page_name + ".html");
	}else{
		$("#content .page_content." + page_name).load("page_content/" + page_name + ".html");
	}
}

function hide_all_page_content(){
	$("#content .page_content").fadeOut(250);
}

function add_active_class(page_name){
	var parentListItem = $('a[href="#' + page_name + '"]').parent("li");
	parentListItem.parents().addClass("active");
	parentListItem.siblings("li").removeClass("active");
	parentListItem.find("li").removeClass("active");
	if(!parentListItem.hasClass("active")){
		parentListItem.addClass("active");
	}
} 