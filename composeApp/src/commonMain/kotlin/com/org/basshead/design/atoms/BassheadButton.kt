package com.org.basshead.design.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.org.basshead.design.theme.BassheadTheme

/**
 * Atomic button component variants following Material Design 3
 * Base building blocks for all button-related UI in the app
 */

/**
 * Primary button atom - highest emphasis actions
 */
@Composable
fun BassheadButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(8.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled && !isLoading,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = BassheadTheme.colors.primary,
            contentColor = BassheadTheme.colors.onPrimary,
            disabledContainerColor = BassheadTheme.colors.onSurface.copy(alpha = 0.12f),
            disabledContentColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f)
        ),
        contentPadding = PaddingValues(horizontal = BassheadTheme.spacing.medium),
        interactionSource = interactionSource,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = BassheadTheme.colors.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            content()
        }
    }
}

/**
 * Secondary button atom - medium emphasis actions
 */
@Composable
fun BassheadOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(8.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled && !isLoading,
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = BassheadTheme.colors.primary,
            disabledContentColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) BassheadTheme.colors.outline else BassheadTheme.colors.onSurface.copy(alpha = 0.12f)
        ),
        contentPadding = PaddingValues(horizontal = BassheadTheme.spacing.medium),
        interactionSource = interactionSource,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = BassheadTheme.colors.primary,
                strokeWidth = 2.dp
            )
        } else {
            content()
        }
    }
}

/**
 * Text button atom - lowest emphasis actions with Material 3 styling
 */
@Composable
fun BassheadTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = BassheadTheme.colors.onPrimary,
            disabledContentColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f)
        ),
        contentPadding = PaddingValues(
            horizontal = BassheadTheme.spacing.medium,
            vertical = BassheadTheme.spacing.small
        ),
        interactionSource = interactionSource,
        content = content
    )
}

/**
 * Icon button atom - for icon-only actions
 */
@Composable
fun BassheadIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = BassheadTheme.colors.onSurfaceVariant,
            disabledContentColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f)
        ),
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Convenience functions for common button patterns
 */

@Composable
fun BassheadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    BassheadButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading
    ) {
        leadingIcon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = text,
            style = BassheadTheme.typography.labelLarge
        )

        trailingIcon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun BassheadOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    BassheadOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading
    ) {
        leadingIcon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = text,
            style = BassheadTheme.typography.labelLarge
        )

        trailingIcon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun BassheadTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    BassheadTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = BassheadTheme.typography.labelLarge
        )
    }
}
