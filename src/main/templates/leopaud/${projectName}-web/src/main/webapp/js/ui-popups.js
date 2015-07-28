/**
 * ui-popups :弹出层.
 * User: xiejinlong@yy.com
 * Time: (2014-02-28 15:46)
 */
define(function(require, exports, module) {
    var Base=require("base");
    var common=require("common");
    var bootbox = require("bootbox");
    var pageui=require("pageui");

    require('chosenCss');
    require('chosenJS');

    require('icheckCss');
    require('icheckJs');

    require('fileuploadCss');
    require('fileuploadJs');

    require('textarea-counter');

    require("howl");


    var index=Base.Base.extend({
        /**
         * 初始化函数
         * @method initialize
         * */

        initialize:function(){
            this.initTooltips();
            this.bindEvent();
        },
        initTooltips:function(){
            if ($.fn.popover) { $('.ui-popover').popover ({ container: 'body',html:true }); }
        },
        bindEvent:function(){
            //提示层
            $('.howler').click (function (e) {
                $.howl ({
                    type: $(this).data ('type'),
                    title: 'Howl Message',
                    content: 'Lorem ipsum dolor sit amet, consect adipisicing elit.',
                    lifetime: 7500,
                    iconCls: $(this).data ('icon')
                });
            });

            //显示弹出层
            $("#showBox").click(function(){
                bootbox.dialog({
                    message: "<p>asdasd</p><p>asdasd</p>",
                    title: "Custom title",
                    buttons: {
                        success: {
                            label: "Success!",
                            className: "btn-success",
                            callback: function() {
                                alert("great success");
                            }
                        },
                        danger: {
                            label: "Danger!",
                            className: "btn-danger",
                            callback: function() {
                                alert("uh oh, look out!");
                            }
                        },
                        main: {
                            label: "Click ME!",
                            className: "btn-primary",
                            callback: function() {
                                alert("Primary button");
                            }
                        }
                    }
                });
            })

            //chosen下拉选择
            $(".chosen-select").chosen();
            $(".chosen-select2").chosen({
                allow_single_deselect:true
            })
            $(".chosen-select3").chosen({
                disable_search_threshold: 10
            })


            $(".chosen-select4").chosen({
                max_selected_options: 2
            })

            $(".chosen-select4").bind("chosen:maxselected", function () {
                console.log("ddd");
            });

            //复选框，单选框样式
            $('input:checkbox, input:radio').iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            })

            $('#count-textarea2').textareaCount({
                maxCharacterSize: 200,
                displayFormat: '#input characters'
            })
        }
    })
    module.exports=index;
})