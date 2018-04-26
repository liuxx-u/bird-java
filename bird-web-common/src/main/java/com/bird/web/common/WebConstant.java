package com.bird.web.common;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public interface WebConstant {

    String UNKNOWN_IP = "unknown";
    /**
     * HTTP content type header name.
     */
    String CONTENT_TYPE = "Content-type";

    /**
     * HTTP content disposition header name.
     */
    String CONTENT_DISPOSITION = "Content-disposition";

    /**
     * HTTP content length header name.
     */
    String CONTENT_LENGTH = "Content-length";

    /**
     * Content-disposition value for form data.
     */
    String FORM_DATA = "form-data";

    /**
     * Content-disposition value for file attachment.
     */
    String ATTACHMENT = "attachment";

    /**
     * Part of HTTP content type header.
     */
    String MULTIPART = "multipart/";

    /**
     * HTTP content type header for multipart forms.
     */
    String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * HTTP content type header for multiple uploads.
     */
    String MULTIPART_MIXED = "multipart/mixed";
}
