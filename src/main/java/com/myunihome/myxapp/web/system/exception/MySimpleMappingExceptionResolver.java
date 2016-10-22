package com.myunihome.myxapp.web.system.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.alibaba.dubbo.common.json.JSONObject;

/**
 * 异常扩展
 * @author kanghc
 *
 */
public class MySimpleMappingExceptionResolver extends
		SimpleMappingExceptionResolver {
	private static final Logger logger = Logger.getLogger(MySimpleMappingExceptionResolver.class);
	/**
	 * 响应客户端结果 成功、失败、错误
	 */
	private static final String RES_RESULT = "RES_RESULT";
	/**
	 * 响应客户端结果描述
	 */
	private static final String RES_MSG = "RES_MSG";
	/**
	 * 响应客户端数据
	 */
	private static final String RES_DATA = "RES_DATA";
	/**
	 * 响应客户端结果 失败
	 */
	private static final String FAILED = "FAILED";

	private static final String CODE = "CODE";

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String viewName = determineViewName(ex, request);
		String _code = "";
		String _detail = "";
		if (ex instanceof BusiException) {
			_code =((BusiException) ex).getCode();
			_detail =((BusiException) ex).getDetail();
		}
		logger.error("捕捉到的异常信息>>>>>>>>>>>>>>>>>>>>>> message:"+ex.getMessage()+" code:"+_code+" detail:"+_detail);
		// Expose ModelAndView for chosen error view.
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request
					.getHeader("X-Requested-With") != null && request
					.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				// 如果不是异步请求
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				return getModelAndView(viewName, ex, request);
			} else {// JSON格式返回
				try {
					setprintWriter(response, ex.getMessage(), _code, null);
				} catch (Exception e) {
					logger.error("MySimpleMappingExceptionResolver.doResolveException错误>>>>>>>>>>>>>", e);
				}
				return null;

			}
		} else {
			return null;
		}
	}

	private void setprintWriter(HttpServletResponse response, String msg,
			String code, JSONObject data) {
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(RES_RESULT, FAILED);
			jsonObject.put(RES_MSG, msg);
			jsonObject.put(CODE, code);
			jsonObject.put(RES_DATA, data);
			printWriter.write(jsonObject.toString());
			printWriter.flush();

		} catch (Exception e) {
			logger.error("MySimpleMappingExceptionResolver.setprintWriter错误>>>>>>>>>>>>>", e);
			throw new BusiException("100002", e.getMessage());
		} finally {
		    if(printWriter != null){
		        printWriter.close();
		    }
		}
	}
	
	/*private void setprintWriter1(HttpServletResponse response, String msg,
			String code, JSONObject data) {
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			printWriter.write(code+msg);
			printWriter.flush();

		} catch (Exception e) {
			logger.error("MySimpleMappingExceptionResolver.setprintWriter错误>>>>>>>>>>>>>", e);
			throw new BusiException(ConstantsResultCode.ECH._response_m_code, e.getMessage());
		} finally {
			printWriter.close();
		}
	}*/
}
