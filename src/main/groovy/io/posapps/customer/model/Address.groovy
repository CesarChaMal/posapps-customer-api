package io.posapps.customer.model

import groovy.transform.EqualsAndHashCode
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Version
import java.sql.Timestamp

@Entity
@EqualsAndHashCode
class Address {
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
  String name
  String line1
  String line2
  String city
  String postCode
}
