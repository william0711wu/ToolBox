package com.duowan.handler;

import com.duowan.exception.BizException;
import com.duowan.commonValidationException
ValidationExceptionHandlerResolver.ErrCode;
import com.duowan.leopard.web.mvc.JsonView;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 项目通用校验结果，根据校验结果返回相应提示信息
 */
public class ValidationExceptionHandlerResolver extends AbstractHandlerMethodExceptionResolver {


    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
        //数据校验未通过
        if(ex instanceof ValidationException){
            return new JsonView(ErrCode.PARAM,"",ex.getMessage());
        }else if(ex instanceof BizException){
            return new JsonView(ErrCode.BIZ,"",ex.getMessage());
        }
        return null;
    }
}
