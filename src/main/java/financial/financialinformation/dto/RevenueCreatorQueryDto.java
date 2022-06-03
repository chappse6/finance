package financial.financialinformation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RevenueCreatorQueryDto {

    private Long creatorId;
    private String creatorName;
    private BigDecimal creatorRevenue;

    public RevenueCreatorQueryDto(Long creatorId, String creatorName, BigDecimal creatorRevenue) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creatorRevenue = creatorRevenue;
    }
}
