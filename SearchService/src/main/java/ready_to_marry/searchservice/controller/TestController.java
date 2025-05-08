package ready_to_marry.searchservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.searchservice.entity.user.Users;
import ready_to_marry.searchservice.repository.TestRepository;
import ready_to_marry.searchservice.service.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class TestController {

    private final TestRepository testRepository;
    private final TestService testService;

    @GetMapping("/")
    public String index() {
        return "Health Check";
    }

    @GetMapping("/user")
    public ResponseEntity<String> callUserService() {
        String result = testService.callUserService();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<String> add() {
        Users user = Users.builder()
                .name("dummy")
                .build();
        testRepository.save(user);
        return ResponseEntity.ok("dummy add ok");
    }
}
