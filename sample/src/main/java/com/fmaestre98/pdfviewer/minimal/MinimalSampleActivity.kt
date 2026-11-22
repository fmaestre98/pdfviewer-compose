package com.fmaestre98.pdfviewer.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.fmaestre98.pdfviewer.sdk.model.PdfSource
import com.fmaestre98.pdfviewer.sdk.ui.PdfReaderView

/**
 * Smallest possible integration of the SDK: no dependency injection and no custom storage.
 * Annotations, bookmarks and reading progress are persisted by the SDK's default Room storage.
 */
class MinimalSampleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                PdfReaderView(
                    source = PdfSource.FromAsset("sample.pdf"),
                    modifier = Modifier.fillMaxSize(),
                    onClose = ::finish
                )
            }
        }
    }
}
