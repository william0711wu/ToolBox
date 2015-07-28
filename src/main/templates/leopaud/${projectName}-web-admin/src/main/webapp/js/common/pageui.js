/**
 * 页面涉及到的UI
 * User: xiejinlong@yy.com
 * Time: (2014-09-25 16:16)
 */
define(function(require, exports, module) {
    var Base=require("base");
    //侧栏下拉菜单
    var Sidebar=Base.Base.extend({
        /**
         * 初始化函数
         * @method initialize
         * */
        initialize:function(){
            var mainnav = $('#main-nav'),
                _this=this,
                openActive = mainnav.is ('.open-active'),
                navActive = mainnav.find ('> .active');

            mainnav.find ('> .dropdown > a').bind ('click', function(e){
                e.preventDefault ();
                var li = $(this).parents ('li');

                if (li.is ('.opened')) {
                    _this.closeAll();
                } else {
                    _this.closeAll();
                    li.addClass ('opened').find ('.sub-nav').slideDown ();
                }
            });

            if (openActive && navActive.is ('.dropdown')) {
                navActive.addClass ('opened').find ('.sub-nav').show ();
            }
        },
        /**
         * 关闭所有
         */
        closeAll:function(){
            $('.sub-nav').slideUp ().parents ('li').removeClass ('opened');
        }
    })

    //返回顶部
    function initBackToTop () {
        var backToTop = $('<a>', { id: 'back-to-top', href: '#top' });
        var icon = $('<i>', { class: 'fa fa-chevron-up' });

        backToTop.appendTo ('body');
        icon.appendTo (backToTop);

        backToTop.hide();

        $(window).scroll(function () {
            if ($(this).scrollTop() > 150) {
                backToTop.fadeIn ();
            } else {
                backToTop.fadeOut ();
            }
        });

        backToTop.click (function (e) {
            e.preventDefault ();

            $('body, html').animate({
                scrollTop: 0
            }, 600);
        });
    }


    //执行
    new Sidebar();
    initBackToTop();

})