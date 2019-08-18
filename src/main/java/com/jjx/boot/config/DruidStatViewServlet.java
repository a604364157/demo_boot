package com.jjx.boot.config;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * druid数据源状态监控.
 *
 * @author Administrator
 */

@WebServlet(urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "loginUsername", value = "jjx"),
                @WebInitParam(name = "loginPassword", value = "123456"),
                @WebInitParam(name = "resetEnable", value = "false")
        }
)
public class DruidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = 9087070781894292958L;
}