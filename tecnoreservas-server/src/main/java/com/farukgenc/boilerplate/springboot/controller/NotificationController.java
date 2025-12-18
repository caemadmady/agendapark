package com.farukgenc.boilerplate.springboot.controller;

import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationDTO;
import com.farukgenc.boilerplate.springboot.security.dto.notification.NotificationPageDTO;
import com.farukgenc.boilerplate.springboot.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotificationDTO> streamNotifications(@RequestParam Long userId) {
        return notificationService.streamNotifications(userId);
    }

    @GetMapping
    public ResponseEntity<NotificationPageDTO> getNotifications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            Pageable pageable) {
        NotificationPageDTO response = notificationService.getNotifications(status, userId, pageable);
        return ResponseEntity.ok(response);
    }
}
