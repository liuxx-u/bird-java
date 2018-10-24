package com.bird.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类
 * 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩等功能
 * @author liuxx
 */
public class FileHelper extends FileUtils {

    private static final int BUFFER_SIZE = 2 * 1024;
    private static Logger log = LoggerFactory.getLogger(FileHelper.class);

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return StringUtils.isBlank(fileName) ? null : fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件后缀名
     *
     * @param file
     * @return
     */
    public static String getSuffix(File file) {
        if (file == null) return null;
        String fileName = file.getName();
        return getSuffix(fileName);
    }

    /**
     * 复制单个文件，如果目标文件存在，则不覆盖
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return FileHelper.copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param descFileName 目标文件名
     * @param coverlay     如果目标文件已存在，是否覆盖
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (coverlay) {
                log.debug("目标文件已存在，准备删除!");
                if (!FileHelper.delFile(descFileName)) {
                    log.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else {
                log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                log.debug("目标文件所在的目录不存在，创建目录!");
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    log.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }

        // 准备复制文件
        // 读取的位数
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            log.debug("复制单个文件 " + srcFileName + " 到" + descFileName
                    + "成功!");
            return true;
        } catch (Exception e) {
            log.debug("复制文件失败：" + e.getMessage());
            return false;
        } finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个目录的内容，如果目标目录存在，则不覆盖
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return FileHelper.copyDirectoryCover(srcDirName, descDirName, false);
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  源目录名
     * @param descDirName 目标目录名
     * @param coverlay    如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (coverlay) {
                // 允许覆盖目标目录
                log.debug("目标目录已存在，准备删除!");
                if (!FileHelper.delFile(descDirNames)) {
                    log.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                return false;
            }
        } else {
            // 创建目标目录
            log.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                log.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = FileHelper.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
                // 如果拷贝文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                flag = FileHelper.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
                // 如果拷贝目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return FileHelper.deleteFile(fileName);
            } else {
                return FileHelper.deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                log.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileHelper.deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileHelper.deleteDirectory(files[i]
                        .getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 创建单个文件
     *
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                log.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 创建目录
     *
     * @param descDirName 目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            log.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            log.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            log.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }

    }

    /**
     * 写入文件
     *
     * @param fileName 要写入的文件
     */
    public static void writeToFile(String fileName, String content, boolean append) {
        try {
            FileHelper.write(new File(fileName), content, "utf-8", append);
            log.debug("文件 " + fileName + " 写入成功!");
        } catch (IOException e) {
            log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }

    /**
     * 写入文件
     *
     * @param fileName 要写入的文件
     */
    public static void writeToFile(String fileName, String content, String encoding, boolean append) {
        try {
            FileHelper.write(new File(fileName), content, encoding, append);
            log.debug("文件 " + fileName + " 写入成功!");
        } catch (IOException e) {
            log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }

    /**
     * 压缩成ZIP 方法1
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            log.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from FileHelper", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩成ZIP 方法
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            log.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from FileHelper", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * 修复路径，将 \\ 或 / 等替换为 File.separator
     *
     * @param path
     * @return
     */
    public static String path(String path) {
        String p = StringUtils.replace(path, "\\", "/");
        p = StringUtils.join(StringUtils.split(p, "/"), "/");
        if (!StringUtils.startsWithAny(p, "/") && StringUtils.startsWithAny(path, "\\", "/")) {
            p += "/";
        }
        if (!StringUtils.endsWithAny(p, "/") && StringUtils.endsWithAny(path, "\\", "/")) {
            p = p + "/";
        }
        return p;
    }
}
