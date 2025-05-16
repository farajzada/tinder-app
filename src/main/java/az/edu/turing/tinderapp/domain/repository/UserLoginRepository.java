package az.edu.turing.tinderapp.domain.repository;

import az.edu.turing.tinderapp.domain.entity.UserLoginEntity;
import az.edu.turing.tinderapp.domain.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, Long> {
    Optional<UserLoginEntity> findByUsername(String username);

}
