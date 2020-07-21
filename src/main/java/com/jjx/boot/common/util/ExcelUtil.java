package com.jjx.boot.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class ExcelUtil {

    private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 创建EXCEL文件
     *
     * @param list 数据集合
     * @return excel文件实体
     */
    public static XSSFWorkbook createExcel2007(List<Map<String, Object>> list, boolean hasTitle) {
        if (!hasTitle) {
            addTitle(list);
        }
        //生成xlsx文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 生成一个sheet
        XSSFSheet sheet = workBook.createSheet("sheet");
        //插入需导出的数据
        int i = 0;
        for (Map<String, Object> map : list) {
            XSSFRow row = sheet.createRow(i);
            int j = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (i == 0) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellStyle(getStyle(workBook));
                    cell.setCellValue(StringTool.o2s(entry.getKey()));
                } else {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellStyle(getStyle(workBook));
                    cell.setCellValue(StringTool.o2s(entry.getValue()));
                }
                j++;
            }
            sheet.autoSizeColumn(i);
            i++;
        }
        return workBook;
    }

    public static HSSFWorkbook createExcel2003(List<Map<String, Object>> list, boolean hasTitle) {
        if (!hasTitle) {
            addTitle(list);
        }
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("sheet");
        int i = 0;
        for (Map<String, Object> map : list) {
            HSSFRow row = sheet.createRow(i);
            int j = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (i == 0) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(getStyle(workBook));
                    cell.setCellValue(StringTool.o2s(entry.getKey()));
                } else {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellStyle(getStyle(workBook));
                    cell.setCellValue(StringTool.o2s(entry.getValue()));
                }
                j++;
            }
            i++;
        }
        return workBook;
    }

    private static void addTitle(List<Map<String, Object>> list) {
        list.add(0, list.get(0));
    }

    private static CellStyle getStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_NORMAL);
        font.setFontName("仿宋");
        style.setFont(font);
        return style;
    }

    public static File workBook2File(Workbook workbook, String fileName) {
        try (FileOutputStream outStream = new FileOutputStream(fileName)) {
            workbook.write(outStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(fileName);
    }

}
