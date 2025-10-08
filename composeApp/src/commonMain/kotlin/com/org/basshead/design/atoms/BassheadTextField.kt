package com.org.basshead.design.atoms

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.org.basshead.design.theme.BassheadTheme

/**
 * Atomic text field components following Material Design 3
 * Base input atoms for consistent form styling across the app
 */

/**
 * Standard filled text field atom
 */
@Composable
fun BassheadTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = BassheadTheme.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(8.dp),
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BassheadTheme.colors.surface,
            unfocusedContainerColor = BassheadTheme.colors.surface,
            disabledContainerColor = BassheadTheme.colors.surface.copy(alpha = 0.04f),
            focusedIndicatorColor = BassheadTheme.colors.primary,
            unfocusedIndicatorColor = BassheadTheme.colors.outline,
            disabledIndicatorColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f),
            errorIndicatorColor = BassheadTheme.colors.error,
            focusedLabelColor = BassheadTheme.colors.primary,
            unfocusedLabelColor = BassheadTheme.colors.onSurfaceVariant,
            disabledLabelColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f),
            errorLabelColor = BassheadTheme.colors.error,
            cursorColor = BassheadTheme.colors.primary,
            errorCursorColor = BassheadTheme.colors.error,
        ),
    )
}

/**
 * Outlined text field atom - preferred for forms
 */
@Composable
fun BassheadOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = BassheadTheme.typography.bodyLarge,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(8.dp),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BassheadTheme.colors.primary,
            unfocusedBorderColor = BassheadTheme.colors.outline,
            disabledBorderColor = BassheadTheme.colors.onSurface.copy(alpha = 0.12f),
            errorBorderColor = BassheadTheme.colors.error,
            focusedLabelColor = BassheadTheme.colors.primary,
            unfocusedLabelColor = BassheadTheme.colors.onSurfaceVariant,
            disabledLabelColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f),
            errorLabelColor = BassheadTheme.colors.error,
            cursorColor = BassheadTheme.colors.primary,
            errorCursorColor = BassheadTheme.colors.error,
        ),
    )
}

/**
 * Convenience functions for common text field patterns
 * Performance optimized: Direct composition without wrapper overhead
 */

@Composable
fun BassheadOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    // Direct composition to Material3 OutlinedTextField - no wrapper overhead
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BassheadTheme.colors.primary,
            unfocusedBorderColor = BassheadTheme.colors.outline,
            disabledBorderColor = BassheadTheme.colors.onSurface.copy(alpha = 0.12f),
            errorBorderColor = BassheadTheme.colors.error,
            focusedLabelColor = BassheadTheme.colors.primary,
            unfocusedLabelColor = BassheadTheme.colors.onSurfaceVariant,
            disabledLabelColor = BassheadTheme.colors.onSurface.copy(alpha = 0.38f),
            errorLabelColor = BassheadTheme.colors.error,
            cursorColor = BassheadTheme.colors.primary,
            errorCursorColor = BassheadTheme.colors.error,
        ),
        // Direct inline composition - eliminates lambda wrapper overhead
        label = {
            Text(
                text = label,
                style = BassheadTheme.typography.bodyMedium,
            )
        },
        placeholder = if (placeholder != null) {
            {
                Text(
                    text = placeholder,
                    style = BassheadTheme.typography.bodyMedium,
                    color = BassheadTheme.colors.onSurfaceVariant,
                )
            }
        } else {
            null
        },
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = BassheadTheme.colors.onSurfaceVariant,
                )
            }
        } else {
            null
        },
        trailingIcon = if (trailingIcon != null) {
            {
                val iconTint = BassheadTheme.colors.onSurfaceVariant
                if (onTrailingIconClick != null) {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = iconTint,
                        )
                    }
                } else {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = iconTint,
                    )
                }
            }
        } else {
            null
        },
        supportingText = if (isError && !errorText.isNullOrBlank()) {
            {
                Text(
                    text = errorText,
                    style = BassheadTheme.typography.bodySmall,
                    color = BassheadTheme.colors.error,
                )
            }
        } else {
            null
        },
    )
}

/**
 * Search text field atom - specialized for search functionality
 */
@Composable
fun BassheadSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    BassheadOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        placeholder = {
            Text(
                text = placeholder,
                style = BassheadTheme.typography.bodyMedium,
                color = BassheadTheme.colors.onSurfaceVariant,
            )
        },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = BassheadTheme.colors.onSurfaceVariant,
                )
            }
        },
        trailingIcon = trailingIcon?.let { icon ->
            {
                if (onTrailingIconClick != null) {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = BassheadTheme.colors.onSurfaceVariant,
                        )
                    }
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BassheadTheme.colors.onSurfaceVariant,
                    )
                }
            }
        },
        shape = RoundedCornerShape(24.dp), // More rounded for search
    )
}
