package com.uvitos.fastoutfit.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvitos.fastoutfit.R
import com.uvitos.fastoutfit.ui.components.AppBackground
import com.uvitos.fastoutfit.ui.components.TopBarWithHelpHomeProfile
import com.uvitos.fastoutfit.ui.theme.*

@Composable
fun SettingsScreen(
    onHelpClick:          () -> Unit = {},
    onHomeClick:          () -> Unit = {},
    onProfileClick:       () -> Unit = {},
    onEditProfileClick:   () -> Unit = {},
    onSecurityClick:      () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onPrivacyClick:       () -> Unit = {},
    onAppearanceClick:    () -> Unit = {},
    onLanguageClick:      () -> Unit = {},
    onStorageClick:       () -> Unit = {},
    onPoliciesClick:      () -> Unit = {},
    onAboutUsClick:       () -> Unit = {},
    onLogOutClick:        () -> Unit = {},
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {


            TopBarWithHelpHomeProfile(
                onHelpClick    = onHelpClick,
                onHomeClick    = onHomeClick,
                onProfileClick = onProfileClick,
            )

            Spacer(Modifier.height(40.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_profile),       onClick = onEditProfileClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_security),      onClick = onSecurityClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_notifications), onClick = onNotificationsClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_privacy),       onClick = onPrivacyClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_appearance),    onClick = onAppearanceClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_language),      onClick = onLanguageClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_storage),       onClick = onStorageClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_policies),      onClick = onPoliciesClick)
                SettingsItem(label = stringResource(com.uvitos.fastoutfit.R.string.settings_about_us),      onClick = onAboutUsClick)

                Spacer(Modifier.height(12.dp))

                HorizontalDivider(
                    modifier  = Modifier.fillMaxWidth(0.5f),
                    thickness = 1.dp,
                    color     = TextSecondary.copy(alpha = 0.3f),
                )

                Spacer(Modifier.height(4.dp))

                SettingsItem(
                    label     = stringResource(com.uvitos.fastoutfit.R.string.settings_log_out),
                    onClick   = onLogOutClick,
                    textColor = GoldAccent,
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    label:     String,
    onClick:   () -> Unit,
    textColor: Color = TextSecondary,
) {
    TextButton(
        onClick        = onClick,
        modifier       = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 6.dp),
    ) {
        Text(
            text           = label,
            color          = textColor,
            fontSize       = 15.sp,
            fontWeight     = FontWeight.SemiBold,
            letterSpacing  = 3.sp,
            textDecoration = TextDecoration.Underline,
            modifier       = Modifier.fillMaxWidth(),
        )
    }
}