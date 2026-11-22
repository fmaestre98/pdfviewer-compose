package com.fmaestre98.pdfviewer.sdk.model

import android.net.Uri
import java.io.File

/**
 * Where the viewer loads a PDF document from.
 */
sealed interface PdfSource {
    /** Android URI (`content://` or `file://`). */
    data class FromUri(val uri: Uri) : PdfSource

    /** File on the device file system. */
    data class FromFile(val file: File) : PdfSource

    /** File bundled in the host app's `assets` folder. */
    data class FromAsset(val assetPath: String) : PdfSource

    /** Stable identifier used to scope annotations, bookmarks and reading progress. */
    fun getDocumentId(): String = when (this) {
        is FromUri -> uri.toString()
        is FromFile -> file.absolutePath
        is FromAsset -> "asset://$assetPath"
    }
}
