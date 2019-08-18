package com.jjx.boot.mapper;

import com.jjx.boot.dto.twocolor.QryBallInDTO;
import com.jjx.boot.dto.twocolor.UptBallInDTO;
import com.jjx.boot.util.DateConst;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.sql.Date;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public class TwoColorMapper {

    private static final String TWO_COLOR = "id_no as idNo, red1, red2, red3, red4, red5, red6, blue, run_date as runDate, op_time as opTime";

    private static final String TWO_COLOR_TABLE = "two_color";

    public String uptTwoColor(UptBallInDTO t) {
        SQL sql = new SQL();
        sql.UPDATE(TWO_COLOR_TABLE);
        if (!StringUtils.isEmpty(t.getRed1())) {
            sql.SET("red1 = #{red1}");
        }
        if (!StringUtils.isEmpty(t.getRed2())) {
            sql.SET("red2 = #{red2}");
        }
        if (!StringUtils.isEmpty(t.getRed3())) {
            sql.SET("red3 = #{red3}");
        }
        if (!StringUtils.isEmpty(t.getRed4())) {
            sql.SET("red4 = #{red4}");
        }
        if (!StringUtils.isEmpty(t.getRed5())) {
            sql.SET("red5 = #{red5}");
        }
        if (!StringUtils.isEmpty(t.getRed6())) {
            sql.SET("red6 = #{red6}");
        }
        if (!StringUtils.isEmpty(t.getBlue())) {
            sql.SET("blue = #{blue}");
        }
        if (t.getRunDate() != null) {
            t.setRunDateP(new Date(DateConst.getDate(t.getRunDate()).getTime()));
            sql.SET("run_date = #{runDateP}");
        }
        sql.SET("op_time = #{opTimeP}");
        sql.WHERE("id_no = #{idNo}");
        return sql.toString();
    }

    public String qryTwoColor(QryBallInDTO t) {
        SQL sql = new SQL();
        sql.SELECT(TWO_COLOR);
        sql.FROM(TWO_COLOR_TABLE);
        paramIdTime(sql, t);
        return sql.toString();
    }

    public String countTwoColor(QryBallInDTO t) {
        SQL sql = new SQL();
        sql.SELECT("count(1)");
        sql.FROM(TWO_COLOR_TABLE);
        paramIdTime(sql, t);
        return sql.toString();
    }

    private void paramIdTime(SQL sql, QryBallInDTO t) {
        if (!StringUtils.isEmpty(t.getIdNo())) {
            sql.WHERE("id_no = #{idNo}");
        }
        if (t.getRunDate() != null) {
            t.setTime(new Date(DateConst.getDate(t.getRunDate()).getTime()));
            sql.WHERE("run_date = #{time}");
        }
    }
}
