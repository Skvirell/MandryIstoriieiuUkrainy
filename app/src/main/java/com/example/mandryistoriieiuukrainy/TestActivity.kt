package com.example.mandryistoriieiuukrainy

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.EditText
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerLayout: LinearLayout
    private lateinit var nextButton: ImageButton
    private lateinit var resultTextView: TextView
    private lateinit var answerEditText: EditText // Це поле вже оголошено, тому не потрібно його дублювати

    private val test = HistoryTest()
    private var currentQuestionIndex = 0
    private val studentAnswers = mutableMapOf<Int, String>()
    private val matchingQuestionSpinners = mutableMapOf<Int, Spinner>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Для Android 11 (API рівень 30) та вище
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Для старіших версій Android
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        questionTextView = findViewById(R.id.questionTextView)
        answerLayout = findViewById(R.id.answerLayout)
        nextButton = findViewById(R.id.nextButton)
        resultTextView = findViewById(R.id.resultTextView)

        initializeQuestions()
        displayCurrentQuestion()

        nextButton.setOnClickListener {
            if (!processAnswer()) {
                Toast.makeText(this, "Будь ласка, дайте відповідь на питання", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener // використання лейбла для повернення з лямбда-виразу
            }

            currentQuestionIndex++
            if (currentQuestionIndex < test.questionsCount()) {
                displayCurrentQuestion()
            } else {
                showResults()
            }
        }

        val buttonReturnToMain: ImageButton = findViewById(R.id.buttonReturnToMain)
        buttonReturnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


        private fun initializeQuestions() {
        test.addQuestion(
            SingleChoiceQuestion(
                id = 1,
                text = "Ким були скіфи?",
                correctAnswer = "Номадичним воїнським народом",
                score = 10,
                options = listOf(
                    "Слов'янським племенем",
                    "Номадичним воїнським народом",
                    "Торговими поселенцями",
                    "Морськими розбійниками"
                ) // Приклад варіантів відповідей
            ))


        test.addQuestion(
            MultipleChoiceQuestion(
                id = 2,
                text = "Які особливості були характерні для скіфської культури? (Оберіть усі правильні варіанти)",
                correctAnswers = setOf("Майстерне ковальство та ювелірні вироби", "Кочовий спосіб життя"),
                score = 15,
                options = listOf ("Розвинене сільське господарство", "Майстерне ковальство та ювелірні вироби","Будівництво великих міст","Кочовий спосіб життя"),
            ) //
        )

        test.addQuestion(
            MatchingQuestion(
                id = 3,
                text = "Встановіть відповідність між скіфськими символами та їх значенням:",
                correctPairs = mapOf(
                    "Золотий олень" to "Символ плодючості та сонця",
                    "Пектораль" to "Вишукана прикраса",
                    "Курган" to "Місце поховання"),

                score = 30
            )
        )

        test.addQuestion(
            FillInTheBlankQuestion(
                id = 4,
                text = "Назвіть найвідоміший археологічний пам'ятник скіфської культури, що знаходиться на території України.",
                correctAnswer = "Курган Товста Могила",
                score = 10
            )
        )

    }



    private fun displayCurrentQuestion() {
        val question = test.getQuestion(currentQuestionIndex)
        questionTextView.text = question?.text ?: "" // Використання безпечного виклику з оператором елвіса
        answerLayout.removeAllViews()

        when (question) {
            is SingleChoiceQuestion -> showSingleChoiceQuestion(question)
            is MultipleChoiceQuestion -> showMultipleChoiceQuestion(question)
            is MatchingQuestion -> showMatchingQuestion(question)
            is FillInTheBlankQuestion -> showFillInTheBlankQuestion(question)
            else -> {} // Додано обробку для null або іншого випадку
        }
    }

    private fun processAnswer(): Boolean {
        val question = test.getQuestion(currentQuestionIndex)
        val answer = when (question) {
            is SingleChoiceQuestion -> {
                val radioGroup = answerLayout.getChildAt(0) as RadioGroup
                if (radioGroup.checkedRadioButtonId == -1) return false // Немає вибраної відповіді
                radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)?.text.toString()
            }
            is MultipleChoiceQuestion -> {
                val checkedBoxes = (0 until answerLayout.childCount)
                    .mapNotNull { answerLayout.getChildAt(it) as? CheckBox }
                    .filter { it.isChecked }
                if (checkedBoxes.isEmpty()) return false // Немає вибраних відповідей
                checkedBoxes.joinToString(",") { it.text.toString() }
            }
            is MatchingQuestion -> processMatchingAnswer(question)
            is FillInTheBlankQuestion -> {
                val editText = answerLayout.getChildAt(0) as EditText
                if (editText.text.isBlank()) return false // Поле для введення пусте
                editText.text.toString()
            }
            else -> return false
        }
        studentAnswers[question?.id ?: return false] = answer
        return true
    }


    private fun processSingleChoiceAnswer(): String {
        val radioGroup = answerLayout.getChildAt(0) as RadioGroup
        return radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)?.text.toString()
    }

    private fun processMultipleChoiceAnswer(): String {
        return (0 until answerLayout.childCount)
            .mapNotNull { answerLayout.getChildAt(it) as? CheckBox }
            .filter { it.isChecked }
            .joinToString(",") { it.text.toString() }
    }

    private fun processMatchingAnswer(question: MatchingQuestion): String {
        return matchingQuestionSpinners.filterKeys { it / 10 == question.id }
            .entries.joinToString(";") { (key, spinner) ->
                "${question.getCorrectPairs().keys.toList()[key % 10]}->${spinner.selectedItem}"
            }
    }

    private fun showMatchingQuestion(question: MatchingQuestion) {
        questionTextView.text = question.text
        answerLayout.removeAllViews()

        val correctPairs = question.getCorrectPairs()
        val values = correctPairs.values.toList()

        correctPairs.keys.forEachIndexed { index, key ->
            val pairLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val keyTextView = TextView(this).apply {
                text = key
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val answerSpinner = Spinner(this).apply {
                val adapter = ArrayAdapter(
                    this@TestActivity,
                    android.R.layout.simple_spinner_item,
                    values
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                this.adapter = adapter
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            pairLayout.addView(keyTextView)
            pairLayout.addView(answerSpinner)
            answerLayout.addView(pairLayout)

            // Зберігаємо спінер з унікальним ідентифікатором для кожної пари
            matchingQuestionSpinners[question.id * 10 + index] = answerSpinner
        }
    }




    private fun processFillInTheBlankAnswer(): String {
        return answerEditText.text.toString()
    }

    private fun showResults() {
        val score = test.takeTest(studentAnswers)
        resultTextView.text = "Загальний бал: $score"
        resultTextView.visibility = TextView.VISIBLE
    }



    private fun showSingleChoiceQuestion(question: SingleChoiceQuestion) {
        val radioGroup = RadioGroup(this)
        radioGroup.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        for (option in question.getOptions()) {
            val radioButton = RadioButton(this).apply {
                text = option
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
            }
            radioGroup.addView(radioButton)
        }
        answerLayout.addView(radioGroup)
    }



    private fun showMultipleChoiceQuestion(question: MultipleChoiceQuestion) {
        questionTextView.text = question.text
        answerLayout.removeAllViews()
        for (option in question.getOptions()) {
            val checkBox = CheckBox(this).apply {
                text = option
            }
            answerLayout.addView(checkBox)
        }
    }



    private fun showFillInTheBlankQuestion(question: FillInTheBlankQuestion) {
        val editText = EditText(this)
        editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        answerLayout.addView(editText)
    }

}
