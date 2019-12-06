package com.jjx.boot.util;

import club.newepoch.utils.StringUtils;
import com.deepoove.poi.NiceXWPFDocument;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.NumbericRenderData;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.StyleUtils;
import com.deepoove.poi.util.TableTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangjx
 */
@Slf4j
public class EachTableRenderPolicy implements RenderPolicy {

    private static final String COUNT_FLAG = "${all}";

    private static final String START_FLAG = "${start}";

    private String regex = "\\$\\{[^{}]+}";

    private Pattern pattern = Pattern.compile(regex);

    @Override
    public void render(ElementTemplate elementTemplate, Object data, XWPFTemplate xwpfTemplate) {
        log.info("word文件生成，自定义规则执行开始");
        NiceXWPFDocument doc = xwpfTemplate.getXWPFDocument();
        RunTemplate runTemplate = (RunTemplate) elementTemplate;
        XWPFRun run = runTemplate.getRun();
        log.debug("目标标签为{}，对应数据为{}", run.getText(0), data);
        run.setText("", 0);
        try {
            if (!TableTools.isInsideTable(run)) {
                throw new IllegalStateException("The template tag " + run.getLang() + " must be inside a table");
            } else {
                CTP ctp = ((XWPFParagraph) run.getParent()).getCTP();
                XmlCursor newCursor = createCursor(ctp);
                XmlObject object = newCursor.getObject();
                XWPFTable table = doc.getTableByCTTbl((CTTbl) object);
                doRender(run, table, data);
            }
        } catch (Exception var10) {
            throw new RenderException("each table error:" + var10.getMessage(), var10);
        }
        log.info("word文件生成，自定义规则执行结束");
    }

    @SuppressWarnings("unchecked")
    private void doRender(XWPFRun run, XWPFTable table, Object data) throws Exception {
        if (data instanceof List) {
            List<Object> objs = (List<Object>) data;
            log.debug("循环语法对应数据长度为{}，将同步生成循环体的个数为{}", objs.size(), objs.size() - 1);
            if (!objs.isEmpty()) {
                XWPFDocument document = run.getDocument();
                CTP ctp = ((XWPFParagraph) run.getParent()).getCTP();
                int index = document.getTables().indexOf(table);
                int ii = 0;
                for (int i = 0; i < objs.size() - 1; i++) {
                    XmlCursor newCursor = createCursor(ctp);
                    document.insertNewTbl(newCursor);
                    XWPFParagraph paragraph = document.insertNewParagraph(createCursor(ctp));
                    TextRenderPolicy.Helper.renderTextRun(paragraph.createRun(), "\n");
                    XWPFTable newTable = cloneTable(table);
                    replaceData(newTable, objs.get(i));
                    document.setTable(index + ii, newTable);
                    ii++;
                }
                replaceData(table, objs.get(objs.size() - 1));
            }
        } else {
            throw new RenderException("each table data error: data class is not collection!");
        }
    }

    private XmlCursor createCursor(CTP ctp) {
        XmlCursor newCursor = ctp.newCursor();
        newCursor.toParent();
        newCursor.toParent();
        newCursor.toParent();
        return newCursor;
    }

    private XWPFTable cloneTable(XWPFTable table) {
        CTTbl ctTbl = CTTbl.Factory.newInstance();
        ctTbl.set(table.getCTTbl());
        return new XWPFTable(ctTbl, table.getBody());
    }

    private void replaceData(XWPFTable table, Object data) throws Exception {
        //原则上这里只会有一个单元格，所以数据对应为一个单元格
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                log.trace("当前结构体内存在段落数{}", paragraphs.size());
                replaceParagraphs(paragraphs, data);
                List<XWPFTable> tables = cell.getTables();
                log.trace("当前结构体内存在段落数{}", tables.size());
                replaceSubTables(tables, data);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void replaceSubTables(List<XWPFTable> tables, Object data) {
        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            for (XWPFTable table : tables) {
                String key = findTableDataKey(table);
                Object item = dataMap.get(key);
                log.trace("表格数据替换：目标表格{}，对应数据{}", key, item);
                if (key != null && item != null) {
                    replaceSubTable(table, item);
                }
            }
        } else {
            throw new RenderException("data is not of map");
        }
    }

