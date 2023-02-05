package com.smuzdev.lab_11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    val TAG = "LAB_11_DEBUG"
    //2. Определение переменных и констант

    //2a Определите несколько переменных с val и var с явным
    //    указанием типа и без (Int, Double, String) на уровне файла.
    var age:Int = 19;
    var name:String = "Darya"
    var faculty = "IT"

    val USER_NAME_FIELD = "UserName";
    val UNIVERSITY = "BELSTU"

    val MEMORY_SIZE = 8096.0000000;

    //2b. Выполните преобразования переменных из типа Byte в
    //    Int, из Int в String.
    val ByteToInt = Byte.MAX_VALUE.toInt()
    val IntToStr = Int.MAX_VALUE.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //2c. Выведите их значения на консоль через строковый литерал
        //    с текстом и с ссылкой на переменные.
        Log.d(TAG, "---------- TASK 2c ---------- ");
        Log.d(TAG, "Age: $age")
        Log.d(TAG, "Name: $name")
        Log.d(TAG, "Faculty: $faculty")

        // 2d. Объявите константу.
        val CONSTANT_STRING = "I'm a constant"

        //2e. Объявите переменную типа Int? Введите с консоли число
        //    (или пустую строку)
        var input: Int? = null //No more NullPointerException


        /*3. Функции, расширения

        3a.
        Напишите функцию sum с переменным числом аргументов типа Double,
        которая суммирует все переданные значения.
         */
        //vararg - принимать переменное количество параметров одного типа
        fun SumDouble(vararg numbers: Double) {
            var result = 0.0;
            for (num in numbers)
                result += num

            Log.d(TAG, "Sum is $result")
        }
        Log.d(TAG, "---------- TASK 3a ---------- ");
        Log.d(TAG, SumDouble(23.4,23.5).toString());
        /*
        3b.
        Напишите функцию isValid проверки корректности ввода логина – пароля (параметры функции).
        Логин должно иметь формат email.
        Пароль от 6 до 12 символов, без пробелов. Логин-пароль не могут быть пустыми.
        Для этой проверки напишите локальную функцию notNull. Функция notNull должна иметь
        тело-выражение (без блока) на основе if.

        */

        fun isValid(login: String, password: String): Boolean {
            val notNull: () -> Boolean = { login.isEmpty() && password.isEmpty() }

            return (!notNull() &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches() &&
                    password.trim().length >= 6 &&
                    password.trim().length <= 12)
        }

        Log.d(TAG, "---------- TASK 3b ---------- ");
        Log.d(TAG, isValid("abc@gmail.com", "Password").toString()); //true
        /*

        3c.
        Задайте через перечисление праздничные дни в году.
        Напишите функцию с использованием when для проверки по введенной дате (день, месяц, год) - будний или праздничный день.
        Предусмотрите вариант, когда пользователь передал null строку или формат не соответствует.

        */

        var christmas = Holdidays.Christmas;
        Log.d(TAG, "---------- TASK 3с ---------- ");
        christmas.isHoliday() //true

        /*

        3d.
        Допишите функцию
        fun doOperation (a:Int , b:Int, operation:Char): Double
        Используя конструкцию when, вычислите значение операции, указанной в operation.
        Функция должна корректно обрабатывать любую допустимую в Kotlin бинарную операцию.
        Если операция допустима – генерируйте исключение. Продемонстрируйте работу функции.

        */

        fun doOperation(a: Int, b: Int, operation: Char): Double {
            if (operation != '+' && operation != '-' && operation != '*' && operation != '/')
                throw Exception();

            when (operation) {
                '+' -> return a.toDouble() + b.toDouble();
                '-' -> return a.toDouble() - b.toDouble();
                '*' -> return a.toDouble() * b.toDouble();
                '/' -> return a.toDouble() / b.toDouble();
                else -> return 0.0;
            }
        }

        Log.d(TAG, "---------- TASK 3d ---------- ");
        Log.d(TAG, doOperation(10, 10, '+').toString())
        Log.d(TAG, doOperation(100, 5, '-').toString())
        Log.d(TAG, doOperation(20, 2, '*').toString())
        Log.d(TAG, doOperation(20, 5, '/').toString())

        /*
        3e
        Реализуйте функцию indexOfMax(), чтобы она возвращала индекс самого большого элемента в массиве,
        или null, если массив пуст или таких элементов несколько. Сделайте ее потом функцией расширения для IntArray

        fun indexOfMax(a: IntArray): Int? {}
        */

        Log.d(TAG, "---------- TASK 3e ---------- ");

        fun indexOfMax(a: IntArray): Int? {
            var maxElem: Int? = a.maxOrNull();
            return maxElem;
        }

        Log.d(TAG, indexOfMax(intArrayOf()).toString())
        Log.d(TAG, indexOfMax(intArrayOf(1, 2, 3, 4, 5, 100, 4)).toString())

        //функция расщирения
        fun IntArray.indexOfMax(a: IntArray): Int? {
            var maxElem: Int? = a.maxOrNull();
            return maxElem;
        }
        /*
        3f.
        Напишите функцию расширения coincidence для String, которая проверяет сколько позиций совпало
        со строкой передаанной в аргументе и возвращает количеством совпавших символов.
         */
        fun String.coincidence(str: String): Int {
            var i = 0;
            var sameSymnCount = 0;
            for (symb in str) {
                if (this.get(i) == symb) {
                    sameSymnCount++
                }
                i++
            }
            return sameSymnCount
        }


        Log.d(TAG, "---------- TASK 3f ---------- ");
        val sameSymbolsCheck = "Hello"
        Log.d(TAG, sameSymbolsCheck.coincidence("He").toString())

        /*
        4a
        Определите функцию вычисления факториала в двух вариантах:
        1) с циклом и диапазоном
        2) рекурсивную на основе индуктивного определения
        fun factorial(n: Int): Double
         */

        Log.d(TAG, "---------- TASK 4a ---------- ");

        //4a 1) с циклом и диапазоном
        fun factorialCycle(number: Int): Double {
            var result = 1.0;
            var calculated_num = number;

            while (calculated_num > 1) {
                result *= calculated_num;
                calculated_num--
            }

            return result;
        }

        Log.d(TAG, factorialCycle(5).toString())

        //4a 2) рекурсивную на основе индуктивного определения
        fun factorialRecursive(n: Int): Double = if (n < 2) 1.0 else n * factorialRecursive(n - 1)
        Log.d(TAG, factorialRecursive(5).toString())


        /*

        4b.
        Определите функцию isPrime, проверки является ли число простым.
        (Напишите ее оптимально. В частности, достаточно проверить делимость числа n на все числа в интервале от 2 до n/2,
        так как на большие числа n всё равно делится не будет. Достаточно ограничится интервалом от 2 до √n — если n и делится
        на какое-то большее √n число (например, 50 делится на 10), то оно будет делится и на какое-то меньшее число (в данном случае, 50 делится на 5=50/10).)

         */

        fun isPrime(num: Int) {
            var flag = false
            for (i in 2..num / 2) {
                // condition for nonprime number
                if (num % i == 0) {
                    flag = true
                    break
                }
            }

            if (!flag)
                println("$num is a PRIME")
            else
                println("$num is NOT a PRIME")
        }

        Log.d(TAG, "---------- TASK 4b ---------- ");
        isPrime(199).toString()


        /*

        5a.
        Напишите 2 лямбды для передачи в функцию, чтобы проверить, содержит ли задуманное коллекция.
        Функция any получает предикат в качестве аргумента и возвращает true, если хотя бы один элемент удовлетворяет предикату.

        */

        var number = 11;
        fun containsIn(collections: Collection<Int>):Boolean = collections.any{collections.contains(0) && collections.contains(1)}
        var nums = listOf(0, 1, 2, 3, 4, 5, 6, 7, 10, 11, 12, 12)
        var nums_2 = listOf(0, 2)

        Log.d(TAG, "---------- TASK 5a ---------- ");

        Log.d(TAG, containsIn(nums).toString()) //true
        Log.d(TAG, containsIn(nums_2).toString()) //false


        /*
        5b.
        Используя listOf сформируйте список целых. Примените несколько разных способов добавления элемента в коллекцию (add, +=).
        Оставьте только уникальные элементы.
        Отфильтруйте и оставьте только нечетные.
        Выведите элементы через forEach.
        Передайте ссылку на функцию проверки на простое число в filter для проверки элементов списка.
        val numbers = listOf(1, 2, 3,.....)
        println(numbers.filter(::isPrime))
        Примените к списку find, groupBy, all, any
        Выполните деструктуризацию первых 2-х элементов списка.

         */

        var numbers = listOf (10, 20, 30, 1, 2, 3, 4, 5, 5, 1, 2);
        numbers += 999

        //Оставьте только уникальные элементы.
        var set: Set<Int> = LinkedHashSet(numbers);

        //Отфильтруйте и оставьте только нечетные.
        var only_even_numbers = numbers.filter { it % 2 != 0 }

        //Выведите элементы через forEach.
        Log.d(TAG, "---------- TASK 5b ---------- ");
        for (i in set)
            Log.d(TAG, i.toString());

        //Примените к списку find, groupBy, all, any
        numbers.find { it == 2 }
        numbers.groupBy { it }
        numbers.all { it > 10}
        numbers.any { it < 20}



        //5c
        var mapMarks = mapOf("Alex" to 35, "Andrey" to 32, "Anton" to 29, "Gleb" to 25, "John" to 22)

        mapMarks.mapValues {
            when {

                it.value in 35..37 -> { Log.d(TAG, "${it.key} - mark is 7") }
                it.value in 32..34 -> { Log.d(TAG, "${it.key} - mark is 6") }
                it.value in 29..31 -> { Log.d(TAG, "${it.key} - mark is 5") }
                it.value in 25..28 -> { Log.d(TAG, "${it.key} - mark is 4") }
                it.value in 22..24 -> { Log.d(TAG, "${it.key} - mark is 3") }
                it.value in 19..21 -> { Log.d(TAG, "${it.key} - mark is 2") }
                it.value in 0..18 -> { Log.d(TAG, "${it.key} - mark is 1") }
                else -> Log.d("tag", "404")
            }
        }

        Log.d(TAG, "---------- TASK 5c ---------- ");
        mapMarks.forEach { (key, value) ->  }


    }

    //  3c.
    enum class Holdidays(val date: String) {
        Christmas("25.12"),
        New_Year("31.12"),
        Independence_Day_of_the_Republic_of_Belarus("03.07"),
        Victory_Day("09.05");

        fun isHoliday() {
            val TAG = "LAB_11_DEBUG"

            if(date.isEmpty() || date.length!=5) {
                throw Exception()
            }

            else {
                when(date) {
                    Christmas.date -> Log.d(TAG, "Is's Christmas!")
                    New_Year.date -> Log.d(TAG, "It's New Year!")
                    Victory_Day.date -> Log.d(TAG, "It's Victory Day!")
                    Independence_Day_of_the_Republic_of_Belarus.date -> Log.d(TAG, "It's Independence Day of the Republic of Belarus!")

                }
            }
        }

    }


}

