package com.bird.core.security;

import java.security.Security;

/**
 * 加密基类
 *
 * @author liuxx
 * @date 2017/5/16
 */
public abstract class SecurityCoder {
    private static Byte ADDFLAG = 0;
    static {
        if (ADDFLAG == 0) {
            // 加入BouncyCastleProvider支持
            Security.addProvider(new BouncyCastleProvider());
            ADDFLAG = 1;
        }
    }
}