    @SuppressWarnings("unchecked")
    private void replaceSubTable(XWPFTable table, Object data) {
        if (data instanceof Map) {
            //文档型表格，非动态，直接进行单元个替换
            log.debug("目标表格为文档类型");
            Map<String, Object> dataMap = (Map<String, Object>) data;
            replaceSubTable4Map(table, dataMap);
        } else if (data instanceof List) {
            //列表型表格，动态，存在动态行数据填充
            log.debug("目标表格为列表类型");
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
            replaceSubTable4List(table, dataList);
        }
    }

    private void replaceSubTable4Map(XWPFTable table, Map<String, Object> data) {
        List<XWPFTableRow> rows = table.getRows();
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                replaceCell(cell, data);
            }
        }
    }

    private void replaceSubTable4List(XWPFTable table, List<Map<String, Object>> dataList) {
        List<XWPFTableRow> rows = table.getRows();
        if (rows.size() > dataList.size()) {
            throw new RenderException("each table data error: data size is not more than row size!");
        }
        //判断是否存在合计行（合计行一般都是最后一行，识别标识${all}）
        boolean hasCount = findCountLine(table);
        //寻找循环填充起始行（循环填充规则与其它的不一样，识别标识${start}）
        int start = findStartLine(table);
        int end = dataList.size();
        if (hasCount) {
            end--;
        }
        for (int i = 0; i < start; i++) {
            Map<String, Object> dataMap = dataList.get(i);
            //循环行之前的替换采用单元格替换
            XWPFTableRow row = rows.get(i);
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                replaceCell(cell, dataMap);
            }
        }
        List<Map<String, Object>> subDataList = dataList.subList(start, end);
        List<RowRenderData> renderDataList = buildRowsData(rows.get(start), subDataList);
        table.removeRow(start);
        for (int i = 0; i < renderDataList.size(); i++) {
            XWPFTableRow insertNewTableRow = table.insertNewTableRow(start + i);
            RowRenderData renderData = renderDataList.get(i);
            for (int j = 0; j < renderData.size(); j++) {
                insertNewTableRow.createCell();
            }
            MiniTableRenderPolicy.Helper.renderRow(table, start + i, renderData);
        }
        if (hasCount) {
            Map<String, Object> dataMap = dataList.get(dataList.size() - 1);
            XWPFTableRow row = rows.get(rows.size() - 1);
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                replaceCell(cell, dataMap);
            }
        }
    }

    private void replaceCell(XWPFTableCell cell, Map<String, Object> data) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        List<XWPFRun> runs = paragraph.getRuns();
        if (!runs.isEmpty()) {
            for (XWPFRun run : runs) {
                String temp = run.getText(0);
                Matcher matcher = pattern.matcher(temp);
                if (matcher.find()) {
                    String str = matcher.group();
                    String key = str.substring(2, str.indexOf("}"));
                    TextRenderPolicy.Helper.renderTextRun(run, StringUtils.defaultString(data.get(key)));
                }
            }
        }
    }

    private String findTableDataKey(XWPFTable table) {
        try {
            XWPFParagraph paragraph = table.getRows().get(0).getTableCells().get(0).getParagraphs().get(0);
            String temp = paragraph.getText();
            Matcher matcher = pattern.matcher(temp);
            if (matcher.find()) {
                String str = matcher.group();
                TextRenderPolicy.Helper.renderTextRun(paragraph.getRuns().get(0), str.substring(str.indexOf("}") + 1));
                return str.substring(2, str.indexOf("}"));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private Boolean findCountLine(XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        XWPFTableRow last = rows.get(rows.size() - 1);
        List<XWPFTableCell> cells = last.getTableCells();
        for (XWPFTableCell cell : cells) {
            XWPFParagraph paragraph = cell.getParagraphs().get(0);
            String str = paragraph.getText();
            if (str.contains(COUNT_FLAG)) {
                TextRenderPolicy.Helper.renderTextRun(paragraph.getRuns().get(0), "");
                return true;
            }
        }
        return false;
    }

    private int findStartLine(XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        int index = 0;
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                XWPFParagraph paragraph = cell.getParagraphs().get(0);
                String str = paragraph.getText();
                if (str.contains(START_FLAG)) {
                    TextRenderPolicy.Helper.renderTextRun(paragraph.getRuns().get(0), str.substring(str.indexOf("}") + 1));
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    private List<RowRenderData> buildRowsData(XWPFTableRow startRow, List<Map<String, Object>> subDataList) {
        List<RowRenderData> renderDataList = new ArrayList<>();
        for (Map<String, Object> map : subDataList) {
            renderDataList.add(buildRowData(startRow, map));
        }
        return renderDataList;
    }

    private RowRenderData buildRowData(XWPFTableRow startRow, Map<String, Object> data) {
        TableStyle rowStyle = new TableStyle();
        rowStyle.setAlign(STJc.CENTER);
        List<XWPFTableCell> cells = startRow.getTableCells();
        String[] strings = new String[cells.size()];
        for (int i = 0; i < cells.size(); i++) {
            XWPFParagraph paragraph = cells.get(i).getParagraphs().get(0);
            String temp = paragraph.getText();
            List<XWPFRun> runs = paragraph.getRuns();
            if (!runs.isEmpty()) {
                Matcher matcher = pattern.matcher(temp);
                if (matcher.find()) {
                    String str = matcher.group();
                    String key = str.substring(2, str.indexOf("}"));
                    strings[i] = StringUtils.defaultString(data.get(key));
                } else {
                    strings[i] = temp;
                }
            }
        }
        RowRenderData rowData = RowRenderData.build(strings);
        rowData.setRowStyle(rowStyle);
        return rowData;
    }

    @SuppressWarnings("unchecked")
    private void replaceParagraphs(List<XWPFParagraph> paragraphs, Object data) throws Exception {
        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                if (!runs.isEmpty()) {
                    for (XWPFRun run : runs) {
                        String temp = run.getText(0);
                        Matcher matcher = pattern.matcher(temp);
                        if (matcher.find()) {
                            run.setText("", 0);
                            String str = matcher.group();
                            String key = str.substring(2, str.indexOf("}"));
                            List<Object> dataList;
                            Object subData = dataMap.get(key);
                            log.trace("进行段落替换，目标标签为{}，对应数据为{}", str, subData);
                            if (subData instanceof List) {
                                dataList = (List<Object>) subData;
                            } else {
                                dataList = Collections.singletonList(subData);
                            }
                            replaceRun(run, dataList);
                        }
                    }
                }
            }
        } else {
            throw new RenderException("data is not of map");
        }
    }

    private void replaceRun(XWPFRun run, List<Object> dataList) throws Exception {
        NiceXWPFDocument document = (NiceXWPFDocument) run.getDocument();
        for (Object data : dataList) {
            if (data instanceof TextRenderData) {
                XWPFRun createRun = document.insertNewParagraph(run).createRun();
                StyleUtils.styleRun(createRun, run);
                TextRenderPolicy.Helper.renderTextRun(createRun, data);
            } else if (data instanceof MiniTableRenderData) {
                com.deepoove.poi.policy.MiniTableRenderPolicy.Helper.renderMiniTable(run, (MiniTableRenderData) data);
            } else if (data instanceof NumbericRenderData) {
                com.deepoove.poi.policy.NumbericRenderPolicy.Helper.renderNumberic(run, (NumbericRenderData) data);
            } else if (data instanceof PictureRenderData) {
                com.deepoove.poi.policy.PictureRenderPolicy.Helper.renderPicture(document.insertNewParagraph(run).createRun(), (PictureRenderData) data);
            }
        }
    }

}
