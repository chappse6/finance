package financial.financialinformation.repository;

import financial.financialinformation.domain.Revenue;
import financial.financialinformation.dto.RevenueCompanyQueryDto;
import financial.financialinformation.dto.RevenueQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {

    /**
     * 채널의 월별 수익금액 합 조회
     */
    @Query("select new financial.financialinformation.dto.RevenueQueryDto(c.id, c.channelName, " +
            "sum(r.channelSideRevenue), function('date_format', r.dateRevenue, '%Y-%m')) " +
            "from Revenue r " +
            "join r.channel c " +
            "where c.id = :channelId and function('date_format', r.dateRevenue, '%Y-%m') = :monthDate " +
            "group by c.id ")
    Optional<RevenueQueryDto> findSumRevenueById(@Param("channelId") Long id, @Param("monthDate") String monthDate);

    /**
     * 전체 채널의 월별 수익금액 합 조회
     */
    @Query("select new financial.financialinformation.dto.RevenueCompanyQueryDto(" +
            "function('date_format', r.dateRevenue, '%Y-%m'), sum(r.channelRevenue), " +
            "sum(r.companySideRevenue)) " +
            "from Revenue r " +
            "join r.channel c " +
            "group by function('date_format', r.dateRevenue, '%Y-%m') " +
            "order by r.dateRevenue desc ")
    Optional<List<RevenueCompanyQueryDto>> findSumRevenueAll();
}
