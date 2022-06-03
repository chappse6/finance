package financial.financialinformation.domain;

import financial.financialinformation.dto.CreatorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 크리에이터 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_id")
    private Long id;

    // 크리에이터 이름
    @NotNull
    private String creatorName;

    // 채널
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    // 요율
    @NotNull
    private int revenueShare;

    // 크리에이터 수익
    @OneToMany(mappedBy = "creator")
    private List<RevenueCreator> revenueCreators = new ArrayList<>();

    //== 생성 메서드 ==//
    public static Creator createCreator(CreatorDto creatorDto) {
        Creator creator = new Creator();
        creator.setCreatorName(creatorDto.getCreatorName());
        creator.setRevenueShare(creatorDto.getRevenueShare());
        return creator;
    }
}
