package az.edu.turing.tinderapp.controller;

import az.edu.turing.tinderapp.domain.entity.MessageEntity;
import az.edu.turing.tinderapp.domain.repository.MessageRepository;
import az.edu.turing.tinderapp.domain.repository.UserProfileRepository;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageRepository messageRepository;
    private final UserProfileRepository userProfileRepository;

    @GetMapping("/messages/{id}")
    public String showMessages(@PathVariable("id") Long receiverId, Model model, HttpSession session) {
        Long senderId = (Long) session.getAttribute("userId");
        if (senderId == null) {
            senderId = 1L;
            session.setAttribute("userId", senderId);
        }

        List<MessageEntity> messages = messageRepository
                .findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(senderId, receiverId, senderId, receiverId);

        model.addAttribute("messages", messages);
        model.addAttribute("receiverId", receiverId);
        model.addAttribute("userId", senderId);
        return "message";
    }

    @PostMapping("/messages/{id}")
    public String sendMessage(@PathVariable("id") Long receiverId,
                              @RequestParam("text") String text,
                              HttpSession session) {
        Long senderId = (Long) session.getAttribute("userId");
        if (senderId == null) {
            senderId = 1L;
            session.setAttribute("userId", senderId);
        }

        MessageEntity message = MessageEntity.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .text(text)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        return "redirect:/messages/" + receiverId;
    }
}
