package org.codenova.moneylog.entity;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Verification {
    private int id;
    private String token;
    private String userEmail;
    private LocalDate expiresAt;
}
