package io.posapps.customer.model

class SystemUser {
  Long id
  String emailAddress
  String password
  List<Subscription> subscriptions
  List<ApiConfiguration> apiConfigurations
}
