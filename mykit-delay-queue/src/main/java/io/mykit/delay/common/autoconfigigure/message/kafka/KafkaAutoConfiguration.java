/**
 * Copyright 2019-2999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mykit.delay.common.autoconfigigure.message.kafka;

import io.mykit.delay.queue.kafka.KafkaProducer;
import io.mykit.delay.queue.kafka.KafkaSenderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author liuyazhuang
 * @version 1.0.0
 * @date 2019/6/12
 * @description 配置ActiveMQ
 */
@Configuration
public class KafkaAutoConfiguration {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired(required = false)
    public KafkaAutoConfiguration(
            final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * kafka 消息生产者
     *
     * @return KafkaProducer
     */
    @Bean
    public KafkaProducer kafkaProducer() {
        KafkaProducer kafkaProducer = new KafkaProducer(kafkaTemplate);
        KafkaSenderFactory.put(KafkaSenderFactory.JMS_TOPIC_SENDER, kafkaProducer);
        return kafkaProducer;
    }


}
