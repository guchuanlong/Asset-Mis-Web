package com.myunihome.myxapp.web.system.exception;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * 异常封装
 * @author kanghc
 *
 */
public class BusiException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 3787730660315875183L;
	private static final Logger log = Logger.getLogger(BusiException.class);
	private String message;
	private String code;
	private String detail;

	public BusiException(String message) {
		super(message);
		this.message = message;
		log.error("message:="+message);
	}

	public BusiException(String code, String message) {
		super(message);
		this.message = message;
		this.code = code;
		log.error("code:="+code+"message:="+message);
	}

	public BusiException(String code, String message, String detail) {
		super(message);
		this.message = message;
		this.code = code;
		this.detail = detail;
		log.error("code:="+code+"message:="+message+"detail:="+detail);
	}
	
	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
