package financial.financialinformation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import financial.financialinformation.controller.model.CreateRevenueRequest;
import financial.financialinformation.dto.ChannelDto;
import financial.financialinformation.dto.CreatorDto;
import financial.financialinformation.exception.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// data.sql 테스트 데이터
@Transactional
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RevenueApiControllerTest {

    private MockMvc mvc;

    private final ObjectWriter objectWriter = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).writer();

    private <T> String toJson(T obj) throws JsonProcessingException {
        return objectWriter.writeValueAsString(obj);
    }

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void 수익금액_데이터_등록() throws Exception {

        mvc.perform(post("/revenue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(CreateRevenueRequest.of(1L, BigDecimal.valueOf(300000)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.revenue_id").exists());
    }

    @Test
    void 수익금액_데이터_등록_예외_유효성검사() throws Exception {

        mvc.perform(post("/revenue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(CreateRevenueRequest.of(1L, BigDecimal.valueOf(-100000)))))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error_code").value(ExceptionCode.INVALID_INPUT_VALUE.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 수익금액_데이터_등록_예외_채널이없을경우() throws Exception {

        mvc.perform(post("/revenue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(CreateRevenueRequest.of(0L, BigDecimal.valueOf(300000)))))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error_code").value(ExceptionCode.NOT_FOUND_CHANNEL.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 채널_크리에이터_계약정보_등록() throws Exception {
        CreatorDto creatorDto1 = CreatorDto.builder().creatorName("Test Creator1").revenueShare(50).build();
        CreatorDto creatorDto2 = CreatorDto.builder().creatorName("Test Creator2").revenueShare(50).build();

        ChannelDto channelDto = ChannelDto.builder().channelName("Test Channel")
                .channelSideRevenueShare(70)
                .companySideRevenueShare(30)
                .creators(Arrays.asList(creatorDto1, creatorDto2))
                .build();

        mvc.perform(post("/revenue/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(channelDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.channel_id").exists())
                .andExpect(jsonPath("$.creator_ids").isNotEmpty());
    }

    @Test
    void 채널_크리에이터_계약정보_등록_예외_유효성검사() throws Exception {
        CreatorDto creatorDto1 = CreatorDto.builder().creatorName("Test Creator1").revenueShare(50).build();
        CreatorDto creatorDto2 = CreatorDto.builder().creatorName("Test Creator2").revenueShare(50).build();

        ChannelDto channelDto = ChannelDto.builder().channelName("")
                .channelSideRevenueShare(70)
                .companySideRevenueShare(30)
                .creators(Arrays.asList(creatorDto1, creatorDto2))
                .build();

        mvc.perform(post("/revenue/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(channelDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error_code").value(ExceptionCode.INVALID_INPUT_VALUE.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 채널_크리에이터_계약정보_등록_예외_이미존재하는채널() throws Exception {
        CreatorDto creatorDto1 = CreatorDto.builder().creatorName("Test Creator1").revenueShare(50).build();
        CreatorDto creatorDto2 = CreatorDto.builder().creatorName("Test Creator2").revenueShare(50).build();

        ChannelDto channelDto = ChannelDto.builder().channelName("youtube")
                .channelSideRevenueShare(70)
                .companySideRevenueShare(30)
                .creators(Arrays.asList(creatorDto1, creatorDto2))
                .build();

        mvc.perform(post("/revenue/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(channelDto)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error_code").value(ExceptionCode.ALREADY_EXIST_CHANNEL.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 채널_크리에이터_정산금액_조회() throws Exception {

        mvc.perform(get("/revenue/channel/{id}",1).param("date","2022-06")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.channel_id").exists())
                .andExpect(jsonPath("$.channel_name").exists())
                .andExpect(jsonPath("$.date_month").exists())
                .andExpect(jsonPath("$.total_channel_side_revenue").exists())
                .andExpect(jsonPath("$.revenue_creator_list").isNotEmpty());
    }

    @Test
    void 채널_크리에이터_정산금액_조회_예외_채널의수익이없을경우() throws Exception {

        mvc.perform(get("/revenue/channel/{id}",1).param("date","2022-04")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error_code").value(ExceptionCode.NOT_FOUND_REVENUE.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 크리에이터_정산금액_조회() throws Exception {

        mvc.perform(get("/revenue/creator/{id}",1).param("date","2022-06")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creator_id").exists())
                .andExpect(jsonPath("$.creator_name").exists())
                .andExpect(jsonPath("$.creator_revenue").exists());
    }

    @Test
    void 크리에이터_정산금액_조회_예외_크리에이터수익이없을경우() throws Exception {

        mvc.perform(get("/revenue/creator/{id}",1).param("date","2021-03")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error_code").value(ExceptionCode.NOT_FOUND_REVENUE_CREATOR.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 회사_매출_조회() throws Exception {

        mvc.perform(get("/revenue/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company_revenue_list").isNotEmpty());
    }
}