package com.org.basshead.design.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.profile_account_section
import basshead.composeapp.generated.resources.profile_change_avatar
import basshead.composeapp.generated.resources.profile_general_section
import basshead.composeapp.generated.resources.profile_joined_festivals_label
import basshead.composeapp.generated.resources.profile_legal_section
import basshead.composeapp.generated.resources.profile_name_label
import basshead.composeapp.generated.resources.profile_navigate_forward
import basshead.composeapp.generated.resources.profile_privacy_policy
import basshead.composeapp.generated.resources.profile_sign_out
import basshead.composeapp.generated.resources.profile_terms_of_service
import basshead.composeapp.generated.resources.profile_title
import basshead.composeapp.generated.resources.profile_total_headbangs_label
import basshead.composeapp.generated.resources.profile_your_festivals
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadHeadlineMedium
import com.org.basshead.design.atoms.BassheadOutlinedButton
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.stringResource

/**
 * Highly optimized profile screen layout - flattened hierarchy for maximum performance
 * Eliminates overdraw by reducing component nesting and using direct LazyColumn items
 *
 * Performance optimizations:
 * - Single LazyColumn with direct item composition
 * - Eliminated intermediate wrapper components
 * - Reduced padding/spacing layers
 * - Minimized recomposition scope
 */
@Composable
fun BassheadProfileScreenLayout(
    userName: String,
    totalHeadbangs: String,
    festivalsCount: String,
    onLogout: () -> Unit,
    onNavigateToAvatar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(BassheadTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
    ) {
        // Profile Header - Direct composition
        item(key = "profile_header") {
            BassheadHeadlineMedium(
                text = stringResource(Res.string.profile_title),
                modifier = Modifier.padding(vertical = BassheadTheme.spacing.medium),
            )
        }

        // Account Section Header
        item(key = "account_header") {
            BassheadTitleMedium(
                text = stringResource(Res.string.profile_account_section),
                modifier = Modifier.padding(top = BassheadTheme.spacing.large),
            )
        }

        // Account Info Rows - Direct composition without wrapper
        item(key = "account_name") {
            ProfileInfoRowDirect(
                label = stringResource(Res.string.profile_name_label),
                value = userName,
            )
        }

        item(key = "account_headbangs") {
            ProfileInfoRowDirect(
                label = stringResource(Res.string.profile_total_headbangs_label),
                value = totalHeadbangs,
            )
        }

        item(key = "account_festivals") {
            ProfileInfoRowDirect(
                label = stringResource(Res.string.profile_joined_festivals_label),
                value = festivalsCount,
            )
        }

        // General Section Header
        item(key = "general_header") {
            BassheadTitleMedium(
                text = stringResource(Res.string.profile_general_section),
                modifier = Modifier.padding(top = BassheadTheme.spacing.large),
            )
        }

        // General Link Rows - Direct composition
        item(key = "general_avatar") {
            ProfileLinkRowDirect(
                title = stringResource(Res.string.profile_change_avatar),
                onClick = onNavigateToAvatar,
            )
        }

        item(key = "general_festivals") {
            ProfileLinkRowDirect(
                title = stringResource(Res.string.profile_your_festivals),
                onClick = { /* Empty for now */ },
            )
        }

        // Legal Section Header
        item(key = "legal_header") {
            BassheadTitleMedium(
                text = stringResource(Res.string.profile_legal_section),
                modifier = Modifier.padding(top = BassheadTheme.spacing.large),
            )
        }

        // Legal Link Rows - Direct composition
        item(key = "legal_privacy") {
            ProfileLinkRowDirect(
                title = stringResource(Res.string.profile_privacy_policy),
                onClick = { /* Empty for now */ },
            )
        }

        item(key = "legal_terms") {
            ProfileLinkRowDirect(
                title = stringResource(Res.string.profile_terms_of_service),
                onClick = { /* Empty for now */ },
            )
        }

        // Sign Out Button - Direct composition
        item(key = "sign_out") {
            BassheadOutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = BassheadTheme.spacing.extraLarge),
            ) {
                BassheadBodyLarge(
                    text = stringResource(Res.string.profile_sign_out),
                )
            }
        }

        // Bottom spacing
        item(key = "bottom_spacing") {
            Spacer(modifier = Modifier.height(BassheadTheme.spacing.large))
        }
    }
}

/**
 * Optimized info row - direct composition without intermediate wrappers
 * Eliminates one layer of Column nesting from BassheadProfileInfoRow
 */
@Composable
private fun ProfileInfoRowDirect(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BassheadTheme.spacing.small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadBodyLarge(
            text = label,
            color = BassheadTheme.colors.outline,
        )

        BassheadBodyLarge(
            text = value,
            color = BassheadTheme.colors.outline,
        )
    }
}

/**
 * Optimized link row - direct composition without intermediate wrappers
 * Eliminates one layer of Column nesting from BassheadProfileLinkRow
 */
@Composable
private fun ProfileLinkRowDirect(
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = BassheadTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BassheadBodyLarge(
            text = title,
            color = BassheadTheme.colors.outline,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(Res.string.profile_navigate_forward),
            tint = BassheadTheme.colors.outline,
            modifier = Modifier.size(20.dp),
        )
    }
}
