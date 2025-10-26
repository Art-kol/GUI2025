package ru.gr05307.model.math.polynomial

import ru.gr05307.model.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()
    private var remember_div_diffs: DoubleArray = doubleArrayOf()
    private var remember_basis_diffs: MutableList<Polynomial> = mutableListOf()
    private var remember_poly: Polynomial = Polynomial()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }
        reconstruct()
    }

    private fun reconstruct() {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
            remember_div_diffs = doubleArrayOf()
            remember_basis_diffs.clear()
            remember_poly = Polynomial()
        } else {
            val (diffs, basis) = construct_by_basis(dots)
            remember_div_diffs = diffs.toDoubleArray()
            remember_basis_diffs = basis.toMutableList()
            remember_poly = build_poly_with_basis(diffs, basis)
            coeffs = remember_poly.coeffs
        }
    }

    private fun construct_by_basis(pts: Map<Double, Double>): Pair<List<Double>, List<Polynomial>> {
        val diffs = divided_diffs(pts)
        val basis = buildremember_basis_diffs(pts)
        return Pair(diffs, basis)
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
        val n = points.size
        val keys = points.keys.toDoubleArray()
        val values = keys.map { points[it]!! }.toDoubleArray()

        val temp_div_diffs = DoubleArray(n) { i -> values[i] }

        for (j in 1 until n) {
            for (i in n - 1 downTo j) {
                temp_div_diffs[i] = (temp_div_diffs[i] - temp_div_diffs[i - 1]) / (keys[i] - keys[i - j])
            }
        }

        return temp_div_diffs.toList()
    }

    private fun buildremember_basis_diffs(points: Map<Double, Double>): List<Polynomial> {
        val keys = points.keys.toList()
        val basis = mutableListOf<Polynomial>()
        var curr_basis = Polynomial(1.0)

        for (i in 0 until points.size) {
            basis.add(curr_basis)
            if (i < points.size - 1) {
                curr_basis = curr_basis * Polynomial(-keys[i], 1.0)
            }
        }

        return basis
    }

    private fun build_poly_with_basis(diffs: List<Double>, basis: List<Polynomial>): Polynomial {
        var result = Polynomial()
        for (i in diffs.indices) {
            result = result + basis[i] * diffs[i]
        }
        return result
    }

    fun addDot(x: Double, y: Double){
        if (dots.containsKey(x)) {
            throw Exception("Everything is bad!!!")
        }

        dots[x] = y
        if (dots.size == 1) {
            remember_div_diffs = doubleArrayOf(y)
            remember_basis_diffs.clear()
            remember_basis_diffs.add(Polynomial(1.0))
            remember_poly = Polynomial(y)
            coeffs = remember_poly.coeffs
        } else {
            update_poly(x, y)
        }
    }

    private fun update_poly(newX: Double, newY: Double) {
        val newSize = dots.size
        val keys = dots.keys.toDoubleArray()

        update_div_diffs(newX, newY, keys)

        update_basis_diffs(newX, keys)

        remember_poly = remember_poly + remember_basis_diffs[newSize - 1] * remember_div_diffs[newSize - 1]

        coeffs = remember_poly.coeffs
    }

    private fun update_div_diffs(newX: Double, newY: Double, keys: DoubleArray) {
        val n = dots.size
        val newremember_div_diffs = DoubleArray(n)

        for (i in 0 until n - 1) {
            newremember_div_diffs[i] = remember_div_diffs[i]
        }
        newremember_div_diffs[n - 1] = newY

        for (j in 1 until n) {
            for (i in n - 1 downTo j) {
                newremember_div_diffs[i] = (newremember_div_diffs[i] - newremember_div_diffs[i - 1]) / (keys[i] - keys[i - j])
            }
        }

        remember_div_diffs = newremember_div_diffs
    }

    private fun update_basis_diffs(newX: Double, keys: DoubleArray) {
        if (remember_basis_diffs.size < dots.size) {
            val lastBasis = remember_basis_diffs.last()
            val newBasis = lastBasis * Polynomial(-keys[remember_basis_diffs.size - 1], 1.0)
            remember_basis_diffs.add(newBasis)
        }
    }

    fun removeDot(x: Double) {
        if (dots.containsKey(x)) {
            dots.remove(x)
            reconstruct()
            //return true
        }
        else{
            throw Exception("Everything is bad!!!")
        }
        //return false
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()
}

