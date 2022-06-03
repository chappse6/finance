package financial.financialinformation.controller;

import financial.financialinformation.controller.model.CompanyRevenueResponse;
import financial.financialinformation.controller.model.CreateChannelResponse;
import financial.financialinformation.controller.model.CreateRevenueRequest;
import financial.financialinformation.controller.model.CreateRevenueResponse;
import financial.financialinformation.dto.ChannelDto;
import financial.financialinformation.dto.ChannelSaveDto;
import financial.financialinformation.dto.RevenueCreatorQueryDto;
import financial.financialinformation.dto.RevenueQueryDto;
import financial.financialinformation.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;


@RestController
@RequestMapping("/revenue")
@Validated
@RequiredArgsConstructor
public class RevenueApiController {

    private final RevenueService revenueService;

    /**
     * 특정 유튜브 채널에 수익금액 데이터 등록
     */
    @PostMapping
    public CreateRevenueResponse saveRevenue(@RequestBody @Valid CreateRevenueRequest createRevenueRequest) {
        Long revenueId = revenueService.saveRevenue(createRevenueRequest.getChannelId(),
                createRevenueRequest.getChannelRevenue());
        return new CreateRevenueResponse(revenueId);
    }

    /**
     * 유튜브 채널과 크리에이터 계약정보 등록
     */
    @PostMapping("/channel")
    public CreateChannelResponse saveChannel(@RequestBody @Valid ChannelDto request) {
        ChannelSaveDto channelSaveDto = revenueService.saveChannelContractInformation(request);
        return new CreateChannelResponse(channelSaveDto);
    }

    /**
     * 특정 채널 수익금액과 계약정보에 따른 크리에이터 정산금액 조회
     */
    @GetMapping("channel/{id}")
    public RevenueQueryDto channelRevenue(@PathVariable("id") Long channelId,
                                          @RequestParam("date") @Pattern(regexp = "^\\d{4}-\\d{2}$") String dateMonth) {
        return revenueService.findRevenueAmountByChannelId(channelId, dateMonth);
    }

    /**
     * 특정 크리에이터 기준으로 채널별 정산금액 조회
     */
    @GetMapping("/creator/{id}")
    public RevenueCreatorQueryDto creatorRevenue(@PathVariable("id") Long creatorId,
                                                 @RequestParam("date") @Pattern(regexp = "^\\d{4}-\\d{2}$") String dateMonth) {
        return revenueService.findRevenueAmountByCreatorId(creatorId, dateMonth);
    }

    /**
     * 월별 회사 총매출 / 순매출 조회
     */
    @GetMapping("/company")
    public CompanyRevenueResponse companyRevenue() {
        return new CompanyRevenueResponse(revenueService.findSalesCompany());
    }
}
