package interview.michalwds.votingApp.repository;

import interview.michalwds.votingApp.model.Role;
import interview.michalwds.votingApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findByNameAndRoleEquals(String name, Role role);
}
