package com.jjx.boot.util;

import com.deepoove.poi.NiceXWPFDocument;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.StyleUtils;
import com.deepoove.poi.util.TableTools;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangjx
 */
public class EachTableRenderPolicy implements RenderPolicy {

    private static final String COUNT_FLAG = "${all}";

    private String regex = "\\$\\{[^{}]+}";

    Pattern pattern = Pattern.compile(regex);

    @Override
    public void render(ElementTemplate elementTemplate, Object data, XWPFTemplate xwpfTemplate) {
        NiceXWPFDocument doc = xwpfTemplate.getXWPFDocument();
        RunTemplate runTemplate = (RunTemplate) elementTemplate;
        XWPFRun run = runTemplate.getRun();
        run.setText("", 0);
        try {
            if (!TableTools.isInsideTable(run)) {
                throw new IllegalStateException("The template tag " + run.getLang() + " must be inside a table");
            } else {
                CTP ctp = ((XWPFParagraph) run.getParent()).getCTP();
                XmlCursor newCursor = createCursor(ctp);
                XmlObject object = newCursor.getObject();
                XWPFTable table = doc.getTableByCTTbl((CTTbl) object);
                this.render(run, table, data);
            }
        } catch (Exception var10) {
            throw new RenderException("each table error:" + var10.getMessage(), var10);
        }
    }

    @SuppressWarnings("unchecked")
    private void render(XWPFRun run, XWPFTable table, Object data) {
        if (data instanceof List) {
            List<Object> objs = (List<Object>) data;
            XWPFDocument document = run.getDocument();
            CTP ctp = ((XWPFParagraph) run.getParent()).getCTP();
            int index = document.getTables().indexOf(table);
            /*
             * 这里新建table定位原理和数据匹配非常复杂
             * 并遵循我所约定的规则
             * 请开发人员切勿乱动此处代码
             */
            int ii = 0;
            for (int i = 1; i < objs.size(); i++) {
                XmlCursor newCursor = createCursor(ctp);
                document.insertNewTbl(newCursor);
                XWPFTable newTable = cloneTable(table);
                replaceData(newTable, objs.get(i));
                document.setTable(index + ii, newTable);
                ii++;
            }
            replaceData(table, objs.get(0));
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

    @SuppressWarnings("unchecked")
    private void replaceData(XWPFTable table, Object data) {
        if (data instanceof Map) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();
                replaceData(cells, dataMap);
            }
        } else if (data instanceof List) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) data;
            List<XWPFTableRow> rows = table.getRows();
            if (rows.size() > dataList.size()) {
                throw new RenderException("each table data error: data size is not more than row size!");
            }
            boolean hasCount = findCountLine(table);
            int start = findStartLine(table);
            int end = dataList.size();
            if (hasCount) {
                end--;
            }
            XWPFTableRow tempRow = cloneTable(table).getRow(start);
            List<String> keys = buildKeys(tempRow);
            int ii = 1;
            for (int i = 0; i < end; i++) {
                Map<String, Object> dataMap = dataList.get(i);
                if (i <= start) {
                    List<XWPFTableCell> cells = rows.get(i).getTableCells();
                    replaceData(cells, dataMap);
                } else {
                    XWPFTableRow newRow = table.insertNewTableRow(start + ii);
                    for (String key : keys) {
                        XWPFTableCell cell = newRow.createCell();
                        replaceData(cell, String.valueOf(dataMap.get(key)));
                    }
                    ii++;
                }
            }
            if (hasCount) {
                Map<String, Object> dataMap = dataList.get(dataList.size() - 1);
                List<XWPFTableCell> cells = rows.get(rows.size() - 1).getTableCells();
                replaceData(cells, dataMap);
            }
        }
    }

    private void replaceData(List<XWPFTableCell> cells, Map<String, Object> dataMap) {
        for (XWPFTableCell cell : cells) {
            String temp = cell.getText();
            Matcher matcher = pattern.matcher(temp);
            while (matcher.find()) {
                String k = matcher.group();
                String key = k.substring(2, k.length() - 1);
                replaceData(cell, temp.replace(k, String.valueOf(dataMap.get(key))));
            }
        }
    }

    private void replaceData(XWPFTableCell cell, String data) {
        XWPFRun run = cell.addParagraph().createRun();
        List<XWPFRun> srcRuns = cell.getParagraphs().get(0).getRuns();
        if (!srcRuns.isEmpty()) {
            StyleUtils.styleRun(run, cell.getParagraphs().get(0).getRuns().get(0));
        }
        cell.removeParagraph(0);
        run.setText(data);
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
    }

    private int findStartLine(XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        int i = 0;
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                String temp = cell.getText();
                Matcher matcher = pattern.matcher(temp);
                if (matcher.find()) {
                    String g = matcher.group();
                    String sub = g.substring(2, g.length() - 1);
                    if ("start".equals(sub)) {
                        cell.removeParagraph(0);
                        XWPFParagraph ph = cell.addParagraph();
                        ph.createRun().setText(temp.replace(g, ""));
                        return i;
                    }
                }
            }
            i++;
        }
        return -1;
    }

    private Boolean findCountLine(XWPFTable table) {
        List<XWPFTableRow> rows = table.getRows();
        XWPFTableRow last = rows.get(rows.size() - 1);
        XWPFTableCell countCell = last.getCell(0);
        String countText = countCell.getText();
        if (countText.contains(COUNT_FLAG)) {
            countCell.removeParagraph(0);
            countCell.setText(countText.replace(COUNT_FLAG, ""));
            return true;
        }
        return false;
    }

    private List<String> buildKeys(XWPFTableRow tempRow) {
        List<String> list = new ArrayList<>();
        List<XWPFTableCell> cells = tempRow.getTableCells();
        for (XWPFTableCell cell : cells) {
            String temp = cell.getText();
            Matcher matcher = pattern.matcher(temp);
            while (matcher.find()) {
                String k = matcher.group();
                list.add(k.substring(2, k.length() - 1));
            }
        }
        return list;
    }

    private XWPFTable cloneTable(XWPFTable table) {
        CTTbl ctTbl = CTTbl.Factory.newInstance();
        ctTbl.set(table.getCTTbl());
        return new XWPFTable(ctTbl, table.getBody());
    }

}
