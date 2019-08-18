package com.jjx.boot.service.inter;

import com.jjx.boot.dto.stander.OutBody;
import com.jjx.boot.dto.stander.PageBean;
import com.jjx.boot.dto.twocolor.AddBallInDTO;
import com.jjx.boot.dto.twocolor.QryBallInDTO;
import com.jjx.boot.dto.twocolor.UptBallInDTO;
import com.jjx.boot.entity.BigLotto;
import com.jjx.boot.entity.TwoColorBall;

import java.util.List;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public interface IBallService {

    /**
     * 查询数据
     *
     * @param inParam 入参
     * @return 结果
     */
    OutBody<List<TwoColorBall>> qryBall(QryBallInDTO inParam);

    /**
     * 查询数据（分页）
     *
     * @param inParam 入参
     * @return 出参
     */
    OutBody<PageBean<TwoColorBall>> qryBallByPage(QryBallInDTO inParam);

    /**
     * 添加数据
     *
     * @param inParam 入参
     * @return 结果
     */
    OutBody<Void> addBall(AddBallInDTO inParam);

    /**
     * 修改数据
     *
     * @param inParam 入参
     * @return 结果
     */
    OutBody<Void> uptBall(UptBallInDTO inParam);

    /**
     * 删除数据
     *
     * @param inParam 入参
     * @return 结果
     */
    OutBody<Void> delBall(QryBallInDTO inParam);

    /**
     * 随机摇号-双色球
     *
     * @param size 随机条数
     * @return 结果
     */
    OutBody<List<TwoColorBall>> runBall(int size);

    /**
     * 随机摇号-大乐透
     *
     * @param size 随机条数
     * @return 结果
     */
    OutBody<List<BigLotto>> runBall2(int size);

}
