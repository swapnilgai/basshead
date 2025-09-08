package com.org.basshead.design

/**
 * Atomic Design System for Basshead
 *
 * This package contains the complete atomic design system following Reddit's engineering patterns
 * and Google-grade standards for scalable, maintainable UI components.
 *
 * Architecture:
 *
 * 1. Design Tokens (Foundation)
 *    - Colors: Brand colors, semantic colors, festival-specific colors
 *    - Typography: Complete type scale with festival-specific styles
 *    - Spacing: 4dp grid system with component-specific spacing
 *    - Elevation: Material Design 3 elevation levels
 *
 * 2. Theme System
 *    - BassheadTheme: Main theme object providing access to all tokens
 *    - ProvideBassheadTheme: Composition provider for theme values
 *    - Light/Dark theme support with festival-specific colors
 *
 * 3. Atomic Components (Building Blocks)
 *    - Atoms: Buttons, Text, TextFields, Visual Elements
 *    - Molecules: SearchBar, FormFields, NavigationItems
 *    - Organisms: Complex components (to be implemented)
 *    - Templates: Screen layouts (to be implemented)
 *    - Pages: Actual screens (to be implemented)
 *
 * Usage:
 *
 * ```kotlin
 * // Wrap your app with theme provider
 * ProvideBassheadTheme {
 *     // Use atomic components
 *     BassheadButton(
 *         text = "Join Festival",
 *         onClick = { /* action */ }
 *     )
 *
 *     BassheadSearchBar(
 *         query = searchQuery,
 *         onQueryChange = { /* update */ },
 *         placeholder = "Search festivals..."
 *     )
 * }
 * ```
 *
 * Benefits:
 * - Consistent visual language across the app
 * - Reduced development time through reusable components
 * - Easy maintenance and updates to design system
 * - Built-in accessibility and theming support
 * - Performance optimized with Compose best practices
 */
