/*!
 * FileName   : Jcode.js
 * Desc       : 游戏联运JS库
 * Author     : 属牛
 * version    : 3.8.6.4
 * */
(function () {
    isIE6 = !(!window.ActiveXObject) && !window.XMLHttpRequest;
    var testIEResult = /MSIE\s*(\d)/.exec(navigator.userAgent);
    IEVersion = (testIEResult) ? parseInt(testIEResult[1]) : 100;
}());
(function($){
	var DEFAULT_ANIMATE_PERIOD = 2000;//默认动画播放间隔时间
    var ANIMATE_SPEED = 800;//默认动画速度
	var Jcode=function(){
		var jdo={
			Cookie:{//操作cookie
				get:function (/*String*/name) {
					var val = null, r = /^(\s|\u00A0)+|(\s|\u00A0)+$/g;
					if (document.cookie && document.cookie != "") {
						var h = document.cookie.split(";");
						for (var g = 0; g < h.length; g++) {
							var f = (h[g] || "").replace(r, "");
							if (f.substring(0, name.length + 1) === (name + "=")) {
								val = decodeURIComponent(f.substring(name.length + 1));
								break;
							}
						}
					}
					return val;
				},
                set:function (/*String*/name, /*String*/val, /*Number*/sec) {
                    var exp = new Date();
                    exp.setTime(exp.getTime() + sec * 1000);
                    document.cookie = name + "=" + val + ";expires=" + exp.toGMTString() + ";path=/;domain=duowan.com";
                },
                clear:function (/*String*/name) {
                    this.set(name, "", -1);
                }
            },
			extend:function (/*Object*/target, /*Object*/origin, /*Boolean*/overwrite) {
                var tgt = target || {};
                var ori = origin || {};
                var ele;
                if (overwrite) {//覆盖
                    for (ele in ori) {
                        tgt[ele] = ori[ele];
                    }
                } else {
                    for (ele in ori) {
                        if (tgt[ele] === undefined || tgt[ele] === null) {
                            tgt[ele] = ori[ele];
                        }
                    }
                }
            },
			hidemoseright:function(){//限制页面鼠标右键功能
 				document.oncontextmenu=function(event){  
            		if(event.preventDefault){
                		event.preventDefault();
            		}
            		event.returnValue=false;
            		return false;
        		};
			},
			addHandler:function (elem, type, func) {//绑定事件到对象上
                if (elem.addEventListener) {//现代浏览器
                    elem.addEventListener(type, func, false);
                } else {//IE
                    elem.attachEvent("on" + type, func);
                }
            },
			centerOnScreen:function (/*Object*/obj) {//居中浏览器
                var w = $(window);
                var d = document.documentElement;
                var left = Math.floor(($(window).width() - obj.width()) / 2);
                var top = Math.floor(($(window).height() - obj.height()) / 2);
                //if IE6
                if (isIE6) {
                    left += d.scrollLeft;
                    top += d.scrollTop;
                    jdo.addHandler(window, "scroll", function () {
                        var w = $(window);
                        var d = document.documentElement;
                        var left = Math.floor((w.width() - obj.width()) / 2) + d.scrollLeft;
                        var top = Math.floor((w.height() - obj.height()) / 2) + d.scrollTop;
                        obj.css({
                            left:left + "px",
                            top:top + "px"
                        });
                    });
                }

                obj.css({
                    left:left + "px",
                    top:top + "px"
                });
            },
			Mask:{//遮盖层
                mask:null,
                IE6Fixed:function () {
                    var d = document.documentElement;
                    this.mask.css({
                        width:d.scrollWidth < d.clientWidth ? d.clientWidth : d.scrollWidth,
                        height:d.scrollHeight < d.clientHeight ? d.clientHeight : d.scrollHeight
                    });
                },
                show:function () {
                    if (!this.mask) {
                        this.mask = $("<div id='mask'></div>");
                    }
                    if (isIE6) {
                        this.mask.css("position", "absolute");
                        this.IE6Fixed();
                        jdo.addHandler(window, "resize", function () {
                            jdo.Mask.IE6Fixed();
                        });
                    }
                    if (this.mask.css("display") === "none") {
                        this.mask.show();
                    } else {
                        $("body").append(this.mask);
                    }
                    return this;
                },
                hide:function () {
                    this.mask.hide();
                    return this;
                },
                destory:function (removeFromDOM) {
                    this.mask.remove();
                    if (removeFromDOM) this.mask = null;
                }
            },
            PopupBox:{ //弹出层
                popupBox:null,
                setting:{
                    isDefault:false, //是否采用默认的样式，默认采用
                    content:"", //内容
                    existMask:true //是否有遮盖
                },
                createBox:function () {
                    var popupBox = $("<div class='popup-box'></div>");
                    popupBox.hide().appendTo($("body"));
                    this.popupBox = popupBox;
                    jdo.addHandler(document, "keydown", function (event) {
                        var e = event || window.event;
                        if (e.keyCode == 27) jdo.PopupBox.close();
                    });
                },
                show:function (setting) {
                    jdo.extend(this.setting, setting, true);
                    if (!this.popupBox) {
                        this.createBox();
                    }
                    var popupBox = this.popupBox;
                    if (this.setting.isDefault) {
                        var html = "<h3 class='popup-box-title'>温馨提示</h3><a href='#' class='popup-box-close-btn close' title='关闭'>关闭</a>";
                        html += "<div class='popup-box-content'>";
                        html += this.setting.content;
                        html += "</div>";
                        popupBox.html(html);
                    } else {
                        popupBox.html("").append(this.setting.content);
                    }
                    //加iframe是为了保证弹框在flash视频之上
                    var iframeHtml = "<iframe style='width:";
                    iframeHtml += popupBox.width() + "px; height:" + popupBox.height();
                    iframeHtml += "px; position:absolute; left:0; top:0; z-index:-1; background-color:#000;filter: alpha(opacity=0);-moz-opacity: 0;opacity: 0;'></iframe>";
                    popupBox.prepend(iframeHtml);
                    popupBox.find(".close").click(function () {
                        jdo.PopupBox.close();
                        return false;
                    });
                    jdo.centerOnScreen(popupBox);
                    jdo.addHandler(window, "resize", function () {
                        jdo.centerOnScreen(popupBox);
                    });
                    if (this.setting.existMask) {
                        jdo.Mask.show();
                    }
                    popupBox.show().focus();
                },
                close:function () {
                    if (this.popupBox) {
                        this.popupBox.hide();
                        if (this.setting.existMask)
                            jdo.Mask.hide();
                    }
                },
                destory:function () {
                    this.close();
                    this.popupBox.remove();
                    this.popupBox = null;
                }
            },
			loadJS:function (/*string*/src, /*function*/callback) {//动态加载js
                var script = document.createElement("script");
                script.type = "text/javascript";
                script.src = src;
                script.onload = callback;
                script.onreadystatechange = function () {
                    if (script.readyState == "loaded" || script.readyState == "complete") {
                        script.onload();
                    }
                };
                document.getElementsByTagName("head")[0].appendChild(script);
            },
			Udblogin:function(userbox,loginbtn){//UDB登录
				jdo.loadJS('http://res.udb.duowan.com/js/oauth/udbsdk/proxy/udbsdkproxy.min.js',function(){
					jdo.userLogin(userbox,loginbtn);
				});
				return;
			},
			userLogin:function(userbox,loginbtn){//UDB用户登录弹窗
				var cookie = jdo.Cookie;
				var username = cookie.get("username");
				function showUserName(username) {
					var username = (username || cookie.get("username"));
					$(userbox).html("<span>欢迎您，"+username+"</span><a href='#' title='退出' class='logout-btn'>退出</a>");
				}
				function isLogin(){
					if (cookie.get("username") && cookie.get("oauthCookie")) {
						return true;
					}
					showloginFrame(false);
					return false;
				}
				function ifLoginThenDo(callback){
					if(!isLogin()) return false;
					callback();
					return true;
				}
				//登录按钮
				$(loginbtn).live("click",function(){
					showloginFrame();
					return false;
				});
				//退出按钮
				$(".logout-btn").live("click",function(){
					logout();
					return false;
				});
				//判断当前是否已经登录，如果已经登录就显示用户名
				if(username && cookie.get("oauthCookie")) showUserName(username);
			},
			formatPic:function (/*Number*/width) {//限定网站的图片宽度，并允许点击放大
                var _imgs = document.images;
                for (var i = 0; i < _imgs.length; i++) {
                    if (_imgs[i].offsetWidth > width) {
                        _imgs[i].width = width;
                        jjo.addHandler(_imgs[i], "click", function (e) {
                            var evt = e || window.event;
                            var img = evt.target || evt.srcElement;
                            window.open(img.src);
                        });
                        _imgs[i].style.cursor = "pointer";
                        _imgs[i].title = "点击放大";
                    }
                }
            },
			SWFPlayer:{//调用FLV播放器
                createObject:function (/*Array*/setting, /*Object*/params, /*Object*/variables, /*String*/id) {
                    var args = arguments;
                    if (!window.SWFObject) {
                        jdo.loadJS("http://gamelibrary.duowan.com/js/swfobject.js", function () {//没有就从格斗俱乐部里加载
                            jdo.SWFPlayer.init.apply(null, args);
                        });
                        return;
                    }
                    jdo.SWFPlayer.init.apply(null,args);
                },
                init:function (setting, params, variables, id) {
                    var swfobject = new SWFObject();
                    SWFObject.apply(swfobject, setting);
                    for (var e in params) {
                        swfobject.addParam(e, params[e]);
                    }
                    for (e in variables) {
                        swfobject.addVariable(e, variables[e]);
                    }
                    swfobject.write(id);
                }
            },
			
            /**
             * 列表动画的父类
             * @param $box 装着列表的父层
             */
            ListAnimation:function ($box) {//列表动画父类
                this.$box = $box;//装着列表的父层
                this.$list = $box.find(".animate-list");//列表
                this.$items = this.$list.find(".animate-item");//列表项
                this.$preBtn = $box.find(".previous");//向前按钮
                this.$nextBtn = $box.find(".next");//向后按钮
                this.$pageBtn = $box.find(".animate-page");//页码按钮
                this.maxIndex = this.$items.length - 1;//最大页码
                this.currentIndex = 0;//当前页码
                this.preIndex = this.currentIndex;//上次页码
                this.loop = false;//自动回到首页，默认为false
                return;
            },

            /**
             * 幻灯片
             * @param setting = {
             $slideBox       *装着列表的父层
             preNextTriggerEvent  *前后按钮触发条件，默认"click"
             pageTriggerEvent  *页码按钮触发条件，默认"mouseenter"
             direction      *方向：true为x轴（默认），false为y轴
             animateType     *动画方式：slide为滑动，fade为渐隐渐出
             loop            *是否轮回翻页，true轮回，false不轮回（默认）
             speed           *动画速度，默认500
             autoPlay        *是否自动播放动画，默认为false
             autoPlayPeriod  *自动播放动画的间隔时间，默认为1000毫秒
             }
             */
            Slide:function (setting) { //幻灯片效果
                /*----------配置----------*/
                /*配置默认参数*/
                jdo.extend(setting, {
                    $slideBox:$("body"),
                    preNextTriggerEvent:"click",
                    pageTriggerEvent:"mouseenter",
                    direction:true,
                    animateType:"slide",
                    loop:false,
                    speed:ANIMATE_SPEED,
                    autoPlay:false
                }, false);
                /*结合父类*/
                jdo.ListAnimation.call(this, setting.$slideBox);
                /*保存setting里的每个对象到this*/
                jdo.extend(this, setting, true);

                var that = this;
                var $list = this.$list;
                var animateTypeFunc = function(){};//animateTypeFunc根据不同的animateType来设置动画函数
                this.loop = this.autoPlay || this.loop;//如果自动播放为true，loop也为true

                if(this.animateType === "slide"){
                    //根据动画方向而设的不同变量：changeAtSize为变化高宽("width"/"height")，itemSize为单体高宽(width/height)，animateDirect为变化参数(margin-left/margin-top)
                    var changeAtSize, itemSize, animateDirect,animateTypeFunc;
                    if (this.direction) { changeAtSize = "width"; animateDirect = "margin-left"; itemSize = this.$items.first().outerWidth(); }
                    else { changeAtSize = "height"; animateDirect = "margin-top"; itemSize = this.$items.first().outerHeight(); }
                    var $listSize = itemSize * (this.maxIndex + 1);

                    var returnAnimateObjectByDirection = function (value) {
                        if (that.direction) return { marginLeft:value };
                        return { marginTop:value };
                    };
                    $list.css(changeAtSize, $listSize);
                    animateTypeFunc = function(){
                        $list.stop(true).animate(returnAnimateObjectByDirection(-that.currentIndex * itemSize), that.speed, "swing");
                    }
                    if(this.loop){//滑动的轮播
                        $list.css("float", "left");
                        var $wrap = $list.wrap("<div class='wrap'></div>").parent().css(changeAtSize, $listSize * 2);
                        var $listCopy = $list.clone(true);
                        $listCopy.insertAfter($list);
                        var changeList = function () {//交换真正的列表和隐藏的列表
                            var temp = $list;
                            $list = $listCopy;
                            $listCopy = temp;
                        };
                        this.getToHead = function () {
                            $listCopy.css(animateDirect, -$listSize).insertBefore($list);
                            changeList();
                        };
                        this.getToEnd = function () {
                            $list.stop(true).animate(returnAnimateObjectByDirection(-(that.maxIndex + 1) * itemSize), that.speed, "swing", function () {
                                $(this).css(animateDirect, 0).appendTo($wrap);
                            });
                            changeList();
                        };
                    }
                }else if(this.animateType === "fade"){
                    var $items = this.$items;
                    for(var i = 1,len = this.$items.length; i < len; ++i){
                        $items.eq(i).hide();
                    }
                    if(IEVersion < 7){
                        animateTypeFunc = function () {
                            if (that.preIndex == that.currentIndex) return;
                            $items.eq(that.preIndex).hide();
                            $items.eq(that.currentIndex).show();
                        }
                    }else{
                        animateTypeFunc = function () {
                            if (that.preIndex == that.currentIndex) return;
                            $items.eq(that.preIndex).stop(true).fadeOut(that.speed/2,function(){
                                $items.eq(that.currentIndex).fadeIn(that.speed/2);
                            });
                        }
                    }
                }

                /*动画效果设置*/
                this.animate = function () {
                    animateTypeFunc();
                    if (this.currentIndex <= 0 && !this.loop) {//隐藏上一页
                        this.$preBtn.hide();
                    } else {//显示
                        this.$preBtn.show();
                    }
                    if (this.currentIndex >= this.maxIndex && !this.loop) {//隐藏下一页
                        this.$nextBtn.hide();
                    } else {//显示下一页
                        this.$nextBtn.show();
                    }
                };
                /*设置向前/向后键，页码键的动画效果*/
                this.preNextAnimate = this.animate;
                this.pageAnimate = function ($page, pageNum) {
                    this.currentIndex = pageNum - 1;
                    this.animate();
                };
                /*配置自动播放*/
                if (this.autoPlay) {
                    this.autoPlayFunc = function () {//自动播放动画函数
                        if (that.currentIndex == that.maxIndex && that.getToEnd) that.getToEnd();
                        if (++that.currentIndex > that.maxIndex) that.currentIndex = 0;
                        if(that.pageAffect$pageBtn) that.pageAffect$pageBtn();
                        that.animate();
                        that.preIndex = that.currentIndex;
                    };
                }

                /*----------初始化----------*/
                /*初始化父类*/
                this.initListAnimation();

                /*初始化本类*/
                this.$preBtn.hide();
                if (this.loop && this.maxIndex > 0) this.$preBtn.show();
                if (this.maxIndex < 1 || this.$nextBtn.attr("animate-num") > this.maxIndex) this.$nextBtn.hide();
                if (this.autoPlay) {
                    this.autoPlayStart();
                    this.$box.delegate(".animate-page,.previous,.next", "mouseleave", function () {
                        that.autoPlayStart();
                    });
                    return{
                        autoPlayStart:that.autoPlayStart,
                        autoPlayStop:that.autoPlayStop
                    }
                }
                return;
            },

            /**
             * 手风琴
             * @param setting = {
             $accordionBox       *装着列表的父层
             pageTriggerEvent  *页码按钮触发条件，默认"mouseenter"
             opacity         *设置百叶窗的透明度，不支持IE6，默认为0.4
             speed           *动画速度，默认500
             autoPlay        *是否自动播放动画，默认为false
             autoPlayPage    *自动播放，从指定页开始
             autoPlayPeriod  *自动播放动画的间隔时间，默认为1000毫秒
             }
             */
            Accordion:function (setting) { //手风琴效果
                /*----------配置----------*/
                /*配置默认参数*/
                jdo.extend(setting, {
                    $accordionBox:$("body"),
                    pageTriggerEvent:"mouseenter",
                    opacity:0.4,
                    speed:ANIMATE_SPEED,
                    autoPlay:false
                }, false);
                var $accordionBox = setting.$accordionBox;
                var that = this;//获得this引用，有些函数是闭包
                /*结合父类*/
                jdo.ListAnimation.call(this, $accordionBox);
                /*保存setting里的每个对象到this*/
                jdo.extend(this, setting, true);

                /*配置单体参数，即每个列表项*/
                this.itemMaxWidth = parseInt(this.$items.first().width());//单体最大宽度
                var listWidth = parseInt(this.$list.width());
                this.itemMinWidth = (listWidth - this.itemMaxWidth) / this.maxIndex;//单体最小宽度
                this.itemNormalWidth = listWidth / (this.maxIndex + 1);//单体平均分布的宽度

                /*动画效果*/
                this.animate = function ($obj, setting) {//动画设置
                    if (IEVersion < 9 && setting.opacity) delete setting.opacity;//IE不能设置opacity，否则会宕机
                    $obj.stop(true).animate(setting, this.speed, "swing");
                };
                this.animateToAppointedIndex = function (index) {//显示指定的页，闭包
                    var $page, pageIndex;
                    if (typeof(index) == "number") {
                        $page = that.$items.eq(index);
                        pageIndex = index;
                    } else {
                        $page = index;
                        pageIndex = $page.index();
                    }
                    that.currentIndex = pageIndex;
                    that.animate(that.$items.find("img"), {opacity:that.opacity});//全部变灰，可以配合reset时全部高亮
                    that.animate($page.find("img"), {opacity:"1"});

                    for (var i = 0; i <= pageIndex; i++) {
                        that.animate(that.$items.eq(i), {left:that.itemMinWidth * i + "px"});
                    }
                    for (i = pageIndex + 1; i <= that.maxIndex; i++) {
                        that.animate(that.$items.eq(i), {left:that.itemMinWidth * (i - 1) + that.itemMaxWidth + "px"});
                    }
                    that.preIndex = that.currentIndex;
                };
                this.reset = function () {//重置动画，闭包
                    that.$items.each(function () {
                        var $this = $(this).stop(true);
                        var i = $this.index();
                        that.animate($this.find("img"), {opacity:1});
                        that.animate($this, {left:that.itemNormalWidth * i + "px"});
                    });

                };
                this.pageAnimate = function (pageEle, pageNum) {//页码触发动画
                    var $page = $(pageEle);
                    this.animateToAppointedIndex($page);
                };

                /*配置自动播放*/
                if (this.autoPlay && !this.autoPlayPage) this.autoPlayPage = 0;
                this.autoPlayFunc = function () {//自动播放动画函数
                    if (that.autoPlayPage > that.maxIndex) {
                        that.reset();
                        that.autoPlayPage = 0;
                    } else {
                        that.animateToAppointedIndex(that.autoPlayPage++);
                    }
                };

                /*----------初始化----------*/
                //初始化父类
                this.initListAnimation();

                //初始化本类
                this.$items.each(function () {
                    var $this = $(this);
                    $this.css("position", "absolute");
                    var i = $this.index();
                    $this.css({left:that.itemNormalWidth * i + "px"});
                });
                this.$list.bind("mouseleave", function () {
                    that.reset();
                    if (that.autoPlay) that.autoPlayStart();
                });
                if (this.autoPlay) {
                    this.autoPlayStart();
                    return{
                        animateToAppointedIndex:that.animateToAppointedIndex,
                        reset:that.reset,
                        autoPlayStart:that.autoPlayStart,
                        autoPlayStop:that.autoPlayStop
                    }
                }
            },

            /**
             * 列表滚动
             * @param setting = {
             $textRollBox    *装着列表的父层
             speed           *动画速度，默认500
             autoPlay        *是否自动播放动画，默认为false
             direction      *自动播放的方向：true为由下至上（默认），false为由上至下
             autoPlayPeriod  *自动播放动画的间隔时间，默认为1000毫秒
             }
             */
            TextRoll:function (setting) {//列表滚动效果
                /*----------配置----------*/
                /*配置默认参数*/
                jdo.extend(setting, {
                    $textRollBox:$("body"),
                    speed:500,
                    direction:true,
                    autoPlay:true,
                    autoPlayPeriod:5000
                }, false);
                /*结合父类*/
                jdo.ListAnimation.call(this, setting.$textRollBox);
                /*保存setting里的每个对象到this*/
                jdo.extend(this, setting, true);
                var that = this;
                var $items = this.$items;
                var itemHeight = parseInt($items.first().outerHeight());
                var animate = function ($obj, setting, callback) {
                    $obj.animate(setting, that.speed, "swing", callback);
                };
                var nextText = function () {
                    if ($items.filter(":animated").length > 0) return false;
                    if (that.currentIndex > that.maxIndex) that.currentIndex = 0;
                    animate($items.eq(that.currentIndex++), {marginTop:-itemHeight}, function () {
                        $(this).css("margin-top", 0).appendTo(that.$list);
                    });
                };
                var preText = function () {
                    if ($items.filter(":animated").length > 0) return false;
                    if (--that.currentIndex < 0) that.currentIndex = that.maxIndex;
                    var $obj = $items.eq(that.currentIndex);
                    $obj.css({marginTop:-itemHeight + "px"}).prependTo(that.$list);
                    animate($obj, {marginTop:0});
                };
                if (this.$nextBtn.length > 0) {
                    this.$nextBtn.click(function () {
                        nextText();
                        return false;
                    });
                }
                if (this.$preBtn.length > 0) {
                    this.$preBtn.click(function () {
                        preText();
                        return false;
                    });
                }
                this.autoPlayFunc = (this.direction) ? nextText : preText;

                this.initListAnimation();
                this.autoPlayStart();
                if (this.autoPlay) return{
                    autoPlayStart:that.autoPlayStart,
                    autoPlayStop:that.autoPlayStop
                };
                return;
            },
            /**
             * 公告板
             * @param $boardBox 装着列表的box
             * @param speed 动画速度
             */
            Board:function ($boardBox, speed) {//公告板效果
                var $list = $boardBox.find(".animate-list");
                var $listHeight = parseInt($list.outerHeight());
                if ($listHeight < parseInt($boardBox.height())) return;
                var $listCopy = $list.clone(false).insertAfter($list);
                var $animateList1 = $list;
                var $animateList2 = $listCopy;
                var marginTop = 0;

                var autoPlayInterval = null;
                var autoPlayStart = function () {
                    if (autoPlayInterval) return false;
                    autoPlayInterval = setInterval(function () {
                        if (marginTop <= -$listHeight) {
                            var temp = $animateList1.remove().clone();
                            temp.css("margin-top", 0).insertAfter($animateList2);
                            marginTop = 0;
                            $animateList1 = $animateList2;
                            $animateList2 = temp;
                        }
                        $animateList1.css("margin-top", --marginTop + "px");
                    }, speed / 10);
                }
                var autoPlayStop = function () {
                    if (autoPlayInterval) {
                        clearInterval(autoPlayInterval);
                        autoPlayInterval = null;
                    }
                }

                $boardBox.mouseenter(function () {
                    autoPlayStop();
                }).mouseleave(function () {
                        autoPlayStart();
                    });

                autoPlayStart();
                return{
                    autoPlayStart:autoPlayStart,
                    autoPlayStop:autoPlayStop
                }
            },
			ImageBrowser:function(/*Array*/imgSrc){//图片浏览
                var html = "<div id='img-browser-box'><a href='#' class='close'>关闭</a><a href='#' class='previous'>上一页</a><a href='#' class='next'>下一页</a></div>";
                var $list = $("<ul class='animate-list'></ul>");
                for ( var i = 0, len = imgSrc.length; i < len; ++i){
                    $list.append($("<li class='animate-item'><img src='" + imgSrc[i] + "'/></li>"));
                }
                var $box = $(html).append($list).appendTo($("body"));
                var s = new jdo.Slide({
                    $slideBox:$("#img-browser-box"),
                    animateType:"fade"
                });
                jdo.PopupBox.show({
                    isDefault:false,
                    content:$box,
                    existMask:true //是否有遮盖
                });
            },
			/**
             * 抽奖模块
             * @param lotteryBox 抽奖装载容器
             * @param lotteryWidth 宽度
			 * @param lotteryHeight 高度
			 * @param lotteryXML 配置XML路径
             */
			lottery:function(setting){
				if (!window.SWFObject) {
					
					jdo.loadJS("http://gamelibrary.duowan.com/js/swfobject.js", function () {//没有就从格斗俱乐部里加载
						var lotterySwf= new SWFObject("swf/lottery.swf", "lotterySWF",setting.lotteryWidth,setting.lotteryHeight,'9');
						lotterySwf.addParam("allowScriptAccess","always");
						lotterySwf.addParam("quality","high");
						lotterySwf.addParam("wmode","transparent");
						console.log(setting.lotteryWidth);
						lotterySwf.write(setting.lotteryBox.slice(1));
					});
					return;
				}
			}
		};
		 /*原型链继承*/
        var inheritPrototype = function(/*Object*/superClass,/*Array*/subClassArray){//服务多个子类
            var f = function(){}
            f.prototype = superClass.prototype;
            for(var i = 0, len = subClassArray.length; i < len; ++i){
                var subProty = new f();
                subProty.constructor = subClassArray[i];
                subClassArray[i].prototype = subProty;
            }
        }
        jdo.ListAnimation.prototype.initListAnimation = function () {
            var that = this;
            if (this.$preBtn.length > 0 && this.preNextAnimate) {
                this.$preBtn.bind(this.preNextTriggerEvent, function () {
                    if (that.autoPlay) that.autoPlayStop();
                    var preAnimateNum = parseInt($(this).attr("animate-num")) || 1;
                    if (that.currentIndex == 0 && that.loop) {
                        if (that.getToHead) that.getToHead();//到了开头
                        that.currentIndex = that.maxIndex;
                    } else {
                        that.currentIndex -= preAnimateNum;
                        if (that.currentIndex < 0) that.currentIndex = 0;
                    }
                    that.preNextAnimate(this);
                    if (that.pageAffect$pageBtn) that.pageAffect$pageBtn();
                    that.preIndex = that.currentIndex;
                    return false;
                });
            }
            if (this.$nextBtn.length > 0 && this.preNextAnimate) {
                this.$nextBtn.bind(this.preNextTriggerEvent, function () {
                    if (that.autoPlay) that.autoPlayStop();
                    var nextAnimateNum = parseInt($(this).attr("animate-num")) || 1;
                    if (that.currentIndex == that.maxIndex && that.loop) {
                        if (that.getToEnd) that.getToEnd();//到了末尾
                        that.currentIndex = 0;
                    } else {
                        that.currentIndex += nextAnimateNum;
                        if (that.currentIndex > that.maxIndex) that.currentIndex = that.maxIndex;
                    }
                    that.preNextAnimate(this);
                    if (that.pageAffect$pageBtn) that.pageAffect$pageBtn();
                    that.preIndex = that.currentIndex;
                    return false;
                });
            }
            if (this.$pageBtn.length > 0) {//如果有页码按钮，显示中的页码按钮会添加样式on
                this.$pageBtn.eq(this.currentIndex).addClass("on");
                this.pageAffect$pageBtn = function () {//因上一页/下一页页面改变而影响到页码按钮的改变
                    that.$pageBtn.eq(that.preIndex).removeClass("on").end()
                        .eq(that.currentIndex).addClass("on");
                }
            }
            if (this.$pageBtn.length > 0 && this.pageAnimate) {
                //页码按钮处理
                this.$box.delegate(".animate-page", this.pageTriggerEvent, function () {
                    if (that.autoPlay) that.autoPlayStop();
                    var $this = $(this);
                    var pageNum = $this.attr("page-num");
                    if (!pageNum) pageNum = 1;
                    if ((pageNum - 1) != that.preIndex) {
                        that.$pageBtn.eq(that.preIndex).removeClass("on");
                        $this.addClass("on");
                    }
                    that.currentIndex = parseInt(pageNum) - 1;
                    that.pageAnimate(this, pageNum);
                    that.preIndex = that.currentIndex;
                    return false;
                });
            }
            if (this.autoPlay && this.autoPlayFunc) {
                if (!that.autoPlayPeriod) that.autoPlayPeriod = DEFAULT_ANIMATE_PERIOD;
                this.autoPlayStart = function () {
                    if (!that.autoPlayInterval)
                        that.autoPlayInterval = setInterval(that.autoPlayFunc, that.autoPlayPeriod);
                };
                this.autoPlayStop = function () {
                    clearInterval(that.autoPlayInterval);
                    that.autoPlayInterval = null;
                };
            }
        }
        inheritPrototype(jdo.ListAnimation,[jdo.Slide,jdo.Accordion,jdo.TextRoll]);
		return jdo;
	}();
	window.Jcode=Jcode;
})(jQuery)