package ready_to_marry.searchservice.Item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ready_to_marry.searchservice.Item.entity.ItemDocument;

public interface ItemSearchCustom {
    Page<ItemDocument> searchByNameOrTags(String keyword, Pageable pageable);
}
