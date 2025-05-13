package az.edu.turing.tinderapp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_like")
public class UserLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "liker_id", nullable = false)
    private Long likerId;

    @Column(name = "liked_id", nullable = false)
    private Long likedId;

    @Column(name = "liked", nullable = false)
    private boolean liked;
}
