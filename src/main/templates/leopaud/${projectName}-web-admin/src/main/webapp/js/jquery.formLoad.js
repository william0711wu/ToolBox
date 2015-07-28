(function($) {
    /**
     * setValue of the input textarea select fields
     */
    function _load(data,form){
    	
        for(var name in data){
            var val = data[name];
            var rr = _checkField(name, val,form);
            if (!rr.length){
                $('input[name="'+name+'"]', form).val(val).removeClass("isEmpty");
                $('textarea[name="'+name+'"]', form).val(val).removeClass("isEmpty");
                $('select[name="'+name+'"]', form).val(val).change();
                
                
            }
        }
    }
    /**
     * check the checkbox and radio fields
     */
    function _checkField(name, val,form){
        var rr = $('input[name="'+name+'"][type=radio], input[name="'+name+'"][type=checkbox]', form);
        $.fn.prop ? rr.prop('checked',false) : rr.attr('checked',false);
        
        rr.each(function(){
            var f = $(this);
            
            if(String(val).indexOf(',')==-1){
                if(f.val()==String(val)){
                   // $.fn.prop?f.prop("checked",true):f.attr("checked",true);
                    f.click();
                }
                }else{
                temparr = String(val).split(',');
                if($.inArray(f.val(),temparr) != -1){
                   // $.fn.prop?f.prop("checked",true):f.attr("checked",true);
                    f.click();
                }
            }
        });
        return rr;
    }
    $.fn.loadForm = function(data){
        var form = this;
		_load(data,form);
    }
})(jQuery);

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}