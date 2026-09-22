package com.cllg.user_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LinkAuthAccountRequest {

    @NotNull(message = "Auth user id is required")
    private Long authUserId;

}
