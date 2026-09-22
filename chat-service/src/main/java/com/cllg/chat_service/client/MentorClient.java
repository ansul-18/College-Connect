package com.cllg.chat_service.client;

import com.cllg.chat_service.config.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "MENTOR-SERVICE",
        configuration = FeignAuthConfig.class
)
public interface MentorClient {

    @GetMapping("/api/mentors/access/chat")
    boolean hasChatAccess(
            @RequestParam("mentorId")
            Long mentorId
    );

    @GetMapping("/api/mentors/{mentorId}/user-id")
    Long getMentorUserId(
            @PathVariable("mentorId")
            Long mentorId
    );

    @GetMapping("/api/mentors/by-user/{userId}/id")
    Long getMentorProfileIdByUserId(
            @PathVariable("userId")
            Long userId
    );
}