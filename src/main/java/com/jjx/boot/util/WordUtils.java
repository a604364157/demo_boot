package com.jjx.boot.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.NumbericRenderData;
import com.deepoove.poi.data.PictureRenderData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本工具是在poi-tl上的扩展
 * 在此感谢deepoove开源贡献
 * <p>
 * 本工具支持5种默认模板语法
 * <p>
 * 文本模板{{var}}
 * 图片模板{{/@var}}（注意这里的语法不带/）
 * 表格模板{{#var}}
 * 列表模板{{*var}}
 * 文档模板{{+var}}
 * <p>
 * 模板语法原则上支持自定义替换，但不建议
 * 因为目前提供的扩展语法内置了${}语法替换
 * <p>
 * EachTableRenderPolicy插件为扩展语法{{&var}}
 * {{&var}}的作用是循环table语法
 * 使用方法为在目标table内的第一个单元格内标注{{&var}}，var是自定义变量名称
 * 循环table一般存在两种情况
 * 备注：以下为循环体，所以变量对应的数据为单个表格对应的逻辑数据
 * 原数据应当再外包一个List，对应循环生成的table数量
 * 一：table不带循环行（及单个table内不会再循环生成行）
 * 这种情况，自定义变量对应数据需要为一个map<String, Object>类型
 * 替换模板标签为${var}
 * 二：table带循环行
 * 这种情况，自定义变量对应数据需要为一个List<map<String, Object>>类型
 * 替换模板标签为${var}
 * 本插件循环填充行采用字段匹配，所以需要模板定义出第一循环行的模板标签
 * 本插件以${start}标签作为循环起始标志，所以循环行第一个标签应为${start}${var},${start}仅做起始符
 * 循环起始行和集合数据索引应当对应，如：从第3行开始循环，集合数据从第3条开始对应，
 * 如果前两行没有替换数据，也应当填充空的map用于占位
 * 本插件以${all}作为合计行，在合计行第一个单元格打上${all}
 * 当存在合计行，程序将自动将集合最后一条数据作为合计数据
 *
 * @author jiangjx
 */
public class WordUtils {

    /**
     * 按word模板生成word文件
     *
     * @param is   模板文件流（仅支持docx）
     * @param os   生成的文件流
     * @param data 数据
     * @throws IOException ex
     */
    public static void compile(InputStream is, OutputStream os, Object data) throws IOException {
        Configure.ConfigureBuilder builder = Configure.newBuilder();
        builder.addPlugin('&', new EachTableRenderPolicy());
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(is, builder.build());
        xwpfTemplate.render(data).write(os);
        xwpfTemplate.close();
    }

    public static void main(String[] args) throws IOException {
        Map<String, Object> data = new HashMap<>(16);
        Map<String, Object> subData = new HashMap<>(16);
        Map<String, Object> m0 = new HashMap<>(16);
        m0.put("name", "名称");
        Map<String, Object> m1 = new HashMap<>(16);
        m1.put("a", "1");
        m1.put("b", "2");
        m1.put("c", "3");
        m1.put("d", "4");
        m1.put("e", "5");
        m1.put("f", "6");
        m1.put("g", "7");
        List<Map<String, Object>> list = Arrays.asList(m0, m1, m1, m1, m1);
        subData.put("tableOne", list);
        subData.put("tableTwo", list);
        NumbericRenderData numbericRenderData = NumbericRenderData.build("项目现场情况项目现场11111情况项目现场情况项目现场情况项目现场情况项目\n现场情况项目现场情况项目现场情况项目现场情况",
                "项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况11111项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况项目评分情况",
                "甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情11111况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况甲方访谈情况");
        PictureRenderData pictureRenderData = new PictureRenderData(400, 400, new File("E:\\9.png"));
        List<Object> os = Arrays.asList(numbericRenderData, pictureRenderData);
        subData.put("p", os);
        List<Map<String, Object>> all = Arrays.asList(subData, subData);
        data.put("eachTable", all);
        FileOutputStream fos = new FileOutputStream("E:\\1_out.docx");
        compile(new FileInputStream("E:\\1.docx"), fos, data);
        fos.flush();
        fos.close();
    }

}