/* package ru.smak.math.polynomial

import ru.smak.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()
    private var remember_div_diffs: List<Double> = emptyList()
    private var basis_for_pol: List<Polynomial> = emptyList()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }
        reconstruct()
    }

    private fun reconstruct() {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
            remember_div_diffs = emptyList()
            basis_for_pol = emptyList()
        } else {
            val (diffs, basis) = construct_with_basis(dots)
            remember_div_diffs = diffs
            basis_for_pol = basis
            coeffs = (diffs.indices).fold(Polynomial()) { acc, i ->
                acc + diffs[i] * basis[i]
            }.coeffs
        }
    }

    private fun construct_with_basis(pts: Map<Double, Double>): Pair<List<Double>, List<Polynomial>> {
        val d_ds = divided_diffs(pts)
        val basis = List(pts.size) { i -> product_of_xs(pts, i) }
        return Pair(d_ds, basis)
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
        val n = points.size
        val keys = points.keys.toList()
        val values = keys.map { points[it]!! }

        val temp_div_diffs = Array(n) { DoubleArray(n) { 0.0 } }

        for (i in 0 until n) {
            temp_div_diffs[i][0] = values[i]
        }

        for (j in 1 until n) {
            for (i in 0 until n - j) {
                temp_div_diffs[i][j] = (temp_div_diffs[i + 1][j - 1] - temp_div_diffs[i][j - 1]) /
                        (keys[i + j] - keys[i])
            }
        }

        return List(n) { temp_div_diffs[0][it] }
    }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        val keys = points.keys.toList()
        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-keys[i], 1.0)
        }
    }

    fun addDot(x: Double, y: Double): Boolean {
        if (dots.containsKey(x)) {
            return false
        } else {
            dots[x] = y
            updateWithNewDot(x, y)
            return true
        }
    }

    private fun updateWithNewDot(x: Double, y: Double) {
        if (dots.size == 1) {
            remember_div_diffs = listOf(y)
            basis_for_pol = listOf(Polynomial(1.0))
            coeffs = mapOf(0 to y)
        } else {
            val newremember_div_diffs = updateremember_div_diffs(x, y)

            val newBasis = updatebasis_for_pol(x)

            val newPoly = Polynomial(coeffs) + newremember_div_diffs.last() * newBasis.last()

            remember_div_diffs = newremember_div_diffs
            basis_for_pol = newBasis
            coeffs = newPoly.coeffs
        }
    }

    private fun updateremember_div_diffs(newX: Double, newY: Double): List<Double> {
        val n = dots.size - 1
        val keys = dots.keys.toList()
        val newremember_div_diffs = remember_div_diffs.toMutableList()

        newremember_div_diffs.add(newY)

        for (j in 1 until n) {
            var current = newremember_div_diffs[j]
            for (i in j downTo 1) {
                val prev = newremember_div_diffs[i - 1]
                newremember_div_diffs[i] = (current - prev) / (keys[n] - keys[i - 1])
                current = prev
            }
            newremember_div_diffs[0] = (newremember_div_diffs[1] - newremember_div_diffs[0]) / (keys[n] - keys[0])
        }

        return newremember_div_diffs
    }

    private fun updatebasis_for_pol(newX: Double): List<Polynomial> {
        val newBasis = basis_for_pol.toMutableList()
        if (newBasis.isEmpty()) {
            newBasis.add(Polynomial(1.0))
        } else {
            val lastBasis = newBasis.last()
            val keys = dots.keys.toList()
            val newPoly = lastBasis * Polynomial(-keys[newBasis.size - 1], 1.0)
            newBasis.add(newPoly)
        }
        return newBasis
    }

    fun removeDot(x: Double): Boolean {
        if (dots.containsKey(x)) {
            dots.remove(x)
            reconstruct()
            return true
        } else {
            return false
        }
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()
} */


