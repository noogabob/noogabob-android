package com.example.noogabab.presentation.ui.start.createGroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.noogabab.R
import com.example.noogabab.presentation.dialog.CreateGroupDialog

import com.example.noogabab.presentation.entity.PresenterBabTime
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_time_line.*
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateGroupActivity : AppCompatActivity() {
    private lateinit var adapter: BabTimeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        loadDogKind()
        loadBabTime()
        addBabTime()
        clickBabTime()
        getKeyClick()
    }

    private fun getKeyClick() {
        btn_get_key.setOnClickListener {
            val dialog = CreateGroupDialog(this@CreateGroupActivity)
            CoroutineScope(Main).launch {
                dialog.show()
                delay(2000)
                dialog.progress_dialog.visibility = View.INVISIBLE
                dialog.btn_dialog_close.visibility = View.VISIBLE
                dialog.txt_dialog_content.text = "발급 완료!"
                dialog.txt_dialog_key.visibility = View.VISIBLE
                dialog.btn_dialog_clone.visibility = View.VISIBLE
            }
//            AwesomeDialog.build(this)
//                .title("Congratulations")
//                .body("Your New Account has been created")
//                .onPositive("Go To My Account") {
//                    Log.d("TAG", "positive ")
//                }
        }
    }

    private fun loadDogKind() {
        val dogs = resources.getStringArray(R.array.dogs)
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogs)
        edit_dog_kind.setAdapter(arrayAdapter)
    }

    private fun loadBabTime() {
        adapter = BabTimeListAdapter()
        adapter.addItem(PresenterBabTime("첫 끼", "-", "-"))
        list_view_bab_time.adapter = adapter
    }

    private fun addBabTime() {
        btn_add_bab_time.setOnClickListener {
            val size = adapter.count
            var bab = "두 끼"
            if (size == 2) bab = "세 끼"
            if (size == 3) Toast.makeText(this, "세끼가 최대야", Toast.LENGTH_SHORT).show()
            else {
                adapter.addItem(PresenterBabTime(bab, "-", "-"))
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickBabTime() {
        list_view_bab_time.setOnItemClickListener { parent, view, p, id ->
            val item = parent.getItemAtPosition(p) as PresenterBabTime
            val timePicker = MaterialTimePicker.Builder()
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .build()
            timePicker.show(supportFragmentManager, "createGroup")
            timePicker.addOnPositiveButtonClickListener {
                var meridiem = "오전"
                var hour: Int = timePicker.hour
                val minute = timePicker.minute
                if (hour > 12) {
                    hour -= 12
                    meridiem = "오후"
                }
                val strHour = if (hour.toString().length == 1) "0$hour" else hour.toString()
                val strMinute = if (minute.toString().length == 1) "0$minute" else minute.toString()
                adapter.setItem(p, item.bab, meridiem, "${strHour}:${strMinute}")
                adapter.notifyDataSetChanged()
            }
        }
    }
}