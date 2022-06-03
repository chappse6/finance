package financial.financialinformation.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;

/**
 * 크리에이터 수익 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RevenueCreator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_creator_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "creator_id")
    private Creator creator;

    private BigDecimal creatorRevenue;

    //== 생성 메서드 ==//
    public static RevenueCreator createRevenueCreator(Creator creator) {
        RevenueCreator revenueCreator = new RevenueCreator();
        revenueCreator.setCreator(creator);
        return revenueCreator;
    }

    public void setRevenue(Revenue revenue) {
        this.revenue = revenue;
        setCreatorRevenueByRevenueShare(revenue.getChannelSideRevenue());
    }

    private void setCreatorRevenueByRevenueShare(BigDecimal channelSideRevenue) {
        this.creatorRevenue = channelSideRevenue
                .multiply(BigDecimal.valueOf(creator.getRevenueShare()).movePointLeft(2));
    }
}
