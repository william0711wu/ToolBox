var api = '/menu/query';
function loadUserMenu(gameId){
	$.ajax({
	    type: "post",
	    url: api,
	    data: "time=" + new Date().getTime()+"&gameId="+gameId,
	    dataType: "json",
	    cache: false,
	    success: function(json) {
	        var status = json.status;
	        if (status == 200) {
	            var menuStr = '';
	            $.each(json.data,
	            function(i, item) {
	                var topMenuName = item.name;
	                menuStr += '<li> <a href="#" class="nav-top-item" style="padding-right: 15px;">' + topMenuName + '</a> ';
	                var childrens = item.childFuncs;
	                if (childrens.length > 0) {
	                    menuStr += '<ul style="display: none;">';
	                    $.each(childrens,
	                    function(j, child) {
	                    	var id = 'menu_'+child.desc.substring(child.desc.lastIndexOf('/')+1).replace('.html','');
	                        menuStr += '<li><a href="#" class="type" id="'+id+'" rel="' + child.desc + '">' + child.name + '</a></li> ';
	                    });
	                    menuStr += '</ul>';
	                }
	            });
	            $('#main-nav').html(menuStr);
	            bindMenuEvent();
	        }
	    }
	});
}

function bindMenuEvent() {
    $("#main-nav li ul").hide();
    $("#main-nav li a.current").parent().find("ul").slideToggle("slow");
    $("#main-nav li a.nav-top-item").click(function() {
        $(this).parent().siblings().find("ul").slideUp("normal");
        $(this).next().slideToggle("normal");
        return false;
    });
    $("#main-nav li a.no-submenu").click(function() {
        window.location.href = (this.href);
        return false;
    });
    $("#main-nav li .nav-top-item").hover(function() {
        $(this).stop().animate({
            paddingRight: "25px"
        },
        200);
    },
    function() {
        $(this).stop().animate({
            paddingRight: "15px"
        });
    });
    $('#main-nav a').bind('click',
    function() {
        if ($(this).hasClass('nav-top-item')) $('#main-nav a.nav-top-item').removeClass('current');
        else $('#main-nav li ul li a').removeClass('current');
        $(this).addClass('current');
    });
    $('a.type').bind('click',
    function() {
        var loading = '<div class="loading"></div>';
        var DType = $(this).attr('rel');
        $('#content').append(loading);
        $('h3.check').html($(this).html());
        $.ajax({
            url: DType,
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            cache: false,
            success: function(html) {
                $(".boxy-wrapper").remove();
                $("#content").html(html);
                $('#content .loading').remove();
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                $('#content .loading').remove();
            }
        });

    });
}

function loadMenuContent(obj){
	var loading='<div class="loading"></div>';
	var DType=$(obj).attr('rel');
	var current = $(".current");
	if(typeof(current)!='undefined' && current!=null){
		current.removeClass('current');
	}
	$(obj).addClass('current');
	$('#content').append(loading);
	$('h3.check').html($(obj).html());
	$.ajax({
	  url: DType,
	  contentType:"application/x-www-form-urlencoded; charset=utf-8",
	  cache: false,
	  success: function(html){
		$(".boxy-wrapper").remove();
		$("#content").html(html);
		$('#content .loading').remove();
	  },
	  error:function (XMLHttpRequest, textStatus, errorThrown) {
		  $('#content .loading').remove();
	  }
	});
}