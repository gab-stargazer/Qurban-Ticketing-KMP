package org.lelestacia.qurban_ticketing.ui.user_management

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.theme.containerColor
import org.lelestacia.qurban_ticketing.ui.user_management.UserItemInteraction.OnClick
import org.lelestacia.qurban_ticketing.ui.user_management.UserItemInteraction.OnEdit
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.tv_management_address
import qurbanticketing.composeapp.generated.resources.tv_type_participant
import qurbanticketing.composeapp.generated.resources.tv_type_recipient

sealed interface UserItemInteraction {
    data object OnClick : UserItemInteraction
    data object OnEdit : UserItemInteraction
}

@Immutable
data class UserData(
    val user: User,
    val isSelected: Boolean
)

@Composable
fun UserItem(
    userData: UserData,
    onInteraction: (UserItemInteraction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    val user = userData.user

    ElevatedCard(
        shape = RoundedCornerShape(25F),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor
        ),
        content = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = screenPadding.horizontal,
                        vertical = screenPadding.vertical
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )

                    Column {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = stringResource(
                                when (user.status) {
                                    Status.Recipient -> Res.string.tv_type_recipient
                                    Status.Participant -> Res.string.tv_type_participant
                                }
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(1F))

                    AnimatedContent(targetState = user.address != null) { isMemberDataExist ->
                        when (isMemberDataExist) {
                            true -> {
                                IconButton(
                                    onClick = {
                                        onInteraction(OnClick)
                                    }
                                ) {
                                    AnimatedContent(targetState = userData.isSelected) { isSelected ->
                                        when (isSelected) {
                                            true -> {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropUp,
                                                    contentDescription = null
                                                )
                                            }

                                            false -> {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropDown,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            false -> {
                                IconButton(
                                    onClick = {
                                        lifecycle.handleWhenLifecycleResumed {
                                            onInteraction(OnEdit)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = userData.isSelected && user.address != null,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(
                                horizontal = screenPadding.horizontal
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = screenPadding.horizontal,
                                    vertical = screenPadding.vertical
                                )
                        ) {
                            Column {
                                user.address?.let { address ->
                                    Text(
                                        text = stringResource(
                                            Res.string.tv_management_address,
                                            address
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1F))

                            IconButton(
                                onClick = {
                                    lifecycle.handleWhenLifecycleResumed {
                                        onInteraction(OnEdit)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}