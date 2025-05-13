package ready_to_marry.searchservice.Item.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import ready_to_marry.searchservice.Item.entity.ItemDocument;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemSearchRepositoryImpl implements ItemSearchCustom{
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<ItemDocument> searchByNameOrTags(String keyword, Pageable pageable) {
        // name 필드: 부분 일치 (contains → *keyword*)
        Criteria nameCriteria = Criteria.where("name").contains(keyword);
        // tags 필드: 정확 일치 (is → ==keyword)
        Criteria tagsCriteria = Criteria.where("tags").is(keyword);

        // 둘 중 하나라도 만족하면 검색
        Criteria criteria = new Criteria().or(nameCriteria).or(tagsCriteria);

        CriteriaQuery query = new CriteriaQuery(criteria, pageable);
        SearchHits<ItemDocument> hits =
                elasticsearchOperations.search(query, ItemDocument.class);

        List<ItemDocument> content = hits.stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }

}
