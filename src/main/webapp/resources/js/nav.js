var didScroll;
var lastScrollTop = 0;
var delta = 5;
var navbarHeight = $('#nav').outerHeight();

$(window).scroll(function(event){
    didScroll = true;
});

setInterval(function() {
    if(didScroll) {
        hasScrolled(); didScroll = false;
}}, 250);

function hasScrolled() {
    var st = $(this).scrollTop();
    if(Math.abs(lastScrollTop - st) <= delta)
    return;

    if (st > lastScrollTop && st > navbarHeight){
        $('#nav').removeClass('nav-down').addClass('nav-up');
        $('#nav .menu li .depth').hide();
    } else {
        if(st + $(window).height() < $(document).height()) {
            $('#nav').removeClass('nav-up').addClass('nav-down');
        }
    }
    lastScrollTop = st;
}

$('#nav .menu li').mouseenter(function () { 
    $(this).prevAll().children('.depth').hide();
    $(this).nextAll().children('.depth').hide();
    $(this).children('.depth').fadeIn(200);
    $(this).addClass('on');
});

$('#nav .menu li').mouseleave(function () { 
    $(this).removeClass('on');
});

$('#nav .menu li .depth').mouseleave(function () { 
    $(this).hide();
    $(this).parent().removeClass('on');
});

let nowUrl = document.location.pathname;
if(nowUrl == "/views/index.html") {
    $("#nav .menu li").removeClass("on");
    $("#nav .menu li").eq(0).addClass("on");
}