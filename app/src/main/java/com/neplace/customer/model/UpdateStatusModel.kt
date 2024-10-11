package com.neplace.customer.model


data class UpdateStatusModel(val status: String)
{
    // No-argument constructor
    constructor() : this( "")
}