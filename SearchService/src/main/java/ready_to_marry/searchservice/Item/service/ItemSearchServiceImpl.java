package ready_to_marry.searchservice.Item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ready_to_marry.searchservice.Item.entity.ItemDocument;
import ready_to_marry.searchservice.Item.repository.ItemSearchRepositoryImpl;
import ready_to_marry.searchservice.common.exception.search.SearchException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemSearchServiceImpl implements ItemSearchService{

    private final RedisTemplate<String, String> redisTemplate;
    private final ListOperations<String, String> listOps;

    // 최대 최근 검색어 개수
    @Value("${search.history.limit:10}")
    private int historyLimit;

    private final ItemSearchRepositoryImpl itemSearchRepository;

    public Page<ItemDocument> search(String keyword, Pageable pageable) {
        return itemSearchRepository.searchByNameOrTags(keyword, pageable);
    }

    // 최근 검색어 저장
    public void saveSearchTerm(String userId, String searchTerm) {

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new SearchException("검색어는 비어 있을 수 없습니다.");
        }

        try {
            // Redis에서 각 사용자에 대한 최근 검색어 리스트에 추가 (최대 historyLimit개까지)
            String userSearchKey = "recent_searches:" + userId;
            listOps.leftPush(userSearchKey, searchTerm);

            // 리스트 크기가 historyLimit을 넘기면 가장 오래된 검색어 삭제
            if (listOps.size(userSearchKey) > historyLimit) {
                listOps.rightPop(userSearchKey);
            }
        } catch (Exception e) {
            throw new SearchException("검색어 저장 중 오류가 발생했습니다.");
        }
    }

    // 최근 검색어 목록 조회
    public List<String> getRecentSearches(String userId) {
        // 각 사용자의 최근 검색어 최대 historyLimit개를 가져옴
        String userSearchKey = "recent_searches:" + userId;
        List<String> result = listOps.range(userSearchKey, 0, historyLimit - 1);
        if (result.isEmpty()) {
            throw new SearchException("해당 검색어에 대한 결과가 없습니다.");
        }
        return result;
    }
}
