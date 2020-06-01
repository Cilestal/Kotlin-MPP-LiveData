package org.michaellang.livedata

import java.util.concurrent.atomic.AtomicBoolean

actual class SingleLiveEvent<T : Any> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(observer: (T) -> Unit) {
        if (hasActiveObservers()) {
            println("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe { t ->
            if (pending.compareAndSet(true, false)) {
                observer(t)
            }
        }
    }

    override fun postValue(value: T) {
        pending.set(true)
        super.postValue(value)
    }

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}