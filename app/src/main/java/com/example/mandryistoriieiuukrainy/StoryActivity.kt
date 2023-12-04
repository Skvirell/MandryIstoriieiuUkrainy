package com.example.mandryistoriieiuukrainy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import android.graphics.drawable.TransitionDrawable
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.mandryistoriieiuukrainy.BackSelect
import androidx.appcompat.app.AppCompatActivity
import com.example.mandryistoriieiuukrainy.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private var currentDialogueIndex = 0
    private var isEconomyChosen = false
    private var isCultureChosen = false
    private var isPoliticsChosen = false
    private var specialPhraseShown = false

    private var dialogues = mutableListOf(
        Dialogue(
            "Мандрівник",
            "Ці незвідані землі завжди манять мене своєю історією. Сьогодні ми в Україні, землі, що тримає в собі безліч таємниць давнини...\n",
            R.drawable.mandrivnik_image
        ),
        Dialogue(
            "Геродот",
            "Так, ці землі мають глибоку історію. Скіфи, які тут живуть, є яскравим прикладом стійкості та пристосування до змін.",
            R.drawable.herodot_image
        ),
        Dialogue(
            "Мандрівник",
            "Ці землі першими освоїли скіфи. Спостерігаючи за ними, можна бачити, як вони ведуть своє кочове життя, пристосовуючись до природи.\n",
            R.drawable.mandrivnik_image
        ),
        Dialogue(
            "Геродот",
            "Цікаво, як вони вміло використовують ресурси, які їм дає земля і небо. Їхній зв'язок з природою - ключ до розуміння їхнього способу життя і культури.\n",
            R.drawable.herodot_image
        ),
        Dialogue(
            "Мандрівник",
            "Геродоте, ось ми й побачили скіфське плем’я. Давай підемо та поспілкуємося з ними про їхні звичаї\n",
            R.drawable.mandrivnik_image
        ),
        Dialogue(
            "Геродот",
            "Це відмінна ідея! Їхнє життя, напевно, сповнене цікавих історій.\n",
            R.drawable.herodot_image
        ),
        Dialogue(
            "Автор",
            "Мандрівник та Геродот підходять до скіфського вождя, який вітає їх з уважним поглядом.\n",
            R.drawable.author_image
        ),
        Dialogue(
            "Скіфський вождь",
            "Ласкаво просимо до нашого табору. Чим можемо вам допомогти, мандрівники?\n",
            R.drawable.scythian_leader_image
        ),
        Dialogue(
            "Мандрівник",
            "Добрий день. Ми дослідники історії та культури, і нам цікаво дізнатись більше про ваш спосіб життя.\n",
            R.drawable.mandrivnik_image)


    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.btnExit.setOnClickListener {
            val intent = Intent(this, SelectHistoryActivity::class.java)
            startActivity(intent)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        updateDialogue()
        binding.root.setOnClickListener {
            currentDialogueIndex++
            if (currentDialogueIndex < dialogues.size) {
                updateDialogue()
            } else {
                showChoices()
            }
        }
        binding.btnEconomy.setOnClickListener { discussEconomy() }
        binding.btnCulture.setOnClickListener { discussCulture() }
        binding.btnPolitics.setOnClickListener { discussPolitics() }
    }


    private fun showChoices() {
        // Оновлюємо видимість кнопок вибору тем
        (if (!isEconomyChosen) View.VISIBLE else View.GONE).also { binding.btnEconomy.visibility = it }
        (if (!isCultureChosen) View.VISIBLE else View.GONE).also { binding.btnCulture.visibility = it }
        (if (!isPoliticsChosen) View.VISIBLE else View.GONE).also { binding.btnPolitics.visibility = it }
        (if (!isEconomyChosen) View.VISIBLE else View.GONE).also { binding.txtEconomy.visibility = it }
        (if (!isCultureChosen) View.VISIBLE else View.GONE).also { binding.txtCulture.visibility = it }
        (if (!isPoliticsChosen) View.VISIBLE else View.GONE).also { binding.txtPolitics.visibility = it }
        // Якщо всі теми (економіка, культура, політика) вже обрані
        if (isEconomyChosen && isCultureChosen && isPoliticsChosen) {
            // Додаємо фінальні діалоги до списку
            addFinalDialogue()
            // Переходимо до відтворення фінальних діалогів
            continueDialogueAfterChoice()
        } else {
            // Інакше показуємо вибір тем
            binding.authorsh.text = "Виберіть тему для обговорення:"
            binding.authorsh.visibility = View.VISIBLE
            binding.imageLeftCharacter.visibility = View.INVISIBLE
            binding.imageRightCharacter.visibility = View.INVISIBLE
            binding.bgShape.visibility = View.INVISIBLE
            binding.bgShape2.visibility = View.INVISIBLE
            binding.textStory.visibility = View.INVISIBLE
            binding.textStory2.visibility = View.INVISIBLE
            binding.imageRightCharacter2.visibility = View.INVISIBLE
        }
    }


    private fun addFinalDialogue() {
        dialogues.addAll(listOf(
            Dialogue("Скіфський вождь", "Ви дізналися багато про наше плем'я. Сподіваюся, ця інформація допоможе вам у вашому дослідженні.\n", R.drawable.scythian_leader_image),
            Dialogue("Мандрівник","Так, ми вам дуже дякуємо за нові познання в історії та культурі України!\n", R.drawable.mandrivnik_image),
            Dialogue("Мандрівник","Кінець 228\n", R.drawable.mandrivnik_image)
        )
        )
    }


    private fun continueDialogueAfterChoice() {
        View.GONE.also { binding.btnEconomy.visibility = it }
        View.GONE.also { binding.btnCulture.visibility = it }
        View.GONE.also { binding.btnPolitics.visibility = it }
        View.GONE.also { binding.txtEconomy.visibility = it }
        View.GONE.also { binding.txtCulture.visibility = it }
        View.GONE.also { binding.txtPolitics.visibility = it }
        View.GONE.also { binding.authorsh.visibility = it }


        currentDialogueIndex++
        if (currentDialogueIndex < dialogues.size) {
            updateDialogue()
        } else {
            // Додаємо лог для відстеження переходу
            Log.d("StoryActivity", "Перехід до BackSelect")

            val intent = Intent(this, BackSelect::class.java)
            startActivity(intent)
            finish()
        }
    }



    private fun updateDialogue() {
        if (currentDialogueIndex >= dialogues.size) {
            Log.d("StoryActivity", "Спроба переходу до BackSelect з updateDialogue")
            val intent = Intent(this, BackSelect::class.java)
            startActivity(intent)
            finish()
            return
        }
        val currentDialogue = dialogues[currentDialogueIndex]
        with(binding) {
            textStory.text = currentDialogue.text
            textStory2.text = currentDialogue.text
            authorsh.text = currentDialogue.text


            imageLeftCharacter.visibility = View.INVISIBLE
            imageRightCharacter.visibility = View.INVISIBLE
            imageRightCharacter2.visibility = View.INVISIBLE
            bgShape.visibility = View.INVISIBLE
            bgShape2.visibility = View.INVISIBLE
            textStory.visibility = View.INVISIBLE
            textStory2.visibility = View.INVISIBLE
            authorsh.visibility = View.INVISIBLE


            // Перевірка конкретної фрази в тексті діалогу
            if (currentDialogue.text.contains("Геродоте, ось ми й побачили скіфське плем’я.")) {
                specialPhraseShown = true
                changeBackground()
            }

            if (currentDialogue.text.contains("Скіфський вождь показує на стадо, що пасеться поруч.")) {
                specialPhraseShown = true
                changeBackground2()
            }

            if (currentDialogue.text.contains("Скіфський вождь привів мандрівників до невеличкої майстерні, де скіфські ремісники створюють приголомшливі вироби з золота.\n")) {
                specialPhraseShown = true
                changeBackground3()
            }

            if (currentDialogue.text.contains("Ось один з наших головних скарбів. Ми відомі своїми уміннями у обробці золота. Кожен виріб є унікальним і розповідає історію нашого народу.\n")) {
                specialPhraseShown = true
                changeBackground4()
            }

            if (currentDialogue.text.contains("Ваше мистецтво — це вікно у минуле, яке дозволяє нам зрозуміти більше про ваше суспільство. Це дійсно незабутній досвід.\n")) {
                specialPhraseShown = true
                changeBackground5()
            }

            if (currentDialogue.text.contains("Скіфський вождь підходить до своєї палатки.\n")) {
                specialPhraseShown = true
                changeBackground6()
            }


            if (currentDialogue.text.contains("Кінець 228")) {
                Log.d("StoryActivity", "Перехід до BackSelect за спеціальною фразою")
                val intent = Intent(this@StoryActivity, BackSelect::class.java) // переконайтеся, що ви використовуєте правильний контекст і клас
                startActivity(intent)
                finish()
                return
            }




            when (currentDialogue.speaker) {
                "Мандрівник" -> {
                    imageLeftCharacter.setImageResource(currentDialogue.imageResId)
                    imageLeftCharacter.visibility = View.VISIBLE
                    bgShape.visibility = View.VISIBLE
                    textStory.visibility = View.VISIBLE
                }
                "Геродот" -> {
                    imageRightCharacter.setImageResource(currentDialogue.imageResId)
                    imageRightCharacter.visibility = View.VISIBLE
                    bgShape2.visibility = View.VISIBLE
                    textStory2.visibility = View.VISIBLE
                }
                "Скіфський вождь" -> {
                    imageRightCharacter2.setImageResource(currentDialogue.imageResId)
                    imageRightCharacter2.visibility = View.VISIBLE
                    bgShape2.visibility = View.VISIBLE
                    textStory2.visibility = View.VISIBLE
                }
                "Автор" -> {
                    authorsh.text = currentDialogue.text
                    authorsh.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun changeBackground() {
        if (specialPhraseShown) {
            binding.root.setBackgroundResource(R.drawable.tabor)
        } else {
            // Використовуємо анімацію переходу
            val transitionDrawable = ContextCompat.getDrawable(this@StoryActivity, R.drawable.transition_background) as TransitionDrawable
            binding.root.background = transitionDrawable
            transitionDrawable.startTransition(2000)
        }
    }
    private fun changeBackground2() {
        if (specialPhraseShown) {
            binding.root.setBackgroundResource(R.drawable.sheeps)
        } else {
            // Використовуємо анімацію переходу
            val transitionDrawable = ContextCompat.getDrawable(this@StoryActivity, R.drawable.transition_background) as TransitionDrawable
            binding.root.background = transitionDrawable
            transitionDrawable.startTransition(2000)
        }
    }


    private fun changeBackground3() {
        if (specialPhraseShown) {
            binding.root.setBackgroundResource(R.drawable.workgold)
        } else {
            // Використовуємо анімацію переходу
            val transitionDrawable = ContextCompat.getDrawable(
                this@StoryActivity,
                R.drawable.transition_background
            ) as TransitionDrawable
            binding.root.background = transitionDrawable
            transitionDrawable.startTransition(2000)
        }
    }

    private fun changeBackground4() {
        if (specialPhraseShown) {
            binding.root.setBackgroundResource(R.drawable.scythiangold)
        } else {
            // Використовуємо анімацію переходу
            val transitionDrawable = ContextCompat.getDrawable(this@StoryActivity, R.drawable.transition_background) as TransitionDrawable
            binding.root.background = transitionDrawable
            transitionDrawable.startTransition(2000)
        }
    }

    private fun changeBackground5() {
        if (specialPhraseShown) {
            binding.root.setBackgroundResource(R.drawable.workgold)
        } else {
            // Використовуємо анімацію переходу
            val transitionDrawable = ContextCompat.getDrawable(
                this@StoryActivity,
                R.drawable.transition_background
            ) as TransitionDrawable
            binding.root.background = transitionDrawable
            transitionDrawable.startTransition(2000)
        }
    }
    private fun changeBackground6() {
        if (specialPhraseShown) {
            binding.root.setBackgroundResource(R.drawable.scythian_chieftains_tent)
        } else {
            // Використовуємо анімацію переходу
            val transitionDrawable = ContextCompat.getDrawable(
                this@StoryActivity,
                R.drawable.transition_background
            ) as TransitionDrawable
            binding.root.background = transitionDrawable
            transitionDrawable.startTransition(2000)
        }
    }



    private fun discussEconomy() {
        isEconomyChosen = true
        dialogues.addAll(listOf(
            Dialogue ("Скіфський вождь","Ах, наші степи – це не лише наш дім, а й наша золота жила.\n", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","Ми ведемо кочовий спосіб життя, завдяки чому завжди маємо свіжі пасовища для нашого худоби.\n", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","Скотарство – це наша основна заняття.\n", R.drawable.scythian_leader_image),
            Dialogue ("Автор","Скіфський вождь показує на стадо, що пасеться поруч.\n",R.drawable.author_image),
            Dialogue ("Скіфський вождь","Ми також займаємося мисливством і трохи торгівлею з сусідніми племенами.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","А коли виникає потреба, наші воїни здійснюють набіги на сусідні території для добування додаткових ресурсів.\n ",R.drawable.scythian_leader_image),
            Dialogue ("Геродот","Це дуже цікаво! Ваше життя є гармонією між природою та необхідністю виживання.\n ", R.drawable.herodot_image),
            Dialogue ("Мандрівник","Та це також показує, як ваше плем’я впливає на економічну структуру регіону.\n ", R.drawable.mandrivnik_image),
            Dialogue ("Мандрівник","Можливо ви розкажете нам ще про...\n ", R.drawable.mandrivnik_image)
        ))


        showChoices()
        continueDialogueAfterChoice()
    }


    private fun discussCulture() {
        isCultureChosen = true
        dialogues.addAll(listOf(
            Dialogue ("Геродот"," Нам би хотілося дізнатись більше про вашу культуру. Ми чули про ваше надзвичайне золоте ремесло та орнаментику. Чи могли б ви розповісти нам трохи більше про це?\n", R.drawable.herodot_image),
            Dialogue ("Автор","Скіфський вождь привів мандрівників до невеличкої майстерні, де скіфські ремісники створюють приголомшливі вироби з золота.\n",R.drawable.author_image),
            Dialogue ("Скіфський вождь","Ось один з наших головних скарбів. Ми відомі своїми уміннями у обробці золота. Кожен виріб є унікальним і розповідає історію нашого народу.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Мандрівник"," Це просто неймовірно! Ці вироби мають надзвичайну естетичну цінність. Це дійсно показує глибокі коріння вашої культурної спадщини.\n ",R.drawable.mandrivnik_image),
            Dialogue ("Скіфський вождь","Так, кожен орнамент має особливе значення. Це спосіб нашого племені виразити свою історію та вірування через мистецтво.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Геродот","Ваше мистецтво — це вікно у минуле, яке дозволяє нам зрозуміти більше про ваше суспільство. Це дійсно незабутній досвід.\n ", R.drawable.herodot_image),
            Dialogue ("Автор","Скіфський вождь посміхається та веде їх далі по табору, де вони бачать різні аспекти скіфського життя.\n",R.drawable.author_image),
            Dialogue ("Автор","Сонце починає сідати, розфарбовуючи небо у золотисті та червоні відтінки, які нагадують орнаменти на золотих прикрасах, які вони щойно бачили\n",R.drawable.author_image),
            Dialogue ("Мандрівник","Можливо ви розкажете нам ще про...\n ", R.drawable.mandrivnik_image)
        ))


        showChoices()
        continueDialogueAfterChoice()
    }


    private fun discussPolitics() {
        isPoliticsChosen = true
        dialogues.addAll(listOf(
            Dialogue ("Мандрівник","Мене цікавить ваш суспільно-політичний устрій. Як організоване ваше суспільство?\n", R.drawable.mandrivnik_image),
            Dialogue ("Скіфський вождь","Ах, це досить цікава тема. Наше суспільство розшароване.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Автор","Скіфський вождь підходить до своєї палатки.\n",R.drawable.author_image),
            Dialogue ("Скіфський вождь","Ми маємо аристократію, яка займається військовими справами та управлінням племенем.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","Тако ж є звичайні члени племені, які займаються скотарством та іншими повсякденними заняттями.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Геродот","Це нагадує мені структуру деяких міст-держав у Греції. Чи є у вас особливий лідер або рада старійшин, яка приймає важливі рішення?\n ", R.drawable.herodot_image),
            Dialogue ("Скіфський вождь","Так, у нас є вождь, який вибирається серед наймудріших та найсильніших членів нашого племені.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","Він приймає ключові рішення і веде наше плем’я під час військових походів.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","Але він також прислухається до ради старійшин, яка складається з досвідчених воїнів та мудрих жінок.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Мандрівник","Це здається дуже збалансованим та розумним підходом.\n ",R.drawable.mandrivnik_image),
            Dialogue ("Мандрівник","І, здається, він працює добре для забезпечення благополуччя вашого племені.\n ",R.drawable.mandrivnik_image),
            Dialogue ("Автор","Скіфський вождь посміхається та киває головою, погоджуючись з мандрівником.\n",R.drawable.author_image),
            Dialogue ("Скіфський вождь","Ми намагаємося жити у гармонії із природою та один з одним, незважаючи на наші військові традиції.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Скіфський вождь","Це допомагає нам зберігати єдність та продовжувати наш спосіб життя через століття.\n ", R.drawable.scythian_leader_image),
            Dialogue ("Мандрівник","Можливо ви розкажете нам ще про...\n ", R.drawable.mandrivnik_image)


        ))


        showChoices()
        continueDialogueAfterChoice()
    }


    data class Dialogue(
        val speaker: String,
        val text: String,
        val imageResId: Int
    )
}