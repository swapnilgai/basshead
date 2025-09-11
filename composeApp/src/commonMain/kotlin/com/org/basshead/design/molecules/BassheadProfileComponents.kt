package com.org.basshead.design.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.profile_navigate_forward
import basshead.composeapp.generated.resources.profile_sign_out
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadOutlinedButton
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Profile screen specific molecules following atomic design pattern
 * Uses existing atomic components and standard Compose layouts
 */

/**
 * Profile section header molecule - uses existing text atom with theme colors
 */
@Composable
fun BassheadProfileSectionHeader(
    title: StringResource,
    modifier: Modifier = Modifier,
) {
    BassheadTitleMedium(
        text = stringResource(title),
        modifier = modifier.padding(horizontal = BassheadTheme.spacing.small),
        // No color override - uses theme-aware default from atom
    )
}

/**
 * Profile information row molecule - displays label-value pairs with theme colors
 */
@Composable
fun BassheadProfileInfoRow(
    label: StringResource,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = BassheadTheme.spacing.small,
                vertical = BassheadTheme.spacing.medium,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadBodyLarge(
            text = stringResource(label),
            color = BassheadTheme.colors.outline
        )

        BassheadBodyLarge(
            text = value,
            color = BassheadTheme.colors.outline
        )
    }
}

/**
 * Profile link row molecule - clickable row with theme colors
 */
@Composable
fun BassheadProfileLinkRow(
    title: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = BassheadTheme.spacing.small,
                vertical = BassheadTheme.spacing.large,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadBodyLarge(
            text = stringResource(title),
            color = BassheadTheme.colors.outline
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(Res.string.profile_navigate_forward),
            tint = BassheadTheme.colors.outline,
            modifier = Modifier.size(20.dp),
        )
    }
}

/**
 * Profile sign out button molecule - uses existing outlined button atom
 */
@Composable
fun BassheadProfileSignOutButton(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BassheadOutlinedButton(
        onClick = onSignOut,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BassheadTheme.spacing.small),
    ) {
        BassheadBodyLarge(
            text = stringResource(Res.string.profile_sign_out),
        )
    }
}

/**
 * Profile section molecule - combines header with content using standard spacing
 */
@Composable
fun BassheadProfileSection(
    title: StringResource,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraLarge))
        BassheadProfileSectionHeader(title = title)
        Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))
        content()
    }
}
