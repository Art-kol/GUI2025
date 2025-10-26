package ru.gr05307.model.math.polynomial

import ru.gr05307.model.math.neq
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sign

open class Polynomial(coeffs: Map<Int, Double>) {
    private val _coeffs: MutableMap<Int, Double>

    var coeffs: Map<Int, Double>
        get() = _coeffs
        protected set(value) {
            _coeffs.clear()
            _coeffs.putAll(value)
        }

    constructor() : this(mapOf(0 to 0.0))

    constructor(coeffs: List<Double>) : this(
        mutableMapOf<Int, Double>().also {
            coeffs.forEachIndexed { i, v ->
                it[i] = v
            }
        }
    )

    constructor(vararg coeffs: Double) : this(coeffs.toList())
    constructor(vararg coeffs: Int): this(coeffs.map { it.toDouble() })

    init{
        _coeffs = coeffs.filter{ (k, v) -> k >= 0 && v neq 0.0 }
            .toMutableMap().also{
                if (it.isEmpty()) it[0] = 0.0
            }
    }

    override fun toString() = buildString {
        val maxPow = coeffs.keys.max()
        coeffs.toSortedMap().reversed().forEach { (k, v) ->
            append( if (v.sign < 0) "-" else if (k != maxPow) "+" else "")
            append(if (v.absoluteValue neq 1.0 || k == 0) v.absoluteValue else "")
            if (k != 0) append("x")
            if (k > 1) append("^", k)
        }
    }

    operator fun plus(other: Polynomial): Polynomial {
        val result = _coeffs.toMutableMap()
        other._coeffs.forEach {
            result[it.key] = (result[it.key] ?: 0.0) + it.value
        }
        return Polynomial(result)
    }

    operator fun plusAssign(other: Polynomial) {
        other._coeffs.forEach {
            _coeffs[it.key] = (_coeffs[it.key] ?: 0.0) + it.value
        }
    }

    operator fun minus(other: Polynomial) = Polynomial(_coeffs.toMutableMap().also{ result ->
        other._coeffs.forEach {
            result[it.key] = (result[it.key] ?: 0.0) - it.value
        }
    })

    operator fun minusAssign(other: Polynomial) {
        other._coeffs.forEach {
            _coeffs[it.key] = (_coeffs[it.key] ?: 0.0) - it.value
        }
    }

    operator fun unaryMinus() = this * -1.0
    operator fun unaryPlus() = Polynomial(_coeffs)

    operator fun times(k: Double) = Polynomial(_coeffs.toMutableMap().mapValues {
        it.value * k
    })

    operator fun timesAssign(k: Double) {
        _coeffs.forEach { _coeffs[it.key] = _coeffs[it.key]!! + it.value }
    }

    operator fun times(other: Polynomial): Polynomial {
        val result = mutableMapOf<Int, Double>()
        _coeffs.forEach { (p1, v1) ->
            other._coeffs.forEach { (p2, v2) ->
                result[p1 + p2] = v1 * v2 + (result[p1 + p2] ?: 0.0)
            }
        }
        return Polynomial(result)
    }

    operator fun timesAssign(other: Polynomial) {
        val result = mutableMapOf<Int, Double>()
        _coeffs.forEach { (p1, v1) ->
            other._coeffs.forEach { (p2, v2) ->
                result[p1 + p2] = v1 * v2 + (result[p1 + p2] ?: 0.0)
            }
        }
        _coeffs.clear()
        _coeffs.putAll(result)
    }

    operator fun div(k: Double) = Polynomial(_coeffs.toMutableMap().mapValues {
        it.value / k
    })

    operator fun divAssign(k: Double) {
        _coeffs.forEach { _coeffs[it.key] = _coeffs[it.key]!! / k }
    }

    override fun equals(other: Any?) : Boolean {
        if (other !is Polynomial) return false
        return _coeffs == other._coeffs
    }

    override fun hashCode() = 17 * _coeffs.hashCode()

    operator fun invoke(x: Double) = this._coeffs.toList().fold(0.0){
            acc, coeff -> acc + coeff.second * x.pow(coeff.first.toDouble())
    }
}

operator fun Double.times(p: Polynomial) = p * this