/* package ru.smak.math.polynomial

import ru.smak.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()
    private var remember_div_diffs = mutableListOf<MutableList<Double>>()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }
        reconstruct()
    }

    private fun reconstruct(fromAdd: Boolean = false) {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
            remember_div_diffs.clear()
        } else {
            coeffs = construct(dots, fromAdd).coeffs
        }
    }

    private fun construct(pts: Map<Double, Double>, fromAdd: Boolean = false): Polynomial {
        val d_ds = divided_diffs(pts, fromAdd)

        return d_ds.indices.fold(Polynomial()) { acc, i ->
            acc + d_ds[i] * product_of_xs(pts, i)
        }
    }

    private fun divided_diffs(points: Map<Double, Double>, fromAdd: Boolean = false): List<Double> {
        if (fromAdd && remember_div_diffs.isNotEmpty() && remember_div_diffs.size == points.size - 1) {
            val sortedKeys = points.keys.toList()
            val newX = sortedKeys.last()
            val newY = points[newX]!!
            val n = points.size

            val newRow = MutableList(n) { 0.0 }
            newRow[0] = newY
            remember_div_diffs.add(newRow)

            for (i in 0 until remember_div_diffs.size - 1) {
                while (remember_div_diffs[i].size < n) {
                    remember_div_diffs[i].add(0.0)
                }
            }

            for (j in 1 until n) {
                for (i in 0 until n - j) {
                    remember_div_diffs[i][j] = (remember_div_diffs[i + 1][j - 1] - remember_div_diffs[i][j - 1]) /
                            (sortedKeys[i + j] - sortedKeys[i])
                }
            }

            return List(n) { remember_div_diffs[0][it] }
        }
        else {
            val n = points.size
            val s_keys = points.keys.toList()
            val values = s_keys.map { points[it]!! }

            remember_div_diffs.clear()

            for (i in 0 until n) {
                val row = MutableList(n) { 0.0 }
                row[0] = values[i]
                remember_div_diffs.add(row)
            }

            for (j in 1 until n) {
                for (i in 0 until n - j) {
                    remember_div_diffs[i][j] = (remember_div_diffs[i + 1][j - 1] - remember_div_diffs[i][j - 1]) /
                            (s_keys[i + j] - s_keys[i])
                }
            }

            return List(n) { remember_div_diffs[0][it] }
        }
    }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        val s_keys = points.keys.toList()
        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-s_keys[i], 1.0)
        }
    }

    fun addDot(x: Double, y: Double): Boolean {
        if (dots.containsKey(x)) {
            return false
        } else {
            dots[x] = y
            reconstruct(true)
            return true
        }
    }

    fun removeDot(x: Double): Boolean {
        if (dots.containsKey(x)) {
            dots.remove(x)
            remember_div_diffs.clear()
            reconstruct()
            return true
        } else {
            return false
        }
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()
} */

