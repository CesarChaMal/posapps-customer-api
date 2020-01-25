package io.posapps.customer.repository

import io.posapps.customer.model.Customer
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface CustomerRepository extends CrudRepository<Customer, Long> {

  @Query('from Customer where emailAddress = :emailAddress and systemUserId = :systemUserId')
  Customer findCustomer(@Param('emailAddress')String emailAddress, @Param('systemUserId')Long systemUserId)

  @Query('from Customer where systemUserId = :systemUserId')
  List<Customer> findCustomers(@Param('systemUserId') Long systemUserId)

}
