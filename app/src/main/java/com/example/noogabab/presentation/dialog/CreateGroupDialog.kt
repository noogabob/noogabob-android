package com.example.noogabab.presentation.dialog

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.noogabab.R
import kotlinx.android.synthetic.main.dialog_create_group.*

class CreateGroupDialog constructor(context: Context, private val onclickCopy: () -> Unit): Dialog(context), View.OnClickListener {
    init {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_group)

        val size = context.resources.displayMetrics
        window!!.attributes.width = (size.widthPixels * 0.7).toInt()
        window!!.attributes.height = (size.heightPixels * 0.441).toInt()
        window!!.setBackgroundDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.dialog_background, null))

        btn_dialog_close.setOnClickListener(this)
        btn_dialog_clone.setOnClickListener(this)
    }

    fun setDialog(progress: Boolean, btnClose: Boolean, description: String?, key: String?) {
        progress_dialog.visibility = if (progress) View.VISIBLE else View.INVISIBLE
        btn_dialog_close.visibility = if (btnClose) View.VISIBLE else View.INVISIBLE
        txt_dialog_content.text = description
        btn_dialog_clone.visibility = if(description != null) View.VISIBLE else View.INVISIBLE
        txt_dialog_key.text = key
        txt_dialog_key.visibility = if (key != null) View.VISIBLE else View.INVISIBLE
    }

    override fun onClick(p0: View?) {
        val text = txt_dialog_key.text
        val clipboardManager: ClipboardManager = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData = ClipData.newPlainText("create_key", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "$text ?????? ??????", Toast.LENGTH_SHORT).show()
        onclickCopy()
        dismiss()
    }
}