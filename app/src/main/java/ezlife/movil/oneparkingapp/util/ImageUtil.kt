package ezlife.movil.oneparkingauxiliar.util

import android.graphics.Bitmap
import android.os.Environment
import java.io.*


fun getBytesFromBitmap(bitmap:Bitmap):ByteArray{

    val dir:File = File("${Environment.getExternalStorageDirectory()}/OneParkingApp/")
    if(!dir.exists()){
        dir.mkdirs()
    }

    val file:File = File("${Environment.getExternalStorageDirectory()}/OneParkingApp/temp.webp")
    val outStream:OutputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.WEBP, 50, outStream)
    outStream.flush()
    outStream.close()

    val data:ByteArray = fileToBytes(file)
    file.delete()
    return data
}

private fun fileToBytes(file: File):ByteArray{
    val stream:FileInputStream = FileInputStream(file)
    val byteBuffer:ByteArrayOutputStream = ByteArrayOutputStream()
    val bufferSize = 1024

    val buffer:ByteArray = ByteArray(bufferSize)
    var len:Int = stream.read(buffer)
    while(len != -1){
        byteBuffer.write(buffer,0,len)
        len = stream.read(buffer)
    }

    return byteBuffer.toByteArray()
}