package com.capgemini.spacex.capsules;

import com.capgemini.spacex.capsules.client.models.CapsuleResponse;
import com.capgemini.spacex.capsules.repositories.CapsuleRepository;
import com.capgemini.spacex.capsules.server.v1.api.ApiWorker;
import com.capgemini.spacex.capsules.server.v1.models.Capsule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @MockBean
    private CapsuleRepository mockedRepository;

    @MockBean
    private RestTemplate mockedRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ApiWorker worker;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testGetAllCapsules() throws Exception {
        // When / Then
        when(mockedRepository.findAll()).thenReturn(TestUtils.createMockCapsuleList());

        // Perform
        MvcResult mvcResult = mockMvc.perform(get("/capsules")).andReturn();

        var capsuleList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Capsule>>() {
                });

        // Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(2, capsuleList.size());
        assertEquals(2, capsuleList.get(0).getMissions().size());
        assertEquals("Test Mission", capsuleList.get(0).getMissions().get(0).getName());
    }

    @Test
    public void testGetAllCapsulesEmptyResponse() throws Exception {
        // When / Then
        when(mockedRepository.findAll()).thenReturn(new ArrayList<>());

        // Perform
        MvcResult mvcResult = mockMvc.perform(get("/capsules")).andReturn();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetCapsuleResponseIfRepositoryNotEmpty() throws Exception {
        // When / Then
        when(mockedRepository.findById(eq("123"))).thenReturn(Optional.of(TestUtils.createMockCapsule()));

        // Perform
        MvcResult mvcResult = mockMvc.perform(get("/capsules/123")).andReturn();

        var capsule = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Capsule.class);

        // Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("C103", capsule.getCapsuleSerial());

        verify(mockedRestTemplate, times(0)).getForObject(anyString(), eq(CapsuleResponse.class), eq("123"));
    }

    @Test
    public void testGetCapsuleResponseIfRepositoryEmpty() throws Exception {
        // When / Then
        // Simulates empty response from in-memory store on the first call, and a populated object on the second call
        when(mockedRepository.findById(eq("123"))).thenReturn(Optional.empty()).thenReturn(Optional.of(TestUtils.createMockCapsule()));

        // Mocks out the SpaceX Client
        when(mockedRestTemplate.getForObject(anyString(), eq(CapsuleResponse.class), eq("123")))
                .thenReturn(TestUtils.createMockClientCapsuleResponse());

        // Perform
        MvcResult mvcResult = mockMvc.perform(get("/capsules/123")).andReturn();

        var capsule = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Capsule.class);

        // Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("C103", capsule.getCapsuleSerial());
        assertEquals(2, capsule.getMissions().size());
        assertEquals("Test Mission", capsule.getMissions().get(0).getName());

        verify(mockedRestTemplate, times(1)).getForObject(anyString(), eq(CapsuleResponse.class), eq("123"));
    }

    @Test
    public void testGetCapsuleResponseIfRepositoryEmptyAndClientFails() throws Exception {
        // When / Then
        when(mockedRepository.findById(eq("123"))).thenReturn(Optional.empty());

        // Mocks out the SpaceX Client
        when(mockedRestTemplate.getForObject(anyString(), eq(CapsuleResponse.class), eq("123")))
                .thenThrow(new RestClientException("Could not call SpaceX REST API"));

        // Perform
        MvcResult mvcResult = mockMvc.perform(get("/capsules/123")).andReturn();

        // Assert
        assertEquals(HttpStatus.PRECONDITION_FAILED.value(), mvcResult.getResponse().getStatus());

        verify(mockedRestTemplate, times(1)).getForObject(anyString(), eq(CapsuleResponse.class), eq("123"));
    }

    @Test
    public void testCreateCapsule() throws Exception {
        // When / Then
        when(mockedRestTemplate.getForObject(anyString(), eq(CapsuleResponse.class), eq("123")))
                .thenReturn(TestUtils.createMockClientCapsuleResponse());

        // Perform
        MvcResult mvcResult = mockMvc.perform(post("/capsules/123")).andReturn();

        // Assert
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());

        verify(mockedRestTemplate, times(1)).getForObject(anyString(), eq(CapsuleResponse.class), eq("123"));
    }

    @Test
    public void testCreateCapsuleApiFailure() throws Exception {
        // When / Then
        when(mockedRestTemplate.getForObject(anyString(), eq(CapsuleResponse.class), eq("123")))
                .thenThrow(new RestClientException("Could not call SpaceX REST API"));

        // Perform
        MvcResult mvcResult = mockMvc.perform(post("/capsules/123")).andReturn();

        // Assert
        assertEquals(HttpStatus.PRECONDITION_FAILED.value(), mvcResult.getResponse().getStatus());

        verify(mockedRestTemplate, times(1)).getForObject(anyString(), eq(CapsuleResponse.class), eq("123"));
    }

    @Test
    public void testDeleteCapsuleWithEntityPresent() throws Exception {
        // When / Then
        when(mockedRepository.findById(eq("123"))).thenReturn(Optional.of(TestUtils.createMockCapsule()));

        // Perform
        MvcResult mvcResult = mockMvc.perform(delete("/capsules/123")).andReturn();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());

        verify(mockedRepository, times(1)).deleteById(eq("123"));
    }

    @Test
    public void testDeleteCapsuleWithoutEntityPresent() throws Exception {
        // When / Then
        when(mockedRepository.findById(eq("123"))).thenReturn(Optional.empty());

        // Perform
        MvcResult mvcResult = mockMvc.perform(delete("/capsules/123")).andReturn();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
        verify(mockedRepository, times(0)).deleteById(eq("123"));
    }
}
