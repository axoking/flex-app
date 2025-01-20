package flex

import mu.KLogger
import mu.KotlinLogging

object Log {
    private lateinit var logger: KLogger

    fun setup() {
        logger = KotlinLogging.logger {}
    }

    fun trace(msg: String) = logger.trace(msg)
    fun debug(msg: String) = logger.debug(msg)
    fun info(msg: String) = logger.info(msg)
    fun warn(msg: String) = logger.warn(msg)
    fun error(msg: String) = logger.error(msg)
}