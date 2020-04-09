package yong.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class SelectTag extends TagSupport {/**
     * 
     */
    private static final long serialVersionUID = 5271689982498479737L;

    private String test1;
    private String test2;
    private String test3;
    
    
    @Override
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }
    
    @Override
    public int doEndTag() throws JspException {
        
        log.debug(test1);
        log.debug(test2);
        log.debug(test3);
        
        
        return EVAL_PAGE;
    }

    
}
