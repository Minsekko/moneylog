package org.codenova.moneylog.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverTokenResponse {

    @JsonProperty("access_token")
    private String accesstoken;

    @JsonProperty("refresh_token")
    private String refreshtoken;

    @JsonProperty("token_type")
    private String tokentype;

    @JsonProperty("expires_in")
    private int expiresin;

    private String error;

    @JsonProperty("error_description")
    private String errordescription;


}
