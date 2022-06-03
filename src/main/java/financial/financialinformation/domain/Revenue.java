package financial.financialinformation.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 수익 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_id")
    private Long id;

    // 채널
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    // 수익
    private BigDecimal channelRevenue;

    // 채널 쪽 수익
    private BigDecimal channelSideRevenue;

    // 회사 쪽 수익
    private BigDecimal companySideRevenue;

    // 수익 날짜
    private LocalDate dateRevenue;

    // 크리에이터 수익
    @OneToMany(mappedBy = "revenue")
    private List<RevenueCreator> revenueCreators = new ArrayList<>();

    //==연관관계 메서드==//
    public void setChannel(Channel channel) {
        this.channel = channel;
        channel.getRevenues().add(this);
    }

    public void addRevenueCreator(RevenueCreator revenueCreator) {
        revenueCreators.add(revenueCreator);
        revenueCreator.setRevenue(this);
    }

    //== 생성 메서드 ==//
    public static Revenue createRevenue(Channel channel, BigDecimal channelRevenue
            , List<RevenueCreator> revenueCreators) {
        Revenue revenue = new Revenue();
        revenue.setChannel(channel);
        revenue.setChannelRevenue(channelRevenue);
        revenue.setChannelSideRevenueByRevenueShare(channelRevenue, channel.getChannelSideRevenueShare());
        revenue.setCompanySideRevenueByRevenueShare(channelRevenue, channel.getCompanySideRevenueShare());
        revenue.setDateRevenue(LocalDate.now());
        for (RevenueCreator revenueCreator : revenueCreators) {
            revenue.addRevenueCreator(revenueCreator);
        }
        return revenue;
    }

    public void setChannelSideRevenueByRevenueShare(BigDecimal revenue, int revenueShare) {
        this.channelSideRevenue = multiplyRevenueSharePercentage(revenue, revenueShare);
    }

    public void setCompanySideRevenueByRevenueShare(BigDecimal revenue, int revenueShare) {
        this.companySideRevenue = multiplyRevenueSharePercentage(revenue, revenueShare);
    }

    private BigDecimal multiplyRevenueSharePercentage(BigDecimal revenue, int revenueShare) {
        return revenue.multiply(BigDecimal.valueOf(revenueShare).movePointLeft(2));
    }

}
