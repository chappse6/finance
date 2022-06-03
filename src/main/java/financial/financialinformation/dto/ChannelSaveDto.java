package financial.financialinformation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import financial.financialinformation.domain.Channel;
import financial.financialinformation.domain.Creator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChannelSaveDto {

    private Long channelId;
    private List<CreatorSaveDto> creators;

    public ChannelSaveDto(Channel channel, List<Creator> creators) {
        this.channelId = channel.getId();
        this.creators = creators.stream()
                .map(c -> new CreatorSaveDto(c.getId()))
                .collect(Collectors.toList());
    }

}
