/**
 * 初始化表单数据
 * zhangbc
 */
(function($) {

	$.AIformInit = function() {
		this.settings = $.extend(true, {}, $.AIformInit.defaults);
	}

	$.extend($.AIformInit, {

		defaults : {

		},
		prototype : {
			init : function(options) {
				init(options);
			}
		}

	});

})(jQuery);

var aiformInit = new $.AIformInit();

var init = function(options) {
	var formName=options.formName;
	values = $("#"+formName).serializeArray(); 
	for(index=0;index<values.length;++index)
    {
		var name=values[index].name;
		$("#"+name).val(options.data[name]);
    }
}