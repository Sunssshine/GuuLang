# Сборка
### Требования
* gradle
* jvm 8 версии, к сожалению, TornadoFX не поддерживает jvm старше 8 версии, скорее всего из-за удаления из более старших версий пакета JavaFX.
* Я бы действительно сильно рекомендовал собирать это в Intellij Idea просто потому, что там оно уже однажды точно собралось и в репозитории лежат готовые конфигурации для сборки :), однако ничего не мешает использовать только gradle

### Процесс

* Импортировать проект в IntelliJ IDEA
* Настроить проект с учетом требований
* Запустить конфигурацию build или run в зависимости от целей

ИЛИ 
// В корневой директории проекта
* gradle build
* gradle jar
* (optional) gradle test
* Запустить полученный .jar файл, который будет находиться в папке ./build/libs

### P.S.: В директории, на всякий случай, в директории builds находится собранный .jar файл, но я надеюсь до этого не дойдёт :)

---

# Использование

### Выполнение программы:
* Напишите исходный текст в поле для исходного текста программы на языке Goo (расположена под блоком управляющих кнопок)
* Выберите желаемые опции, отметив нужные чекбоксы (см. описание управляющих элементов)
* Нажмите кнопку "Execute"
* Если требуется, управляйте потоком выполнения программы посредством описанных ниже элементов.

### Интерфейс программы:
![Интерфейс GooLang дебаггера](https://i.imgur.com/TZjPTQM.png)


### Описание элементов вывода информации:
* **Output**: Read-only поле ввода расположенное под меткой **Output** содержит в себе всю информацию получаемую в процессе выполнения программы, например вывод функции *print* или отладочную информацию запрошенную пользователем в процессе выполнения в режиме отладки (список переменных, stack trace).
* **Errors**: Read-only поле ввода расположенное под меткой **Errors** содержит в себе информацию об ошибках возникших в процессе выполнения программы, а также об ошибках возникших в процессе взаимодействия дебаггера с программой (Выполнение следующей функции у завершенной программы, инфомация о неожиданных для парсера токенах)
* Текстовое поле расположенное под элементом ввода исходного текста программы: содержит информацию о следующей инструкции при пошаговом выполнении программы.

### Описание управляющих элементов:
* Чекбокс **"Debug"** -  отвечает за выполнение в режиме отладки, если во время нажатия кнопки "Execute" он был отмечен, программа будет выполнена пошагово начиная с первой инструкции в функции main. В красной области под полем ввода отображается, какая следующая инструкция будет выполнена при использовании кнопок управления потоком выполнения. Если этот чекбокс не был отмечен, программа выполнится целиком, или до первой ошибки, в зависимости от выбранных вами параметров.
* Чекбокс **"Ignore errors"** - отвечает за игнорирование ошибок в процессе выполнения программы, если он отмечен - все ошибки возникающие в процессе выполнения программы будут проигнорированы, а вызвавшие ошибку инструкции опущены. **Обратите внимание**, если этот чекбокс не был отмечен, первая встреченная интерпретатором ошибка вызовет падение программы и невозможность её дальнейшего исполнения.
* Кнопка **"Execute"** - применяет выбранные посредством чекбоксов параметры, инициализирует среду выполнения программы, и выполняет её либо полностью, либо предоставляет пользователю возможность выполнять программу пошагово начиная с первой инструкции в main.
* Кнопка **"Stack trace"** - выводит в **Output** блок информацию о текущем состоянии стека вызовов функций.
* Кнопка **"Variables"** - выводит в **Output** блок информацию о текущих инициализированных переменных и их значениях
* Кнопка **"Step into"** - выполняет следующую инструкцию, посмотреть на которую можно в блоке отображения следующей инструкции, с заходом, если эта инструкция *call*
* Кнопка **"Step over"** - выполняет следующую инструкцию, если следующая инструкция - *call* - выполняет её без участия дебаггера, управление пользователю передаётся сразу после выхода из вызванной функции.


---

# Описание языка Guu

Программа на Guu состоит из набора процедур. Каждая процедура начинается со строки sub <subname> и завершается объявлением другой процедуры (или концом файла, если процедура в файле последняя). Исполнение начинается с sub main.

Тело процедуры – набор инструкций, каждая из которых находится на отдельной строке. В начале строки могут встречаться незначимые символы табуляции или пробелы. Пустые строки игнорируются. Комментариев в Guu нет.

В Guu есть лишь три оператора: - set <varname> <new value> – задание нового целочисленного значения переменной. - call <subname> – вызов процедуры. Вызовы могут быть рекурсивными. - print <varname> – печать значения переменной на экран.

Переменные в Guu имеют глобальную область видимости.


---

# Пример программы на языке Guu
sub hello\
    print somevar\
    set somevar 42\
    print somevar\
    print wtf\
\
sub main\
    set somevar 13\
    call hello\
    set smth 422\
    print 1337\
    call wtf\
\
sub wtf\
    call hello

# Известные проблемы
* Рекурсия работает слишком хорошо, нужно запускать выполнение в корутине, но я не успел :с
* По окончанию выполнения программы переменные и стек вызовов всё ещё доступны для получения из среды
* Интерфейс абсолютно не адаптивный (Спешка + Отсутствие опыта в JavaFX, TornadoFX)
* Тесты не покрывают кейсы проверки валидности получения Stack trace и variables, у меня не хватило времени, и я не уверен можно ли вносить изменения после отправки ссылки на репозиторий, поэтому оставлю так, прошу прощения за это.
