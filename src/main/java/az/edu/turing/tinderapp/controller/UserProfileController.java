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

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileRepository profileRepository;
    private final UserLikeRepository userLikeRepository;

    private int currentIndex = 0;

    @GetMapping("/users")
    public String getUser(Model model) {
        List<UserProfileEntity> profiles = profileRepository.findAll();
        if (profiles.isEmpty()) {
            return "no-profiles";
        }

        if (currentIndex >= profiles.size()) {
            currentIndex = 0;
        }

        UserProfileEntity current = profiles.get(currentIndex++);
        model.addAttribute("profile", current);
        return "user";
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

        userLikeRepository.save(UserLikeEntity.builder()
                .likerId(userId)
                .likedId(profileId)
                .liked(liked)
                .build());

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