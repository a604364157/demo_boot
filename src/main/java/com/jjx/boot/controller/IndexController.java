package com.jjx.boot.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 */
@CrossOrigin
@RestController
@RequestMapping("api")
public class IndexController {

    @RequestMapping(value = "index/{param}", method = RequestMethod.POST)
    public String index(@PathVariable String param) {
        return "测试服务index1"+param;
    }

    @RequestMapping(value = "index/{param}", method = RequestMethod.GET)
    public String index2(@PathVariable String param) {
        return "测试服务index2"+param;
    }

    @GetMapping("index3")
    public String index3(@RequestParam int param) {
        return param+"";
    }

    @GetMapping("index4")
    public String index4() {
        RestTemplate rest = new RestTemplate();
        HttpEntity<Object> requestEntity = new HttpEntity<>(new JSONObject(), new HttpHeaders());
        ResponseEntity<String> response = rest.exchange("http://127.0.0.1:8080/api/index3?param=1", HttpMethod.GET, requestEntity, String.class);
        HttpHeaders headers = response.getHeaders();
        return headers.get("Content-Type").get(0);
    }
}
