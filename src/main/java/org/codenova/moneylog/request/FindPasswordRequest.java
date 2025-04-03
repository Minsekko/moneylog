package org.codenova.moneylog.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindPasswordRequest {
    @Email
    private String email;
}
