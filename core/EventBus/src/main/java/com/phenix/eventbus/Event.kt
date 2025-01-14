package com.phenix.eventbus

interface IEvent {
    val channel: String
}

open class Event<T>(override val channel: String, val data: T?): IEvent
