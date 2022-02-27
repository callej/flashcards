package flashcards

import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import kotlin.random.Random

const val CARD_SIZE = 3

class Card(val term: String, val definition: String, var mistakes: Int) {

    fun wrongAnswer() = ++mistakes

    fun reset() {
        mistakes = 0
    }
}

class Deck {
    private val cards = emptyList<Card>().toMutableList()
    private var logText = ""

    fun logUserInput(text: String): String {
        logText += text + "\n"
        return text
    }

    fun message(text: String): String {
        logText += text
        print(text)
        return text
    }

    fun saveLog(filename: String) {
        val file = File(filename)
        file.writeText(logText)
        file.appendText("The log has been saved.")
        message("The log has been saved.\n")
    }

    fun size() = cards.size

    fun isEmpty() = size() == 0

    fun first() = cards[0]

    fun addCard(card: Card) = cards.add(card)

    fun remove(term: String) = cards.removeAt(terms().indexOf(term))

    fun terms() = cards.map { it.term }

    fun definitions() = cards.map { it.definition }

    fun termLookup(definition: String) = cards[definitions().indexOf(definition)].term

    fun random(n: Int = 1): Deck {
        val randomSelection = Deck()
        repeat(n) { randomSelection.addCard(cards[Random.nextInt(cards.size)]) }
        return randomSelection
    }

    fun export(filename: String) {
        var data = ""
        for (card in cards) {
            data += "${card.term}\n${card.definition}\n${card.mistakes}\n"
        }
        val file = File(filename)
        file.writeText(data.dropLast(1))
    }

    fun import(filename: String): Int {
        val file = File(filename)
        if (!file.exists()) {
            message("File not found.\n")
            return 0
        }
        val data = file.readText().lines()
        val imported = Deck()
        for (index in data.indices step CARD_SIZE) {
            imported.addCard(Card(data[index], data[index + 1], data[index + 2].toInt()))
        }
        for (card in imported) {
            if (card.term in terms()) remove(card.term)
            addCard(card)
        }
        return imported.size()
    }

    fun hardest(): Deck {
        var hardestCards = Deck()
        var mistakes = 0
        for (card in cards) {
            when {
                card.mistakes > mistakes -> {
                    hardestCards = Deck()
                    hardestCards.addCard(card)
                    mistakes = card.mistakes
                }
                mistakes != 0 && card.mistakes == mistakes -> hardestCards.addCard(card)
            }
        }
        return hardestCards
    }

    fun resetStats() {
        for (card in cards) card.reset()
    }

    operator fun iterator() = CardIterator(this)

    class CardIterator(private val deck: Deck) {
        private var index = 0

        operator fun next(): Card {
            return deck.cards[index++]
        }

        operator fun hasNext(): Boolean {
            return index < deck.cards.size
        }
    }
}