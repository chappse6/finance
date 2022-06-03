package financial.financialinformation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RevenueQueryDto {

    private Long channelId;
    private String channelName;

    private String dateMonth;

    private BigDecimal totalChannelSideRevenue;

    @Builder.Default
    private List<RevenueCreatorQueryDto> revenueCreatorList = new ArrayList<>();

    public RevenueQueryDto(Long channelId, String channelName, BigDecimal totalChannelSideRevenue, String dateMonth) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.totalChannelSideRevenue = totalChannelSideRevenue;
        this.dateMonth = dateMonth;
    }

}
