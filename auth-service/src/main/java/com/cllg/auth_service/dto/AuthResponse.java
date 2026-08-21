package com.cllg.auth_service.dto;

import com.cllg.auth_service.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//not required or required
public class AuthResponse {

    private Long userId;
    private String name;
    private String email;
    private Role role;
    private String token;
}
