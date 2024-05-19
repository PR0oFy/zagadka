package com.pashkov.zagadka

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Объявление переменных для элементов пользовательского интерфейса
    private lateinit var textRiddle: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnCheck: Button
    private lateinit var btnStats: Button
    private lateinit var textRiddleCount: TextView
    private lateinit var btnRiddle: Button // Добавляем кнопку загадки

    private lateinit var answerOrder: MutableList<Int>


    // Переменная для отслеживания нажатия кнопки загадки
    private var isRiddleButtonClicked = false

    // Список с загадками, ответами и индексом правильного ответа
    private val answers = listOf(
        Triple(
            "Висит груша, нельзя скушать.",
            listOf("Груша", "Шар", "Лампочка", "Яблоко"),
            2
        ),
        Triple(
            "Что можно сломать, но нельзя увидеть?",
            listOf("Сердце", "Шар", "Воздух", "Доверие"),
            0
        ),
        Triple(
            "Что можно увидеть с закрытыми глазами?",
            listOf("Звезды", "Сны", "Цветы", "Тени"),
            1
        ),
        Triple("Кто его делает, тот не пользуется, кто пользуется, тот его не делает. Что это?", listOf("Ключ", "Гроб", "Ложка", "Ведро"), 1),
        Triple(
            "Что можно увидеть вместе с его создателем?",
            listOf("Душа", "Тень", "Свет", "Отражение"),
            1
        ),
        Triple(
            "Что идет, не шагает, что идет, не едет?",
            listOf("Дождь", "Свет", "Время", "Путь"),
            2
        ),
        Triple(
            "День спит, ночь глядит, утром умирает, другой сменяет. Что это?",
            listOf("Месяц", "Свечи", "Вампир", "Ветер"),
            1
        ),
        Triple("Хоть без глаз, могу бегущих догонять, но только никому меня нельзя обнять. Что это?", listOf("Туча", "Тень", "Деньги", "Звезда"), 1),
        Triple(
            "Без языка, а сказывается. Что это?",
            listOf("Боль", "Жест", "Лай", "Старость"),
            0
        ),
        Triple(
            "Не море, не земля, корабли не плавают, и ходить нельзя. Что за место такое?",
            listOf("Небеса", "Лава", "Болото", "Пустыня"),
            2
        ),
        Triple(
            "Первый говорит — побежим, другой говорит — полежим, третий говорит — покачаемся. Кто первый?",
            listOf("Время", "Грунтовая дорога", "Конь", "Вода"),
            3
        ),
        Triple(
            "Деревянные ноги, хоть всё лето стой. Что это за зверь такой?",
            listOf("Забор", "Ткацкий станок", "Табуретка", "Протезы"),
            1
        ),
        Triple(
            "В лесу выросло, из лесу вынесли, на руках плачет, а по полу скачут. Что это?",
            listOf("Лапти", "Балалайка", "Деревянные доски", "Лук и стрелы"),
            1
        ),
        Triple(
            "Четыре четырки, две растопырки, седьмой вертун, два стёклушка в нём. Что это?",
            listOf("Бык", "Часы", "Автомобиль", "Бинокль"),
            0
        ),
        Triple(
            "Утка в море, хвост на заборе. Что за чудо-юдо?",
            listOf("Ковш", "Морковь", "Буй", "Якорь"),
            0
        )
    )

    // Переменные для управления текущим состоянием загадок
    private var currentRiddles: List<Triple<String, List<String>, Int>> = emptyList()
    private var currentRiddleIndex = -1
    private var numberAnswer = 0
    private var correctAnswersCount = 0
    private var incorrectAnswersCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация элементов пользовательского интерфейса
        textRiddle = findViewById(R.id.text_riddle)
        radioGroup = findViewById(R.id.radio_group)
        btnCheck = findViewById(R.id.btn_check)
        btnStats = findViewById(R.id.btn_stats)
        textRiddleCount = findViewById(R.id.text_riddle_count)

        // Инициализация кнопки загадки и установка обработчика нажатия
        btnRiddle = findViewById(R.id.btn_riddle)
        btnRiddle.setOnClickListener {
            isRiddleButtonClicked = true
            btnRiddle.visibility = View.GONE // Скрываем кнопку загадки после нажатия
            generateRiddles() // Генерируем новый набор загадок
            showNextRiddle() // Отображаем следующую загадку
        }

        // Устанавливаем обработчики нажатия кнопок
        btnCheck.setOnClickListener {
            checkAnswer()
        }

        btnStats.setOnClickListener {
            showStats()
        }


    }

    // Генерация нового набора загадок
    private fun generateRiddles() {
        currentRiddles = getRandomRiddles()
    }

    // Получение случайного набора загадок shuffled()
    private fun getRandomRiddles(): List<Triple<String, List<String>, Int>> {
        val shuffledRiddles = answers.shuffled()
        return shuffledRiddles.take(10)
    }

    // Отображение следующей загадки
    private fun showNextRiddle() {
        numberAnswer++
        currentRiddleIndex++
        // Проверяем, есть ли еще загадки в текущем наборе
        if (currentRiddleIndex < currentRiddles.size) {
            // Получаем данные о текущей загадке из списка
            val (riddle, answersList) = currentRiddles[currentRiddleIndex]

            // Отображаем текст текущей загадки в текстовом поле textRiddle
            textRiddle.text = riddle

            // Удаляем все элементы из радиогруппы radioGroup
            radioGroup.removeAllViews()

            // Перемешиваем список вариантов ответов для текущей загадки
            answersList.forEach { answer ->
                // Для каждого варианта создаем радиокнопку
                val radioButton = RadioButton(this)
                // Устанавливаем текст варианта ответа на радиокнопку
                radioButton.text = answer
                // Добавляем радиокнопку в радиогруппу
                radioGroup.addView(radioButton)
            }

            // Обновляем текст счетчика загадок
            textRiddleCount.text = "Загадка $numberAnswer из ${currentRiddles.size}"

            // Делаем видимыми элементы пользовательского интерфейса для отображения загадки и вариантов ответов
            textRiddle.visibility = View.VISIBLE
            radioGroup.visibility = View.VISIBLE
            btnCheck.visibility = View.VISIBLE
            textRiddleCount.visibility = View.VISIBLE
        } else {
            // Если все загадки отгаданы, скрываем кнопку проверки ответа
            btnCheck.visibility = View.GONE
            // Разрешаем кнопку статистики
            btnStats.isEnabled = true
            // Выводим сообщение, что все загадки отгаданы
            Toast.makeText(this, "Все загадки отгаданы", Toast.LENGTH_SHORT).show()
        }
    }


    // Проверка ответа на текущую загадку
    private fun checkAnswer() {
        // Получаем идентификатор выбранной радиокнопки
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId

        // Проверяем, была ли выбрана какая-либо радиокнопка
        if (checkedRadioButtonId != -1) {
            // Получаем ссылку на выбранную радиокнопку
            val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
            // Получаем текст выбранного ответа
            val selectedAnswer = checkedRadioButton.text.toString()

            // Получаем текущую загадку и правильный ответ
            val (_, answersList, correctAnswerIndex) = currentRiddles[currentRiddleIndex]
            val correctAnswer = answersList[correctAnswerIndex]

            // Сравниваем выбранный ответ с правильным и выводим сообщение
            if (selectedAnswer == correctAnswer) {
                Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
                // Увеличиваем счетчик правильных ответов
                correctAnswersCount++
            } else {
                Toast.makeText(this, "Неправильно!, привильный ответ $correctAnswer", Toast.LENGTH_SHORT).show()
                // Увеличиваем счетчик неправильных ответов
                incorrectAnswersCount++
            }

            // Показываем следующую загадку
            showNextRiddle()
        } else {
            // Если ответ не выбран, выводим сообщение
            Toast.makeText(this, "Выберите ответ", Toast.LENGTH_SHORT).show()
        }
    }


    // Показ статистики
    private fun showStats() {
        val totalRiddles = currentRiddles.size
        val correctAnswers = correctAnswersCount
        val incorrectAnswers = incorrectAnswersCount

        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("totalRiddles", totalRiddles)
        intent.putExtra("correctAnswers", correctAnswers)
        intent.putExtra("incorrectAnswers", incorrectAnswers)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем факт нажатия кнопки "Загадка" и счетчики правильных, неправильных ответов
        outState.putBoolean("isRiddleButtonClicked", isRiddleButtonClicked)
        outState.putInt("correctAnswersCount", correctAnswersCount)
        outState.putInt("incorrectAnswersCount", incorrectAnswersCount)
        outState.putInt("numberAnswer", numberAnswer)
        // Сохраняем текущий индекс загадки
        outState.putInt("currentRiddleIndex", currentRiddleIndex)
        // Сохраняем текущую загадку
        val currentRiddlesArray = currentRiddles.map { it.toList() }.toTypedArray()
        outState.putSerializable("currentRiddles", currentRiddlesArray)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Восстанавливаем сохраненные данные
        isRiddleButtonClicked = savedInstanceState.getBoolean("isRiddleButtonClicked")
        correctAnswersCount = savedInstanceState.getInt("correctAnswersCount")
        incorrectAnswersCount = savedInstanceState.getInt("incorrectAnswersCount")
        currentRiddleIndex = savedInstanceState.getInt("currentRiddleIndex")
        numberAnswer = savedInstanceState.getInt("numberAnswer")

        // Восстанавливаем текущую загадку
        val currentRiddlesArray = savedInstanceState.getSerializable("currentRiddles") as Array<List<Any>>
        currentRiddles = currentRiddlesArray.map { it.toTriple() }

        // Отображаем или скрываем кнопку "Загадка" в зависимости от сохраненного значения
        if (isRiddleButtonClicked) {
            btnRiddle.visibility = View.GONE
        } else {
            btnRiddle.visibility = View.VISIBLE
        }

        // Отображаем текущую загадку
        showCurrentRiddle()
    }

    // Метод расширения для преобразования списка элементов в Triple
    private fun List<Any>.toTriple(): Triple<String, List<String>, Int> {
        return Triple(this[0] as String, this[1] as List<String>, this[2] as Int)
    }

    // Отображение текущей загадки без изменения порядка вариантов ответов
    private fun showCurrentRiddle() {
        if (currentRiddleIndex < currentRiddles.size) {
            val (riddle, answersList) = currentRiddles[currentRiddleIndex]
            textRiddle.text = riddle
            radioGroup.removeAllViews()
            answersList.forEach { answer ->
                val radioButton = RadioButton(this)
                radioButton.text = answer
                radioGroup.addView(radioButton)
            }
            textRiddleCount.text = "Загадка $numberAnswer из ${currentRiddles.size}"
            textRiddle.visibility = View.VISIBLE
            radioGroup.visibility = View.VISIBLE
            btnCheck.visibility = View.VISIBLE
            textRiddleCount.visibility = View.VISIBLE
        } else {
            btnCheck.visibility = View.GONE
            btnStats.isEnabled = true
            btnStats.alpha = 1.0f
            Toast.makeText(this, "Все загадки отгаданы", Toast.LENGTH_SHORT).show()
        }
    }
}