package io.posapps.customer.endpoint

import io.posapps.customer.model.Request
import io.posapps.customer.model.Response

abstract class Endpoint {
    public static final DEVICE  = 'device'
    public static final EMAIL   = 'email'
    public static final CREATED = 'created'
    public static final DELETED = 'deleted'
    public static final UPDATED = 'updated'

    public static final POST    = 'POST'
    public static final GET     = 'GET'
    public static final DELETE  = 'DELETE'
    public static final PUT     = 'PUT'

    abstract boolean route(Request request)
    abstract Response respond(Request request)
}
