package com.org.basshead.design.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.profile_account_section
import basshead.composeapp.generated.resources.profile_change_avatar
import basshead.composeapp.generated.resources.profile_general_section
import basshead.composeapp.generated.resources.profile_joined_festivals_label
import basshead.composeapp.generated.resources.profile_legal_section
import basshead.composeapp.generated.resources.profile_name_label
import basshead.composeapp.generated.resources.profile_privacy_policy
import basshead.composeapp.generated.resources.profile_terms_of_service
import basshead.composeapp.generated.resources.profile_title
import basshead.composeapp.generated.resources.profile_total_headbangs_label
import basshead.composeapp.generated.resources.profile_your_festivals
import com.org.basshead.design.atoms.BassheadHeadlineMedium
import com.org.basshead.design.molecules.BassheadProfileInfoRow
import com.org.basshead.design.molecules.BassheadProfileLinkRow
import com.org.basshead.design.molecules.BassheadProfileSection
import com.org.basshead.design.molecules.BassheadProfileSignOutButton
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.stringResource

/**
 * Profile screen organisms - complete profile sections
 * Uses existing atomic components and standard Compose layouts
 */

/**
 * Profile header organism - uses existing headline atom without color override
 */
@Composable
fun BassheadProfileHeader(
    modifier: Modifier = Modifier,
) {
    BassheadHeadlineMedium(
        text = stringResource(Res.string.profile_title),
        modifier = modifier.padding(vertical = BassheadTheme.spacing.large),
    )
}

/**
 * Profile account information organism
 */
@Composable
fun BassheadProfileAccountSection(
    userName: String,
    totalHeadbangs: String,
    festivalsCount: String,
    modifier: Modifier = Modifier,
) {
    BassheadProfileSection(
        title = Res.string.profile_account_section,
        modifier = modifier,
    ) {
        BassheadProfileInfoRow(
            label = Res.string.profile_name_label,
            value = userName,
        )

        BassheadProfileInfoRow(
            label = Res.string.profile_total_headbangs_label,
            value = totalHeadbangs,
        )

        BassheadProfileInfoRow(
            label = Res.string.profile_joined_festivals_label,
            value = festivalsCount,
        )
    }
}

/**
 * Profile general settings organism
 */
@Composable
fun BassheadProfileGeneralSection(
    onNavigateToAvatar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BassheadProfileSection(
        title = Res.string.profile_general_section,
        modifier = modifier,
    ) {
        BassheadProfileLinkRow(
            title = Res.string.profile_change_avatar,
            onClick = onNavigateToAvatar,
        )

        BassheadProfileLinkRow(
            title = Res.string.profile_your_festivals,
            onClick = { /* Empty lambda for now */ },
        )
    }
}

/**
 * Profile legal section organism
 */
@Composable
fun BassheadProfileLegalSection(
    modifier: Modifier = Modifier,
) {
    BassheadProfileSection(
        title = Res.string.profile_legal_section,
        modifier = modifier,
    ) {
        BassheadProfileLinkRow(
            title = Res.string.profile_privacy_policy,
            onClick = { /* Empty lambda for now */ },
        )

        BassheadProfileLinkRow(
            title = Res.string.profile_terms_of_service,
            onClick = { /* Empty lambda for now */ },
        )
    }
}

/**
 * Complete profile screen layout organism - flattened hierarchy to reduce overdraw
 * Uses atomic components and standard Compose layouts
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
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = BassheadTheme.spacing.large,
            vertical = BassheadTheme.spacing.small,
        ),
    ) {
        // Profile Header
        item(key = "profile_header") {
            BassheadProfileHeader()
        }

        // Account Section
        item(key = "account_section") {
            BassheadProfileAccountSection(
                userName = userName,
                totalHeadbangs = totalHeadbangs,
                festivalsCount = festivalsCount,
            )
        }

        // General Section
        item(key = "general_section") {
            BassheadProfileGeneralSection(
                onNavigateToAvatar = onNavigateToAvatar,
            )
        }

        // Legal Section
        item(key = "legal_section") {
            BassheadProfileLegalSection()
        }

        // Sign Out Section
        item(key = "sign_out_section") {
            Column {
                Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraLarge))
                BassheadProfileSignOutButton(onSignOut = onLogout)
            }
        }

        // Bottom spacing
        item(key = "bottom_spacing") {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
