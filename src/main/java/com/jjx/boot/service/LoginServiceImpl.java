package com.jjx.boot.service;

import com.jjx.boot.common.config.BallException;
import com.jjx.boot.common.constant.Constant;
import com.jjx.boot.dto.login.LoginDTO;
import com.jjx.boot.dto.stander.OutBody;
import com.jjx.boot.mapper.ILoginMapper;
import com.jjx.boot.service.inter.ILoginService;
import com.jjx.boot.common.util.LoginUtil;
import com.jjx.boot.common.util.StringTool;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jjx
 */
@Service("loginService")
public class LoginServiceImpl implements ILoginService {

    private static final String ADMIN_NO = "jjx";
    private static final String ADMIN_PWD = "123456";

    @Resource
    private ILoginMapper loginMapper;

    @Override
    public OutBody<Void> login(LoginDTO inParam, HttpServletRequest request) {
        OutBody<Void> out = new OutBody<>();
        String loginNo = inParam.getLoginNo();
        String pwd = inParam.getPassword();
        String authCode = inParam.getAuthCode();
        if (!StringUtils.isEmpty(authCode)) {
            throw new BallException("0001", "验证码不正确");
        }
        String loginPwd = ADMIN_PWD;
        if (!ADMIN_NO.equals(loginNo)) {
            loginPwd = loginMapper.qryPasswordByLoginNo(loginNo);
        }
        if (StringTool.isEqual(loginPwd, pwd)) {
            String ip = LoginUtil.getIPAddress(request);
            HttpSession session = request.getSession();
            LoginUtil.session.put(ip, session.getId());
        }
        out.setStatus(Constant.SUCCESS);
        out.setMsg("登录成功");
        return out;
    }

    @Override
    public OutBody<Void> logout(HttpServletRequest request) {
        String ip = LoginUtil.getIPAddress(request);
        LoginUtil.session.remove(ip);
        return new OutBody<>();
    }
}
