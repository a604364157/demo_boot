package com.jjx.boot.controller;

import com.jjx.boot.common.config.BallException;
import com.jjx.boot.common.constant.Constant;
import com.jjx.boot.dto.stander.InDTO;
import com.jjx.boot.dto.stander.OutBody;
import com.jjx.boot.dto.stander.OutDTO;
import com.jjx.boot.dto.stander.PageBean;
import com.jjx.boot.dto.twocolor.AddBallInDTO;
import com.jjx.boot.dto.twocolor.QryBallInDTO;
import com.jjx.boot.dto.twocolor.RandDTO;
import com.jjx.boot.dto.twocolor.UptBallInDTO;
import com.jjx.boot.entity.BigLotto;
import com.jjx.boot.entity.TwoColorBall;
import com.jjx.boot.service.inter.IBallService;
import com.jjx.boot.common.util.FileUtils;
import com.jjx.boot.common.util.MapBean;
import com.jjx.boot.common.util.RedisUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * CrossOrigin注解是允许前台跨域请求
 *
 * @author Administrator
 */
@CrossOrigin
@RestController
@RequestMapping("twoColor")
public class BallController {

    @Resource
    private IBallService ballService;

    @GetMapping("rand_tc")
    public OutDTO<List<TwoColorBall>> randTwoColor(@RequestBody InDTO<RandDTO> in) {
        return new OutDTO<>(ballService.runBall(Integer.valueOf(in.getBody().getTimes())));
    }

    @GetMapping("rand_bt")
    public OutDTO<List<BigLotto>> randBigLotto(@RequestBody InDTO<RandDTO> in) {
        return new OutDTO<>(ballService.runBall2(Integer.valueOf(in.getBody().getTimes())));
    }

    @RequestMapping("qry")
    public OutDTO<List<TwoColorBall>> qryBall(@RequestBody InDTO<QryBallInDTO> in) {
        return new OutDTO<>(ballService.qryBall(in.getBody()));
    }

    @RequestMapping("qryPage")
    public OutDTO<PageBean<TwoColorBall>> qryBallByPage(@RequestBody InDTO<QryBallInDTO> in) {
        return new OutDTO<>(ballService.qryBallByPage(in.getBody()));
    }

    @RequestMapping("add")
    public OutDTO<Void> addBall(@RequestBody InDTO<AddBallInDTO> in) {
        return new OutDTO<>(ballService.addBall(in.getBody()));
    }

    @RequestMapping("upt")
    public OutDTO<Void> uptBall(@RequestBody InDTO<UptBallInDTO> in) {
        return new OutDTO<>(ballService.uptBall(in.getBody()));
    }

    @RequestMapping("del")
    public OutDTO<Void> delBall(@RequestBody InDTO<QryBallInDTO> in) {
        return new OutDTO<>(ballService.delBall(in.getBody()));
    }

    @RequestMapping("upload")
    public OutDTO<Void> upload(@RequestParam MultipartFile file) {
        OutBody<Void> outBody = new OutBody<>();
        if (file != null) {
            String fileName = file.getOriginalFilename();
            InputStream fileData;
            try {
                fileData = file.getInputStream();
            } catch (IOException e) {
                throw new BallException("0001", "文件流异常");
            }
            FileUtils.uploadFile(fileData, Constant.FILE_PATH, UUID.randomUUID() + "_" + fileName);
        }
        outBody.setStatus("0");
        outBody.setMsg("文件上传成功");
        return new OutDTO<>(outBody);
    }

    @RequestMapping("ex")
    public OutDTO<Void> ex(@RequestBody InDTO<QryBallInDTO> in) {
        String idNo = in.getBody().getIdNo();
        if ("1".equals(idNo)) {
            String[] a = {"1", "2"};
            System.out.println(a[2]);
        } else if ("2".equals(idNo)) {
            throw new RuntimeException("程序异常");
        } else if ("3".equals(idNo)) {
            throw new BallException("2", "程序异常");
        }
        return new OutDTO<>();
    }

    @Resource
    private RedisUtil redisUtil;

    @RequestMapping("ca")
    public OutDTO<Void> cache(@RequestBody InDTO<QryBallInDTO> in) {
        QryBallInDTO dto = in.getBody();
        Map<String, Object> map = MapBean.objectToMap(dto, true);
        redisUtil.hmset("map", map, 0);
        System.out.println(redisUtil.hmget("map").toString());
        return new OutDTO<>();
    }

}
