package io.posapps.customer.endpoint

import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.customer.auth.Auth
import io.posapps.customer.model.Device
import io.posapps.customer.model.Request
import io.posapps.customer.model.Response
import io.posapps.customer.repository.CustomerRepository
import io.posapps.customer.service.ApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Log4j
@Component
class DeleteCustomer extends Endpoint {

  private int statusCode
  private String body

  @Lazy
  @Autowired
  CustomerRepository customerRepository

  @Lazy
  @Autowired
  Auth auth

  @Lazy
  @Autowired
  ApiService apiService

  @Override
  boolean route(Request request) {
    def method = request.httpMethod()
    def device = request.queryString(DEVICE)
    def customerEmail = request.queryString(EMAIL)
    return method == DELETE && customerEmail && device
  }

  @Override
  Response respond(Request request) {
    def systemUser = auth.authenticateUser(request?.headers()?.Authorization)

    if (systemUser) {
      log.info("Delete customer request customer: ${request.queryString(EMAIL)} systemUser: ${systemUser.id}")
      def existingCustomer = customerRepository.findCustomer(request.queryString(EMAIL), systemUser.id)

      if (existingCustomer) {
        def device = existingCustomer.devices.find { it.name == request.queryString(DEVICE) }
        device ? device.event = DELETED : existingCustomer.devices.add(new Device(name: request.queryString(DEVICE), event: DELETED))
        statusCode = 200
        body = JsonOutput.toJson(customerRepository.save(existingCustomer))
        // send delete to api
        apiService.deleteCustomer(existingCustomer, systemUser)
      }
      else {
        statusCode = 400
        body = '{"error": "customer does not exist "}'
      }
    }
    return Response.builder().statusCode(statusCode).body(body).build()
  }

}
