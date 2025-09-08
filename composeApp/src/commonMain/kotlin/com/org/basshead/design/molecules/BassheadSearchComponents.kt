package com.org.basshead.design.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.org.basshead.design.atoms.BassheadIconButton
import com.org.basshead.design.atoms.BassheadSearchTextField
import com.org.basshead.design.theme.BassheadTheme

/**
 * Molecular search components
 * Combinations of atoms that form functional search units
 */

/**
 * Search bar molecule - combines text field with search and filter actions
 */
@Composable
fun BassheadSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showFilter: Boolean = true,
    onSearchSubmit: (() -> Unit)? = null,
    onClearClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadSearchTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = placeholder,
            modifier = Modifier.weight(1f),
            enabled = enabled,
            leadingIcon = Icons.Default.Search,
            trailingIcon = if (query.isNotEmpty()) Icons.Default.Clear else null,
            onTrailingIconClick = if (query.isNotEmpty()) {
                onClearClick ?: { onQueryChange("") }
            } else null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchSubmit?.invoke() }
            ),
        )

        if (showFilter) {
            BassheadIconButton(
                onClick = { onFilterClick?.invoke() },
                icon = Icons.Default.FilterList,
                contentDescription = "Filter",
                enabled = enabled,
            )
        }
    }
}

/**
 * Simple search field molecule - just the search input without filter
 */
@Composable
fun BassheadSimpleSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onSearchSubmit: (() -> Unit)? = null,
    onClearClick: (() -> Unit)? = null,
) {
    BassheadSearchTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        leadingIcon = Icons.Default.Search,
        trailingIcon = if (query.isNotEmpty()) Icons.Default.Clear else null,
        onTrailingIconClick = if (query.isNotEmpty()) {
            onClearClick ?: { onQueryChange("") }
        } else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchSubmit?.invoke() }
        ),
    )
}
