package start.up.tracker.database

import start.up.tracker.R
import start.up.tracker.database.TechniquesIds.EISENHOWER_MATRIX
import start.up.tracker.database.TechniquesIds.PARETO
import start.up.tracker.database.TechniquesIds.POMODORO
import start.up.tracker.entities.Technique
import start.up.tracker.utils.resources.ResourcesUtils

object TechniquesStorage {

    fun getTechniques(): List<Technique> {
        val techniques: MutableList<Technique> = mutableListOf()

        techniques.add(getParetoInfo())
        techniques.add(getEisenhowerInfo())
        techniques.add(getPomodoroInfo())

        return techniques.toList()
    }

    fun getIncompatiblePrinciplesIds(id: Int): List<Int> {
        return when (id) {
            PARETO -> listOf(1)
            EISENHOWER_MATRIX -> listOf(1)
            POMODORO -> listOf(1)
            else -> listOf(-1)
        }
    }

    fun getParetoInfo(): Technique {
        return Technique(
            id = PARETO,
            title = ResourcesUtils.getString(R.string.pareto_title),
            body = ResourcesUtils.getString(R.string.pareto_body),
            timeToRead = 4,
        )
    }

    fun getEisenhowerInfo(): Technique {
        return Technique(
            id = EISENHOWER_MATRIX,
            title = ResourcesUtils.getString(R.string.eisenhower_title),
            body = ResourcesUtils.getString(R.string.eisenhower_body),
            timeToRead = 8,
        )
    }

    fun getPomodoroInfo(): Technique {
        return Technique(
            id = POMODORO,
            title = ResourcesUtils.getString(R.string.pomodoro_title),
            body = ResourcesUtils.getString(R.string.pomodoro_body),
            timeToRead = 6,
        )
    }
}

object TechniquesIds {
    const val PARETO = 1
    const val EISENHOWER_MATRIX = 2
    const val POMODORO = 3
}
