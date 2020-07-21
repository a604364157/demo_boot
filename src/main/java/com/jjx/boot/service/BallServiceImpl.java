package com.jjx.boot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jjx.boot.common.config.BallException;
import com.jjx.boot.common.constant.Constant;
import com.jjx.boot.dto.stander.OutBody;
import com.jjx.boot.dto.stander.PageBean;
import com.jjx.boot.dto.twocolor.AddBallInDTO;
import com.jjx.boot.dto.twocolor.QryBallInDTO;
import com.jjx.boot.dto.twocolor.UptBallInDTO;
import com.jjx.boot.entity.BigLotto;
import com.jjx.boot.entity.TwoColorBall;
import com.jjx.boot.mapper.ITwoColorMapper;
import com.jjx.boot.service.inter.IBallService;
import com.jjx.boot.common.util.DateConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 */
@Service("ballService")
public class BallServiceImpl implements IBallService {

    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(BallServiceImpl.class);
    @Resource
    private ITwoColorMapper twoColorMapper;

    @Override
    public OutBody<List<TwoColorBall>> qryBall(QryBallInDTO inParam) {
        OutBody<List<TwoColorBall>> outBody = new OutBody<>();
        List<TwoColorBall> t = twoColorMapper.qryTwoColor(inParam);
        if (t == null || t.isEmpty()) {
            outBody.setStatus(Constant.ERROR);
            outBody.setMsg("查询数据为空");
        } else {
            outBody.setStatus(Constant.SUCCESS);
            outBody.setOutData(t);
        }
        return outBody;
    }

    @Override
    public OutBody<PageBean<TwoColorBall>> qryBallByPage(QryBallInDTO inParam) {
        OutBody<PageBean<TwoColorBall>> out = new OutBody<>();
        PageHelper.startPage(inParam.getPageNum(), inParam.getPageSize(), "id_no");
        Page<TwoColorBall> page = (Page<TwoColorBall>) twoColorMapper.qryTwoColor(inParam);
//        Page<TwoColorBall> page2 = (Page<TwoColorBall>) twoColorMapper.qryTwoColor(new RowBounds(inParam.getPageNum(), inParam.getPageSize()), inParam)
        List<TwoColorBall> list = page.getResult();
        if (list == null || list.isEmpty()) {
            out.setStatus(Constant.ERROR);
            out.setMsg("查询数据为空");
        } else {
            out.setStatus(Constant.SUCCESS);
            out.setOutData(new PageBean<>(page.getTotal(), list));
        }
        return out;
    }

