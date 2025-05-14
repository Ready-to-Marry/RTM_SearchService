package ready_to_marry.searchservice.Item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ready_to_marry.searchservice.Item.entity.ItemDocument;
import ready_to_marry.searchservice.Item.service.ItemSearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class ItemSearchController {

    private final ItemSearchService itemSearchService;

    @GetMapping
    public Page<ItemDocument> searchItems(
            @RequestParam String keyword,
            @RequestParam String userId,
            @PageableDefault(size = 10) Pageable pageable) {

        // redis에 검색어 저장
        itemSearchService.saveSearchTerm(userId, keyword);

        return itemSearchService.search(keyword, pageable);
    }

    @GetMapping("/history")
    public List<String> getRecentSearches(@RequestParam String userId) {
        return itemSearchService.getRecentSearches(userId);
    }
}
