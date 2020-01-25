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

import static io.posapps.customer.common.CustomerUtil.getCustomer

@Log4j
@Component
class UpdateCustomer extends Endpoint {

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
    return method == PUT && customer && device
  }

  @Override
  Response respond(Request request) {
    def systemUser = auth.authenticateUser(request?.headers()?.Authorization)

    if (systemUser) {
      def customerUpdate = new Gson().fromJson(request.body(), Customer)

      if (!customerUpdate.firstName || !customerUpdate.lastName || !customerUpdate.emailAddress) {
        statusCode = HttpStatus.BAD_REQUEST.value()
        body = '{"error": "firstName, lastName and emailAddress are required"}'
        return Response.builder().statusCode(statusCode).body(body).build()
      }

      def existingCustomer = customerRepository.findCustomer(customerUpdate.emailAddress, systemUser.id)

      if (existingCustomer) {
        updateCustomerDetails(existingCustomer, customerUpdate)
        def existingDevice = existingCustomer.devices.find { it.name == request.queryString(DEVICE) }
        if (!existingDevice) {
          existingCustomer.devices.add(new Device(name: request.queryString(DEVICE), event: CREATED))
        }
        else {
          existingDevice.event = UPDATED
        }
        statusCode = 200
        body = JsonOutput.toJson(customerRepository.save(existingCustomer))
        // send customer to api
        apiService.updateCustomer(existingCustomer, systemUser)
      }
      else {
        statusCode = HttpStatus.BAD_REQUEST.value()
        body = '{\"error\": \"customer does not exist - please create customer first\"}'
      }
    }
    return Response.builder().statusCode(statusCode).body(body).build()
  }

  private void updateCustomerDetails(Customer existingCustomer, Customer customerUpdate) {
    existingCustomer.firstName = customerUpdate.firstName
    existingCustomer.lastName = customerUpdate.lastName
    existingCustomer.address = customerUpdate.address
    existingCustomer.telephone = customerUpdate.telephone
    existingCustomer.mobile = customerUpdate.mobile
  }
}
