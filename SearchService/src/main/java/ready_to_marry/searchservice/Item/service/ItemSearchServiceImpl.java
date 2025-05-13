package ready_to_marry.searchservice.Item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ready_to_marry.searchservice.Item.entity.ItemDocument;
import ready_to_marry.searchservice.Item.repository.ItemSearchRepositoryImpl;

@Service
@RequiredArgsConstructor
public class ItemSearchServiceImpl implements ItemSearchService{
    private final ItemSearchRepositoryImpl itemSearchRepository;

    public Page<ItemDocument> search(String keyword, Pageable pageable) {
        return itemSearchRepository.searchByNameOrTags(keyword, pageable);
    }
}
