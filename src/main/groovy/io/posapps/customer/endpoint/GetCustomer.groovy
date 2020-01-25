package io.posapps.customer.endpoint

import groovy.json.JsonOutput
import io.posapps.customer.auth.Auth
import io.posapps.customer.model.Customer
import io.posapps.customer.model.Device
import io.posapps.customer.model.Request
import io.posapps.customer.model.Response
import io.posapps.customer.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class GetCustomer extends Endpoint {

  int statusCode
  String body

  @Lazy
  @Autowired
  Auth auth

  @Lazy
  @Autowired
  CustomerRepository customerRepository

  @Override
  boolean route(Request request) {
    request.resourcePath() == '/' && request.httpMethod() == GET && request.queryString(DEVICE)
  }

  @Override
  Response respond(Request request) {
    def authorized = auth.authenticateUser(request?.headers()?.Authorization)

    if (authorized) {
      def customers = customerRepository.findCustomers(authorized.id)
      def customerUpdates = findCustomersUpdates(customers, request.queryString(DEVICE))
      statusCode = HttpStatus.OK.value()
      body = JsonOutput.toJson(customerUpdates)
    } else {
      statusCode = HttpStatus.FORBIDDEN.value()
      body = '{"error": "No Subscription"}'
    }

    return Response.builder().statusCode(statusCode).body(body).build()
  }

  List<Customer> findCustomersUpdates(List<Customer> customers, String deviceName) {
    def customerUpdates = customers.findAll { customer ->
      !customer.devices.find { it.name == deviceName } || customerHasChanged(deviceName, customer.devices)
    }
    // This device has made the change so it does not need to know about the change
    customerUpdates.removeIf { it.devices?.last()?.name == deviceName }

    return customerUpdates
  }

  private boolean customerHasChanged(String deviceName, Set<Device> existingDevices) {
    def currentDevice = existingDevices.find { it.name == deviceName }
    return existingDevices.find { it.event != currentDevice.event}
  }
}
