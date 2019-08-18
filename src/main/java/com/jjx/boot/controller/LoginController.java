package com.jjx.boot.controller;

import com.alibaba.fastjson.JSON;
import com.jjx.boot.dto.login.LoginDTO;
import com.jjx.boot.dto.login.User;
import com.jjx.boot.dto.stander.InDTO;
import com.jjx.boot.dto.stander.OutDTO;
import com.jjx.boot.service.LoginServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author jjx
 */
@CrossOrigin
@RestController
@RequestMapping("login")
public class LoginController {

    @Resource
    private LoginServiceImpl loginService;

    @RequestMapping(value = "sig")
    public OutDTO<Void> login(@RequestBody InDTO<LoginDTO> in, HttpServletRequest request) {
        return new OutDTO<>(loginService.login(in.getBody(), request));
    }

    @RequestMapping("out")
    public OutDTO<Void> logout(HttpServletRequest request) {
        return new OutDTO<>(loginService.logout(request));
    }

    @PostMapping("test")
    public String test(@Valid @RequestBody User user) {
        return JSON.toJSONString(user);
    }

}
