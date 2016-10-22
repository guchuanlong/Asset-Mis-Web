/*
 * Runner-BIS FramePage
 * 负责管理首页框架的页面元素与数据渲染
 * based on jquery,jquery.pageController
 * author:zhangchao
 * */
(function($){
	$.RunnerBisTemplateFramePageManager = function(basePath,options){
		this.options = $.extend(true,options?options:{},$.RunnerBisTemplateFramePageManager.defaults);
		this.basePath = basePath;
	}
	$.extend($.RunnerBisTemplateFramePageManager,{		
		defaults: {
			
		},	
		prototype: {
			
			init: function(){
				this.loadingLoginUser();
				this.loadingMenus();
			},
			
			loadingLoginUser: function(){
				var _this = this;
				ajaxController.ajax({
	        		url: _this.basePath+"/framepage/getCurrentLoginUser",
	 	 			method: "POST",
	 	 			dataType: "JSON",
	 	            showWait: false,
	 	            success: function (result) {
	 	            	var template = $.templates("#_BIS_TEMPLATE_USERINFO_RENDER_");
	 	                var htmlOutput = template.render(result.data);
	 	                $("#_BIS_TEMPLATE_USERINFO_DIV_").html(htmlOutput);
	 	                _this.renderLoginUser();
	 	            }
	        	});
			},
			
			renderLoginUser: function(){
				$(".user1").mouseover(function(){
					$(".user1 ul").show();
					$(".user1").addClass("usera");
				});
				$(".user1").mouseout(function(){
					$(".user1 ul").hide();
					$(".user1").removeClass("usera");
				});
			},
			
			loadingMenus: function(){
				var _this = this;
				ajaxController.ajax({
	        		url: _this.basePath+"/framepage/getMenusByLimited",
	 	 			method: "POST",
	 	 			dataType: "JSON",
	 	            showWait: false,
	 	            /*data: {
	 	            	systemId: _this.options.systemId
	 	            },*/
	 	            success: function (result) {
	 	            	var template = $.templates("#_BIS_TEMPLATE_MENU_RENDER_");
	 	                var htmlOutput = template.render(result.data);
	 	                $("#_BIS_TEMPLATE_MENU_UL_").html(htmlOutput);
	 	                _this.renderMenus();
	 	            }
	        	});
			},
			
			renderMenus: function(){
				$("[name='_BIS_TEMPLATE_MENU_LI_']").each(function(indx,obj){
					$(this).mouseover(function () {
						$(this).find("ul").show(0);
						$(this).find("p a ").removeClass("reorder").addClass("remove");
					});
					$(this).mouseout(function () {
						$(this).find("ul").hide(0);
						$(this).find("p a ").removeClass("remove").addClass("reorder");
					});
				});
				
			}
			
		}
		
	});
	
	
})(jQuery);