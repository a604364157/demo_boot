package com.jjx.boot.aop;

import com.jjx.boot.constant.Constant;
import com.jjx.boot.dto.stander.OutBody;
import com.jjx.boot.dto.stander.OutDTO;
import com.jjx.boot.util.LoginUtil;
import com.jjx.boot.util.StringTool;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        if ("".equals(url)) {
            return true;
        } else {
            if (checkLogin(request)) {
                return true;
            } else {
                OutDTO<Void> out = new OutDTO<>();
                OutBody<Void> outBody = new OutBody<>();
                outBody.setStatus(Constant.ERROR);
                outBody.setMsg("请重新登录");
                out.setBody(outBody);
                returnJson(response, out.toString());
                return false;
            }
        }
    }

    private boolean checkLogin(HttpServletRequest request) {
        String ip = LoginUtil.getIPAddress(request);
        String sessionId = request.getSession().getId();
        return StringTool.isEqual(LoginUtil.session.get(ip), sessionId);
    }

    private void returnJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
