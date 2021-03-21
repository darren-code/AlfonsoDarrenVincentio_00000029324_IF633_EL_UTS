package umn.ac.id.artuno

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_music_list.*

class MusicList : AppCompatActivity() {
    companion object {
        var musicList: MutableList<Music> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        else musicList = fetchMusic(this)

        if (intent.getStringExtra("status").equals("success", true)) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Selamat Datang")
                .setMessage("Alfonso Darren Vincentio\n00000029324")
                .setPositiveButton("Okay") { dialog, which -> dialog.cancel() }
                .show()
        }

        rvMusic.adapter = MusicListAdapter(this, musicList)
        rvMusic.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.opProfile)
            startActivity(Intent(this, AboutActivity::class.java)
                .putExtra("session", "active"))
        else if (item.itemId == R.id.opSignOut) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                musicList = fetchMusic(this)
            else requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    fun fetchMusic(context: Context): MutableList<Music> {
        val musicList: MutableList<Music> = mutableListOf()
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION
        )
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                musicList.add(Music(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)
                ))
            }
            cursor.close()
        }
        return musicList
    }

    override fun onBackPressed() {
        Snackbar.make(mainView, "Are you trying to sign out?", Snackbar.LENGTH_LONG)
            .setAction("Sign Out") {
                finish()
            }
            .show()
    }
}