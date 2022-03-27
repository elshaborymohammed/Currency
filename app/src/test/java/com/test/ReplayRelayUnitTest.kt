package com.test

import io.reactivex.rxjava3.observers.TestObserver

import com.jakewharton.rxrelay3.ReplayRelay
import io.reactivex.rxjava3.internal.schedulers.SingleScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit


class ReplayRelayUnitTest {

    @Test
    fun whenObserverSubscribedToReplayRelay_itReceivesEmittedEvents() {
        val replayRelay = ReplayRelay.create<Int>()
        val firstObserver = TestObserver.create<Int>()
        val secondObserver = TestObserver.create<Int>()
        replayRelay.subscribe(firstObserver)
        replayRelay.accept(5)
        replayRelay.accept(10)
        replayRelay.accept(15)
        replayRelay.subscribe(secondObserver)
        firstObserver.assertValues(5, 10, 15)
        secondObserver.assertValues(5, 10, 15)

        System.err.println(firstObserver.values().toString())
        System.err.println(secondObserver.values().toString())
    }

    @Test
    fun whenObserverSubscribedToReplayRelayWithLimitedSize_itReceivesEmittedEvents() {
        val replayRelay = ReplayRelay.createWithSize<Int>(2)
        val firstObserver = TestObserver.create<Int>()
        replayRelay.accept(5)
        replayRelay.accept(10)
        replayRelay.accept(15)
        replayRelay.accept(20)
        replayRelay.subscribe(firstObserver)
        firstObserver.assertValues(15, 20)

        System.err.println(firstObserver.values().toString())
    }

    @Test
    fun whenObserverSubscribedToReplayRelayWithMaxAge_thenItReceivesEmittedEvents() {
        val scheduler = SingleScheduler()
        val replayRelay = ReplayRelay.createWithTime<Int>(2000, TimeUnit.MILLISECONDS, scheduler)
        val current = scheduler.now(TimeUnit.MILLISECONDS)
        val firstObserver = TestObserver.create<Int>()
        replayRelay.accept(5)
        replayRelay.accept(10)
        replayRelay.accept(15)
        replayRelay.accept(20)
        Thread.sleep(3000)
        replayRelay.subscribe(firstObserver)
        firstObserver.assertEmpty()

        System.err.println(firstObserver.values().toString())
    }
}