package az.edu.turing.tinderapp.controller;

import az.edu.turing.tinderapp.domain.entity.UserEntity;
import az.edu.turing.tinderapp.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository profileRepository;

    private int currentIndex = 0;

    @GetMapping("/users")
    public String getUser(Model model) {
        List<UserEntity> profiles = profileRepository.findAll();
        if (profiles.isEmpty()) {
            return "no-profiles";
        }

        if (currentIndex >= profiles.size()) {
            currentIndex = 0;
        }

        UserEntity current = profiles.get(currentIndex++);
        model.addAttribute("profile", current);
        return "user";
    }
}
