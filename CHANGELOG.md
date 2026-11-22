# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.1] - 2026-11-22

### Fixed

- Crash (`ClassNotFoundException: androidx.compose.material.icons.Icons`) when opening a document in apps that use Compose BOM 2025.x or newer. The SDK now declares `material-icons-core` explicitly instead of relying on Material 3 to bring it transitively.

## [1.0.0] - 2026-11-22

### Added

- `PdfReaderView` composable with horizontal paging and continuous vertical scrolling, pinch-to-zoom, pan and double-tap zoom.
- Text selection with draggable handles, including selections that span several pages, and copy to the clipboard.
- Highlights in several colors, stored as normalized rectangles. Multi-page highlights share a single `groupId`.
- Bookmarks, page notes, full-text search, thumbnail drawer and automatic resume from the last read page.
- `PdfViewerStorage` persistence contract with a default Room implementation (`RoomPdfViewerStorage`).
- `PdfViewerConfig` feature flags, initial page and orientation, and `PdfViewerColors`.
- Optional per-document `AnnotationsScreen` and `AnnotationsViewModel`.
- Sample app with a minimal integration and a full bookshelf app using Hilt and a custom storage.

[Unreleased]: https://github.com/fmaestre98/pdfviewer-compose/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/fmaestre98/pdfviewer-compose/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/fmaestre98/pdfviewer-compose/releases/tag/v1.0.0