/*package ru.smak.math.polynomial

import ru.smak.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }
        reconstruct()
    }

    private fun reconstruct() {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
        } else {
            coeffs = construct(dots).coeffs
        }
    }

    private fun construct(pts: Map<Double, Double>): Polynomial {
        val d_ds = divided_diffs(pts)
        return d_ds.indices.fold(Polynomial()) { acc, i ->
            acc + d_ds[i] * product_of_xs(pts, i)
        }
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
        val n = points.size
        // Берем ключи в произвольном порядке (как они хранятся в Map)
        val keys = points.keys.toList()
        val values = keys.map { points[it]!! }

        val temp_div_diffs = Array(n) { DoubleArray(n) { 0.0 } }

        for (i in 0 until n) {
            temp_div_diffs[i][0] = values[i]
        }

        for (j in 1 until n) {
            for (i in 0 until n - j) {
                temp_div_diffs[i][j] = (temp_div_diffs[i + 1][j - 1] - temp_div_diffs[i][j - 1]) /
                        (keys[i + j] - keys[i])
            }
        }

        return List(n) { temp_div_diffs[0][it] }
    }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        // Берем ключи в том же порядке, что и в divided_diffs
        val keys = points.keys.toList()
        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-keys[i], 1.0)
        }
    }

    fun addDot(x: Double, y: Double): Boolean {
        if (dots.containsKey(x)) {
            return false
        } else {
            dots[x] = y
            reconstruct()
            return true
        }
    }

    fun removeDot(x: Double): Boolean {
        if (dots.containsKey(x)) {
            dots.remove(x)
            reconstruct()
            return true
        } else {
            return false
        }
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()
} */

/* package ru.smak.math.polynomial

import ru.smak.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }

        reconstruct()
    }

    private fun reconstruct() {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
        } else {
            coeffs = construct(dots).coeffs
        }
    }

    private fun construct(pts: Map<Double, Double>): Polynomial {
        val d_ds = divided_diffs(pts)

        return d_ds.indices.fold(Polynomial()) { acc, i ->
            acc + d_ds[i] * product_of_xs(pts, i)
        }
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
            val n = points.size
            val s_keys = points.keys.sorted()
            val values = s_keys.map { points[it]!! }

            val temp_div_diffs = Array(n) { DoubleArray(n) { 0.0 } }

            for (i in 0 until n) {
                temp_div_diffs[i][0] = values[i]
            }

            for (j in 1 until n) {
                for (i in 0 until n - j) {
                    temp_div_diffs[i][j] = (temp_div_diffs[i + 1][j - 1] - temp_div_diffs[i][j - 1]) / (s_keys[i + j] - s_keys[i])
                }
            }


            return List(n) { temp_div_diffs[0][it] }
        }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        val s_keys = points.keys.sorted()
        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-s_keys[i], 1.0)
        }
    }

    fun addDot(x: Double, y: Double) : Boolean {
        if (dots.containsKey(x)) {
            return false
        }
        else{
            dots[x] = y
            reconstruct()
            return true
        }
    }

    fun removeDot(x: Double): Boolean {
        if (dots.containsKey(x)) {
            dots.remove(x)
            reconstruct()
            return true
        } else {
            return false
        }
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()
} */


/* package ru.smak.math.polynomial

import ru.smak.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }

        reconstruct()
    }

    private fun reconstruct() {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
        } else {
            coeffs = construct(dots).coeffs
        }
    }

    private fun construct(pts: Map<Double, Double>): Polynomial {
        val d_ds = divided_diffs(pts)

        return d_ds.indices.fold(Polynomial()) { acc, i ->
            acc + d_ds[i] * product_of_xs(pts, i)
        }
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
        val n = points.size
        val differences = mutableListOf<Double>()

        val s_keys = points.keys.sorted()
        val values = s_keys.map { points[it]!! }

        val tempDiffs = values.toMutableList()
        differences.add(tempDiffs[0])

        for (k in 1 until n) {
            for (i in 0 until n - k) {
                tempDiffs[i] = (tempDiffs[i + 1] - tempDiffs[i]) / (s_keys[i + k] - s_keys[i])
            }
            differences.add(tempDiffs[0])
        }

        return differences
    }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        val s_keys = points.keys.sorted()

        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-s_keys[i], 1.0)
        }
    }

    fun addDot(x: Double, y: Double) : Boolean {
        if (dots.containsKey(x)) {
            return false
        }
        else{
            dots[x] = y
            reconstruct()
            return true
        }

    }

    fun removeDot(x: Double): Boolean {
        if (dots.containsKey(x)) {
            dots.remove(x)
            reconstruct()
            return true

        } else {
            return false
        }
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()
} */

