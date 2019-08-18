package com.jjx.boot.service.inter;

import com.jjx.boot.dto.login.LoginDTO;
import com.jjx.boot.dto.stander.OutBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jjx
 */
public interface ILoginService {

    /**
     * 登录接口
     * @param inParam 入参
     * @return 出参
     */
    OutBody<Void> login(LoginDTO inParam, HttpServletRequest request);

    /**
     * 登出接口
     * @param request 入参
     * @return 出参
     */
    OutBody<Void> logout(HttpServletRequest request);

}
