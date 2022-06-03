package financial.financialinformation.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateRevenueRequest {


    @NotNull(message = "id는 필수 값입니다.")
    private Long channelId;

    @PositiveOrZero(message = "채널 수익은 0 이상이어야 합니다.")
    private BigDecimal channelRevenue;

    public static CreateRevenueRequest of(Long channelId, BigDecimal channelRevenue) {
        CreateRevenueRequest request = new CreateRevenueRequest();
        request.channelId = channelId;
        request.channelRevenue = channelRevenue;
        return request;
    }
}
