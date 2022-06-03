package financial.financialinformation.service;

import financial.financialinformation.domain.Channel;
import financial.financialinformation.domain.Creator;
import financial.financialinformation.domain.Revenue;
import financial.financialinformation.domain.RevenueCreator;
import financial.financialinformation.dto.*;
import financial.financialinformation.exception.ExceptionCode;
import financial.financialinformation.exception.FinancialInformationException;
import financial.financialinformation.repository.ChannelRepository;
import financial.financialinformation.repository.CreatorRepository;
import financial.financialinformation.repository.RevenueCreatorRepository;
import financial.financialinformation.repository.RevenueRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Slf4j
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class RevenueServiceTest {

    @Mock
    ChannelRepository channelRepository;

    @Mock
    CreatorRepository creatorRepository;

    @Mock
    RevenueRepository revenueRepository;

    @Mock
    RevenueCreatorRepository revenueCreatorRepository;

    @InjectMocks
    RevenueServiceImpl revenueService;

    private ChannelDto channelDto = new ChannelDto();
    private List<CreatorDto> creatorDtos = new ArrayList<>();
    private List<Creator> creators = new ArrayList<>();
    private Channel channel = new Channel();

    @BeforeEach
    void setUp() {
        CreatorDto creatorDto1 = CreatorDto.builder().creatorName("testCreator1").revenueShare(50).build();
        CreatorDto creatorDto2 = CreatorDto.builder().creatorName("testCreator2").revenueShare(50).build();
        creatorDtos = new ArrayList<>(Arrays.asList(creatorDto1, creatorDto2));

        creators = creatorDtos.stream()
                .map(c -> Creator.createCreator(c))
                .collect(Collectors.toList());

        channelDto = ChannelDto.builder()
                .channelName("testChannel")
                .channelSideRevenueShare(40).companySideRevenueShare(60)
                .creators(creatorDtos)
                .build();

        channel = Channel.createChannel(channelDto, creators);
        channel.setId(1L);

        when(channelRepository.findById(anyLong())).thenReturn(Optional.of(channel));
        when(creatorRepository.findByChannelId(anyLong())).thenReturn(Optional.of(creators));

        RevenueCreator revenueCreator = RevenueCreator.createRevenueCreator(creators.get(0));
        revenueCreator.setId(1L);

        Revenue revenue = Revenue.createRevenue(channel, BigDecimal.valueOf(100000), new ArrayList<>(Arrays.asList(revenueCreator)));
        revenue.setId(1L);

        when(revenueCreatorRepository.save(any(RevenueCreator.class)))
                .thenReturn(revenueCreator);
        when(revenueRepository.save(any(Revenue.class))).thenReturn(revenue);

    }

    @Test
    void 수익등록() {
        //given
        //when
        Long revenueId = revenueService.saveRevenue(1L, BigDecimal.valueOf(100000));

        //then
        assertThat(revenueId).isNotNull();

        verify(channelRepository).findById(anyLong());
        verify(creatorRepository).findByChannelId(anyLong());

        verify(revenueCreatorRepository, atLeastOnce()).save(any(RevenueCreator.class));
        verify(revenueRepository).save(any(Revenue.class));
    }

    @Test
    void 수익등록_예외_채널이없을경우() {
        //given
        when(channelRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> revenueService.saveRevenue(1L, BigDecimal.valueOf(100000)))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_CHANNEL);
    }

    @Test
    void 수익등록_예외_크리에이터가없을경우() {
        //given
        when(creatorRepository.findByChannelId(anyLong())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> revenueService.saveRevenue(1L, BigDecimal.valueOf(100000)))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_CREATOR);
    }

    @Test
    void 채널등록() {
        //given
        //when
        ChannelSaveDto channelSaveDto = revenueService.saveChannelContractInformation(channelDto);

        //then
        assertThat(channelSaveDto).isNotNull();

        verify(channelRepository).save(any(Channel.class));
        verify(creatorRepository, atLeastOnce()).save(any(Creator.class));
    }

    @Test
    void 채널등록_예외_이미존재하는채널() {
        //given
        when(channelRepository.findByChannelName(anyString())).thenReturn(Optional.of(channel));
        //when
        //then
        assertThatThrownBy(() -> revenueService.saveChannelContractInformation(channelDto))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.ALREADY_EXIST_CHANNEL);

    }

    @Test
    void 채널등록_예외_이미존재하는크리에이터() {
        //given
        when(creatorRepository.findByCreatorName(anyString())).thenReturn(Optional.of(creators.get(0)));
        //when
        //then
        assertThatThrownBy(() -> revenueService.saveChannelContractInformation(channelDto))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.ALREADY_EXIST_CREATOR);
    }

    @Test
    void 채널등록_예외_채널과회사_요율_합_100프로() {
        //given
        channelDto.setChannelSideRevenueShare(30);
        channelDto.setCompanySideRevenueShare(30);
        //when
        //then
        assertThatThrownBy(() -> revenueService.saveChannelContractInformation(channelDto))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_HUNDRED_CHANNEL_REVENUE_SHARE);
    }

    @Test
    void 채널등록_예외_크리에이터_요율_합_100프로() {
        //given
        creatorDtos.get(0).setRevenueShare(10);
        creatorDtos.get(1).setRevenueShare(20);
        //when
        //then
        assertThatThrownBy(() -> revenueService.saveChannelContractInformation(channelDto))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_HUNDRED_CREATORS_REVENUE_SHARE);
    }

    @Test
    void 채널_수익조회() {
        //given
        Long channelId = 1L;
        String dateMonth = "2022-05";
        when(revenueRepository.findSumRevenueById(channelId, dateMonth)).thenReturn(Optional.of(RevenueQueryDto.builder().build()));
        when(revenueCreatorRepository.findSumRevenueCreatorByChannelId(channelId, dateMonth)).thenReturn(Optional.of(Arrays.asList(RevenueCreatorQueryDto.builder().build())));
        //when
        RevenueQueryDto revenueQueryDto = revenueService.findRevenueAmountByChannelId(channelId,dateMonth);
        //then
        assertThat(revenueQueryDto).isNotNull();

        verify(revenueRepository).findSumRevenueById(channelId, dateMonth);
        verify(revenueCreatorRepository).findSumRevenueCreatorByChannelId(channelId, dateMonth);
    }

    @Test
    void 채널_수익조회_예외_채널의수익이없을경우() {
        //given
        Long channelId = 1L;
        String dateMonth = "2022-05";
        when(revenueRepository.findSumRevenueById(channelId, dateMonth)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> revenueService.findRevenueAmountByChannelId(channelId, dateMonth))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_REVENUE);
    }

    @Test
    void 채널_수익조회_예외_크리에이터수익이없을경우() {
        //given
        Long channelId = 1L;
        String dateMonth = "2022-05";
        when(revenueRepository.findSumRevenueById(channelId, dateMonth)).thenReturn(Optional.of(RevenueQueryDto.builder().build()));
        when(revenueCreatorRepository.findSumRevenueCreatorByChannelId(channelId, dateMonth)).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> revenueService.findRevenueAmountByChannelId(channelId, dateMonth))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_REVENUE_CREATOR);
    }

    @Test
    void 크리에이터_수익조회() {
        //given
        Long creatorId = 1L;
        String dateMonth = "2022-05";
        when(revenueCreatorRepository.findSumRevenueCreatorByCreatorId(creatorId, dateMonth)).thenReturn(Optional.of(RevenueCreatorQueryDto.builder().build()));
        //when
        RevenueCreatorQueryDto revenueCreatorQueryDto = revenueService.findRevenueAmountByCreatorId(creatorId,dateMonth);
        //then
        assertThat(revenueCreatorQueryDto).isNotNull();

        verify(revenueCreatorRepository).findSumRevenueCreatorByCreatorId(creatorId, dateMonth);
    }

    @Test
    void 크리에이터_수익조회_예외_크리에이터수익이없을경우() {
        //given
        Long creatorId = 1L;
        String dateMonth = "2022-05";
        when(revenueCreatorRepository.findSumRevenueCreatorByCreatorId(creatorId, dateMonth)).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> revenueService.findRevenueAmountByCreatorId(creatorId, dateMonth))
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_REVENUE_CREATOR);
    }

    @Test
    void 회사매출조회() {
        //given
        when(revenueRepository.findSumRevenueAll()).thenReturn(Optional.of(Arrays.asList(RevenueCompanyQueryDto.builder().build())));
        //when
        List<RevenueCompanyQueryDto> revenueCompanyQueryDtos = revenueService.findSalesCompany();
        //then
        assertThat(revenueCompanyQueryDtos).isNotNull();

        verify(revenueRepository).findSumRevenueAll();
    }

    @Test
    void 회사매출조회_예외_수익정보가없을경우() {
        //given
        when(revenueRepository.findSumRevenueAll()).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> revenueService.findSalesCompany())
                .isInstanceOf(FinancialInformationException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_REVENUE);
    }
}