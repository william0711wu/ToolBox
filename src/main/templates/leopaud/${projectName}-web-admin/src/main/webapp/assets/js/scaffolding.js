(function ($) {

    $.fn.scaffolding = function (options) {
        var defaults = {
            'queryUrl': '',
            'newUrl': '',
            'editUrl': '',
            'delUrl': '',
            'showUrl': '',
        };
        var settings = $.extend(defaults, options);
        //你自己的插件代码
        var scaffolding = {
            settings:settings,
            tableResults:$("#tableResults"),//表格区
            modal:$('#ajax-modal'),//弹出modal
            pageNo:1,
            pageSize:10,
            searchData:'',
            validator:null,
            search: function(){
                //搜索条件
                this.searchData = $("#searchForm").serialize()
                this.query(1)

            },
            query : function(pageNo,pageSize){
                //执行查询，并展现数据
                Metronic.blockUI({
                    boxed: true
                });
                this.pageNo =pageNo?pageNo:1
                this.pageSize  =pageSize?pageSize:10
                this.tableResults.load(this.settings.queryUrl+"?pageNo="+this.pageNo+"&pageSize="+this.pageSize,this.searchData,function(response, status, xhr ){
                    Metronic.unblockUI();
                    if(status=="success") {
                        $('[data-toggle="confirmation"]').confirmation();
                    }else{
                        showWarningGrowl("加载数据失败")
                    }

                });
            },
            pagenation:function(option){ //ajax 分页设置
                if(option.totalPages > 0 && option.startPage > option.totalPages){//如果查询页没有数据
                    scaffolding.query(option.totalPages);
                    return
                }
                $('#pagination').twbsPagination({
                    totalPages: option.totalPages,
                    startPage: option.startPage,
                    visiblePages: 10,
                    first:"最前",
                    prev:"上一页",
                    next:"下一页",
                    last:"最后",
                    onPageClick: function (event, crupageNo) {
                        scaffolding.query(crupageNo);//点击分页时
                    }
                });
            },
            reload : function(){
                //重新加载当前页
                this.query(this.pageNo,this.pageSize)
            },
            add:function(obj){
                //新增页面
                // create the backdrop and wait for next modal to be triggered
                $('body').modalmanager('loading');
                var $modal = this.modal;
                setTimeout(function () {
                    $modal.load(settings.newUrl, '', function () {
                        $modal.modal();
                    });
                }, 100);
            },
            edit:function(id){
                //新增页面
                // create the backdrop and wait for next modal to be triggered
                $('body').modalmanager('loading');
                var $modal = this.modal;
                setTimeout(function () {
                    $modal.load(settings.editUrl, 'id='+id, function () {
                        $modal.modal();
                    });
                }, 100);
            },
            show:function(id){
                //查看页面
                // create the backdrop and wait for next modal to be triggered
                $('body').modalmanager('loading');
                var $modal = this.modal;
                setTimeout(function () {
                    $modal.load(settings.showUrl, 'id='+id, function () {
                        $modal.modal();
                    });
                }, 100);
            },
            del:function(id){
                Metronic.blockUI({
                    boxed: true,
                    message:"处理中..."
                });
                //删除记录
                $.ajax({
                    url: settings.delUrl,
                    data: "id="+id,
                    dataType : 'json'
                }).done(function (response) {
                    Metronic.unblockUI();
                    if (response.status == '200') {
                        scaffolding.reload()
                        showSuccessGrowl('数据已删除');
                    } else {
                        showWarningGrowl("数据删除失败")
                    }
                }).fail(function (jqXHR, textStatus, errorThrown){
                    Metronic.unblockUI();
                    showDangerGrowl("接口异常，请与管理员联系")
                    // log the error to the console
                    console.error(
                        "The following error occurred: " + textStatus, errorThrown
                    );
                });//ajax 提交数据
            },
            validate:function(ruleOpt){
                //校验表单设置
                var form2 = $('#data_form');
                this.validator = form2.validate({
                    errorElement: 'span', //default input error message container
                    errorClass: 'help-block help-block-error', // default input error message class
                    focusInvalid: false, // do not focus the last invalid input
                    ignore: "upfile",  // validate all fields including form hidden input
                    rules: ruleOpt,
                    invalidHandler: function (event, validator) { //display error alert on form submit
                        showWarningGrowl("数据校验有误，请检查")
                        //success2.hide();
                       // error2.show();
                       // Metronic.scrollTo(error2, -200);//滚动到提示位置
                    },

                    errorPlacement: function (error, element) { // render error placement for each input type
                        var icon = $(element).parent('.input-icon').children('i');
                        icon.removeClass('fa-check').addClass("fa-warning");
                        icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
                    },

                    highlight: function (element) { // hightlight error inputs
                        $(element)
                            .closest('.form-group').removeClass("has-success").addClass('has-error'); // set error class to the control group
                    },
                    unhighlight: function (element) { // revert the change done by hightlight
                    },
                    success: function (label, element) {
                        var icon = $(element).parent('.input-icon').children('i');
                        $(element).closest('.form-group').removeClass('has-error').addClass('has-success'); // set success class to the control group
                        icon.removeClass("fa-warning").addClass("fa-check");
                    },
                    submitHandler: function (form) {
                        Metronic.blockUI({
                            boxed: true,
                            zIndex:99999,
                            message:"处理中..."
                        });
                      //  error2.hide();
                        $.ajax({
                            type: $(form).attr('method'),
                            url: $(form).attr('action'),
                            data: $(form).serialize(),
                            dataType : 'json'
                        }).done(function (response) {
                            Metronic.unblockUI();
                            if (response.status == '200') {
                                scaffolding.reload()
                                showSuccessGrowl('数据已保存');
                                var $modal = $('#ajax-modal');
                                $modal.modal('hide');
                            }else if(response.status =='-417'){
                                //数据校验不通过,高亮相应字段或提示用户
                                showWarningGrowl("数据校验失败!请检查！")
                                $.each(response.data,function(){
                                    var element = scaffolding.validator.findByName(this.field)
                                    if(!element){
                                        showWarningGrowl(this.defaultMessage)
                                        return
                                    }
                                    $(element)
                                        .closest('.form-group').removeClass("has-success").addClass('has-error');
                                    var icon = $(element).parent('.input-icon').children('i');
                                    icon.removeClass('fa-check').addClass("fa-warning");
                                    icon.attr("data-original-title", this.defaultMessage).tooltip({'container': 'body'});
                                });
                            } else if(response.status =='-450'){//业务校验失败
                                showWarningGrowl(response.message)
                            } else {
                                showWarningGrowl("数据保存失败!"+response.message)
                            }
                        }).fail(function (jqXHR, textStatus, errorThrown){
                            Metronic.unblockUI();
                            showDangerGrowl("接口异常，请与管理员联系")
                            // log the error to the console
                            console.error(
                                "The following error occurred: " + textStatus, errorThrown
                            );
                        });//ajax 提交数据

                        return false;
                    }
                });//end form2.validate
            }//end validate


        }

        return scaffolding
    };
})(jQuery);

var scaffolding;//通用curd相关实现
$(document).ready(function () {
    // 设置modal加载条与其它通用设置
    $.fn.modal.defaults.spinner = $.fn.modalmanager.defaults.spinner =
        '<div class="loading-spinner" style="width: 200px; margin-left: -100px;">' +
        '<div class="progress progress-striped active">' +
        '<div class="progress-bar" style="width: 100%;"></div>' +
        '</div>' +
        '</div>';

    $.fn.modalmanager.defaults.resize = true;

});