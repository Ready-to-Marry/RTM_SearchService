package ready_to_marry.searchservice.Item.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ready_to_marry.searchservice.Item.entity.ItemDocument;

import java.util.List;


public interface ItemSearchService {
    // 검색 기능
    Page<ItemDocument> search(String keyword, Pageable pageable);

    // 검색어 저장 (redis)
    void saveSearchTerm(String searchTerm);

    // 최근 검색어 조회 (최대 10개)
    List<String> getRecentSearches();
}
