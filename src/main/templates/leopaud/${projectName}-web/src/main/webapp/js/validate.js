/**
 * validate :验证.
 * User: xiejinlong@yy.com
 * Time: (2014-02-28 15:46)
 */
define(function(require, exports, module) {
    var Base=require("base");
    var common=require("common");
    var pageui=require("pageui");

    require("validate");


    var Validate=Base.Base.extend({
        /**
         * 初始化函数
         * @method initialize
         * */

        initialize:function(){
            $("#test").on("click",function(){

                $("#comment_form").validate({
                    rules:{ //定义验证规则
                        name :{
                            required:true,
                            maxlength:16,
                            minlength:3
                        }
                    },
                    messages:{
                         name :{
                             required:"用户名不能为空！"
                         }
                    },
                    debug:true
                })
            })

        }
    })
    module.exports=Validate;
})