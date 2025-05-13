package az.edu.turing.tinderapp.controller;

import az.edu.turing.tinderapp.domain.entity.UserLikeEntity;
import az.edu.turing.tinderapp.domain.entity.UserProfileEntity;
import az.edu.turing.tinderapp.domain.repository.UserLikeRepository;
import az.edu.turing.tinderapp.domain.repository.UserProfileRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
}