/* class Newton(private val points: Map<Double, Double>) : Polynomial() {

    private val dots = mutableMapOf<Double, Double>()

    init {
        coeffs = construct(points).coeffs
    }

    fun getPoints(): Map<Double, Double> = dots.toMap()

    constructor() : super()                                     // Вызов родительского конструктора

    constructor(points: Map<Double, Double>) : super() {
        dots.putAll(points)
        reconstruct()
    }

    private fun construct(pts: Map<Double, Double>): Polynomial {
        val d_ds = divided_diffs(pts)

        return d_ds.indices.fold(Polynomial()) { acc, i ->
            acc + d_ds[i] * product_of_xs(pts, i)
        }
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
        val n = points.size
        val differences = mutableListOf<Double>()

        val s_keys = points.keys.sorted()
        val values = s_keys.map { points[it]!! }

        val tempDiffs = values.toMutableList()
        differences.add(tempDiffs[0])

        for (k in 1 until n) {
            for (i in 0 until n - k) {
                tempDiffs[i] = (tempDiffs[i + 1] - tempDiffs[i]) / (s_keys[i + k] - s_keys[i])
            }
            differences.add(tempDiffs[0])
        }

        return differences
    }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        val s_keys = points.keys.sorted()

        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-s_keys[i], 1.0)
        }
    }
} */


/*
class Newton (private val points: Map<Double, Double>) : Polynomial() {
    init{
        coeffs = points.keys.fold(Polynomial()){acc, v ->
            acc + points[v]!! * fundamental(v)
        }.coeffs
    }

    private fun divided_difference(pts: Map<Double, Double>): List<Polynomial> {

        // Раскомментить, если захотим хранить все f(x1), f(x1, x2), ...
        //val d_ds = mutableListOf<Polynomial>() // Сохраняем f(x1,x2,...,xn)
        // !!! ПРИСВОИТЬ НАЧАЛЬНЫЕ ЗНАЧЕНИЯ КАК У y-ков
        val n = pts.keys.size // Сохраняем размер приходных точек
        val d_ds = mutableListOf<Polynomial>() // Храним ответ
        // !!! Добавить 1 значение как у y1

        val s_keys = pts.keys.sorted()
        val values = s_keys.map { pts[it]!! }


        val keys_to_x = mutableListOf<Double>() // Список x до xi

        val temp_ds = mutableListOf<Polynomial>().apply {
            addAll(values)
        }
        // Сами частные суммы

        for (x in pts.keys) {
            keys_to_x.add(x)                    // Добавляем xi
            val k = keys_to_x.size // Какое кол-во переменных мы обрабатываем на итерации


            // Расчет всех частичных сумм от i переменных для будующей итерации
            for (i in 0 until n + 1 - k) {
                temp_ds[i] = (temp_ds[i] - temp_ds[i + 1]) / (pts.keys[i] - pts.keys[i + k])
            }
            drop last element in list(temp_ds)
            add to d_ds temp_ds[0]
        }

        return d_ds
    }
} */

