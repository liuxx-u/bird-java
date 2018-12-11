package com.bird.gateway.common.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author liuxx
 * @date 2018/11/29
 */
public class ByteBuffUtils {

    /**
     * bufferToString.
     *
     * @param buffer buffer
     * @return java.lang.String
     */
    public static String byteBufferToString(final ByteBuffer buffer) {
        CharBuffer charBuffer;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
//            buffer.flip();
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
