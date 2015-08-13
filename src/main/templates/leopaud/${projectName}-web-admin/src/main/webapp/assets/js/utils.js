/**
 * 工具类相关js
 */

/**
 * 创建浮动提示
 * @param msg
 */
function showInfoGrowl(msg){
    showGrowl(msg,'info')
}
/**
 * 创建危险浮动提示
 * @param msg
 */
function showDangerGrowl(msg){
    showGrowl(msg,'danger')
}
/**
 * 创建成功浮动提示
 * @param msg
 */
function showSuccessGrowl(msg){
    showGrowl(msg,'success')
}
/**
 * 创建警告浮动提示
 * @param msg
 */
function showWarningGrowl(msg){
    showGrowl(msg,'warning')
}
/**
 * 创建浮动提示
 * @param msg 提示信息
 * @param type 信息类型
 */
function showGrowl(msg,type){
    $.bootstrapGrowl(msg, {
        ele: 'body', // which element to append to
        type: type, // (null, 'info', 'danger', 'success', 'warning')
        offset: {
            from: 'top',
            amount: 50
        }, // 'top', or 'bottom'
        align: 'right', // ('left', 'right', or 'center')
        width: 'auto', // (integer, or 'auto')
        delay: 3000, // Time while the message will be displayed. It's not equivalent to the *demo* timeOut!
        allow_dismiss: true, // If true then will display a cross to close the popup.
        stackup_spacing: 10 // spacing between consecutively stacked growls.
    });
}