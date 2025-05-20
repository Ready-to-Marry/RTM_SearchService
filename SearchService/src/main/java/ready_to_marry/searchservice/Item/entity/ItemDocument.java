package ready_to_marry.searchservice.Item.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(indexName = "items")  // Elasticsearch 인덱스 이름 설정
public class ItemDocument {

    @Id
    @Field(type = FieldType.Long, name = "item_id")
    private Long itemId;  // RDB item_id (PK)

    @Field(type = FieldType.Long, name = "partner_id")
    private Long partnerId;  // 파트너 전용 목록

    @Field(type = FieldType.Keyword)
    private String category;  // 대분류 필터

    @Field(type = FieldType.Keyword)
    private String field;  // 서브카테고리 필터

    @Field(type = FieldType.Text)
    private String name;  // 검색 대상

    @Field(type = FieldType.Date, name = "created_at")
    private String createdAt;  // 최신순 정렬/필터

    @Field(type = FieldType.Keyword)
    private String region;  // 지역

    @Field(type = FieldType.Long)
    private Long price;  // 정렬/필터

    @Field(type = FieldType.Keyword, name = "thumbnail_url")
    private String thumbnailUrl;  // 목록 썸네일

    @Field(type = FieldType.Keyword)
    private List<String> tags;  // 검색 보조 키워드

    @Field(type = FieldType.Keyword)
    private List<String> styles;  // 카테고리별 스타일 필터

}