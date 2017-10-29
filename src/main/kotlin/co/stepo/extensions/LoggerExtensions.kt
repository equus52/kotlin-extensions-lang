package co.stepo.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.companionObjectInstance

inline fun Logger.error(t: Throwable? = null, message: () -> String) {
    if (this.isErrorEnabled) {
        this.error(message(), t)
    }
}

inline fun Logger.errorForList(t: Throwable? = null, messages: () -> List<String>) {
    if (this.isErrorEnabled) {
        messages().forEach { this.error(it, t) }
    }
}

inline fun Logger.warn(t: Throwable? = null, message: () -> String) {
    if (this.isWarnEnabled) {
        this.warn(message(), t)
    }
}

inline fun Logger.warnForList(t: Throwable? = null, messages: () -> List<String>) {
    if (this.isWarnEnabled) {
        messages().forEach { this.warn(it, t) }
    }
}

inline fun Logger.info(t: Throwable? = null, message: () -> String) {
    if (this.isInfoEnabled) {
        this.info(message(), t)
    }
}

inline fun Logger.infoForList(t: Throwable? = null, messages: () -> List<String>) {
    if (this.isInfoEnabled) {
        messages().forEach { this.info(it, t) }
    }
}

inline fun Logger.debug(t: Throwable? = null, message: () -> String) {
    if (this.isDebugEnabled) {
        this.debug(message(), t)
    }
}

inline fun Logger.debugForList(t: Throwable? = null, messages: () -> List<String>) {
    if (this.isDebugEnabled) {
        messages().forEach { this.debug(it, t) }
    }
}

inline fun Logger.trace(t: Throwable? = null, message: () -> String) {
    if (this.isTraceEnabled) {
        this.trace(message(), t)
    }
}

inline fun Logger.traceForList(t: Throwable? = null, messages: () -> List<String>) {
    if (this.isTraceEnabled) {
        messages().forEach { this.trace(it, t) }
    }
}

fun Any.logger(): Logger = LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name)

fun Any.lazyLogger(): Lazy<Logger> = lazy { logger() }

fun String.logger(): Logger = LoggerFactory.getLogger(this)

inline fun <reified T : Any> logger(): Logger = LoggerFactory.getLogger(unwrapCompanionClass(T::class.java).name)

fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObjectInstance?.javaClass == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}