package com.bird.core.utils;

import com.bird.core.exception.UserFriendlyException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Poi操作excel工具类
 *
 * @author liuxx
 * @date 2018/6/8
 */
public class PoiExcelHelper {

    private static Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0");

    /**
     * 读取本地EXCEL文件
     *
     * @param path   文件路径
     * @param config 头配置
     * @return excel数据
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
     * @return excel数据
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
     * @return excel数据
     */
    public static List<Map<String, Object>> read(String path, Map<String, String> config, Integer sheetIndex, Integer headerIndex) throws IOException {

        List<Map<String, Object>> result = new ArrayList<>();
        try (InputStream stream = new FileInputStream(path)) {
            result = read(stream, config, sheetIndex, headerIndex);
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        }
        return result;
    }

    /**
     * 读取远程EXCEL文件
     *
     * @param uri    url地址
     * @param config 头配置
     * @return excel数据
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
     * @return excel数据
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
     * @return excel数据
     */
    public static List<Map<String, Object>> readUrl(String uri, Map<String, String> config, Integer sheetIndex, Integer headerIndex) throws IOException {
        URL url = new URL(uri);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10 * 60 * 1000);

        List<Map<String, Object>> result = new ArrayList<>();

        try(InputStream stream = conn.getInputStream()) {

            result = read(stream, config, sheetIndex, headerIndex);
        } catch (MalformedURLException e) {
            logger.error("文件获取失败", e);
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
     * @return excel数据
     */
    public static List<Map<String, Object>> read(InputStream stream, Map<String, String> config, Integer sheetIndex, Integer headerIndex) {
        List<Map<String, Object>> result = new ArrayList<>();

        try (Workbook wb = WorkbookFactory.create(stream)) {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            result = read(sheet,config,headerIndex);
        } catch (InvalidFormatException | IOException e) {
            logger.error("Excel流读取失败", e);
        }

        return result;
    }

    /**
     * 读取EXCEL文件
     *
     * @param sheet      sheet页
     * @param config      表头与Key映射\
     * @param headerIndex header行序号
     * @return excel数据
     */
    public static List<Map<String, Object>> read(Sheet sheet, Map<String, String> config, Integer headerIndex) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (sheet == null) return result;

        List<String> headKeys = getHeadKeys(sheet, config, headerIndex);
        if (CollectionUtils.isEmpty(headKeys)) return result;

        int max = sheet.getLastRowNum();
        FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        for (int i = headerIndex + 1; i <= max; i++) {
            Map<String, Object> line = readLine(sheet.getRow(i), headKeys, evaluator);
            if (line == null) continue;
            result.add(line);
        }

        return result;
    }

    /**
     * 导出Excel（2003）
     *
     * @param list      数据源
     * @param config    表头与Key映射
     * @param sheetName 工作表的名称
     * @param out       导出流
     */
    public static void listToExcel(List<Map> list, LinkedHashMap<String, String> config, String sheetName, OutputStream out) {
        Integer sheetSize = 65535;

        if (CollectionUtils.isEmpty(list)) {
            throw new UserFriendlyException("数据源中没有任何数据");
        }
        try(Workbook wb = new HSSFWorkbook()) {
            //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
            //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            //1.计算一共有多少个工作表
            double sheetNum = Math.ceil(list.size() / 65535d);
            if (sheetNum == 1) {
                Sheet sheet = wb.createSheet(sheetName);
                fillSheet(sheet, list, config, 0, list.size() - 1);
            } else {
                //2.创建相应的工作表，并向其中填充数据
                for (int i = 0; i < sheetNum; i++) {
                    Sheet sheet = wb.createSheet(sheetName + (i + 1));

                    //获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = Math.min((i + 1) * sheetSize - 1, list.size() - 1);
                    //填充工作表
                    fillSheet(sheet, list, config, firstIndex, lastIndex);
                }
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error("Excel流写入失败", e);
        }
    }

    /**
     * 导出Excel（2003）
     *
     * @param list      数据源
     * @param config    表头与Key映射
     * @param sheetName 工作表的名称
     * @param out       导出流
     */
    public static void listToExcel7(List<Map> list, LinkedHashMap<String, String> config, String sheetName, OutputStream out) {
        if (CollectionUtils.isEmpty(list)) {
            throw new UserFriendlyException("数据源中没有任何数据");
        }
        //2007的Excel一个工作表最多可以有1048576条记录，除去列头剩下1048575条
        if(list.size() > 1048575){
            throw new UserFriendlyException("数据条数太多，无法导出");
        }
        try(Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet(sheetName);
            fillSheet(sheet, list, config, 0, list.size() - 1);
            wb.write(out);
        } catch (Exception e) {
            logger.error("Excel流写入失败", e);
        }
    }

    /**
     * 获取表头对应的字段名称
     *
     * @param sheet  sheet页
     * @param config 头信息配置
     * @return 字段集合
     */
    private static List<String> getHeadKeys(Sheet sheet, Map<String, String> config, Integer headerIndex) {
        if (sheet == null || NumberHelper.isNotPositive(sheet.getLastRowNum())) return null;
        ArrayList<String> headKeys = new ArrayList<>();

        Row headRow = sheet.getRow(headerIndex);
        if (headRow == null) return headKeys;

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
     * @param row       行数据
     * @param headKeys  keys
     * @param evaluator 公式计算器
     * @return 行数据
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
                    value = StringUtils.replaceAll(cell.getStringCellValue(),"^\\s*|\\s*$|\t|\r|\n","");
                    break;
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    } else {
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
                default:
                    break;
            }

            if (value != null && Objects.equals(value.getClass().getName(), "java.lang.Double") && StringUtils.indexOf(value.toString(), "E") >= 0) {
                value = DECIMAL_FORMAT.format(value);
            }
            line.put(key, value);
        }
        return line;
    }

    /**
     * 向工作表中填充数据
     *
     * @param sheet      工作表
     * @param list       数据源
     * @param fieldMap   中英文字段对应关系的Map
     * @param firstIndex 开始索引
     * @param lastIndex  结束索引
     */
    private static void fillSheet(Sheet sheet, List<Map> list, LinkedHashMap<String, String> fieldMap, int firstIndex, int lastIndex) throws Exception {

        //定义存放英文字段名和中文字段名的数组
        String[] cnFields = new String[fieldMap.size()];
        String[] enFields = new String[fieldMap.size()];

        //填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            cnFields[count] = entry.getKey();
            enFields[count] = entry.getValue();
            count++;
        }
        //填充表头
        Row headRow = sheet.createRow(0);
        CellStyle headStyle = sheet.getWorkbook().createCellStyle();
        headStyle.setFillBackgroundColor((short) 44);

        for (int i = 0; i < cnFields.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            headRow.createCell(i).setCellValue(cnFields[i]);
        }

        //填充内容
        int rowNo = 1;
        for (int index = firstIndex; index <= lastIndex; index++) {
            Row row = sheet.createRow(rowNo);
            //获取单个对象
            Map item = list.get(index);
            for (int i = 0; i < enFields.length; i++) {
                Object objValue = item.get(enFields[i]);
                if (objValue == null) continue;
                if (objValue instanceof Date) {
                    objValue = DateHelper.format((Date) objValue, "yyyy-MM-dd HH:mm:ss");
                }
                row.createCell(i).setCellValue(objValue.toString());
            }
            rowNo++;
        }
    }
}
