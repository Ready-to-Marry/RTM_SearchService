package ready_to_marry.searchservice.Item.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ready_to_marry.searchservice.Item.entity.ItemDocument;
import ready_to_marry.searchservice.Item.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ItemRepository itemRepository;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "items", groupId = "spring")
    public void listen(ItemDocument itemDocument) {

        if (itemDocument == null || itemDocument.getItemId() == null) {
            throw new IllegalArgumentException("itemId is required");
        }

        logger.info("Received message: {}", itemDocument);
        itemRepository.save(itemDocument);
        System.out.println("Received message: " + itemDocument);
    }
}