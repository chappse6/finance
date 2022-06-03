package financial.financialinformation.domain;

import financial.financialinformation.dto.ChannelDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 채널 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    // 채널명
    @NotNull
    private String channelName;

    // 회사 측 수익분배 요율
    @NotNull
    private int companySideRevenueShare;

    // 채널 측 수익분배 요율
    @NotNull
    private int channelSideRevenueShare;

    // 크리에이터
    @OneToMany(mappedBy = "channel")
    private List<Creator> creators = new ArrayList<>();

    // 수익
    @OneToMany(mappedBy = "channel")
    private List<Revenue> revenues = new ArrayList<>();

    //== 연관 관계 메서드 ==//
    public void setCreator(Creator creator) {
        creators.add(creator);
        creator.setChannel(this);
    }

    //== 생성 메서드 ==//
    public static Channel createChannel(ChannelDto channelDto, List<Creator> creators) {
        Channel channel = new Channel();
        channel.setChannelName(channelDto.getChannelName());
        channel.setCompanySideRevenueShare(channelDto.getCompanySideRevenueShare());
        channel.setChannelSideRevenueShare(channelDto.getChannelSideRevenueShare());
        for (Creator creator : creators) {
            channel.setCreator(creator);
        }
        return channel;
    }
}
