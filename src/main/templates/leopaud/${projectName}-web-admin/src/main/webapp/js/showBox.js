/*!
 * FileName   : showBox.js
 * Desc       : 弹出层库
 * Author     : 属牛
 * version    : 20130918
 * */
function showBox(type,text,title){
    switch (type){
		case "loading":
			Jcode.PopupBox.show({content:'<div class="showLoading">正在加载中，请稍等...</div>'});
			break;
		case "doing":
			Jcode.PopupBox.show({content:'<div class="showLoading">操作进行中，请稍等...</div>'});
			break;
		case "error":
			Jcode.PopupBox.show({content:'<div class="showerror">'+text+'</div>'});
			break;
		case "attention":
			Jcode.PopupBox.show({content:'<div class="showattention">'+text+'</div>'});
			break;
		case "information":
			Jcode.PopupBox.show({content:'<div class="showinformation">'+text+'</div>'});
			break;
		case "success":
			Jcode.PopupBox.show({content:'<div class="showsuccess">'+text+'</div>'});
			break;
		case "close":
			Jcode.PopupBox.close();
			break;
		default:
			var title = title || "温馨提示";
       		var html = text|| "操作失败，请稍后再试，如果问题依然存在，请联系相关技术处理，谢谢！";
        	Jcode.PopupBox.show({
            	content:'<div class="showdefault"><h3>'+ title + '<a class="close close-btn">关闭</a></h3><div class="popUpContent">'+html+'</div></div>'
        	});
        	break;
	}
	return false;
}
