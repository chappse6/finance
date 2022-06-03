package financial.financialinformation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChannelDto {

    @NotNull(message = "채널 이름은 필수 값입니다.")
    @Size(min=2, message="채널 이름은 최소 2 글자이어야 합니다.")
    private String channelName;
    @NotNull(message = "회사의 요율은 필수 값입니다.")
    private int companySideRevenueShare;
    @NotNull(message = "채널의 요율은 필수 값입니다.")
    private int channelSideRevenueShare;
    private List<CreatorDto> creators;


}
