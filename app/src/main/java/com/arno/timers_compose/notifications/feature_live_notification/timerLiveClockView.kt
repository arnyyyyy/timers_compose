import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.createBitmap

internal fun timerLiveClockView(
        progress: Int,
        timeText: String,
        accentColor: Int
): Bitmap {
        val size = 256
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)

        val backgroundPaint = Paint().apply {
                color = Color.WHITE
                isAntiAlias = true
                style = Paint.Style.FILL
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 10, backgroundPaint)

        val strokePaint = Paint().apply {
                color = Color.LTGRAY
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = 4f
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 12, strokePaint)

        val progressPaint = Paint().apply {
                color = accentColor
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = 12f
                strokeCap = Paint.Cap.ROUND
        }

        val rect = RectF(24f, 24f, size - 24f, size - 24f)
        val sweepAngle = (progress / 100f) * 360f
        canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)

        val textPaint = Paint().apply {
                color = Color.BLACK
                isAntiAlias = true
                textSize = 70f
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
        }

        canvas.drawText(timeText, size / 2f, size / 2f + 25f, textPaint)

        return bitmap
}