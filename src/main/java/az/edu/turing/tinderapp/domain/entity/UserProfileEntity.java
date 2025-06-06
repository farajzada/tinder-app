package az.edu.turing.tinderapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profiles")
public class UserProfileEntity {
    @Id
    private Long id;

    @Column(name = "user_name",nullable = false)
    private String name;

    @Column(name = "photo_url",nullable = false)
    private String photoUrl;

}
