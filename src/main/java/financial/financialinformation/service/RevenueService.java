package financial.financialinformation.service;

import financial.financialinformation.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface RevenueService {

    /**
     * 수익 금액 데이터 등록
     */
    Long saveRevenue(Long channelId, BigDecimal amountRevenue);

    /**
     * 채널, 크리에이터 계약 정보 등록
     */
    ChannelSaveDto saveChannelContractInformation(ChannelDto channelDto);

    /**
     * 월별 채널,크리에이터 수익 금액 조회
     */
    RevenueQueryDto findRevenueAmountByChannelId(Long channelId, String dateMonth);

    /**
     * 크리에이터 정산 금액 조회
     */
    RevenueCreatorQueryDto findRevenueAmountByCreatorId(Long creatorId, String dateMonth);

    /**
     * 회사 매출 조회
     */
    List<RevenueCompanyQueryDto> findSalesCompany();

}
