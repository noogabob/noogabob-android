package com.example.noogabab.presentation.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.noogabab.R
import com.example.noogabab.presentation.ui.main.MainActivity
import com.example.noogabab.presentation.ui.start.StartActivity
import com.example.noogabab.util.SharedGroup
import com.example.noogabab.util.SharedUser
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#ffc176")
        setContentView(R.layout.activity_splash)
        startSplash()
    }

    private fun startSplash() {
        this.supportActionBar?.hide()
        if (!getNetworkConnected()) {
            Handler().postDelayed({
                Toast.makeText(this, "네트워크 연결 문제로 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.finishAffinity(this);
                exitProcess(0)
            }, 1500)
        }else {
            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                settingPermission()
            }
        }
    }

    private fun getNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork : NetworkInfo? = cm.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun isUser(): Boolean {
        val sharedGroup = getSharedPreferences(SharedGroup.NAME, Context.MODE_PRIVATE)
        val sharedUser = getSharedPreferences(SharedUser.NAME, Context.MODE_PRIVATE)
        val userId = sharedUser.getInt(SharedUser.USER_ID_KEY, -1)
        val groupId = sharedGroup.getInt(SharedGroup.GROUP_ID_KEY, -1)
        val groupKey = sharedGroup.getString(SharedGroup.GROUP_UUID_KEY, "")
        return !(userId == -1 || groupId == -1 || groupKey == "")
    }

    private fun settingPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent: Intent = if (!isUser()) Intent(applicationContext, StartActivity::class.java)
                else Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_SHORT).show()
                ActivityCompat.finishAffinity(this@SplashActivity) // 권한 거부시 앱 종료
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
//            .setRationaleMessage("카메라와 갤러리 접근 권한 필요합니다.")
            .setDeniedMessage("앱을 사용하기 위한 필수 권한 거부로 앱을 종료합니다.")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }
}