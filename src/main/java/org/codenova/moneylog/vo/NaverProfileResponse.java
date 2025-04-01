package org.codenova.moneylog.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverProfileResponse {
    private String id;
    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;
}
