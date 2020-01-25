package io.posapps.customer.common

import com.google.gson.Gson
import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.posapps.customer.model.Customer
import io.posapps.customer.model.Request

@Log4j
class CustomerUtil {

  static Customer getCustomer(Request request) {
    Customer customer
    try {
      customer = new Gson().fromJson(request.body(), Customer)
    }
    catch (Exception e) {
      log.debug("Request does not contain a customer: ${JsonOutput.toJson(request)}")
      customer = null
    }
    return customer
  }
}
