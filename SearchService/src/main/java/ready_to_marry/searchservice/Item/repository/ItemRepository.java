package ready_to_marry.searchservice.Item.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ready_to_marry.searchservice.Item.entity.ItemDocument;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<ItemDocument, Long> {
    List<ItemDocument> findByCategory(String category);
}
