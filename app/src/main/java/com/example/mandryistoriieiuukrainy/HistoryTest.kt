package com.example.mandryistoriieiuukrainy

class HistoryTest {
    private val questions: MutableList<Question> = mutableListOf()
    private val questionsMap by lazy { questions.associateBy { it.id } }

    fun addQuestion(question: Question) {
        questions.add(question)
    }

    fun takeTest(answers: Map<Int, String>): Int {
        return answers.mapNotNull { (questionId, userAnswer) ->
            questionsMap[questionId]?.let { question ->
                if (question.isCorrectAnswer(userAnswer)) question.score else 0
            }
        }.sum()
    }

    fun getQuestion(index: Int): Question? {
        return questions.getOrNull(index)
    }

    fun questionsCount(): Int {
        return questions.size
    }
}

abstract class Question(
    val id: Int,
    val text: String,
    val score: Int
) {
    abstract fun isCorrectAnswer(answer: String): Boolean
}

class SingleChoiceQuestion(
    id: Int,
    text: String,
    private val correctAnswer: String,
    score: Int,
    private val options: List<String>
) : Question(id, text, score) {

    override fun isCorrectAnswer(answer: String) = answer.equals(correctAnswer, ignoreCase = true)

    fun getOptions(): List<String> = options
}

class MultipleChoiceQuestion(
    id: Int,
    text: String,
    private val correctAnswers: Set<String>,
    score: Int,
    private val options: List<String>
) : Question(id, text, score) {

    override fun isCorrectAnswer(answer: String): Boolean {
        val userAnswers = answer.split(",").map { it.trim() }.toSet()
        return userAnswers == correctAnswers
    }

    fun getOptions(): List<String> = options
}

class MatchingQuestion(
    id: Int,
    text: String,
    private val correctPairs: Map<String, String>,
    score: Int
) : Question(id, text, score) {

    override fun isCorrectAnswer(answer: String): Boolean {
        val userPairs = answer.split(";").associate {
            val (key, value) = it.split("->").map { it.trim() }
            key to value
        }
        return userPairs == correctPairs
    }

    fun getCorrectPairs(): Map<String, String> = correctPairs
}

class FillInTheBlankQuestion(
    id: Int,
    text: String,
    private val correctAnswer: String,
    score: Int
) : Question(id, text, score) {

    override fun isCorrectAnswer(answer: String) = answer.equals(correctAnswer, ignoreCase = true)
}
