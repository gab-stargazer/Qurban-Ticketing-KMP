package org.lelestacia.qurban_ticketing.domain.background_scheduler

interface BackgroundScheduler {
    fun execute(vararg input: Any)
}
