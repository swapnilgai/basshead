package com.org.basshead.design.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.default_email_label
import basshead.composeapp.generated.resources.default_password_label
import basshead.composeapp.generated.resources.hide_password
import basshead.composeapp.generated.resources.required_field
import basshead.composeapp.generated.resources.show_password
import org.jetbrains.compose.resources.stringResource
import com.org.basshead.design.atoms.BassheadBodySmall
import com.org.basshead.design.atoms.BassheadLabelMedium
import com.org.basshead.design.atoms.BassheadOutlinedTextField
import com.org.basshead.design.theme.BassheadTheme

/**
 * Molecular form components
 * Complex form field combinations with labels, validation, and helper text
 */

/**
 * Form field molecule - combines label, text field, and error/helper text
 */
@Composable
fun BassheadFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isRequired: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    helperText: String? = null,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        // Label with required indicator
        Row {
            BassheadLabelMedium(
                text = label,
                color = if (isError) BassheadTheme.colors.error else BassheadTheme.colors.onSurface
            )
            if (isRequired) {
                BassheadLabelMedium(
                    text = stringResource(Res.string.required_field),
                    color = BassheadTheme.colors.error
                )
            }
        }

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraSmall))

        // Text field
        BassheadOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            placeholder = placeholder,
            enabled = enabled,
            isError = isError,
            errorText = errorText,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext?.invoke() ?: focusManager.moveFocus(FocusDirection.Down)
                },
                onDone = {
                    onDone?.invoke() ?: focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Helper text
        if (!isError && !helperText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraSmall))
            BassheadBodySmall(
                text = helperText,
                color = BassheadTheme.colors.onSurfaceVariant
            )
        }
    }
}

/**
 * Email field molecule - specialized text field for email input
 * Performance optimized: Stable keyboard options, email validation ready
 */
@Composable
fun BassheadEmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    placeholder: String? = null,
    label: String? = null
) {
    val keyboardOptions = remember {
        KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    }

    BassheadOutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = label ?: stringResource(Res.string.default_email_label),
        placeholder = placeholder,
        leadingIcon = Icons.Default.Email,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        enabled = enabled,
        isError = isError,
        errorText = errorText,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Password field molecule - specialized text field with visibility toggle
 * Performance optimized: Isolated visibility state, configuration change survival
 */
@Composable
fun BassheadPasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    placeholder: String? = null,
    label: String? = null
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val keyboardOptions = remember {
        KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    }

    BassheadOutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = label ?: stringResource(Res.string.default_password_label),
        placeholder = placeholder,
        leadingIcon = Icons.Default.Lock,
        trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
        onTrailingIconClick = { passwordVisible = !passwordVisible },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        singleLine = true,
        enabled = enabled,
        isError = isError,
        errorText = errorText,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Form section molecule - groups related form fields
 */
@Composable
fun BassheadFormSection(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
    ) {
        Column {
            BassheadLabelMedium(
                text = title.uppercase(),
                color = BassheadTheme.colors.primary
            )

            subtitle?.let {
                Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraSmall))
                BassheadBodySmall(
                    text = it,
                    color = BassheadTheme.colors.onSurfaceVariant
                )
            }
        }

        content()
    }
}
