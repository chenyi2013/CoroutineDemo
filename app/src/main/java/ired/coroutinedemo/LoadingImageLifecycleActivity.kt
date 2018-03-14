package ired.coroutinedemo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*

class LoadingImageLifecycleActivity : AppCompatActivity() {

    private var dialog: ProgressDialog? = null;

    companion object {
        private const val IMAGE_PICK_REQUEST = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_image_lifecycle)


        setContentView(R.layout.activity_image)
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { it.type = "image/*" }
        startActivityForResult(intent, LoadingImageLifecycleActivity.IMAGE_PICK_REQUEST)
        dialog = ProgressDialog(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoadingImageLifecycleActivity.IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            dialog?.setMessage("正在加载图片...")
//            dialog?.setCancelable(false)
            dialog?.show()

            load {
                //模拟长耗时
                Thread.sleep(6000)
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            } then {
                image_view.setImageBitmap(it)
                dialog?.dismiss()
            }
        }
    }

}
