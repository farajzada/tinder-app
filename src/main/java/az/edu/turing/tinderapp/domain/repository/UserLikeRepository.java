package az.edu.turing.tinderapp.domain.repository;

import az.edu.turing.tinderapp.domain.entity.UserLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLikeEntity, Long> {
    List<UserLikeEntity> findByLikerIdAndLikedTrue(Long likerId);
    Optional<UserLikeEntity> findByLikerIdAndLikedId(Long likerId, Long likedId);
    List<UserLikeEntity> findByLikerId(Long likerId);
}
