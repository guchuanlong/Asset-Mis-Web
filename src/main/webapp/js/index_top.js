function quanp(){
var single_hh = $(window).height();
var single_ww = $(window).width();
$('.banner').height(single_hh);
$('.banner').width(single_ww);
}
quanp();
$(window).resize(function(){
	if (typeof indexSlides != 'undefined' && indexSlides.reformat) 
		indexSlides.reformat();
		quanp();
});