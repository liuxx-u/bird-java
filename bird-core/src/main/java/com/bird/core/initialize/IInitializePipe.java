package com.bird.core.initialize;

/**
 * 系统初始化管道
 * 系统启动时，反射扫描的包，执行初始化操作
 *
 * @author liuxx
 * @date 2018/11/15
 */
public interface IInitializePipe extends Comparable<IInitializePipe> {

    /**
     * 扫描Class文件
     * @param clazz class
     */
    default void scanClass(Class<?> clazz){}

    /**
     * 执行初始化方法
     */
    default void initialize(){}

    /**
     * 排序号
     *
     * @return 默认值：0
     */
    default Integer getOrder() {
        return 0;
    }

    /**
     * 比较管道位置
     *
     * @param o 比较的管道
     * @return 管道按order从小到大排列
     */
    @Override
    default int compareTo(IInitializePipe o) {
        if (this.getOrder().equals(o.getOrder())) {
            return 0;
        } else if (this.getOrder() > o.getOrder()) {
            return 1;
        } else {
            return -1;
        }
    }
}
