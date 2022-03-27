package com.test

import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test

class PublishRelayUnitTest {

    @Test
    fun whenObserverSubscribedToPublishRelay_itReceivesEmittedEvents() {
        val publishRelay = PublishRelay.create<Int>()
        val firstObserver = TestObserver.create<Int>()
        val secondObserver = TestObserver.create<Int>()

        publishRelay.subscribe(firstObserver)
        //firstObserver.assertSubscribed()
        publishRelay.accept(5)
        publishRelay.accept(10)
        publishRelay.subscribe(secondObserver)
        //secondObserver.assertSubscribed()
        publishRelay.accept(15)
        firstObserver.assertValues(5, 10, 15)
        // second receives only the last event
        secondObserver.assertValue(15)

        System.err.println(firstObserver.values().toString())
        System.err.println(secondObserver.values().toString())
    }
}