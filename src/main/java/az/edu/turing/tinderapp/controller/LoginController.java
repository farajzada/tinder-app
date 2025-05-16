package az.edu.turing.tinderapp.controller;

import az.edu.turing.tinderapp.domain.entity.UserLoginEntity;
import az.edu.turing.tinderapp.domain.entity.UserProfileEntity;
import az.edu.turing.tinderapp.domain.repository.UserLoginRepository;
import az.edu.turing.tinderapp.domain.repository.UserProfileRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserLoginRepository userLoginRepository;
    private final UserProfileRepository profileRepository;


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<UserLoginEntity> optionalUser = userLoginRepository.findByUsername(username);

        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            UserLoginEntity user = optionalUser.get();

            session.setAttribute("userId", user.getId());
            String randomAvatar = "https://api.dicebear.com/7.x/miniavs/svg?seed=" + username;

            if (!profileRepository.existsById(user.getId())) {
                UserProfileEntity profile = UserProfileEntity.builder()
                        .id(user.getId()) // User və Profile eyni ID ilə uyğun olsun
                        .name(user.getUsername())
                        .photoUrl("https://default.avatar") // istəsən random və ya default şəkil
                        .build();

                profileRepository.save(profile);
            }

            return "redirect:/users";
        }

        model.addAttribute("error", "İstifadəçi adı və ya şifrə yalnışdır.");
        return "login";
    }
}
