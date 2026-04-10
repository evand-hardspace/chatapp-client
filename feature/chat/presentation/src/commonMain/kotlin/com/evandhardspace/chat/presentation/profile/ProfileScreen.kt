package com.evandhardspace.chat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.core.design_system.generated.resources.upload_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.cancel
import chatapp.feature.chat.presentation.generated.resources.changing_email_is_not_supported
import chatapp.feature.chat.presentation.generated.resources.current_password
import chatapp.feature.chat.presentation.generated.resources.delete
import chatapp.feature.chat.presentation.generated.resources.delete_profile_picture
import chatapp.feature.chat.presentation.generated.resources.delete_profile_picture_description
import chatapp.feature.chat.presentation.generated.resources.email
import chatapp.feature.chat.presentation.generated.resources.new_password
import chatapp.feature.chat.presentation.generated.resources.password
import chatapp.feature.chat.presentation.generated.resources.password_hint
import chatapp.feature.chat.presentation.generated.resources.profile_image
import chatapp.feature.chat.presentation.generated.resources.save
import chatapp.feature.chat.presentation.generated.resources.upload_image
import com.evandhardspace.chat.presentation.profile.component.ProfileHeaderSection
import com.evandhardspace.chat.presentation.profile.component.ProfileSectionLayout
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.AvatarSize
import com.evandhardspace.core.designsystem.component.avatar.ChatAppAvatarPhoto
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.dialog.ChatAppAdaptiveDialogSheetLayout
import com.evandhardspace.core.designsystem.component.dialog.ChatAppDialog
import com.evandhardspace.core.designsystem.component.dialog.rememberAdaptiveDialogSheetController
import com.evandhardspace.core.designsystem.component.textfield.ChatAppPasswordTextField
import com.evandhardspace.core.designsystem.component.textfield.ChatAppTextField
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.OnEffect
import com.evandhardspace.core.presentation.util.compose.clearFocusOnTap
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import chatapp.core.design_system.generated.resources.Res as DesignRes

@Composable
internal fun ProfileScreen(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dialogSheetController = rememberAdaptiveDialogSheetController(onDismiss)

    OnEffect(viewModel.effects) { effect ->
        when (effect) {
            ProfileEffect.Dismiss -> dialogSheetController.dismiss()
        }
    }

    ChatAppAdaptiveDialogSheetLayout(
        controller = dialogSheetController,
    ) {
        ProfileContent(
            state = state,
            action = viewModel::onAction,
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    action: (ProfileAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(MaterialTheme.paddings.default),
            )
            .verticalScroll(rememberScrollState()),
    ) {
        ProfileHeaderSection(
            username = state.username,
            onCloseClick = { action(ProfileAction.OnDismiss) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.paddings.default,
                    horizontal = MaterialTheme.paddings.fourQuarters,
                ),
        )
        ChatAppHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.profile_image),
        ) {
            Row {
                ChatAppAvatarPhoto(
                    displayText = state.userInitials,
                    size = AvatarSize.Large,
                    imageUrl = state.profilePictureUrl,
                    onClick = { action(ProfileAction.OnUploadPictureClick) },
                )
                Spacer(modifier = Modifier.width(MaterialTheme.paddings.fourQuarters))
                FlowRow(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
                ) {
                    ChatAppButton(
                        text = stringResource(Res.string.upload_image),
                        onClick = { action(ProfileAction.OnUploadPictureClick) },
                        style = ChatAppButtonStyle.Secondary,
                        enabled = !state.isUploadingImage && !state.isDeletingImage,
                        isLoading = state.isUploadingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(DesignRes.drawable.upload_icon),
                                contentDescription = stringResource(Res.string.upload_image),
                            )
                        },
                    )

                    ChatAppButton(
                        text = stringResource(Res.string.delete),
                        onClick = { action(ProfileAction.OnDeletePictureClick) },
                        style = ChatAppButtonStyle.DestructiveSecondary,
                        enabled = state.isUploadingImage.not()
                                && state.isDeletingImage.not()
                                && state.profilePictureUrl != null,
                        isLoading = state.isDeletingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(Res.string.delete),
                            )
                        },
                    )
                }
            }
        }

        if (state.imageError != null) {
            Text(
                text = state.imageError.asComposableString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        ChatAppHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.email),
        ) {
            ChatAppTextField(
                state = state.emailTextState,
                enabled = false,
                supportingText = stringResource(Res.string.changing_email_is_not_supported),
            )
        }
        ChatAppHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.password),
        ) {
            ChatAppPasswordTextField(
                state = state.currentPasswordTextState,
                isPasswordVisible = state.isCurrentPasswordVisible,
                onToggleVisibilityClick = {
                    action(ProfileAction.OnToggleCurrentPasswordVisibility)
                },
                placeholder = stringResource(Res.string.current_password),
                isError = state.currentPasswordError != null,
                supportingText = state.currentPasswordError?.asComposableString(),
            )

            ChatAppPasswordTextField(
                state = state.newPasswordTextState,
                isPasswordVisible = state.isNewPasswordVisible,
                onToggleVisibilityClick = { action(ProfileAction.OnToggleNewPasswordVisibility) },
                placeholder = stringResource(Res.string.new_password),
                isError = state.newPasswordError != null,
                supportingText = state.newPasswordError?.asComposableString()
                    ?: stringResource(Res.string.password_hint),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.paddings.default,
                    Alignment.End,
                ),
            ) {
                ChatAppButton(
                    text = stringResource(Res.string.cancel),
                    style = ChatAppButtonStyle.Secondary,
                    onClick = { action(ProfileAction.OnDismiss) },
                )
                ChatAppButton(
                    text = stringResource(Res.string.save),
                    onClick = { action(ProfileAction.OnChangePasswordClick) },
                    enabled = state.canChangePassword,
                    isLoading = state.isChangingPassword,
                )
            }
        }

        val deviceConfiguration = currentDeviceConfiguration()
        if (
            deviceConfiguration in listOf(
                DeviceConfiguration.MobilePortrait,
                DeviceConfiguration.MobileLandscape,
            )
        ) {
            Spacer(modifier = Modifier.weight(1f))
        }

        if (state.showDeleteConfirmationDialog) {
            ChatAppDialog(
                title = stringResource(Res.string.delete_profile_picture),
                description = stringResource(Res.string.delete_profile_picture_description),
                confirmButtonText = stringResource(Res.string.delete),
                cancelButtonText = stringResource(Res.string.cancel),
                onConfirmClick = { action(ProfileAction.OnConfirmDeleteClick) },
                onCancelClick = { action(ProfileAction.OnDismissDeleteConfirmationDialogClick) },
                onDismiss = { action(ProfileAction.OnDismissDeleteConfirmationDialogClick) },
            )
        }
    }
}

@ThemedPreview
@Composable
private fun ProfileContentPreview() {
    ChatAppPreview {
        ProfileContent(
            state = ProfileState(
                username = "Ivan"
            ),
            action = {},
        )
    }
}
