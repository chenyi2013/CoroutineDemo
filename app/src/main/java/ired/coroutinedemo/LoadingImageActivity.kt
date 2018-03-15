package ired.coroutinedemo

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class LoadingImageActivity : AppCompatActivity() {


    companion object {
        private const val IMAGE_PICK_REQUEST = 101
    }

    private var imageUri: Uri? = null
    internal val Background = newFixedThreadPoolContext(2, "bg")
    private var dialog: ProgressDialog? = null;
    private var job:Job? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { it.type = "image/*" }
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
        dialog = ProgressDialog(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data // So we can store it in onSaveInstanceState
            dialog?.setMessage("正在加载图片...")
//            dialog?.setCancelable(false)
            dialog?.show()
            job = async (Background) {
                //模拟长耗时
                delay(6000)
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                launch(UI) {
                    image_view.setImageBitmap(bitmap)
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }


}
