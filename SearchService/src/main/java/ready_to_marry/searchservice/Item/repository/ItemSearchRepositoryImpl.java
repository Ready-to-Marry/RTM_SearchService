package ready_to_marry.searchservice.Item.repository;

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

@Repository
@RequiredArgsConstructor
public class ItemSearchRepositoryImpl implements ItemSearchCustom{
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<ItemDocument> searchByNameOrTags(String keyword, Pageable pageable) {
        Criteria criteria = new Criteria()
                .or(new Criteria("name").matches(keyword))
                .or(new Criteria("tags").matches(keyword));

        CriteriaQuery query = new CriteriaQuery(criteria, pageable);

        SearchHits<ItemDocument> searchHits =
                elasticsearchOperations.search(query, ItemDocument.class);

        List<ItemDocument> content = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(content, pageable, searchHits.getTotalHits());
    }
}
