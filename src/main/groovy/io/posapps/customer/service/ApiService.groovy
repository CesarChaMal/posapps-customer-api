package io.posapps.customer.service

import com.google.gson.Gson
import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.customer.auth.Auth
import io.posapps.customer.model.Customer
import io.posapps.customer.model.SystemUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Log4j
@Component
class ApiService {

  @Lazy
  @Autowired
  RestTemplate restTemplate

  @Lazy
  @Autowired
  Auth auth

  @Value('${posapps.woocom.adapter.endpoint}')
  String woocomAdapter

  void createCustomer(Customer customer, SystemUser systemUser) {

    try {
      HttpHeaders headers = new HttpHeaders()
      headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(systemUser.emailAddress, systemUser.password))
      headers.add('Content-Type', 'application/json')
      HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(customer), headers)

      log.info("Create woocom customer url: $woocomAdapter request: ${JsonOutput.toJson(request)}")
      def response = restTemplate.exchange("$woocomAdapter?domain=na", HttpMethod.POST, request, String)
      log.info("Create woocom customer response: ${JsonOutput.toJson(response)}")
    }
    catch (Exception ex) {
      log.error("Error with create request to woocom adapter: $ex")
    }
  }

  void updateCustomer(Customer customer, SystemUser systemUser) {
    try {
      HttpHeaders headers = new HttpHeaders()
      headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(systemUser.emailAddress, systemUser.password))
      headers.add('Content-Type', 'application/json')
      HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(customer), headers)

      log.info("Update woocom customer url: $woocomAdapter request: ${JsonOutput.toJson(request)}")
      def response = restTemplate.exchange("$woocomAdapter?domain=na", HttpMethod.PUT, request, String)
      log.info("Update woocom customer response: ${JsonOutput.toJson(response)}")
    }
    catch (Exception ex) {
      log.error("Error with update request to woocom adapter: $ex}")
    }
  }

  void deleteCustomer(Customer customer, SystemUser systemUser) {
    try {
      HttpHeaders headers = new HttpHeaders()
      headers.add(HttpHeaders.AUTHORIZATION, auth.buildBasicAuth(systemUser.emailAddress, systemUser.password))
      headers.add('Content-Type', 'application/json')
      HttpEntity<String> request = new HttpEntity<String>(null, headers)

      log.info("Delete woocom customer url: $woocomAdapter?domain=na&email=${customer.emailAddress} request: ${JsonOutput.toJson(request)}")
      def response = restTemplate.exchange("$woocomAdapter?domain=na&email=${customer.emailAddress}", HttpMethod.DELETE, request, String)
      log.info("Delete woocom customer response: ${JsonOutput.toJson(response)}")
    }
    catch (Exception ex) {
      log.error("Error with delete request to woocom adapter: $ex}")
    }
  }
}
