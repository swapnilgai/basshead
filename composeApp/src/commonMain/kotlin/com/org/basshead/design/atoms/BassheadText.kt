package com.org.basshead.design.atoms

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.org.basshead.design.theme.BassheadTheme

/**
 * Atomic typography components following the type scale
 * Base text atoms for consistent typography across the app
 */

/**
 * Display text atoms - largest text for hero content
 */
@Composable
fun BassheadDisplayLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.displayLarge,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadDisplayMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.displayMedium,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadDisplaySmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.displaySmall,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Headline text atoms - section headers
 */
@Composable
fun BassheadHeadlineLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.headlineLarge,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadHeadlineMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.headlineMedium,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadHeadlineSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.headlineSmall,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Title text atoms - card titles, dialog headers
 */
@Composable
fun BassheadTitleLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.titleLarge,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadTitleMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.titleMedium,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadTitleSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.titleSmall,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Body text atoms - main content text
 */
@Composable
fun BassheadBodyLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.bodyLarge,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadBodyMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.bodyMedium,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadBodySmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurfaceVariant,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.bodySmall,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Label text atoms - small text for buttons, tabs
 */
@Composable
fun BassheadLabelLarge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.labelLarge,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadLabelMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurfaceVariant,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.labelMedium,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadLabelSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurfaceVariant,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.labelSmall,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = overflow,
        maxLines = maxLines,
    )
}

/**
 * Festival-specific text atoms
 */
@Composable
fun BassheadFestivalName(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 2,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.festivalName,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadArtistName(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurface,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.artistName,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadVenueText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurfaceVariant,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.venueText,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadDateTime(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.onSurfaceVariant,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = 1,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.dateTime,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}

@Composable
fun BassheadPrice(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = BassheadTheme.colors.primary,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = 1,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = BassheadTheme.typography.price,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
    )
}
