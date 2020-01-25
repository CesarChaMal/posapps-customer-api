package io.posapps.customer.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class Status {
    String service
    String state
    String profiles
}
