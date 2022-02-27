package flashcards

fun addFlashCard(flashCards: Deck) {
    flashCards.message("The card:\n")
    val term = flashCards.logUserInput(readln())
    if (term in flashCards.terms()) {
        flashCards.message("The card \"$term\" already exists.\n")
        return
    }
    flashCards.message("The definition of the card:\n")
    val definition = flashCards.logUserInput(readln())
    if (definition in flashCards.definitions()) {
        flashCards.message("The definition \"$definition\" already exists.\n")
        return
    }
    flashCards.addCard(Card(term, definition, 0))
    flashCards.message("The pair (\"$term\":\"$definition\") has been added.\n")
}

fun removeFlashCard(flashCards: Deck) {
    flashCards.message("Which card?\n")
    val removeCard = flashCards.logUserInput(readln())
    if (removeCard in flashCards.terms()) {
        flashCards.remove(removeCard)
        flashCards.message("The card has been removed.\n")
    } else {
        flashCards.message("Can't remove \"$removeCard\": there is no such card.\n")
    }
}

fun exportCards(flashCards: Deck) {
    flashCards.message("File name:\n")
    flashCards.export(flashCards.logUserInput(readln()))
    flashCards.message("${flashCards.size()} cards have been saved.\n")
}

fun importCards(flashCards: Deck) {
    flashCards.message("File name:\n")
    flashCards.message("${flashCards.import(flashCards.logUserInput(readln()))} cards have been loaded.\n")
}

fun knowledgeTest(flashCards: Deck) {
    if (flashCards.isEmpty()) {
        flashCards.message("There are no flashcards. Please add some.\n")
        return
    }
    flashCards.message("How many times to ask?\n")
    for (card in flashCards.random(flashCards.logUserInput(readln()).toInt())) {
        flashCards.message("Print the definition of \"${card.term}\":\n")
        when (val answer = flashCards.logUserInput(readln())) {
            card.definition -> flashCards.message("Correct!\n")
            in flashCards.definitions() -> {
                flashCards.message("Wrong. The right answer is \"${card.definition}\", " +
                        "but your definition is correct for \"${flashCards.termLookup(answer)}\".\n")
                card.wrongAnswer()
            }
            else -> {
                flashCards.message("Wrong. The right answer is \"${card.definition}\".\n")
                card.wrongAnswer()
            }
        }
    }
}

fun saveLog(flashCards: Deck) {
    flashCards.message("File name:\n")
    flashCards.saveLog(flashCards.logUserInput(readln()))
}

fun hardest(flashCards: Deck) {
    val hardCards = flashCards.hardest()
    when (hardCards.size()) {
        0 -> flashCards.message("There are no cards with errors.")
        1 -> flashCards.message("The hardest card is \"${hardCards.first().term}\". " +
                "You have ${hardCards.first().mistakes} errors answering it")
        else -> {
            var cardString = ""
            for (card in hardCards) {
                cardString += "\"${card.term}\", "
            }
            flashCards.message("The hardest cards are ${cardString.dropLast(2)}. " +
                    "You have ${hardCards.first().mistakes} errors answering them.")
        }
    }
}

fun resetStats(flashCards: Deck) {
    flashCards.resetStats()
    flashCards.message("Card statistics have been reset.")
}

fun main(args: Array<String>) {
    val flashCards = Deck()
    if ("-import" in args && args.size > args.indexOf("-import") + 1) {
        flashCards.message("${flashCards.import(args[args.indexOf("-import") + 1])} cards have been loaded.\n")
    }
    while (true) {
        flashCards.message("\nInput the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n")
        when (val answer = flashCards.logUserInput(readln())) {
            "add" -> addFlashCard(flashCards)
            "remove" -> removeFlashCard(flashCards)
            "import" -> importCards(flashCards)
            "export" -> exportCards(flashCards)
            "ask" -> knowledgeTest(flashCards)
            "log" -> saveLog(flashCards)
            "hardest card" -> hardest(flashCards)
            "reset stats" -> resetStats(flashCards)
            "exit" -> break
            else -> flashCards.message("$answer:  No such action!\nTry again\n")
        }
    }
    if ("-export" in args && args.size > args.indexOf("-export") + 1) {
        flashCards.export(args[args.indexOf("-export") + 1])
        flashCards.message("${flashCards.size()} cards have been saved.\n")
    }
    flashCards.message("Bye bye!\n")
}