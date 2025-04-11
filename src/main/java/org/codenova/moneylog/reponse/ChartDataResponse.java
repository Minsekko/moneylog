package org.codenova.moneylog.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ChartDataResponse {
    private List<String> labels;
    private List<Long> data;
}
