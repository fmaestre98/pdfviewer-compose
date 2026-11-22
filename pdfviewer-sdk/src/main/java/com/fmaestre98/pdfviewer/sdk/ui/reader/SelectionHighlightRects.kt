package com.fmaestre98.pdfviewer.sdk.ui.reader

import com.fmaestre98.pdfviewer.sdk.core.model.PdfChar
import com.fmaestre98.pdfviewer.sdk.core.viewmodel.PdfInteractiveState
import com.fmaestre98.pdfviewer.sdk.storage.NormalizedRect
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Builds normalized highlight rects (one per text line) for the current selection,
 * grouped by page. Supports selections spanning multiple pages.
 */
internal fun buildSelectionHighlightRects(
    interactiveState: PdfInteractiveState
): Map<Int, List<NormalizedRect>> {
    val startChar = interactiveState.selectionStartChar ?: return emptyMap()
    val endChar = interactiveState.selectionEndChar ?: return emptyMap()
    val startPage = interactiveState.selectionStartPageIndex
    val endPage = interactiveState.selectionEndPageIndex
    if (startPage < 0 || endPage < 0) return emptyMap()

    val isForward = startPage <= endPage
    val firstPage = min(startPage, endPage)
    val lastPage = max(startPage, endPage)
    val firstChar = if (isForward) startChar else endChar
    val lastChar = if (isForward) endChar else startChar

    val result = linkedMapOf<Int, List<NormalizedRect>>()
    for (page in firstPage..lastPage) {
        val optimizedModel = interactiveState.getOptimizedPageModel(page) ?: continue
        val pageModel = interactiveState.getPageModel(page) ?: continue
        val pageStart = if (page == firstPage) firstChar else optimizedModel.firstChar
        val pageEnd = if (page == lastPage) lastChar else optimizedModel.lastChar
        if (pageStart == null || pageEnd == null) continue

        val chars = optimizedModel.getCharactersBetween(pageStart, pageEnd)
        val rects = mergeCharsIntoLineRects(
            chars = chars,
            pageWidth = pageModel.targetWidth.toFloat(),
            pageHeight = pageModel.targetHeight.toFloat()
        )
        if (rects.isNotEmpty()) result[page] = rects
    }
    return result
}

private fun mergeCharsIntoLineRects(
    chars: List<PdfChar>,
    pageWidth: Float,
    pageHeight: Float
): List<NormalizedRect> {
    if (chars.isEmpty() || pageWidth <= 0f || pageHeight <= 0f) return emptyList()

    val rects = mutableListOf<NormalizedRect>()
    var lineLeft = chars[0].rect.left
    var lineTop = chars[0].rect.top
    var lineRight = chars[0].rect.right
    var lineBottom = chars[0].rect.bottom

    fun flushLine() {
        rects.add(
            NormalizedRect(
                startX = lineLeft / pageWidth,
                startY = lineTop / pageHeight,
                endX = lineRight / pageWidth,
                endY = lineBottom / pageHeight
            )
        )
    }

    for (i in 1 until chars.size) {
        val charRect = chars[i].rect
        val charCenterY = (charRect.top + charRect.bottom) / 2f
        val lineCenterY = (lineTop + lineBottom) / 2f

        if (abs(charCenterY - lineCenterY) < 5f) {
            lineLeft = minOf(lineLeft, charRect.left)
            lineRight = maxOf(lineRight, charRect.right)
            lineTop = minOf(lineTop, charRect.top)
            lineBottom = maxOf(lineBottom, charRect.bottom)
        } else {
            flushLine()
            lineLeft = charRect.left
            lineTop = charRect.top
            lineRight = charRect.right
            lineBottom = charRect.bottom
        }
    }
    flushLine()
    return rects
}
