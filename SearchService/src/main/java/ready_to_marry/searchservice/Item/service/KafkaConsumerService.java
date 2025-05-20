package ready_to_marry.searchservice.Item.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ready_to_marry.searchservice.Item.dto.ItemKafkaDTO;
import ready_to_marry.searchservice.Item.entity.ItemDocument;
import ready_to_marry.searchservice.Item.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ItemRepository itemRepository;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "items", groupId = "spring")
    public void listen(ItemKafkaDTO message) {

        if (message == null || message.getOperation() == null) {
            throw new IllegalArgumentException("itemId is required");
        }

        switch (message.getOperation()) {
            case "create":
            case "update":
                if (message.getItemId() == null) {
                    throw new IllegalArgumentException("itemId is required");
                }
                ItemDocument itemDocument = ItemDocument.builder()
                        .itemId(message.getItemId())
                        .partnerId(message.getPartnerId())
                        .category(message.getCategory())
                        .field(message.getField())
                        .name(message.getName())
                        .createdAt(message.getCreated_at())
                        .region(message.getRegion())
                        .price(message.getPrice())
                        .thumbnailUrl(message.getThumbnail_url())
                        .tags(message.getTags())
                        .styles(message.getStyles())
                        .build();

                itemRepository.save(itemDocument);
                logger.info("Saved item: {}", itemDocument);
                break;

            case "delete":
                if (message.getItemIds() == null || message.getItemIds().isEmpty()) {
                    logger.error("itemIds is required for delete: {}", message);
                    return;
                }
                System.out.println("deleting items: " + message.getItemIds());
                itemRepository.deleteAllById(message.getItemIds());
                logger.info("Deleted itemIds: {}", message.getItemIds());
                break;

            default:
                logger.warn("Unknown operation: {}", message.getOperation());
        }
    }
}