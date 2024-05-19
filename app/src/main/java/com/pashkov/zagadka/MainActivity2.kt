package com.pashkov.zagadka

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Получаем данные о загадках и ответах из предыдущей активности
        val totalRiddles = intent.getIntExtra("totalRiddles", 0)
        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val incorrectAnswers = intent.getIntExtra("incorrectAnswers", 0)

        // Находим элементы текстового представления для отображения статистики
        val textTotalRiddles = findViewById<TextView>(R.id.text_total_riddles)
        val textCorrectAnswers = findViewById<TextView>(R.id.text_correct_answers)
        val textIncorrectAnswers = findViewById<TextView>(R.id.text_incorrect_answers)

        // Устанавливаем текст статистики загадок и ответов
        textTotalRiddles.text = "Полученные загадки: $totalRiddles"
        textCorrectAnswers.text = "Правильные ответы: $correctAnswers"
        textIncorrectAnswers.text = "Неправильные ответы: $incorrectAnswers"

        // Находим кнопку "Назад" и устанавливаем для нее обработчик нажатия
        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Закрываем текущую активность и возвращаемся к предыдущей
        }

        // Находим кнопку "Новая сессия" и устанавливаем для нее обработчик нажатия
        val btnNewSession = findViewById<Button>(R.id.btn_new_session)
        btnNewSession.setOnClickListener {
            startNewSession() // Запускаем новую сессию
        }
    }

    // Функция для запуска новой сессии (активности с загадками)
    private fun startNewSession() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent) // Запускаем активность с загадками
        finish() // Закрываем текущую активность
    }
}
