package ready_to_marry.searchservice.common.util;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DummyDataGenerator {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DummyDataGenerator(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void generateAndSendDummyData() {
        List<String> categories = List.of("가전", "의류", "도서", "식품");
        List<String> fields = List.of("냉장고", "세탁기", "컴퓨터", "셔츠");
        List<String> regions = List.of("서울", "부산", "대구", "인천");
        List<String> tags = List.of("신혼가전", "모던", "가전", "생활");

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            String itemId = String.valueOf(i + 1);
            String partnerId = String.valueOf(random.nextInt(100) + 1); // 1~100의 랜덤 partnerId
            String category = categories.get(random.nextInt(categories.size()));
            String field = fields.get(random.nextInt(fields.size()));
            String name = "아이템 " + itemId + " " + field;
            String region = regions.get(random.nextInt(regions.size()));
            int price = random.nextInt(1000000) + 100000; // 100,000 ~ 1,100,000 사이의 가격
            String thumbnailUrl = "https://example.com/image" + itemId + ".jpg";
            List<String> itemTags = List.of(tags.get(random.nextInt(tags.size())), "기타");
            List<String> styles = List.of("모던", "빈티지");

            // tags와 styles 값을 JSON 형식에 맞게 문자열로 변환
            String tagsJson = itemTags.stream().map(tag -> "\"" + tag + "\"").collect(Collectors.joining(",", "[", "]"));
            String stylesJson = styles.stream().map(style -> "\"" + style + "\"").collect(Collectors.joining(",", "[", "]"));

            String jsonData = String.format(
                    "{\"itemId\":%s,\"partnerId\":%s,\"category\":\"%s\",\"field\":\"%s\",\"name\":\"%s\",\"createdAt\":\"2025-05-13\",\"region\":\"%s\",\"price\":%d,\"thumbnailUrl\":\"%s\",\"tags\":%s,\"styles\":%s}",
                    itemId, partnerId, category, field, name, region, price, thumbnailUrl,
                    tagsJson, stylesJson
            );

            // 카프카에 데이터 전송
            kafkaTemplate.send("items", jsonData);
        }
    }

}