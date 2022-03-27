package com.test

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test

class BehaviorRelayUnitTest {
    @Test
    fun whenObserverSubscribedToBehaviorRelay_itReceivesEmittedEvents() {
        val behaviorRelay = BehaviorRelay.create<Int>()
        val firstObserver = TestObserver.create<Int>()
        val secondObserver = TestObserver.create<Int>()

        behaviorRelay.accept(5)
        behaviorRelay.subscribe(firstObserver)
        behaviorRelay.accept(10)
        behaviorRelay.subscribe(secondObserver)
        behaviorRelay.accept(15)

        firstObserver.assertValues(5, 10, 15)
        secondObserver.assertValues(10, 15)

        System.err.println(firstObserver.values().toString())
        System.err.println(secondObserver.values().toString())
    }

    @Test
    fun whenObserverSubscribedToBehaviorRelay_itReceivesDefaultValue() {
        val behaviorRelay = BehaviorRelay.createDefault(1)
        val firstObserver = TestObserver<Int>()
        behaviorRelay.subscribe(firstObserver)
        firstObserver.assertValue(1)

        System.err.println(firstObserver.values().toString())
    }

    @Test
    fun whenObserverSubscribedToBehaviorRelayWithoutDefaultValue_itIsEmpty() {
        val behaviorRelay = BehaviorRelay.create<Int>()
        val firstObserver = TestObserver<Int>()
        behaviorRelay.subscribe(firstObserver)
        firstObserver.assertEmpty()

        System.err.println(firstObserver.values().toString())
    }
}