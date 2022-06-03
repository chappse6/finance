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
public class RevenueCompanyQueryDto {

    private String dateMonth;
    private BigDecimal totalRevenue;
    private BigDecimal totalCompanySideRevenue;

    public RevenueCompanyQueryDto(String dateMonth, BigDecimal totalRevenue, BigDecimal totalCompanySideRevenue) {
        this.dateMonth = dateMonth;
        this.totalRevenue = totalRevenue;
        this.totalCompanySideRevenue = totalCompanySideRevenue;
    }

}
