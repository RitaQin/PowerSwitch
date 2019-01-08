package com.ncr.powerswitch.exception;

public class PowerswitchException extends Exception {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**系统错误码*/
    private String code;
    /**错误描述*/
    private String msg;
    
    public PowerswitchException() {
        super();
    }
    
    public PowerswitchException(Throwable t) {
        super(t);
    }
    
    public PowerswitchException(String code, String msg) {
        super(code + '|' + msg);
        this.code = code;
        this.msg = msg;
    }
    
    public String getMessage(){
    	return this.msg;
    }    
    
    
    /**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

}
