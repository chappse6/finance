package financial.financialinformation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatorDto {

    @NotNull(message = "크리에이터 이름은 필수 값입니다.")
    @Size(min=2, message="크리에이터 이름은 최소 2 글자이어야 합니다.")
    private String creatorName;
    @NotNull(message = "크리에이터 요율은 필수 값입니다.")
    private int revenueShare;

}
