package financial.financialinformation.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import financial.financialinformation.dto.ChannelSaveDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateChannelResponse {

    private Long channelId;
    private List<CreateCreator> creatorIds;

    public CreateChannelResponse(ChannelSaveDto channelSaveDto) {
        this.channelId = channelSaveDto.getChannelId();
        this.creatorIds = channelSaveDto.getCreators().stream()
                .map(c -> new CreateCreator(c.getCreatorId()))
                .collect(Collectors.toList());
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class CreateCreator{

        private Long creatorId;

        public CreateCreator(Long creatorId) {
            this.creatorId = creatorId;
        }
    }
}
