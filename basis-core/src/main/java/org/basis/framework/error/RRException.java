
package org.basis.framework.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义异常
 *
 */
public class RRException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(BaseException.class);

    private String msg;
    private int code = BizCodeEnume.DEFAULT.getCode();
    
    public RRException(String msg) {
		super(msg);
		this.msg = msg;
		log.warn(this.toString());
	}
	
	public RRException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
		log.warn(this.toString());
	}
	
	public RRException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RRException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
