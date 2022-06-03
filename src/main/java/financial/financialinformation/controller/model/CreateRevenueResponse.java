package financial.financialinformation.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateRevenueResponse {
    private Long revenueId;

    public CreateRevenueResponse(Long revenueId) {
        this.revenueId = revenueId;
    }
}
