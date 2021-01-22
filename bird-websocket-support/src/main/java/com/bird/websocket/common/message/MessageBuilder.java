package com.bird.websocket.common.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yuanjian
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageBuilder {

    /**
     * 单点消息构建器
     */
    public static SingleMessageBuilder singleMessage() {
        return new SingleMessageBuilder();
    }

    /**
     * 单点消息构建器
     */
    public static MultipartMessageBuilder multipartMessage() {
        return new MultipartMessageBuilder();
    }

    /**
     * 单点消息构建器
     */
    public static BroadcastMessageBuilder broadcastMessage() {
        return new BroadcastMessageBuilder();
    }


    public static class SingleMessageBuilder extends BasicMessageBuilder {

        private String token;
        private String userId;

        public SingleMessageBuilder token(String token) {
            this.token = token;
            return this;
        }

        public SingleMessageBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public SingleMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public SingleMessage builder() {
            super.validate();
            if (StringUtils.isBlank(token) && StringUtils.isBlank(userId)) {
                throw new NullPointerException("message token and userId cannot be both empty");
            }

            SingleMessage singleMessage = new SingleMessage()
                    .setToken(this.token)
                    .setUserId(this.userId);
            singleMessage.setContent(this.content);
            singleMessage.setAsync(this.isAsync);
            return singleMessage;
        }
    }

    public static class MultipartMessageBuilder extends BasicMessageBuilder {

        private List<String> tokens;
        private List<String> userIds;

        public MultipartMessageBuilder tokens(List<String> tokens) {
            this.tokens = tokens;
            return this;
        }

        public MultipartMessageBuilder userIds(List<String> userIds) {
            this.userIds = userIds;
            return this;
        }

        public MultipartMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MultipartMessage builder() {
            super.validate();
            if (CollectionUtils.isEmpty(tokens) && CollectionUtils.isEmpty(userIds)) {
                throw new NullPointerException("message tokens and userIds cannot be both empty");
            }

            MultipartMessage multipartMessage = new MultipartMessage()
                    .setTokens(this.tokens)
                    .setUserIds(this.userIds);
            multipartMessage.setContent(this.content);
            multipartMessage.setAsync(this.isAsync);
            return multipartMessage;
        }
    }

    public static class BroadcastMessageBuilder extends BasicMessageBuilder {

        public BroadcastMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public BroadcastMessage builder() {
            super.validate();
            BroadcastMessage multipartMessage = new BroadcastMessage();
            multipartMessage.setContent(this.content);
            multipartMessage.setAsync(this.isAsync);
            return multipartMessage;
        }
    }

    static class BasicMessageBuilder {
        /** 消息体 */
        protected String content;
        /** 是否异步发送消息 */
        protected boolean isAsync;

        public void validate() {
            if (StringUtils.isBlank(content)) {
                throw new NullPointerException("message content cannot be empty");
            }
        }
    }
}
