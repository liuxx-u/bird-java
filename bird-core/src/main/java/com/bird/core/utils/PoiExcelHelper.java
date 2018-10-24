package com.bird.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Poi操作excel工具类
 *
 * @author liuxx
 * @date 2018/6/8
 */
public class PoiExcelHelper {

    /**
     * 读取本地EXCEL文件
     *
     * @param path   文件路径
     * @param config 头配置
     * @return
     */
    public static List<Map<String, Object>> read(String path, Map<String, String> config) throws IOException {
        return read(path, config, 0, 0);
    }

    /**
     * 读取远程EXCEL文件
     *
     * @param path        文件路径
     * @param config      头配置
     * @param headerIndex 头所在的行
     * @return
     */
    public static List<Map<String, Object>> read(String path, Map<String, String> config, Integer headerIndex) throws IOException {
        return read(path, config, 0, headerIndex);
    }

    /**
     * 读取本地EXCEL文件
     *
     * @param path        文件路径
     * @param config      头配置
     * @param sheetIndex  sheet序号
     * @param headerIndex 头所在的行
     * @return
     */
    public static List<Map<String, Object>> read(String path, Map<String, String> config, Integer sheetIndex, Integer headerIndex) throws IOException {
        InputStream stream = null;
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            stream = new FileInputStream(path);
            result = read(stream, config, sheetIndex, headerIndex);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return result;
    }

    /**
     * 读取远程EXCEL文件
     *
     * @param uri    url地址
     * @param config 头配置
     * @return
     */
    public static List<Map<String, Object>> readUrl(String uri, Map<String, String> config) throws IOException {
        return readUrl(uri, config, 0, 0);
    }

    /**
     * 读取远程EXCEL文件
     *
     * @param uri         url地址
     * @param config      头配置
     * @param headerIndex 头所在的行
     * @return
     */
    public static List<Map<String, Object>> readUrl(String uri, Map<String, String> config, Integer headerIndex) throws IOException {
        return readUrl(uri, config, 0, headerIndex);
    }

    /**
     * 读取远程EXCEL文件
     *
     * @param uri         url地址
     * @param config      头配置
     * @param sheetIndex  sheet序号
     * @param headerIndex 头所在的行
     * @return
     */
    public static List<Map<String, Object>> readUrl(String uri, Map<String, String> config, Integer sheetIndex, Integer headerIndex) throws IOException {
        InputStream stream = null;
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10 * 60 * 1000);
            stream = conn.getInputStream();

            result = read(stream, config, sheetIndex, headerIndex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return result;
    }

    /**
     * 读取EXCEL文件
     *
     * @param stream      文件流
     * @param config      表头与Key映射
     * @param sheetIndex  sheet序号
     * @param headerIndex header行序号
     * @return
     */
    public static List<Map<String, Object>> read(InputStream stream, Map<String, String> config, Integer sheetIndex, Integer headerIndex) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            Workbook wb = WorkbookFactory.create(stream);
            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            Sheet sheet = wb.getSheetAt(sheetIndex);
            if (sheet == null) return null;

            List<String> headKeys = getHeadKeys(sheet, config, headerIndex);
            if (CollectionUtils.isEmpty(headKeys)) return null;

            int max = sheet.getLastRowNum();
            for (int i = headerIndex + 1; i <= max; i++) {
                Map<String, Object> line = readLine(sheet.getRow(i), headKeys, evaluator);
                if (line == null) continue;
                result.add(line);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取表头对应的字段名称
     *
     * @param sheet
     * @param config
     * @return
     */
    private static List<String> getHeadKeys(Sheet sheet, Map<String, String> config, Integer headerIndex) {
        if (sheet == null || NumberHelper.isNotPositive(sheet.getLastRowNum())) return null;
        ArrayList<String> headKeys = new ArrayList<>();

        Row headRow = sheet.getRow(headerIndex);
        if (headRow == null) return null;

        short max = headRow.getLastCellNum();
        for (short i = headRow.getFirstCellNum(); i < max; i++) {
            String headName = StringUtils.strip(headRow.getCell(i).getStringCellValue());
            String key = config.getOrDefault(headName, headName);
            headKeys.add(key);
        }
        return headKeys;
    }

    /**
     * 读取每一行的数据
     *
     * @param row
     * @param headKeys
     * @param evaluator
     * @return
     */
    private static Map<String, Object> readLine(Row row, List<String> headKeys, FormulaEvaluator evaluator) {
        if (row == null) return null;

        Map<String, Object> line = new HashMap<>(8);
        short max = row.getLastCellNum();
        for (short i = row.getFirstCellNum(); i < max; i++) {
            if (i >= headKeys.size()) break;

            Cell cell = row.getCell(i);
            if (cell == null) continue;

            String key = headKeys.get(i);
            Object value = null;
            CellType cellType = cell.getCellTypeEnum();
            switch (cellType) {
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if(HSSFDateUtil.isCellDateFormatted(cell)){
                        value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    }else {
                        value = cell.getNumericCellValue();
                    }
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break;
                case FORMULA:
                    CellValue cellValue = evaluator.evaluate(cell);
                    value = cellValue.getCellTypeEnum() == CellType.NUMERIC ? cellValue.getNumberValue() : cellValue.getStringValue();
                    break;
            }
            line.put(key, value);
        }
        return line;
    }
}
