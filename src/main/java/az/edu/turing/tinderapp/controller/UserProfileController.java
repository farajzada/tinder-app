package az.edu.turing.tinderapp.controller;

import az.edu.turing.tinderapp.domain.entity.UserLikeEntity;
import az.edu.turing.tinderapp.domain.entity.UserProfileEntity;
import az.edu.turing.tinderapp.domain.repository.UserLikeRepository;
import az.edu.turing.tinderapp.domain.repository.UserProfileRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileRepository profileRepository;
    private final UserLikeRepository userLikeRepository;

    private int currentIndex = 0;

    @GetMapping("/users")
    public String getUser(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<UserProfileEntity> allProfiles = profileRepository.findAll();
        List<UserLikeEntity> likes = userLikeRepository.findByLikerId(userId);

        Set<Long> alreadySeen = likes.stream()
                .map(UserLikeEntity::getLikedId)
                .collect(Collectors.toSet());

        // ✅ Öz profilini göstərməmək üçün filtrə əlavə:
        Optional<UserProfileEntity> nextProfile = allProfiles.stream()
                .filter(p -> !alreadySeen.contains(p.getId()))
                .filter(p -> !p.getId().equals(userId)) // ⛔ özünü çıxar
                .findFirst();

        if (nextProfile.isPresent()) {
            model.addAttribute("profile", nextProfile.get());
            return "user";
        } else {
            return "redirect:/liked";
        }
    }

    @PostMapping("/users")
    public String handleUserChoice(@RequestParam("profileId") Long profileId,
                                   @RequestParam("liked") boolean liked,
                                   HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            userId = 1L;
            session.setAttribute("userId", userId);
        }

        Optional<UserLikeEntity> existingLike = userLikeRepository.findByLikerIdAndLikedId(userId, profileId);

        if (existingLike.isPresent()) {
            UserLikeEntity likeEntity = existingLike.get();
            likeEntity.setLiked(liked);
            userLikeRepository.save(likeEntity);
        } else {
            userLikeRepository.save(UserLikeEntity.builder()
                    .likerId(userId)
                    .likedId(profileId)
                    .liked(liked)
                    .build());
        }

        return "redirect:/users";
    }

    @GetMapping("/liked")
    public String showLikedProfiles(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            userId = 1L;
            session.setAttribute("userId", userId);
        }

        List<UserLikeEntity> likes = userLikeRepository.findByLikerIdAndLikedTrue(userId);

        List<UserProfileEntity> likedProfiles = likes.stream()
                .map(like -> profileRepository.findById(like.getLikedId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        model.addAttribute("likedProfiles", likedProfiles);
        return "liked";
    }

}