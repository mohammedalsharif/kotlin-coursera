package io.allune.rationals

import java.math.BigInteger
import java.math.BigInteger.ZERO

data class Rational(var numerator: BigInteger, var denominator: BigInteger) : Comparable<Rational> {
    operator fun plus(other: Rational): Rational {
        return Rational(
            numerator.times(other.denominator) + other.numerator.times(denominator),
            denominator.times(other.denominator)
        )
    }

    operator fun minus(other: Rational): Rational {
        return Rational(
            numerator.times(other.denominator) - other.numerator.times(denominator),
            denominator.times(other.denominator)
        )
    }

    operator fun times(other: Rational): Rational {
        return Rational(numerator.times(other.numerator), denominator.times(other.denominator))
    }

    operator fun div(other: Rational): Rational {
        return Rational(numerator.times(other.denominator), denominator.times(other.numerator))
    }

    operator fun unaryMinus(): Rational {
        return Rational(-numerator, denominator)
    }

    override fun compareTo(other: Rational): Int {
        return numerator.times(other.denominator).compareTo(other.numerator.times(denominator))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Rational

        val thisN = simplify(this)
        val otherN = simplify(other)

        return thisN.numerator.toDouble().div(thisN.denominator.toDouble()) ==
                (otherN.numerator.toDouble().div(otherN.denominator.toDouble()))
    }

    override fun toString(): String {
        return when {
            denominator == 1.toBigInteger() || numerator.rem(denominator) == 0.toBigInteger() -> numerator.div(
                denominator
            ).toString()
            else -> {
                val rational = simplify(this)

                if (rational.denominator < 0.toBigInteger() || (rational.numerator < 0.toBigInteger() && rational.denominator < 0.toBigInteger())) {
                    formatRational(Rational(rational.numerator.negate(), rational.denominator.negate()))
                } else {
                    formatRational(Rational(rational.numerator, rational.denominator))
                }
            }
        }
    }

    private fun highestCommonFactor(n1: BigInteger, n2: BigInteger): BigInteger =
        if (n2 != 0.toBigInteger()) highestCommonFactor(n2, n1 % n2) else n1

    private fun simplify(rational: Rational): Rational {
        val hcf = highestCommonFactor(rational.numerator, rational.denominator).abs()
        return Rational(rational.numerator.div(hcf), rational.denominator.div(hcf))
    }

    private fun formatRational(rational: Rational): String =
        rational.numerator.toString() + "/" + rational.denominator.toString()

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

    init {
        if (denominator == ZERO) {
            throw IllegalArgumentException()
        }
    }
}

infix fun BigInteger.divBy(other: BigInteger): Rational = Rational(this, other)

infix fun Long.divBy(other: Long): Rational = Rational(toBigInteger(), other.toBigInteger())

infix fun Int.divBy(other: Int): Rational = Rational(toBigInteger(), other.toBigInteger())

fun String.toRational(): Rational {
    val values = split("/")
    return when (values.size) {
        1 -> Rational(values[0].toBigInteger(), 1.toBigInteger())
        else -> Rational(values[0].toBigInteger(), values[1].toBigInteger())
    }
}

fun main() {

    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}


