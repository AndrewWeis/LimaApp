package start.up.tracker.database

import start.up.tracker.entities.Technique

object TechniquesStorage {

    fun getTechniques(): List<Technique> {
        val techniques: MutableList<Technique> = mutableListOf()

        repeat(10) { index ->
            techniques.add(Technique(index, "Title: $index", "Body: $index", "$index min"))
        }

        return techniques.toList()
    }
}
