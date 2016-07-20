/**
 * 
 */
package com.thecdmgroup.driiverseatservice.common;

/**
 * @author vinothn
 *
 */
public class ExceptionInfo {
	
	private int status;
    private String msg;
    private String desc;
    
    public ExceptionInfo(int status, String msg, String desc) {
        this.status=status;
        this.msg=msg;
        this.desc=desc;
    }

    /**
     * @return
     */
    public int getStatus() { return status; }
    
    /**
     * @return
     */
    public String getMessage() { return msg; }
    
    /**
     * @return
     */
    public String getDescription() { return desc; }
}
