package financial.financialinformation.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import financial.financialinformation.dto.RevenueCompanyQueryDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompanyRevenueResponse {

    private List<RevenueCompanyQueryDto> companyRevenueList;

}
