package com.jjx.boot.mapper;

import com.jjx.boot.dto.twocolor.AddBallInDTO;
import com.jjx.boot.dto.twocolor.QryBallInDTO;
import com.jjx.boot.dto.twocolor.UptBallInDTO;
import com.jjx.boot.entity.TwoColorBall;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Administrator
 */
@Mapper
@SuppressWarnings("unused")
public interface ITwoColorMapper {

    /**
     * 添加数据
     *
     * @param t 参数实体
     */
    @Insert("INSERT INTO two_color (id_no, red1, red2, red3, red4, red5, red6, blue, run_date, op_time) VALUES (#{t.idNo}, #{t.red1}, #{t.red2}, #{t.red3}, #{t.red4}, #{t.red5}, #{t.red6}, #{t.blue}, #{t.runDateP}, #{t.opTimeP})")
    void addTwoColor(@Param("t") AddBallInDTO t);

    /**
     * 删除数据
     *
     * @param idNo    ID
     * @param runDate 开奖日期
     */
    void delTwoColor(@Param("idNo") String idNo, @Param("runDate") Date runDate);

    /**
     * 查询数据
     *
     * @param t bean
     * @return 查询到的数据
     */
    @SelectProvider(type = TwoColorMapper.class, method = "qryTwoColor")
    List<TwoColorBall> qryTwoColor(QryBallInDTO t);
//    List<TwoColorBall> qryTwoColor(RowBounds rb, QryBallInDTO t)

    /**
     * 查询总数
     * @param inParam 入参
     * @return 出参
     */
    @SelectProvider(type = TwoColorMapper.class, method = "countTwoColor")
    int countTwoColor(QryBallInDTO inParam);

    /**
     * 根据ID查询数据
     *
     * @param idNo 数据ID
     * @return 单条数据
     */
    @Select("SELECT id_no, red1, red2, red3, red4, red5, red6, blue, run_date, op_time FROM two_color WHERE id_no=#{idNo}")
    TwoColorBall qryTwoColorByIdNo(String idNo);

    /**
     * 根据开奖时间查询数据
     *
     * @param date 时间
     * @return 单条数据
     */
    @Select("SELECT id_no, red1, red2, red3, red4, red5, red6, blue, run_date, op_time FROM two_color WHERE run_date=#{date}")
    TwoColorBall qryTwoColorByDate(Date date);

    /**
     * 查询两个时间之间的数据
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 查询到的数据
     */
    @Select("SELECT id_no, red1, red2, red3, red4, red5, red6, blue, run_date, op_time FROM two_color WHERE  op_time > #{startTime} AND op_time < #{endTime}")
    List qryTwoColorBetweenTime(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);

    /**
     * 修改数据
     *
     * @param t 带修改实体
     */
    @UpdateProvider(type = TwoColorMapper.class, method = "uptTwoColor")
    void uptTwoColor(UptBallInDTO t);

}
