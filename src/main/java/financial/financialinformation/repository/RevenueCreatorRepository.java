package financial.financialinformation.repository;

import financial.financialinformation.domain.RevenueCreator;
import financial.financialinformation.dto.RevenueCreatorQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RevenueCreatorRepository  extends JpaRepository<RevenueCreator, Long> {

    /**
     * 크리에이터의 월별 수익금액 합 조회
     */
    @Query("select new financial.financialinformation.dto.RevenueCreatorQueryDto(c.id, c.creatorName, " +
            "sum(r.creatorRevenue)) " +
            "from RevenueCreator r " +
            "join r.creator c " +
            "join r.revenue re " +
            "where c.channel.id = :channelId and function('date_format', re.dateRevenue, '%Y-%m') = :monthDate " +
            "group by c.id ")
    Optional<List<RevenueCreatorQueryDto>> findSumRevenueCreatorByChannelId(@Param("channelId") Long channelId, @Param("monthDate") String monthDate);

    /**
     * 크리에이터의 월별 수익금액 합 조회
     */
    @Query("select new financial.financialinformation.dto.RevenueCreatorQueryDto(c.id, c.creatorName, " +
            "sum(r.creatorRevenue)) " +
            "from RevenueCreator r " +
            "join r.creator c " +
            "join r.revenue re " +
            "where c.id = :creatorId and function('date_format', re.dateRevenue, '%Y-%m') = :monthDate " +
            "group by c.id ")
    Optional<RevenueCreatorQueryDto> findSumRevenueCreatorByCreatorId(@Param("creatorId") Long creatorId, @Param("monthDate") String monthDate);

}