/* package ru.smak.math.polynomial

import ru.smak.math.neq

class Newton : Polynomial {

    private val dots = mutableMapOf<Double, Double>()
    private var remember_div_diffs: MutableList<Double> = mutableListOf()
    private var sortedX: List<Double> = listOf()

    constructor() : super()

    constructor(p: Map<Double, Double>) : super() {
        dots.clear()
        p.entries.forEach { entry ->
            dots[entry.key] = entry.value
        }

        reconstruct()
    }

    private fun reconstruct() {
        if (dots.isEmpty()) {
            coeffs = mapOf(0 to 0.0)
            remember_div_diffs.clear()
            sortedX = listOf()
        } else {
            coeffs = construct(dots).coeffs
        }
    }

    private fun construct(pts: Map<Double, Double>): Polynomial {
        val d_ds = divided_diffs(pts)
        remember_div_diffs = d_ds.toMutableList()
        sortedX = pts.keys.sorted()

        return d_ds.indices.fold(Polynomial()) { acc, i ->
            acc + d_ds[i] * product_of_xs(pts, i)
        }
    }

    private fun divided_diffs(points: Map<Double, Double>): List<Double> {
        val n = points.size
        val differences = mutableListOf<Double>()

        val s_keys = points.keys.sorted()
        val values = s_keys.map { points[it]!! }

        val tempDiffs = values.toMutableList()
        differences.add(tempDiffs[0])

        for (k in 1 until n) {
            for (i in 0 until n - k) {
                tempDiffs[i] = (tempDiffs[i + 1] - tempDiffs[i]) / (s_keys[i + k] - s_keys[i])
            }
            differences.add(tempDiffs[0])
        }

        return differences
    }

    private fun product_of_xs(points: Map<Double, Double>, k: Int): Polynomial {
        val s_keys = points.keys.sorted()

        return (0 until k).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-s_keys[i], 1.0)
        }
    }

    fun addDot(x: Double, y: Double) : Boolean {
        if (dots.containsKey(x)) {
            return false
        }
        else{
            dots[x] = y

            if (dots.size == 1) {
                reconstruct()
            }
            else {
                updateWithNewDot(x, y)
            }
            return true
        }
    }

    private fun updateWithNewDot(newX: Double, newY: Double) {
        // Обновляем sortedX с новой точкой
        sortedX = (sortedX + newX).sorted()

        // Вычисляем новые разделенные разности на основе старых
        val newremember_div_diffs = computeNewremember_div_diffs(newX, newY)
        remember_div_diffs = newremember_div_diffs.toMutableList()

        // Строим новый полином: L_{n+1}(x) = L_n(x) + f[x_0,...,x_{n+1}] * (x-x_0)...(x-x_n)
        val newCoeffs = buildPolynomialFromremember_div_diffs()
        coeffs = newCoeffs
    }

    private fun computeNewremember_div_diffs(newX: Double, newY: Double): List<Double> {
        val n = dots.size - 1 // старый размер
        val newremember_div_diffs = remember_div_diffs.toMutableList()

        // Временный массив для вычисления новых разностей
        val temp = mutableListOf<Double>()
        temp.add(newY)

        // Вычисляем разделенные разности для новой точки
        for (i in 0 until n) {
            val nextTemp = mutableListOf<Double>()
            for (j in 0 until temp.size - 1) {
                val diff = (temp[j + 1] - temp[j]) / (newX - sortedX[j])
                nextTemp.add(diff)
            }
            // Добавляем существующую разделенную разность из предыдущих вычислений
            if (i < remember_div_diffs.size - 1) {
                val existingDiff = (temp.last() - remember_div_diffs[i]) / (newX - sortedX[i])
                nextTemp.add(existingDiff)
            }
            temp.clear()
            temp.addAll(nextTemp)
        }

        // Последняя разделенная разность
        val lastDiff = (temp[0] - remember_div_diffs.last()) / (newX - sortedX.last())
        newremember_div_diffs.add(lastDiff)

        return newremember_div_diffs
    }

    private fun buildPolynomialFromremember_div_diffs(): Map<Int, Double> {
        // Начинаем с текущего полинома
        val currentPoly = Polynomial(coeffs)

        // Вычисляем произведение (x - x_0)(x - x_1)...(x - x_{n-1})
        val productPoly = (0 until sortedX.size - 1).fold(Polynomial(1.0)) { acc, i ->
            acc * Polynomial(-sortedX[i], 1.0)
        }

        // Новый полином: L_{n+1}(x) = L_n(x) + f[x_0,...,x_n] * product
        val newPoly = currentPoly + remember_div_diffs.last() * productPoly

        return newPoly.coeffs
    }

    fun removeDot(x: Double): Boolean {
        if (dots.containsKey(x)) {
            dots.remove(x)
            reconstruct()
            return true
        } else {
            return false
        }
    }

    fun dotsCopy(): Map<Double, Double> = dots.toMap()

    fun getremember_div_diffs(): List<Double> = remember_div_diffs.toList()
} */