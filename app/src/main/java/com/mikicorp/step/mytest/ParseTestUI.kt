package com.mikicorp.step.mytest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikicorp.step.R
import com.mikicorp.step.lib.TextListItemType
import com.mikicorp.step.lib.TextListTypes
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ParseTestUI(val context: Context?, private var docList: MutableList<DocToList>) {
    private var myTest = arrayListOf<MyTest>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onSubmit: (docList: DocToList) -> Unit = {},
        onBackPressed: () -> Unit = {},
        onClose: () -> Unit = {}
    ) {
        val coroutineScope = rememberCoroutineScope()
        val scroll = rememberLazyListState(0)
        val listIndex = remember { mutableIntStateOf(0) }
        var selectedAnswer by remember { mutableIntStateOf(-1) }
        val nextQuestion = remember { mutableIntStateOf(-1) }
        val prevQuestion = remember { mutableIntStateOf(-1) }
        val enabledSendButton = remember { mutableStateOf(false) }
        var imageBase64 by remember { mutableStateOf("") }
        var image by remember {
            mutableStateOf<Bitmap?>( null)
        }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            val bm = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(uri!!))
            val stream = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            val byteFormat = stream.toByteArray()
            imageBase64 = Base64.encodeToString(byteFormat, Base64.DEFAULT)
            val imageBytes = Base64.decode(imageBase64, 0)
            image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.create_test),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onBackPressed()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = ""
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                            },
                            enabled = enabledSendButton.value
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DoneAll,
                                tint = if (enabledSendButton.value)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.onSecondary,
                                contentDescription = ""
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        image?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(100.dp)
                                    .width(100.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Inside,
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn {
                            items(getQuestion(listIndex.intValue)) { question ->
                                Text(
                                    text = when (question.listType.value) {
                                        TextListTypes.Letters -> question.str.drop(2).trim()
                                        TextListTypes.Numeric -> question.str.drop(2).trim()
                                        else -> question.str
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        LazyColumn {
                            items(
                                getAnswers(
                                    listIndex.intValue,
                                    prevQuestion,
                                    nextQuestion
                                )
                            ) { answer ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .border(
                                            if (answer.idx == selectedAnswer) 2.dp else 0.dp,
                                            if (answer.idx == selectedAnswer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                        .selectable(
                                            selected = answer.idx == selectedAnswer,
                                            onClick = {
                                                checkForValidity(enabledSendButton)
                                                selectedAnswer =
                                                    if (selectedAnswer != answer.idx)
                                                        answer.idx else -1
                                            })
                                        .padding(5.dp, 10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        imageVector = if (answer.idx == selectedAnswer)
                                            Icons.Filled.RadioButtonChecked
                                        else
                                            Icons.Filled.RadioButtonUnchecked,
                                        tint = MaterialTheme.colorScheme.primary,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = when (answer.listType.value) {
                                            TextListTypes.Letters -> answer.str.drop(2).trim()
                                            TextListTypes.Numeric -> answer.str.drop(2).trim()
                                            else -> answer.str
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Button(
                            onClick = {
                                if (prevQuestion.intValue >= 0) {
                                    listIndex.intValue = prevQuestion.intValue
                                    coroutineScope.launch {
                                        scroll.animateScrollToItem(listIndex.intValue)
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ChevronLeft,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        Button(
                            onClick = {
                                launcher.launch("image/*")
                            },
                            modifier = Modifier
                                .size(65.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Attachment,
                                modifier = Modifier.rotate(135f),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = "Attach Photo"
                            )
                        }

                        Button(
                            onClick = {
                                if (nextQuestion.intValue >= 0) {
                                    listIndex.intValue = nextQuestion.intValue
                                    coroutineScope.launch {
                                        scroll.animateScrollToItem(listIndex.intValue)
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = if (selectedAnswer >= 0)
                                    Icons.Filled.Check
                                else
                                    Icons.Filled.ChevronRight,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        )
    }

    private fun checkForValidity(enabledSendButton: MutableState<Boolean>) {
        var lastItemType = TextListItemType.Answer
        docList.forEach { item ->
            if (item.isQuestion.value) {
                if (lastItemType == TextListItemType.Answer) {
                    myTest.add(MyTest(question = ""))
                }
                myTest.last().question += item.str
                lastItemType = TextListItemType.Question
            }
            if (item.isAnswer.value) {
                myTest.last().answers.add(
                    MyAnswer(
                        answer = item.str,
                        image = "",
                        correct = item.isCorrect.value
                    )
                )
            }
        }
        var valid = true
        myTest.forEach { item ->
            if (!item.answers.any { it.correct }) valid = false
        }
        enabledSendButton.value = valid
    }

    private fun getQuestion(listIndex: Int): ArrayList<DocToList> {
        val result = arrayListOf<DocToList>()
        var index = listIndex
        while (docList[index].isQuestion.value || docList[index].disabled.value) {
            if (docList[index].isQuestion.value) result.add(docList[index])
            index++
        }
        return result
    }

    private fun getAnswers(
        listIndex: Int,
        prevQuestion: MutableState<Int>,
        nextQuestion: MutableState<Int>
    ): ArrayList<DocToList> {
        var index = listIndex
        var found = false
        while (true) {
            index--
            if (index < 0) {
                if (found)
                    prevQuestion.value = 0
                else
                    prevQuestion.value = -1
                break
            } else {
                if (docList[index].isAnswer.value)
                    if (found) {
                        prevQuestion.value = index + 1
                        break
                    } else {
                        continue
                    }
                if (docList[index].isQuestion.value) {
                    found = true
                }
            }
        }
        val result = arrayListOf<DocToList>()
        index = listIndex - 1
        found = false
        while (true) {
            index++
            if (index >= docList.size) {
                nextQuestion.value = -1
                break
            } else {
                if (docList[index].isAnswer.value) {
                    found = true
                    result.add(docList[index])
                }
                if (docList[index].isQuestion.value)
                    if (found) {
                        nextQuestion.value = index
                        break
                    } else {
                        continue
                    }
            }
        }
        return result
    }
}

@Preview
@Composable
fun ParseTestUIPreview() {
    val ddd = "Кинематика: Перемещение, Путь, Равномерное движение\n" +
            "Тело переместилось из точки с координатами (0,3) (м) в точку с координатами (3, 1) (м). Найдите модуль перемещения тела.\n" +
            "A. 3 м\n" +
            "B. 4 м\n" +
            "C. 5 м\n" +
            "D. 6 м\n" +
            "Правильный ответ: C\n" +
            "Мяч упал с высоты 3 м, отскочил от пола и был пойман после отскока на высоте 1 м. Во сколько раз путь, пройденный мячом, больше модуля перемещения мяча?\n" +
            "A. 1\n" +
            "B. 2\n" +
            "C. 3\n" +
            "D. 4\n" +
            "Правильный ответ: B\n" +
            "Самолет пролетел по прямой 600 км, затем повернул под прямым углом и пролетел еще 800 км. Чему равен модуль вектора перемещения (в км) самолета?\n" +
            "A. 900 км\n" +
            "B. 1000 км\n" +
            "C. 1100 км\n" +
            "D. 1200 км\n" +
            "Правильный ответ: B\n" +
            "Человек прошел по проспекту 240 м, затем повернул на перекрестке и прошел в перпендикулярном направлении еще 70 м. На сколько процентов путь, пройденный человеком, больше модуля его перемещения?\n" +
            "A. 10%\n" +
            "B. 20%\n" +
            "C. 24%\n" +
            "D. 30%\n" +
            "Правильный ответ: C\n" +
//            "Тело начало двигаться вдоль оси x с постоянной скоростью 6 м/с из точки, имеющей координату x0 = –7 м. Через сколько секунд координата тела окажется равной 5 м?\n" +
//            "A. 1 с\n" +
//            "B. 2 с\n" +
//            "C. 3 с\n" +
//            "D. 4 с\n" +
//            "Правильный ответ: B\n" +
//            "Пешеход переходил дорогу со скоростью 4,2 км/ч по прямой, составляющей угол 30° с направлением дороги, в течение одной минуты. Определите ширину дороги.\n" +
//            "A. 30 м\n" +
//            "B. 35 м\n" +
//            "C. 40 м\n" +
//            "D. 45 м\n" +
//            "Правильный ответ: B\n" +
//            "Со станции вышел товарный поезд, идущий со скоростью 20 м/с. Через 10 минут по тому же направлению вышел экспресс, скорость которого 30 м/с. На каком расстоянии (в км) от станции экспресс нагонит товарный поезд?\n" +
//            "A. 10 км\n" +
//            "B. 20 км\n" +
//            "C. 30 км\n" +
//            "D. 36 км\n" +
//            "Правильный ответ: D\n" +
//            "Спортсмены бегут колонной длиной 20 м с одинаковой скоростью 3 м/с. Навстречу бежит тренер со скоростью 1 м/с. Каждый спортсмен, поравнявшись с тренером, бежит назад с прежней скоростью. Какова будет длина колонны, когда все спортсмены развернутся?\n" +
//            "A. 5 м\n" +
//            "B. 10 м\n" +
//            "C. 15 м\n" +
//            "D. 20 м\n" +
//            "Правильный ответ: B\n" +
//            "С подводной лодки, погружающейся равномерно, испускаются звуковые импульсы длительностью 30,1 с. Длительность импульса, принятого на лодке после его отражения от дна, равна 29,9 с. Определите скорость погружения лодки. Скорость звука в воде 1500 м/с.\n" +
//            "A. 2 м/с\n" +
//            "B. 3 м/с\n" +
//            "C. 4 м/с\n" +
//            "D. 5 м/с\n" +
//            "Правильный ответ: D\n" +
//            "Относительность движения: Сложение скоростей\n" +
//            "Определите скорость течения (в км/ч), если скорость теплохода вниз по реке равна 22 км/ч, а вверх 18 км/ч.\n" +
//            "A. 1 км/ч\n" +
//            "B. 2 км/ч\n" +
//            "C. 3 км/ч\n" +
//            "D. 4 км/ч\n" +
//            "Правильный ответ: B\n" +
//            "Скорость мотоциклиста 54 км/ч, а скорость встречного ветра 3 м/с. Какова скорость ветра в системе отсчета, связанной с мотоциклистом? В ответе дайте модуль скорости.\n" +
//            "A. 12 км/ч\n" +
//            "B. 15 км/ч\n" +
//            "C. 18 км/ч\n" +
//            "D. 21 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Пассажир поезда, движущегося равномерно со скоростью 54 км/ч, видит в течение 60 с другой поезд длиной 300 м, который движется по соседнему пути в том же направлении с большей скоростью. Найдите скорость (в км/ч) второго поезда.\n" +
//            "A. 60 км/ч\n" +
//            "B. 66 км/ч\n" +
//            "C. 72 км/ч\n" +
//            "D. 78 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Сколько секунд пассажир, стоящий у окна поезда, идущего со скоростью 54 км/ч, будет видеть проходящий мимо него встречный поезд, скорость которого 36 км/ч, а длина 150 м?\n" +
//            "A. 3 с\n" +
//            "B. 4 с\n" +
//            "C. 5 с\n" +
//            "D. 6 с\n" +
//            "Правильный ответ: D\n" +
//            "Автомобиль, двигаясь со скоростью 45 км/ч, в течение 10 с прошел такой же путь, какой автобус, двигающийся в том же направлении с постоянной скоростью, прошел за 15 с. Найдите величину их относительной скорости (в км/ч).\n" +
//            "A. 10 км/ч\n" +
//            "B. 15 км/ч\n" +
//            "C. 20 км/ч\n" +
//            "D. 25 км/ч\n" +
//            "Правильный ответ: B\n" +
//            "По шоссе в одном направлении движутся два мотоциклиста. Скорость первого 10 м/с, второго 20 м/с. В начальный момент второй мотоциклист отстает от первого на 200 м. Через сколько секунд он его догонит?\n" +
//            "A. 10 с\n" +
//            "B. 15 с\n" +
//            "C. 20 с\n" +
//            "D. 25 с\n" +
//            "Правильный ответ: C\n" +
//            "Скорость лодки относительно воды в два раза больше скорости течения реки. Во сколько раз больше времени занимает поездка между двумя пунктами против течения, чем по течению?\n" +
//            "A. 1.5 раза\n" +
//            "B. 2 раза\n" +
//            "C. 2.5 раза\n" +
//            "D. 3 раза\n" +
//            "Правильный ответ: D\n" +
//            "Эскалатор метрополитена, двигаясь равномерно, поднимает неподвижно стоящего на нем пассажира в течение одной минуты. По неподвижному эскалатору пассажир, двигаясь равномерно, поднимается за 3 минуты. Сколько секунд будет подниматься пассажир по движущемуся вверх эскалатору?\n" +
//            "A. 40 с\n" +
//            "B. 45 с\n" +
//            "C. 50 с\n" +
//            "D. 55 с\n" +
//            "Правильный ответ: B\n" +
//            "В безветренную погоду для перелета из города А в город Б и обратно самолет затрачивает 8 часов полетного времени. На сколько минут увеличится это время, если все время полета будет дуть ветер со скоростью 20 м/с в направлении от А к Б? Скорость самолета относительно воздуха 312 км/ч.\n" +
//            "A. 15 минут\n" +
//            "B. 20 минут\n" +
//            "C. 27 минут\n" +
//            "D. 30 минут\n" +
//            "Правильный ответ: C\n" +
//            "Скорость лодки относительно воды равна 4 м/с и направлена перпендикулярно берегу, а скорость течения реки 3 м/с. Найдите скорость лодки относительно берега.\n" +
//            "A. 4 м/с\n" +
//            "B. 5 м/с\n" +
//            "C. 6 м/с\n" +
//            "D. 7 м/с\n" +
//            "Правильный ответ: B\n" +
//            "Катер, переправляясь через реку шириной 800 м, двигался со скоростью 4 м/с перпендикулярно течению реки в системе отсчета, связанной с водой. На сколько будет снесен катер течением, если скорость течения реки 1,5 м/с?\n" +
//            "A. 200 м\n" +
//            "B. 300 м\n" +
//            "C. 400 м\n" +
//            "D. 500 м\n" +
//            "Правильный ответ: B\n" +
//            "Самолет летел на север со скоростью 48 м/с относительно земли. С какой скоростью относительно земли будет лететь самолет, если подует западный ветер со скоростью 14 м/с?\n" +
//            "A. 48 м/с\n" +
//            "B. 50 м/с\n" +
//            "C. 52 м/с\n" +
//            "D. 54 м/с\n" +
//            "Правильный ответ: B\n" +
//            "Два велосипедиста едут со скоростями 10,8 км/ч и 14,4 км/ч по взаимно перпендикулярным дорогам. Чему равна их относительная скорость (в км/ч)?\n" +
//            "A. 10 км/ч\n" +
//            "B. 14 км/ч\n" +
//            "C. 18 км/ч\n" +
//            "D. 22 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Из пункта А по взаимно перпендикулярным дорогам одновременно выехали два автомобиля: один со скоростью 80 км/ч, другой — со скоростью 60 км/ч. С какой скоростью (в км/ч) они удаляются друг от друга?\n" +
//            "A. 60 км/ч\n" +
//            "B. 80 км/ч\n" +
//            "C. 100 км/ч\n" +
//            "D. 120 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Когда автобус стоит на остановке, капли дождя оставляют на боковом стекле вертикальные следы, а когда он едет со скоростью 72 км/ч, следы капель наклонены к вертикали под углом 30°. С какой скоростью падают капли дождя?\n" +
//            "A. 28 км/ч\n" +
//            "B. 32 км/ч\n" +
//            "C. 34 км/ч\n" +
//            "D. 36 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "При скорости ветра, равной 10 м/с, капли дождя падают под углом 30° к вертикали. При какой скорости ветра капли будут падать под углом 60° к вертикали?\n" +
//            "A. 20 м/с\n" +
//            "B. 25 м/с\n" +
//            "C. 30 м/с\n" +
//            "D. 35 м/с\n" +
//            "Правильный ответ: C\n" +
//            "При скорости ветра 20 м/с скорость капель дождя 40 м/с. Какой будет скорость капель при скорости ветра 5 м/с?\n" +
//            "A. 25 м/с\n" +
//            "B. 30 м/с\n" +
//            "C. 35 м/с\n" +
//            "D. 40 м/с\n" +
//            "Правильный ответ: C\n" +
//            "В безветренную погоду самолет затрачивает на перелет между городами 6 часов. На сколько минут увеличится время полета, если будет дуть боковой ветер со скоростью 20 м/с перпендикулярно линии полета? Скорость самолета относительно воздуха равна 328 км/ч.\n" +
//            "A. 5 минут\n" +
//            "B. 7 минут\n" +
//            "C. 9 минут\n" +
//            "D. 11 минут\n" +
//            "Правильный ответ: C\n" +
//            "Самолет, совершающий перелеты из города А в город Б и обратно, развивает в полете скорость 328 км/ч относительно воздуха. При боковом ветре, перпендикулярном линии полета перелет туда и обратно занял 6 часов полетного времени. На сколько минут дольше займет этот перелет, если ветер будет все время дуть в направлении от А к Б? Скорость ветра в обоих случаях 20 м/с.\n" +
//            "A. 5 минут\n" +
//            "B. 7 минут\n" +
//            "C. 9 минут\n" +
//            "D. 11 минут\n" +
//            "Правильный ответ: C\n" +
//            "При переправе через реку шириной 60 м надо попасть в точку, лежащую на 80 м ниже по течению, чем точка старта. Лодочник управляет моторной лодкой так, что она движется точно к цели со скоростью 8 м/с относительно берега. Какова при этом скорость лодки относительно воды, если скорость течения реки 2,8 м/с?\n" +
//            "A. 5 м/с\n" +
//            "B. 6 м/с\n" +
//            "C. 7 м/с\n" +
//            "D. 8 м/с\n" +
//            "Правильный ответ: C\n" +
//            "При переправе через реку шириной 80 м надо попасть в точку, лежащую на 60 м выше по течению, чем точка старта. Лодочник управляет моторной лодкой так, что она движется точно к цели со скоростью 4,5 м/с относительно берега. Какова при этом скорость лодки относительно воды, если скорость течения реки 2,1 м/с?\n" +
//            "A. 5 м/с\n" +
//            "B. 6 м/с\n" +
//            "C. 7 м/с\n" +
//            "D. 8 м/с\n" +
//            "Правильный ответ: B\n" +
//            "Скорость течения реки 5 м/с, ее ширина 32 м. Переправляясь через реку на лодке, скорость которой относительно воды 4 м/с, рулевой обеспечил наименьший возможный снос лодки течением. Чему равен этот снос?\n" +
//            "A. 20 м\n" +
//            "B. 22 м\n" +
//            "C. 24 м\n" +
//            "D. 26 м\n" +
//            "Правильный ответ: C\n" +
//            "Автомобиль приближается к пункту А со скоростью 80 км/ч. В тот момент, когда ему оставалось проехать 10 км, из пункта А в перпендикулярном направлении выезжает грузовик со скоростью 60 км/ч. Чему равно наименьшее расстояние (в км) между автомобилем и грузовиком?\n" +
//            "A. 4 км\n" +
//            "B. 5 км\n" +
//            "C. 6 км\n" +
//            "D. 7 км\n" +
//            "Правильный ответ: C\n" +
//            "Средняя скорость\n" +
//            "В течение первых 5 часов поезд двигался со средней скоростью 60 км/ч, а затем в течение 4 часов — со средней скоростью 15 км/ч. Найдите среднюю скорость (в км/ч) поезда за все время движения.\n" +
//            "A. 30 км/ч\n" +
//            "B. 35 км/ч\n" +
//            "C. 40 км/ч\n" +
//            "D. 45 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Велосипедист, проехав 4 км со скоростью 12 км/ч, остановился и отдыхал в течение 40 мин. Оставшиеся 8 км пути он проехал со скоростью 8 км/ч. Найдите среднюю скорость (в км/ч) велосипедиста на всем пути.\n" +
//            "A. 5 км/ч\n" +
//            "B. 6 км/ч\n" +
//            "C. 7 км/ч\n" +
//            "D. 8 км/ч\n" +
//            "Правильный ответ: B\n" +
//            "Велосипедист за первые 5 с проехал 35 м, за последующие 10 с — 100 м и за последние 5 с — 25 м. Найдите среднюю скорость движения на всем пути.\n" +
//            "A. 6 м/с\n" +
//            "B. 7 м/с\n" +
//            "C. 8 м/с\n" +
//            "D. 9 м/с\n" +
//            "Правильный ответ: C\n" +
//            "Первые 3/4 времени своего движения поезд шел со скоростью 80 км/ч, остальное время — со скоростью 40 км/ч. Какова средняя скорость (в км/ч) движения поезда на всем пути?\n" +
//            "A. 60 км/ч\n" +
//            "B. 65 км/ч\n" +
//            "C. 70 км/ч\n" +
//            "D. 75 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Первую половину пути автомобиль прошел со скоростью 40 км/ч, вторую — со скоростью 60 км/ч. Найдите среднюю скорость (в км/ч) автомобиля на всем пути.\n" +
//            "A. 45 км/ч\n" +
//            "B. 48 км/ч\n" +
//            "C. 50 км/ч\n" +
//            "D. 52 км/ч\n" +
//            "Правильный ответ: B\n" +
//            "Первую четверть пути автомобиль двигался со скоростью 60 км/ч, остальной путь — со скоростью 20 км/ч. Найдите среднюю скорость (в км/ч) автомобиля.\n" +
//            "A. 20 км/ч\n" +
//            "B. 22 км/ч\n" +
//            "C. 24 км/ч\n" +
//            "D. 26 км/ч\n" +
//            "Правильный ответ: C\n" +
//            "Катер прошел первую половину пути со средней скоростью в три раза большей, чем вторую. Средняя скорость на всем пути составляет 6 км/ч. Какова средняя скорость (в км/ч) катера на первой половине пути?\n" +
//            "A. 10 км/ч\n" +
//            "B. 12 км/ч\n" +
//            "C. 14 км/ч\n" +
//            "D. 16 км/ч\n" +
//            "Правильный ответ: B\n" +
//            "Автомобиль проехал первую половину пути со скоростью 60 км/ч. Оставшуюся часть пути он половину времени ехал со скоростью 35 км/ч, а последний участок — со скоростью 45 км/ч. Найдите среднюю скорость (в км/ч) автомобиля на всем пути.\n" +
//            "A. 40 км/ч\n" +
//            "B. 42 км/ч\n" +
//            "C. 44 км/ч\n" +
//            "D. 46 км/ч\n" +
//            "Правильный ответ: B\n" +
//            "Велосипедист проехал 3 км со скоростью 12 км/ч, затем повернул и проехал некоторое расстояние в перпендикулярном направлении со скоростью 16 км/ч. Чему равен модуль перемещения (в км) тела, если средняя скорость пути за все время движения равна 14 км/ч?\n" +
//            "A. 3 км\n" +
//            "B. 4 км\n" +
//            "C. 5 км\n" +
//            "D. 6 км\n" +
//            "Правильный ответ: C\n" +
//            "Первую половину времени тело движется со скоростью 30 м/с под углом 30° к заданному направлению, а вторую половину времени — под углом 120° к этому же направлению со скоростью 41 м/с. Найдите среднюю скорость (в см/с) перемещения тела вдоль заданного направления √3 = 1,7.\n" +
//            "A. 220 см/с\n" +
//            "B. 240 см/с\n" +
//            "C. 250 см/с\n" +
//            "D. 260 см/с\n" +
//            "Правильный ответ: C\n" +
//            "Равноускоренное движение\n" +
//            "Автомобиль, двигаясь равноускоренно, через 10 с после начала движения достиг скорости 36 км/ч. Найдите ускорение автомобиля.\n" +
//            "A. 1 м/с²\n" +
//            "B. 2 м/с²\n" +
//            "C. 3 м/с²\n" +
//            "D. 4 м/с²\n" +
//            "Правильный ответ: A\n" +
//            "Длина дорожки для взлета самолета 675 м. Какова скорость самолета при взлете, если он движется равноускоренно и взлетает через 15 с после старта?\n" +
//            "A. 60 м/с\n" +
//            "B. 70 м/с\n" +
//            "C. 80 м/с\n" +
//            "D. 90 м/с\n" +
//            "Правильный ответ: D\n" +
//            "Какую скорость приобретает ракета, движущаяся из состояния покоя с ускорением 6 м/с², на пути разгона 75 м?\n" +
//            "A. 20 м/с\n" +
//            "B. 25 м/с\n" +
//            "C. 30 м/с\n" +
//            "D. 35 м/с\n" +
//            "Правильный ответ: C\n" +
//            "Шар, двигаясь из состояния покоя равноускоренно, за первую секунду прошел путь 10 см. Какой путь (в см) он пройдет за 3 секунды от начала движения?\n" +
//            "A. 70 см\n" +
//            "B. 80 см\n" +
//            "C. 90 см\n" +
//            "D. 100 см\n" +
//            "Правильный ответ: C\n" +
//            "Во сколько раз скорость пули, прошедшей 1/4 часть ствола винтовки, меньше, чем при вылете из ствола? Ускорение пули считайте постоянным.\n" +
//            "A. В 2 раза\n" +
//            "B. В 3 раза\n" +
//            "C. В 4 раза\n" +
//            "D. В 5 раз\n" +
//            "Правильный ответ: B\n" +
//            "С какой скоростью двигался поезд до начала торможения, если тормозной путь он прошел за 30 с с ускорением 0,5 м/с²?\n" +
//            "A. 10 м/с\n" +
//            "B. 15 м/с\n" +
//            "C. 20 м/с\n" +
//            "D. 25 м/с\n" +
//            "Правильный ответ: B\n" +
//            "Какое расстояние пройдет автомобиль до полной остановки, если шофер резко тормозит при скорости 20 м/с, а от момента торможения до остановки проходит 6 с?\n" +
//            "A. 50 м\n" +
//            "B. 60 м\n" +
//            "C. 70 м\n" +
//            "D. 80 м\n" +
//            "Правильный ответ: D\n" +
//            "При аварийном торможении автомобиль, двигавшийся со скоростью 30 м/с, проходит тормозной путь с ускорением 5 м/с². Найдите тормозной путь.\n" +
//            "A. 80 м\n" +
//            "B. 90 м\n" +
//            "C. 100 м\n" +
//            "D. 110 м\n" +
//            "Правильный ответ: B\n" +
            "\n"
    CreateTestUI(null, ddd).UI()
}