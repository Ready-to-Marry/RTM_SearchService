package ready_to_marry.searchservice.common.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ready_to_marry.searchservice.Item.dto.ItemKafkaDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Bean
    public ConsumerFactory<String, ItemKafkaDTO> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094,localhost:9095,localhost:9096");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "item-group");

        // Key도 ErrorHandlingDeserializer로 래핑
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());

        // Value도 ErrorHandlingDeserializer로 래핑
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // JsonDeserializer 세부 설정
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");        // 추후 패키지 제한
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ItemKafkaDTO.class.getName());

        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * 공통 오류 처리기
     * - DeserializationException: JSON 포맷 오류 → 스킵(경고 로그)
     * - IllegalArgumentException: itemId 누락 → 스킵(에러 로그)
     * - 그 외 예외는 스킵하면서 에러 로그
     */
    @Bean
    public CommonErrorHandler commonErrorHandler() {
        FixedBackOff fixedBackOff = new FixedBackOff(0L, 0L);
        return new DefaultErrorHandler(
                // recoverer: 실패한 레코드와 예외를 받아 처리
                (ConsumerRecord<?, ?> record, Exception exception) -> {
                    // 감싸진 예외(Unwrap)
                    Throwable cause = exception;
                    if (exception instanceof ListenerExecutionFailedException) {
                        cause = exception.getCause();
                    }

                    // 삼항연산자
//                    Throwable cause = exception instanceof ListenerExecutionFailedException
//                            ? exception.getCause()
//                            : exception;

                    if (cause instanceof DeserializationException) {
                        logger.warn("역직렬화 오류, 스킵: {}", record);
                    }
                    else if (cause instanceof IllegalArgumentException) {
                        logger.warn("itemId 누락으로 스킵: {}", record);
                    }
                    else {
                        logger.error("처리 중 오류, 스킵: {}", record, exception);
                    }
                },
                fixedBackOff
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemKafkaDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, ItemKafkaDTO> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, ItemKafkaDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // CommonErrorHandler 구현체인 DefaultErrorHandler를 등록
        factory.setCommonErrorHandler(commonErrorHandler());

        return factory;
    }
}