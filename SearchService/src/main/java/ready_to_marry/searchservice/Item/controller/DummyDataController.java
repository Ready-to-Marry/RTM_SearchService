package ready_to_marry.searchservice.Item.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ready_to_marry.searchservice.common.util.DummyDataGenerator;

@RestController
public class DummyDataController {

    private final DummyDataGenerator dummyDataGenerator;

    public DummyDataController(DummyDataGenerator dummyDataGenerator) {
        this.dummyDataGenerator = dummyDataGenerator;
    }

    @GetMapping("/dummy")
    public String generateDummyData() {
        dummyDataGenerator.generateAndSendDummyData();
        return "더미 데이터 100개가 카프카에 전송되었습니다!";
    }
}