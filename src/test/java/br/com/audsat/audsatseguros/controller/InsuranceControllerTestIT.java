package br.com.audsat.audsatseguros.controller;

import br.com.audsat.audsatseguros.domain.Insurance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InsuranceControllerTestIT {

    private static final String INSURANCE_URI = "/insurance/budget";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getInsuranceById_When_notFound() {
        String insuranceIdNotExistent = "50";
        var uri = UriComponentsBuilder.fromUriString(INSURANCE_URI + "/{insuranceId}")
                .buildAndExpand(insuranceIdNotExistent)
                .toUri();

        ResponseEntity<Insurance> responseEntity = restTemplate.getForEntity(uri, Insurance.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void getInsuranceById() {
        String insuranceIdNotExistent = "1";
        var uri = UriComponentsBuilder.fromUriString(INSURANCE_URI + "/{insuranceId}")
                .buildAndExpand(insuranceIdNotExistent)
                .toUri();

        ResponseEntity<Insurance> responseEntity = restTemplate.getForEntity(uri, Insurance.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEntity.getBody().getId(), equalTo(1L));
        assertThat(responseEntity.getBody().getCar().getModel(), equalTo("COROLLA"));
        assertThat(responseEntity.getBody().getCustomer().getName(), equalTo("Charles Xavier"));
        assertThat(responseEntity.getBody().getQuote(), equalTo(8d));
    }

    @Test
    void deleteInsuranceById() {
        String insuranceIdNotExistent = "2";
        var uri = UriComponentsBuilder.fromUriString(INSURANCE_URI + "/{insuranceId}")
                .buildAndExpand(insuranceIdNotExistent)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }
}