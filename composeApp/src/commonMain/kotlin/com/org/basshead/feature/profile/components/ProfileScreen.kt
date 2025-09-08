package com.org.basshead.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.basshead.feature.dashboard.model.DailyHeadbangState
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.dashboard.model.UserProfileState

@Stable
data class ProfileDisplayData(
    val userName: String,
    val totalHeadbangs: String,
    val festivalsCount: String,
)

@Composable
fun ProfileScreen(
    profile: UserProfileState?,
    userFestivals: List<FestivalItemState>,
    dailyHeadbangs: List<DailyHeadbangState>,
    totalHeadbangs: Long,
    onLogout: () -> Unit,
    onNavigateToAvatar: () -> Unit,
    onFestivalClick: (String) -> Unit = {},
    onViewLeaderboard: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // Remember expensive calculations and stable data
    val profileDisplayData = remember(profile, totalHeadbangs, userFestivals) {
        ProfileDisplayData(
            userName = profile?.name ?: "Unknown User",
            totalHeadbangs = totalHeadbangs.toString(),
            festivalsCount = userFestivals.size.toString(),
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        // Profile Header
        item(key = "profile_header") {
            ProfileHeader()
        }

        // Profile Info Section
        item(key = "profile_info_section") {
            ProfileInfoSection(profileDisplayData = profileDisplayData)
        }

        // General Section
        item(key = "general_section") {
            GeneralSection(onNavigateToAvatar = onNavigateToAvatar)
        }

        // Legal Section
        item(key = "legal_section") {
            LegalSection()
        }

        // Sign Out Button Section
        item(key = "sign_out_section") {
            SignOutSection(onLogout = onLogout)
        }

        // Bottom spacing
        item(key = "bottom_spacing") {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ProfileHeader() {
    Text(
        text = "Profile",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Composable
private fun ProfileInfoSection(
    profileDisplayData: ProfileDisplayData,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionHeader(title = "Account")

        Spacer(modifier = Modifier.height(8.dp))

        ProfileInfoRow(
            label = "Name",
            value = profileDisplayData.userName,
        )

        ProfileInfoRow(
            label = "Total Headbangs",
            value = profileDisplayData.totalHeadbangs,
        )

        ProfileInfoRow(
            label = "Joined Festivals",
            value = profileDisplayData.festivalsCount,
        )
    }
}

@Composable
private fun GeneralSection(
    onNavigateToAvatar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionHeader(title = "General")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileLinkRow(
            title = "Change Avatar",
            onClick = onNavigateToAvatar,
        )

        ProfileLinkRow(
            title = "Your Festivals",
            onClick = { /* Empty lambda for now */ },
        )
    }
}

@Composable
private fun LegalSection(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionHeader(title = "Legal")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileLinkRow(
            title = "Privacy Policy",
            onClick = { /* Empty lambda for now */ },
        )

        ProfileLinkRow(
            title = "Terms of Service",
            onClick = { /* Empty lambda for now */ },
        )
    }
}

@Composable
private fun SignOutSection(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(32.dp))

        // Sign Out Button - styled like the reference image
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
            ),
        ) {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp,
        modifier = modifier.padding(horizontal = 4.dp),
    )
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun ProfileLinkRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
        )

        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}
