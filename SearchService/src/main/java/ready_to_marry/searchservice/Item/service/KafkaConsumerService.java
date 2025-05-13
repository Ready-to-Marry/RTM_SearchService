package ready_to_marry.searchservice.Item.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ready_to_marry.searchservice.Item.entity.Item;
import ready_to_marry.searchservice.Item.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ItemRepository itemRepository;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "item", groupId = "spring")
    public void listen(Item item) {

        if (item == null || item.getItemId() == null) {
            throw new IllegalArgumentException("itemId is required");
        }

        logger.info("Received message: {}", item);
        itemRepository.save(item);
        System.out.println("Received message: " + item);
    }
}