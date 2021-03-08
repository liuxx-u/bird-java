package com.bird.websocket.common.message;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

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


    public static class SingleMessageBuilder extends BasicMessageBuilder<SingleMessageBuilder> {

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

        public SingleMessage build() {
            super.validate();
            if (StringUtils.isBlank(token) && StringUtils.isBlank(userId)) {
                throw new NullPointerException("message token and userId cannot be both empty");
            }
            SingleMessage singleMessage = new SingleMessage()
                    .setToken(this.token)
                    .setUserId(this.userId);
            return super.build(singleMessage);
        }
    }

    public static class MultipartMessageBuilder extends BasicMessageBuilder<MultipartMessageBuilder> {

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

        public MultipartMessage build() {
            super.validate();
            if (CollectionUtils.isEmpty(tokens) && CollectionUtils.isEmpty(userIds)) {
                throw new NullPointerException("message tokens and userIds cannot be both empty");
            }

            MultipartMessage multipartMessage = new MultipartMessage()
                    .setTokens(this.tokens)
                    .setUserIds(this.userIds);
            return super.build(multipartMessage);
        }
    }

    public static class BroadcastMessageBuilder extends BasicMessageBuilder<BroadcastMessageBuilder> {

        public BroadcastMessage build() {
            super.validate();
            return super.build(new BroadcastMessage());
        }
    }

    static class BasicMessageBuilder<T extends BasicMessageBuilder<?>> {
        /** 消息体 */
        protected String content;
        /** 是否异步发送消息 */
        protected boolean isAsync;

        /** 是否开启延时缓存功能 */
        protected boolean isDelay;
        /** 自定义延时缓存时间 */
        protected long delayDuration;
        /** 业务key */
        protected Map<String, String> item = Maps.newHashMap();

        @SuppressWarnings("unchecked")
        public T content(String content) {
            this.content = content;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T isAsync(boolean isAsync) {
            this.isAsync = isAsync;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T isDelay(boolean isDelay) {
            this.isDelay = isDelay;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T delayDuration(long delayDuration) {
            this.delayDuration = delayDuration;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T addItem(String itemKey, String itemValue) {
            item.put(itemKey, itemValue);
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T addItemMap(Map<String, String> itemMap) {
            if (MapUtils.isNotEmpty(itemMap)) {
                item.putAll(itemMap);
            }
            return (T) this;
        }

        protected void validate() {
            if (StringUtils.isBlank(content)) {
                throw new NullPointerException("message content cannot be empty");
            }
        }

        protected <V extends BasicMessage> V build(V message) {
            message.setContent(this.content);
            message.setAsync(this.isAsync);
            message.setDelay(this.isDelay);
            message.setDelayDuration(this.delayDuration);
            message.setItem(this.item);
            return message;
        }
    }
}
