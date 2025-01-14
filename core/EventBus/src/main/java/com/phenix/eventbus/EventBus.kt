package com.phenix.eventbus

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import java.util.function.Predicate
import kotlin.reflect.KClass

object EventBus {
    private val _eventFlow =
        MutableSharedFlow<IEvent>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val eventFlow: SharedFlow<IEvent> = _eventFlow.asSharedFlow()

    suspend fun post(event: IEvent) = _eventFlow.emit(event)

    fun tryPost(event: IEvent): Boolean = _eventFlow.tryEmit(event)

    fun subscribe(channel: String) = eventFlow.filter { it.channel == channel }

    fun <T : IEvent> subscribe(clazz: KClass<T>) = eventFlow.filterIsInstance(clazz)

    fun <T : IEvent> subscribe(channel: String, clazz: KClass<T>) =
        eventFlow.filter { it.channel == channel && clazz.isInstance(it) }

    fun subscribe(predicate: (IEvent) -> Boolean) = eventFlow.filter { predicate(it) }

    fun subscribe(
        channel: String,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        handler: suspend (IEvent) -> Unit
    ): Job {
        val job = Job()
        val scope = CoroutineScope(dispatcher + job)

        scope.launch {
            eventFlow.filter { it.channel == channel }.collect { event ->
                handler(event)
            }
        }
        return job
    }
}




