// JavaScript Document
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
		paddingRight : "25px"
	}, 200);
}, function() {
	$(this).stop().animate({
		paddingRight : "15px"
	});
});
$(".content-box-header h3").css({
	"cursor" : "pointer"
});
$(".closed-box .content-box-content").hide();
$(".closed-box .content-box-tabs").hide();
$(".content-box-header h3").click(function() {
	$(this).parent().next().toggle();
	$(this).parent().parent().toggleClass("closed-box");
	$(this).parent().find(".content-box-tabs").toggle();
});
$('.content-box .content-box-content div.tab-content').hide();
$('ul.content-box-tabs li a.default-tab').addClass('current');
$('.content-box-content div.default-tab').show();
$('.content-box ul.content-box-tabs li a').click(function() {
	$(this).parent().siblings().find("a").removeClass('current');
	$(this).addClass('current');
	var currentTab = $(this).attr('href');
	$(currentTab).siblings().hide();
	$(currentTab).show();
	return false;
});
$('#main-nav a').bind('click', function() {
	if ($(this).hasClass('nav-top-item'))
		$('#main-nav a.nav-top-item').removeClass('current');
	else
		$('#main-nav li ul li a').removeClass('current');
	$(this).addClass('current');
});
$(".close").live('click', function() {
	$(this).parent().fadeTo(400, 0, function() {
		$(this).slideUp(400);
	});
	return false;
});
// 提示信息，type:attention(警告) information(信息) success(成功) error(错误)
function tis(type, tisText) {
	var tis = new Array();
	tis
			.push('<div class="notification '
					+ type
					+ ' png_bg"><a href="#" class="close"><img src="images/icons/cross_grey_small.png" title="关闭提示" alt="close" /></a><div>');
	tis.push(tisText);
	tis.push('</div></div>');
	$('#tisbox').html(tis.join('')).slideDown('slow');
	if (type == 'error') {
		var errorTime = setInterval(function() {
			$('.notification a.close').click();
			clearInterval(errorTime);
		}, 3000);
	}
}
function divTis(id, type, tisText) {
	var tis = new Array();
	tis
			.push('<div class="notification '
					+ type
					+ ' png_bg"><a href="#" class="close"><img src="images/icons/cross_grey_small.png" title="关闭提示" alt="close" /></a><div>');
	tis.push(tisText);
	tis.push('</div></div>');
	$('#' + id + '').html(tis.join('')).slideDown('slow');
	if (type == 'error') {
		var errorTime = setInterval(function() {
			$('.notification a.close').click();
			clearInterval(errorTime);
		}, 3000);
	}
}
$('a.type').bind('click', function() {
	var loading = '<div class="loading"></div>';
	var DType = $(this).attr('rel');
	$('#content').append(loading);
	$('h3.check').html($(this).html());
	$.ajax({
		url : DType,
		cache : false,
		success : function(html) {
			$(".boxy-wrapper").remove();
			$("#content").html(html);
			$('#content .loading').remove();
		}
	});
});
$("#datetime").live('focus', function() {
	var e = $dp.$('endtime');
	WdatePicker({
		onpicked : function() {
			e.focus();
		},
		maxDate : '#F{$dp.$D(\'endtime\')}',
		startDate : '%y-%M-01 00:00:00',
		dateFmt : 'yyyy-MM-dd'
	});
	$(this).attr('title', '(日期时间格式：YY-MM-DD)');
});
$("#endtime").live('focus', function() {
	WdatePicker({
		minDate : '#F{$dp.$D(\'datetime\')}',
		startDate : '%y-%M-01 00:00:00',
		dateFmt : 'yyyy-MM-dd'
	});
	$(this).attr('title', '(日期时间格式：YY-MM-DD)');
});

function alias(srcParam) {
	var str = '';
	if (srcParam != null) {
		var array = srcParam.split('&');
		for ( var i = 0; i < array.length; i++) {
			var tmp = array[i];
			var a = tmp.split('=');
			if (a.length == 2) {
				var name = a[0].replace(/[^&\s]+?_/g, '');
				var value = a[1];
				str += '&' + name + '=' + value;
			}
		}
		return str;
	}
	return '';
}

function firstPage() {
	currentPage = 1;
	doQuery();
}
function lastPage() {
	if (currentPage > 1) {
		currentPage -= 1;
	}
	doQuery();
}
function nextPage() {
	if (currentPage < totalPage) {
		currentPage += 1;
	}
	doQuery();
}
function endPage() {
	currentPage = totalPage;
	doQuery();
}

function jumpToPage() {
	var value = $('#currentPage').val();
	if (value == currentPage) {
		return false;
	}
	if (value == null || !/^\d+$/.test(value)) {
		return false;
	}
	if (value > totalPage) {
		return false;
	}
	currentPage = value;
	doQuery();
}

