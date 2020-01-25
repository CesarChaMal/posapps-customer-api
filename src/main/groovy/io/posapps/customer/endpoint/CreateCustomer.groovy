package io.posapps.customer.endpoint

import com.google.gson.Gson
import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.customer.auth.Auth
import io.posapps.customer.model.Customer
import io.posapps.customer.model.Device
import io.posapps.customer.model.Request
import io.posapps.customer.model.Response
import io.posapps.customer.repository.CustomerRepository
import io.posapps.customer.service.ApiService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

import javax.transaction.Transactional

import static io.posapps.customer.common.CustomerUtil.getCustomer

@Log4j
@Component
class CreateCustomer extends Endpoint {

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
    def customer = getCustomer(request)
    def device = request.queryString(DEVICE)
    return method == POST && customer && device
  }

  @Override
  @Transactional
  Response respond(Request request) {
    def systemUser = auth.authenticateUser(request?.headers()?.Authorization)

    if (systemUser) {
      def customer = new Gson().fromJson(request.body(), Customer) as Customer

      if (!customer.firstName || !customer.lastName || !customer.emailAddress) {
        statusCode = HttpStatus.BAD_REQUEST.value()
        body = '{"error": "firstName, lastName and emailAddress are required"}'
        return Response.builder().statusCode(statusCode).body(body).build()
      }

      def existingCustomer = customerRepository.findCustomer(customer.emailAddress, systemUser.id)
      def device = new Device(name: request.queryString(DEVICE), event: CREATED)
      if (existingCustomer){
        if (existingCustomer.devices.contains(device)) {
          statusCode = HttpStatus.BAD_REQUEST.value()
          body = '{"error": "Customer has already been created - please use an update"}'
        }
        else {
          existingCustomer.devices.add(device)
          customerRepository.save(existingCustomer)
        }
        statusCode = HttpStatus.OK.value()
        body = JsonOutput.toJson(existingCustomer)
        // need to send the customer to the users configured apis
        apiService.createCustomer(existingCustomer, systemUser)
      }
      else {
        customer.systemUserId = systemUser.id
        customer.devices = [device]
        statusCode = HttpStatus.OK.value()
        body = JsonOutput.toJson(customerRepository.save(customer))
        // need to send the customer to the users configured apis
        apiService.createCustomer(customer, systemUser)
      }

    } else {
      statusCode = HttpStatus.FORBIDDEN.value()
      body = '{"error": "No Subscription"}'
    }

    return Response.builder().statusCode(statusCode).body(body).build()
  }
}
