package ready_to_marry.searchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ready_to_marry.searchservice.entity.user.Users;

@Repository
public interface TestRepository extends JpaRepository<Users, Long> {
}
