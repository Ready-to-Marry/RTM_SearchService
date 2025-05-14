package ready_to_marry.searchservice.Item.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ready_to_marry.searchservice.Item.entity.ItemDocument;


public interface ItemSearchService {
    Page<ItemDocument> search(String keyword, Pageable pageable);
}
