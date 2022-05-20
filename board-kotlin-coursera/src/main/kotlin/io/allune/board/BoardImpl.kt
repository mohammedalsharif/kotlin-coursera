package io.allune.board

import io.allune.board.Direction.*

fun createSquareBoard(width: Int): SquareBoard {
    return SquareBoardImpl(width)
}

fun <T> createGameBoard(width: Int): GameBoard<T> {
    return GameBoardImpl(width)
}

open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    var cells: Array<Array<Cell>> = arrayOf()

    init {
        (1..width).forEach { i ->
            var row = arrayOf<Cell>()
            (1..this.width).forEach { j ->
                row += Cell(i, j)
            }
            cells += row
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return when {
            i > width || j > width || i == 0 || j == 0 -> null
            else -> getCell(i, j)
        }
    }

    override fun getCell(i: Int, j: Int): Cell {
        return cells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        return IntRange(1, width).flatMap { i: Int ->
            IntRange(1, width).map { j: Int ->
                getCell(i, j)
            }
        }.toList()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return when {
            jRange.last > width -> IntRange(jRange.first, width).map { j: Int -> getCell(i, j) }.toList()
            else -> jRange.map { j: Int -> getCell(i, j) }.toList()
        }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return when {
            iRange.last > width -> IntRange(iRange.first, width).map { i: Int -> getCell(i, j) }.toList()
            else -> iRange.map { i: Int -> getCell(i, j) }.toList()
        }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(i - 1, j)
            DOWN -> getCellOrNull(i + 1, j)
            LEFT -> getCellOrNull(i, j - 1)
            RIGHT -> getCellOrNull(i, j + 1)
        }
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val cellValues = mutableMapOf<Cell, T?>()

    init {
        cells.forEach { unit -> unit.forEach { cell -> cellValues[cell] = null } }
    }

    override fun get(cell: Cell): T? {
        return cellValues[cell]
    }

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellValues.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellValues.filterValues(predicate).keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellValues.values.any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellValues.values.all(predicate)
    }
}