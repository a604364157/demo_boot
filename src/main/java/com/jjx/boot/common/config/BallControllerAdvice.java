package com.jjx.boot.common.config;

import com.jjx.boot.common.constant.Constant;
import com.jjx.boot.dto.stander.OutBody;
import com.jjx.boot.dto.stander.OutDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Administrator
 */
@ControllerAdvice
public class BallControllerAdvice {

    /**
     * 全局异常捕捉处理
     * @param ex ex
     * @return ret
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public OutDTO<Void> errorHandler(Exception ex) {
        ex.printStackTrace();
        OutBody<Void> body = new OutBody<>();
        body.setStatus(Constant.ERROR);
        body.setMsg(ex.getClass()+":"+ex.getMessage());
        return new OutDTO<>(body);
    }

    /**
     * 拦截捕捉自定义异常 BallException
     * @param ex ex
     * @return ret
     */
    @ResponseBody
    @ExceptionHandler(value = BallException.class)
    public OutDTO<Void> ballErrorHandler(BallException ex) {
        ex.printStackTrace();
        OutBody<Void> body = new OutBody<>();
        body.setStatus(ex.getCode());
        body.setMsg(ex.getMsg());
        return new OutDTO<>(body);
    }

}
