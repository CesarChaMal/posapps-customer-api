package io.posapps.customer

import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import io.posapps.customer.model.Status
import io.posapps.customer.model.Subscription
import io.posapps.customer.model.SystemUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.delete
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.put
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo

@RunWith(SpringRunner)
class CustomerApplicationTest {

  def customerApplication

  @Rule
  public WireMockRule wireMockRule = new WireMockRule()

  @Before
  void setup() {
    customerApplication = new CustomerApplication()
    initWireMock()
  }

  @Test
  void shouldGetStatusResponse() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/get-status-input.json').text) as Map
    def response = customerApplication.handleRequest(input, null)
    assert response.statusCode == 200
    assert new JsonSlurper().parseText(response.body) as Status
  }

  @Test
  void shouldCreateCustomer() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/create-customer-input.json').text) as Map
    assert input
    def response = customerApplication.handleRequest(input, null)
    assert response.statusCode == 200

    //def input2 = new JsonSlurper().parseText(getClass().getResource('/input/create-customer-2-input.json').text) as Map
    //assert input2
    //def response2 = customerApplication.handleRequest(input2, null)
    //assert response2.statusCode == 200
  }

  @Test
  void 'should Not Create Customer With Null Values'() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/null-customer-input.json').text) as Map

    assert input
    def response = customerApplication.handleRequest(input, null)

    assert response.statusCode == 400
  }


  @Test
  void 'should Not Update Customer With Null Values'() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/update-customer-null-value-input.json').text) as Map

    assert input
    def response = customerApplication.handleRequest(input, null)

    assert response.statusCode == 400
  }


  @Test
  void 'should Not Create Customer With Empty Values'() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/empty-customer-input.json').text) as Map

    assert input
    def response = customerApplication.handleRequest(input, null)

    assert response.statusCode == 400
  }

  @Test
  void shouldUpdateCustomer() {
    def createCustomerInput = new JsonSlurper().parseText(getClass().getResource('/input/create-customer-input.json').text) as Map
    customerApplication.handleRequest(createCustomerInput, null)
    def input = new JsonSlurper().parseText(getClass().getResource('/input/update-customer-input.json').text) as Map
    assert input
    def response = customerApplication.handleRequest(input, null)
    assert response.statusCode == 200
  }

  @Test
  void shouldGetCustomerUpdates() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/get-customer-updates-input.json').text) as Map
    def response = customerApplication.handleRequest(input, null)
    assert response.statusCode == 200
  }

  @Test
  void shouldDeleteCustomer() {
    def input = new JsonSlurper().parseText(getClass().getResource('/input/delete-customer-input.json').text) as Map
    def response = customerApplication.handleRequest(input, null)
    assert response.statusCode == 200
  }

  private static void initWireMock() {
    stubFor(get(urlPathEqualTo('/admin/authenticate'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody(JsonOutput.toJson(
            new SystemUser(
                    id: 1,
                    emailAddress: 'test@test.com',
                    password: 'password',
                    subscriptions: [new Subscription(name: 'CustomerApi')])))))

    stubFor(post(urlPathEqualTo('/woocom/customer'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody("")))

    stubFor(put(urlPathEqualTo('/woocom/customer'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody("")))

    stubFor(delete(urlPathEqualTo('/woocom/customer'))
            .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withBody("")))
  }
}
