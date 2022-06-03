package financial.financialinformation.service;

import financial.financialinformation.domain.Channel;
import financial.financialinformation.domain.Creator;
import financial.financialinformation.domain.Revenue;
import financial.financialinformation.domain.RevenueCreator;
import financial.financialinformation.dto.*;
import financial.financialinformation.exception.FinancialInformationException;
import financial.financialinformation.repository.ChannelRepository;
import financial.financialinformation.repository.CreatorRepository;
import financial.financialinformation.repository.RevenueCreatorRepository;
import financial.financialinformation.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static financial.financialinformation.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final ChannelRepository channelRepository;
    private final CreatorRepository creatorRepository;
    private final RevenueRepository revenueRepository;
    private final RevenueCreatorRepository revenueCreatorRepository;

    @Transactional
    @Override
    public Long saveRevenue(Long channelId, BigDecimal channelRevenue) {

        //엔티티 조회
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(()-> new FinancialInformationException(NOT_FOUND_CHANNEL));
        List<Creator> creators = creatorRepository.findByChannelId(channelId)
                .orElseThrow(() -> new FinancialInformationException(NOT_FOUND_CREATOR));

        //수익 엔티티 생성
        List<RevenueCreator> revenueCreators = creators.stream()
                .map(c -> RevenueCreator.createRevenueCreator(c))
                .collect(Collectors.toList());
        Revenue revenue = Revenue.createRevenue(channel, channelRevenue, revenueCreators);

        //수익 등록
        for(RevenueCreator revenueCreator : revenueCreators) {
            revenueCreatorRepository.save(revenueCreator);
        }
        return revenueRepository.save(revenue).getId();
    }

    @Transactional
    @Override
    public ChannelSaveDto saveChannelContractInformation(ChannelDto channelDto) {
        //중복 채널 검증
        validateDuplicateChannel(channelDto.getChannelName());
        //중복 크리에이터 검증
        validateDuplicateCreator(channelDto.getCreators());
        //요율의 합 100% 검증
        validateRevenueShareOneHundred(channelDto.getCompanySideRevenueShare(),
                channelDto.getChannelSideRevenueShare(), channelDto.getCreators());

        // 크리레이터 생성
        List<Creator> creators = channelDto.getCreators().stream()
                .map(c -> Creator.createCreator(c)).collect(Collectors.toList());

        // 채널 생성
        Channel channel = Channel.createChannel(channelDto, creators);

        for(Creator creator : creators) {
            creatorRepository.save(creator);
        }
        channelRepository.save(channel);

        return new ChannelSaveDto(channel, creators);
    }

    private void validateDuplicateChannel(String channelName) {
        channelRepository.findByChannelName(channelName)
                .ifPresent(c -> {
                    throw new FinancialInformationException(ALREADY_EXIST_CHANNEL);
                });
    }

    private void validateDuplicateCreator(List<CreatorDto> creatorDtos) {
        for( CreatorDto creatorDto : creatorDtos) {
            creatorRepository.findByCreatorName(creatorDto.getCreatorName())
                    .ifPresent(c -> {
                        throw new FinancialInformationException(ALREADY_EXIST_CREATOR);
                    });
        }
    }

    private void validateRevenueShareOneHundred(int companySideRevenueShare, int channelSideRevenueShare,
                                          List<CreatorDto> creators) {
        if(isOneHundredCompanyRsAndChannelRs(companySideRevenueShare, channelSideRevenueShare)) {
            throw new FinancialInformationException(NOT_HUNDRED_CHANNEL_REVENUE_SHARE);
        }
        if(isOneHundredCreatorsRs(creators)) {
            throw new FinancialInformationException(NOT_HUNDRED_CREATORS_REVENUE_SHARE);
        }
    }

    private boolean isOneHundredCompanyRsAndChannelRs(int companySideRevenueShare, int channelSideRevenueShare){
        return companySideRevenueShare + channelSideRevenueShare != 100;
    }

    private boolean isOneHundredCreatorsRs(List<CreatorDto> creators) {
        return creators.stream().map(c -> c.getRevenueShare()).mapToInt(c -> c).sum() != 100;
    }

    @Override
    public RevenueQueryDto findRevenueAmountByChannelId(Long channelId, String dateMonth) {
        RevenueQueryDto revenueQueryDto = revenueRepository.findSumRevenueById(channelId, dateMonth)
                .orElseThrow(() -> new FinancialInformationException(NOT_FOUND_REVENUE));

        List<RevenueCreatorQueryDto> revenueCreatorList = revenueCreatorRepository
                .findSumRevenueCreatorByChannelId(channelId, dateMonth)
                .orElseThrow(() -> new FinancialInformationException(NOT_FOUND_REVENUE_CREATOR));

        revenueQueryDto.setRevenueCreatorList(revenueCreatorList);

        return revenueQueryDto;
    }

    @Override
    public RevenueCreatorQueryDto findRevenueAmountByCreatorId(Long creatorId, String dateMonth) {
        return revenueCreatorRepository.findSumRevenueCreatorByCreatorId(creatorId, dateMonth)
                .orElseThrow(() -> new FinancialInformationException(NOT_FOUND_REVENUE_CREATOR));
    }

    @Override
    public List<RevenueCompanyQueryDto> findSalesCompany() {
        return revenueRepository.findSumRevenueAll()
                .orElseThrow(() -> new FinancialInformationException(NOT_FOUND_REVENUE));
    }

}
