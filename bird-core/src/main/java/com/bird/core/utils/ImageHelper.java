package com.bird.core.utils;

import com.bird.core.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * @author liuxx
 * @date 2018/5/2
 */
public class ImageHelper {

    /**
     * 图片分割最大行
     */
    private static final int SPLIT_MAX_ROW = 20;
    /**
     * 图片分割最大列
     */
    private static final int SPLIT_MAX_COL = 20;
    /**
     * 图片分割默认行
     */
    private static final int SPLIT_DEFAULT_ROW = 2;
    /**
     * 图片分割默认列
     */
    private static final int SPLIT_DEFAULT_COL = 2;
    /**
     * 图片分割默认宽度
     */
    private static final int SPLIT_DEFAULT_WIDTH = 200;
    /**
     * 图片分割默认高度
     */
    private static final int SPLIT_DEFAULT_HEIGHT = 150;
    /**
     * 默认水印字体
     */
    private static final Font DEFAULT_FONT = new Font("黑体",Font.BOLD,20);
    /**
     * 默认的透明度
     */
    private static final float DEFAULT_ALPHA = 0.5F;

    /**
     * 缩放图像（按比例缩放）
     * @param srcPath 源图像文件地址
     * @param targetPath 缩放后的图像地址
     * @param ratio 比例
     */
    public static void scale(String srcPath, String targetPath, double ratio) {
        if (ratio <= 0) return;
        try {
            //读取原图
            BufferedImage srcImg = ImageIO.read(new File(srcPath));
            int width = (int) (srcImg.getWidth() * ratio);
            int height = (int) (srcImg.getHeight() * ratio);

            // 绘制缩放后的图
            Image tempImg = srcImg.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage targetImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = targetImg.getGraphics();
            g.drawImage(tempImg, 0, 0, null);
            g.dispose();

            // 输出到文件流
            ImageIO.write(targetImg, "JPEG", new File(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 缩放图像（按高度和宽度缩放,使用较长的边进行缩放）
     * @param srcPath 源图像文件地址
     * @param targetPath 缩放后的图像地址
     * @param width 缩放后的宽度
     * @param height 缩放后的高度
     * @param fillEmpty 比例不对时是否需要补白
     */
    public static void scale(String srcPath, String targetPath, int width, int height, boolean fillEmpty) {
        if (width <= 0 || height <= 0) return;
        try {
            BufferedImage srcImg = ImageIO.read(new File(srcPath));
            int srcWidth = srcImg.getWidth();
            int srcHeight = srcImg.getHeight();
            int targetWidth = width;
            int targetHeight = height;

            // 使用较长的边作为缩放基准
            if (srcWidth >= srcHeight) {
                targetHeight = (int) (Integer.valueOf(width).doubleValue() / srcWidth * srcHeight);
            } else {
                targetWidth = (int)(Integer.valueOf(height).doubleValue() / srcHeight * srcWidth);
            }
            BufferedImage targetImg = new BufferedImage(targetWidth,targetHeight,BufferedImage.TYPE_INT_RGB);
            Graphics g = targetImg.getGraphics();
            g.drawImage(srcImg.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT), 0, 0, null);
            g.dispose();

            //填充空白区域
            if (fillEmpty) {
                BufferedImage tempImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = tempImg.createGraphics();
                g2.setColor(Color.white);
                g2.fillRect(0, 0, width, height);

                if (width == targetWidth) {
                    g2.drawImage(targetImg, 0, (height - targetHeight) / 2, targetWidth, targetHeight, Color.white, null);
                } else {
                    g2.drawImage(targetImg, (width - targetWidth) / 2, 0, targetWidth, targetHeight, Color.white, null);
                }
                g2.dispose();
                targetImg = tempImg;
            }
            ImageIO.write(targetImg, "JPEG", new File(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcPath 源图像地址
     * @param targetPath 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public static void cut(String srcPath, String targetPath,int x, int y, int width, int height) {
        try {
            BufferedImage srcImg = ImageIO.read(new File(srcPath));
            int srcWidth = srcImg.getWidth();
            int srcHeight = srcImg.getHeight();
            if (srcWidth <= 0 || srcHeight <= 0 || width <= 0 || height <= 0 || x > srcWidth || y > srcHeight) return;
            //宽高不能超过原图，否则会出现黑边
            if (width + x > srcWidth) {
                width = srcWidth - x;
            }
            if (height + y > srcHeight) {
                height = srcHeight - y;
            }

            ImageFilter filter = new CropImageFilter(x, y, width, height);
            Image tempImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcImg.getSource(), filter));
            BufferedImage targetImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 绘制切割后的图
            Graphics g = targetImg.getGraphics();
            g.drawImage(tempImg, 0, 0, width, height, null);
            g.dispose();

            ImageIO.write(targetImg, "JPEG", new File(targetPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的行数和列数）
     * @param srcPath 源图像地址
     * @param targetDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public static void split(String srcPath, String targetDir, int rows, int cols) {
        try {
            BufferedImage srcImg = ImageIO.read(new File(srcPath));
            split(srcImg, targetDir, rows, cols);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图像切割（指定切片的宽度和高度）
     * @param srcPath 源图像地址
     * @param targetDir 切片目标文件夹
     * @param width 目标切片宽度。默认200
     * @param height 目标切片高度。默认150
     */
    public static void split2(String srcPath, String targetDir, int width, int height) {
        if (width <= 0) width = SPLIT_DEFAULT_WIDTH;
        if (height <= 0) height = SPLIT_DEFAULT_HEIGHT;

        try {
            BufferedImage srcImg = ImageIO.read(new File(srcPath));

            int rows = (int) Math.ceil(Integer.valueOf(srcImg.getHeight()).doubleValue() / height);
            int cols = (int) Math.ceil(Integer.valueOf(srcImg.getWidth()).doubleValue() / width);
            split(srcImg, targetDir, rows, cols);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图像切割（指定切片的行数和列数）
     * @param srcImg 源图像
     * @param targetDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public static void split(BufferedImage srcImg,String targetDir, int rows, int cols) {
        if (rows <= 0 || rows > SPLIT_MAX_ROW) rows = SPLIT_DEFAULT_ROW;
        if (cols <= 0 || cols > SPLIT_MAX_COL) cols = SPLIT_DEFAULT_COL;

        try {
            int srcWidth = srcImg.getWidth();
            int srcHeight = srcImg.getHeight();
            if (srcWidth <= 0 || srcHeight <= 0) return;

            Image tempImg;
            ImageFilter filter;

            // 计算切片的宽度和高度
            int targetWidth = (int) Math.ceil(Integer.valueOf(srcWidth).doubleValue() / cols);
            int targetHeight = (int) Math.ceil(Integer.valueOf(srcHeight).doubleValue() / rows);

            // 循环建立切片
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int x = j * targetWidth;
                    int y = i * targetHeight;
                    //消除最后一张图宽高溢出的问题
                    int width = x + targetWidth > srcWidth ? srcWidth - x : targetWidth;
                    int height = y + targetHeight > srcHeight ? srcHeight - y : targetHeight;

                    filter = new CropImageFilter(x, y, width, height);
                    tempImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcImg.getSource(), filter));
                    BufferedImage targetImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics g = targetImg.getGraphics();
                    // 绘制缩小后的图
                    g.drawImage(tempImg, 0, 0, null);
                    g.dispose();
                    // 输出为文件
                    ImageIO.write(targetImg, "JPEG", new File(String.format("%s_r%d_c%d.jpg", targetDir, i, j)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * @param srcPath 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param targetPath 目标图像地址
     */
    public static void convert(String srcPath, String targetPath, String formatName) {
        try {
            File f = new File(srcPath);
            BufferedImage srcImg = ImageIO.read(f);
            ImageIO.write(srcImg, formatName, new File(targetPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 彩色转为黑白
     * @param srcPath 源图像地址
     * @param targetPath 目标图像地址
     */
    public static void gray(String srcPath, String targetPath) {
        try {
            BufferedImage srcImg = ImageIO.read(new File(srcPath));
            ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(colorSpace, null);
            srcImg = op.filter(srcImg, null);
            ImageIO.write(srcImg, "JPEG", new File(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加文字水印
     * @param srcPath 源图像地址
     * @param targetPath 目标图像地址
     * @param text 水印文字
     * @param font 水印的字体
     * @param color 水印的颜色
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void mark(String srcPath, String targetPath,String text, Font font , Color color,int x,int y, float alpha) {
        if (font == null) font = DEFAULT_FONT;
        if (alpha < 0 || alpha > 1) {
            alpha = DEFAULT_ALPHA;
        }
        try {
            Image srcImg = ImageIO.read(new File(srcPath));
            int width = srcImg.getWidth(null);
            int height = srcImg.getHeight(null);

            BufferedImage targetImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImg.createGraphics();
            g.drawImage(srcImg, 0, 0, width, height, null);
            g.setFont(font);
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawString(text, (width - (getLength(text) * font.getSize())) / 2 + x, (height - font.getSize()) / 2 + y);
            g.dispose();
            ImageIO.write(targetImg, "JPEG", new File(targetPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 给图片添加图片水印
     * @param srcPath 源图像地址
     * @param markPath 水印图片地址
     * @param targetPath 目标图像地址
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public static void mark(String srcPath, String markPath, String targetPath, float alpha) {
        if (alpha < 0 || alpha > 1) {
            alpha = DEFAULT_ALPHA;
        }
        try {
            Image srcImg = ImageIO.read(new File(srcPath));
            int srcWidth = srcImg.getWidth(null);
            int srcHeight = srcImg.getHeight(null);

            BufferedImage targetImg = new BufferedImage(srcWidth, srcHeight,BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcWidth, srcHeight, null);

            // 水印文件
            Image markImg = ImageIO.read(new File(markPath));
            int markWidth = markImg.getWidth(null);
            int markHeight = markImg.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));

            int x = srcWidth - markWidth;
            int y = srcHeight - markHeight;
            g.drawImage(markImg, x,y, markWidth, markHeight, null);

            g.dispose();
            ImageIO.write(targetImg,  "JPEG", new File(targetPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算text的长度（一个中文算两个字符）
     * @param text 文字
     * @return 文字长度
     */
    private static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if ((text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
}
