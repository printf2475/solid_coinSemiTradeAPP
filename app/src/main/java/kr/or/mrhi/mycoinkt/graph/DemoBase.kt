package kr.or.mrhi.mycoinkt.graph

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.graphics.Typeface
import android.os.Bundle
import kr.or.mrhi.mycoinkt.graph.DemoBase
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import android.widget.TextView
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Intent
import android.widget.BaseAdapter
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.or.mrhi.mycoinkt.adapter.OwnBankAdapter
import kr.or.mrhi.mycoinkt.adapter.CoinListAdapter
import kr.or.mrhi.mycoinkt.adapter.WalletTabLayoutAdapter
import android.widget.EditText
import com.google.android.material.tabs.TabLayout
import kr.or.mrhi.mycoinkt.DBController
import kr.or.mrhi.mycoinkt.R
import androidx.lifecycle.ViewModelProvider
import kr.or.mrhi.mycoinkt.CoinMain
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.text.TextWatcher
import android.text.Editable
import kr.or.mrhi.mycoinkt.MainActivity
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.app.Activity
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient

/**
 * Base class of all Activities of the Demo Application.
 *
 * @author Philipp Jahoda
 */
abstract class DemoBase : AppCompatActivity(), OnRequestPermissionsResultCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun getRandom(range: Float, start: Float): Float {
        return (Math.random() * range).toFloat() + start
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(applicationContext, "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    protected fun requestStoragePermission(view: View?) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Snackbar.make(
                view!!,
                "Write permission is required to save image to gallery",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(android.R.string.ok) {
                    ActivityCompat.requestPermissions(
                        this@DemoBase, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), PERMISSION_STORAGE
                    )
                }
                .show()
        } else {
            Toast.makeText(applicationContext, "Permission Required!", Toast.LENGTH_SHORT)
                .show()
            ActivityCompat.requestPermissions(
                this@DemoBase,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_STORAGE
            )
        }
    }

    companion object {
        private const val PERMISSION_STORAGE = 0
    }
}