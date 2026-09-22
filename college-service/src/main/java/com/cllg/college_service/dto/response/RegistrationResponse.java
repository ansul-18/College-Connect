package com.cllg.college_service.dto.response;

import com.cllg.college_service.enums.RegistrationStatus;
import lombok.*;
import org.bouncycastle.pqc.jcajce.interfaces.LMSKey;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponse {
    private Long id;
    private Long eventId;
    private Long studentId;

    private LocalDateTime registeredAt;

    private RegistrationStatus status;
}
