package com.example.demo._core.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo._core.utils.Resp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ValidAdvice {

    @Around("execution(* com.example.demo..*Controller.*(..))")
    public Object validationBind(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();

        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult br = (BindingResult) arg;

                if (br.hasErrors()) {
                    String errMsg = br.getFieldErrors().get(0).getDefaultMessage();
                    
                    // 해당 컨트롤러가 @RestController인지 확인
                    boolean isRestController = jp.getTarget().getClass().isAnnotationPresent(RestController.class);

                    if (isRestController) {
                        return Resp.fail(org.springframework.http.HttpStatus.BAD_REQUEST, errMsg);
                    } else {
                        // SSR용 자바스크립트 응답 (alert + history.back)
                        return script(errMsg);
                    }
                }
            }
        }

        return jp.proceed();
    }

    private String script(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }
}
