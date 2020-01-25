package io.posapps.customer.model

import groovy.transform.EqualsAndHashCode
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.OrderBy
import javax.persistence.Version
import org.hibernate.validator.constraints.NotEmpty
import java.sql.Timestamp

@Entity
@EqualsAndHashCode
class Customer {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "int")
  Long id
  @CreationTimestamp
  Timestamp created
  @UpdateTimestamp
  Timestamp updated
  @Version
  Integer version
  Long systemUserId
  @NotEmpty
  String firstName
  @NotEmpty
  String lastName
  @NotEmpty
  String emailAddress
  String telephone
  String mobile
  String userName
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  Address address
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @OrderBy("updated")
  Set<Device> devices
}
