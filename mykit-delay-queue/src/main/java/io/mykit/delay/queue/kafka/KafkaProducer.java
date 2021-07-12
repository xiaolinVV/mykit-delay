package io.mykit.delay.queue.kafka;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

/**
 * @program: stone-server
 * @description: kafka 生产者
 * @author: zhangshaolin
 * @create: 2019-06-24 15:10
 **/
public class KafkaProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 发送的消息统一通过google-gson序列化
     */

    private final KafkaTemplate<String, String> mKafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.mKafkaTemplate = kafkaTemplate;
    }

    /**
     * 发送消息
     *
     * @param topic topic名称
     * @param key   消息key
     * @param msg   消息内容
     * @return
     */
    public void send(String topic, String key, Object msg) {
        // 将发送的消息序列化为json
        String json = toJsonString(msg);
        try {
            ListenableFuture<SendResult<String, String>> futureResult = mKafkaTemplate.send(
                    topic, key, json);
            futureResult.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    logger.error("Kafka send success.");
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("Kafka send failed. {}",ex);
                }
            });
            // 这里的输出日志, 不能用fastjson, fastjson默认依赖bean的setter/getter方法,
            // 而SendResult中的RecordMetadata的属性并没有setter/getter方法
            //logger.info("Kafka send result: {}", GSON.toJson(result));
            //SendResult<String, String> result = futureResult.get();
            //return result != null;
        } catch (Throwable e) {
            logger.error("Kafka send failed.", e);
        }
    }

    /**
     * @param topic topic名称
     * @param msg   消息内容
     * @return
     */
    public void send(String topic, Object msg) {
        send(topic, UUID.randomUUID().toString(), msg);
    }

    /**
     * @param o
     * @return
     */
    private String toJsonString(Object o) {
        String value;
        if (o instanceof String) {
            value = (String) o;
        } else {
            value = JSON.toJSONString(o);
        }
        return value;
    }
}