    @Override
    public OutBody<List<TwoColorBall>> runBall(int size) {
        List<TwoColorBall> balls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            balls.add(runBall());
        }
        return returnCheck(balls, size);
    }

    private TwoColorBall runBall() {
        List<String> reds = initBall(Constant.TWO_RED_SIZE);
        List<String> blues = initBall(Constant.TWO_BLUE_SIEZ);
        Random r = new Random();
        List<String> ball = new ArrayList<>();
        while (ball.size() != Constant.TWO_RED_LENGTH) {
            if (ball.size() > Constant.TWO_RED_LENGTH) {
                ball = new ArrayList<>();
                reds = initBall(Constant.TWO_RED_SIZE);
            }
            int index = r.nextInt(reds.size());
            ball.add(reds.get(index));
            reds.remove(index);
        }
        ball.sort(Comparator.comparingInt(Integer::valueOf));
        return new TwoColorBall(ball.get(0), ball.get(1), ball.get(2), ball.get(3), ball.get(4), ball.get(5), blues.get(r.nextInt(blues.size())));
    }

    @Override
    public OutBody<List<BigLotto>> runBall2(int size) {
        List<BigLotto> balls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            balls.add(runBall2());
        }
        return returnCheck(balls, size);
    }

    private BigLotto runBall2() {
        List<String> reds = initBall(Constant.BIG_RED_SIZE);
        List<String> blues = initBall(Constant.BIG_BLUE_SIZE);
        Random r = new Random();
        List<String> ball = new ArrayList<>();
        while (ball.size() != Constant.BIG_RED_LENGTH) {
            if (ball.size() > Constant.BIG_RED_LENGTH) {
                ball = new ArrayList<>();
                reds = initBall(Constant.BIG_RED_SIZE);
            }
            int index = r.nextInt(reds.size());
            ball.add(reds.get(index));
            reds.remove(index);
        }
        ball.sort(Comparator.comparingInt(Integer::valueOf));
        List<String> tmp = new ArrayList<>();
        while (tmp.size() != Constant.BIG_BLUE_LENGTH) {
            if (tmp.size() > Constant.BIG_BLUE_LENGTH) {
                tmp = new ArrayList<>();
                blues = initBall(Constant.BIG_BLUE_SIZE);
            }
            int index = r.nextInt(blues.size());
            tmp.add(blues.get(index));
            blues.remove(index);
        }
        tmp.sort(Comparator.comparingInt(Integer::valueOf));
        return new BigLotto(ball.get(0), ball.get(1), ball.get(2), ball.get(3), ball.get(4), tmp.get(0), tmp.get(1));
    }

    private <T> OutBody<List<T>> returnCheck(List<T> list, int size) {
        OutBody<List<T>> out = new OutBody<>();
        boolean b = list == null || list.size() != size;
        if (b) {
            out.setStatus(Constant.ERROR);
            out.setMsg("异常，请重试");
        } else {
            out.setStatus(Constant.SUCCESS);
            out.setOutData(list);
            out.setMsg("成功");
        }
        return out;
    }

    private List<String> initBall(int max) {
        if (max < Constant.TWO_BLUE_LENGTH || max > Constant.BIG_RED_SIZE) {
            throw new BallException("0001", "参数值溢出");
        }
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            String val = String.valueOf(i);
            list.add(val);
        }
        if (list.size() != max) {
            throw new BallException("0001", "未知异常");
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OutBody<Void> addBall(AddBallInDTO inParam) {
        OutBody<Void> outBody = new OutBody<>();
        try {
            inParam.setRunDateP(new Date(DateConst.getDate(inParam.getRunDate()).getTime()));
            inParam.setOpTimeP(new Timestamp(System.currentTimeMillis()));
            inParam.setIdNo(null);
            twoColorMapper.addTwoColor(inParam);
            outBody.setStatus(Constant.SUCCESS);
            outBody.setMsg("写入数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            outBody.setStatus(Constant.ERROR);
            outBody.setMsg("写入数据失败");
        }
        return outBody;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OutBody<Void> uptBall(UptBallInDTO inParam) {
        OutBody<Void> outBody = new OutBody<>();
        String idNo = inParam.getIdNo();
        TwoColorBall tmp = twoColorMapper.qryTwoColorByIdNo(idNo);
        if (tmp == null) {
            throw new BallException("0001", "待修改的数据并不存在,无法修改");
        }
        try {
            inParam.setOpTimeP(new Timestamp(System.currentTimeMillis()));
            twoColorMapper.uptTwoColor(inParam);
            outBody.setStatus(Constant.SUCCESS);
            outBody.setMsg("修改数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            outBody.setStatus(Constant.ERROR);
            outBody.setMsg("修改数据失败");
        }
        return outBody;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OutBody<Void> delBall(QryBallInDTO inParam) {
        OutBody<Void> outBody = new OutBody<>();
        outBody.setStatus(Constant.ERROR);
        outBody.setMsg("删除数据失败");
        try {
            twoColorMapper.delTwoColor(inParam.getIdNo(), new Date(DateConst.getDate(inParam.getRunDate()).getTime()));
            outBody.setStatus(Constant.SUCCESS);
            outBody.setMsg("删除数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outBody;
    }
}