function getLocalTime(ns) {
	if (ns == null || ns == '') {
		return "";
	}
	var date = new Date(ns);
	Y = date.getFullYear() + '-';
	M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date
			.getMonth() + 1)
			+ '-';
	D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
	h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
	m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes())
			+ ':';
	s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
	return Y + M + D + h + m + s;
}

function getLocalDate(ns) {
	if (ns == null || ns == '') {
		return "";
	}
	var date = new Date(ns);
	Y = date.getFullYear() + '-';
	M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date
			.getMonth() + 1)
			+ '-';
	D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
	return Y + M + D;
}

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return decodeURI(r[2]);
	return null;
}

function queryListData(url){
	var form = $('#form');
	showBox('loading');
	$.ajax({      
		type : 'GET',    
		contentType:"application/x-www-form-urlencoded; charset=utf-8", 
		url : url,
		data: form.formSerialize()+'&pageSize=10&currPage='+currentPage,
		dataType : 'json',      
		success : function(result) {
			showBox('close');
			if (result && result.status == "200"){
				if(result.data.result!=null){
					buildTableData(result.data);
				}
				currentPage=result.data.page.currPage;
				totalPage=result.data.page.totalPage;
				$('#totalCount').html(result.data.page.totalRecord);
				$('#pageSize').html(10);
				$('#pageCombine').html(currentPage+"/"+totalPage);
				$('#totalPage').val(totalPage);
				$('#currentPage').val(currentPage);
			}else{
				$('#tbody').empty();
				showBox('',result.message,'');
			}
		}
	});
}

function loadGameServers(bindNode){
	var gameId = getQueryString('nowGame');
	var servers='<option value="">--全部--</option>';
	$.ajax({      
		type : 'GET',    
		contentType:"application/x-www-form-urlencoded; charset=utf-8", 
		url : 'game/queryServers',
		data:'gameId='+gameId,
		dataType : 'json',      
		success : function(result) {
			if (result && result.status == '200'){
				if(result.data!=null){
					$.each(result.data, function(i, item) {						
						servers+='<option value="'+item.parnterServerId+'">'+(item.serverId+'_'+item.parnterServerId+'_'+item.serverName)+'</option>';
					});
				}
			}
			$(bindNode).each(function(index){
				$(this).html(servers);
			});
		}
	});
}

function queryUserRoles(account , accountType,server, obj){
	var gameId = getQueryString('nowGame');
	var reqeustData = 'game='+gameId;
	var roles='<option value="">--请选择--</option>';
	$.ajax({      
		type : 'GET',    
		contentType:"application/x-www-form-urlencoded; charset=utf-8", 
		url : 'user/queryUserInfo',
		data:'&pageSize=50&currPage=1&game='+gameId+'&account='+account+'&server='+server+'&accountType='+accountType,
		dataType : 'json',      
		success : function(result) {
			if (result && result.status == "0"){
				var listData = result.data.result;
				$.each(listData, function(i, item) {
					roles+='<option value="'+item.obj.roleId+'">'+(item.obj.roleName)+'</option>';
				});
				$(obj).html(roles);
			}
		}
	});
}


function selectTypeOnChange(selectType){
	var selectVal = $(selectType).val();
	if(selectVal=="passport"){
		$("#passportDiv").show();
		$("#roleIdDiv").hide();
	}else{
		$("#passportDiv").hide();
		$("#roleIdDiv").show();
	}
}

function accountOnBlur(){
	var gameId = getQueryString('nowGame');
	var reqeustData = 'game='+gameId;
	var account = $("#account").val();
	var server = $("#queryServer").val();
	var options = "<option value=''>&nbsp;</option>";
	$.ajax({      
		type : 'POST',    
		contentType:"application/x-www-form-urlencoded; charset=utf-8", 
		url : 'user/queryUserInfo',
		data:'game='+gameId+'&account='+account+'&server='+server,
		dataType : 'json',      
		success : function(result) {
			if (result && result.status == "0"){
				var listData = result.data.result;
				for(var i = 0;i<listData.length;i++){
					var gameUserInfo = listData[i];
					options+="<option value='"+gameUserInfo.obj.roleId+"'>"+gameUserInfo.obj.roleName+"</option>";
				}
			}
			 $("#queryRoleName").html(options); 
		}
	});
}

function printNullValue(obj){
	if(obj==null){
		return '';
	}
	return obj;
}


Date.prototype.format = function(format) {
	/*
	 * eg:format="YYYY-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" :this.getMonth() + 1, // month
		"d+" :this.getDate(), // day
		"h+" :this.getHours(), // hour
		"m+" :this.getMinutes(), // minute
		"s+" :this.getSeconds(), // second
		"q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" :this.getMilliseconds()
		// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
			.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
				: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}