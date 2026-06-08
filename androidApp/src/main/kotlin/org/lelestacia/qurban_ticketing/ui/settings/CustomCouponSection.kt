package org.lelestacia.qurban_ticketing.ui.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_participant
import qurbanticketing.composeapp.generated.resources.btn_recipient
import qurbanticketing.composeapp.generated.resources.coupon_participant
import qurbanticketing.composeapp.generated.resources.coupon_recipient
import qurbanticketing.composeapp.generated.resources.custom_image_body
import qurbanticketing.composeapp.generated.resources.custom_image_label

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomCouponSection(
    recipientCustomCoupon: String,
    participantCustomCoupon: String,
    onRecipientClicked: () -> Unit,
    onDeleteRecipient: () -> Unit,
    onParticipantClicked: () -> Unit,
    onDeleteParticipant: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LocalScreenPadding.current.horizontal)
    ) {
        Text(
            text = stringResource(Res.string.custom_image_label),
            style = MaterialTheme.typography.labelLargeEmphasized,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = stringResource(Res.string.custom_image_body),
            style = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.padding(start = 4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = LocalScreenPadding.current.vertical / 2)
        ) {
            Column(
                modifier = Modifier.weight(1F)
            ) {
                AnimatedContent(recipientCustomCoupon.isNotBlank()) { isNotBlank ->
                    when (isNotBlank) {
                        true -> {
                            AsyncImage(
                                model = recipientCustomCoupon,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.weight(1F)
                            )
                        }

                        false -> {
                            Image(
                                painter = painterResource(resource = Res.drawable.coupon_recipient),
                                contentDescription = null,
                                modifier = Modifier.weight(1F)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1F)
            ) {
                AnimatedContent(participantCustomCoupon.isNotBlank()) { isNotBlank ->
                    when (isNotBlank) {
                        true -> {
                            AsyncImage(
                                model = participantCustomCoupon,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.weight(1F)
                            )
                        }

                        false -> {
                            Image(
                                painter = painterResource(resource = Res.drawable.coupon_participant),
                                contentDescription = null,
                                modifier = Modifier.weight(1F)
                            )
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = LocalScreenPadding.current.vertical / 2)
        ) {
            AnimatedContent(
                recipientCustomCoupon.isNotBlank(),
                modifier = Modifier.weight(1F)
            ) { isNotBlank ->
                when (isNotBlank) {
                    true -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    lifecycle.handleWhenLifecycleResumed(onRecipientClicked)
                                },
                                shape = RoundedCornerShape(
                                    topStart = 25F,
                                    bottomStart = 25F,
                                    topEnd = 15F,
                                    bottomEnd = 15F
                                ),
                                modifier = Modifier.weight(1F)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }

                            Button(
                                onClick = {
                                    lifecycle.handleWhenLifecycleResumed(onDeleteRecipient)
                                },
                                shape = RoundedCornerShape(15F),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.weight(1F)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    false -> {
                        Button(
                            onClick = {
                                lifecycle.handleWhenLifecycleResumed(onRecipientClicked)
                            },
                            shape = RoundedCornerShape(
                                topStart = 25F,
                                bottomStart = 25F,
                                topEnd = 15F,
                                bottomEnd = 15F
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(Res.string.btn_recipient))
                        }
                    }
                }
            }

            AnimatedContent(
                participantCustomCoupon.isNotBlank(),
                modifier = Modifier.weight(1F)
            ) { isNotBlank ->
                when (isNotBlank) {
                    true -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    lifecycle.handleWhenLifecycleResumed(onParticipantClicked)
                                },
                                shape = RoundedCornerShape(15F),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                modifier = Modifier.weight(1F)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }

                            Button(
                                onClick = {
                                    lifecycle.handleWhenLifecycleResumed(onDeleteParticipant)
                                },
                                shape = RoundedCornerShape(
                                    topStart = 15F,
                                    bottomStart = 15F,
                                    topEnd = 25F,
                                    bottomEnd = 25F
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.weight(1F)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    false -> {
                        Button(
                            onClick = {
                                lifecycle.handleWhenLifecycleResumed(onParticipantClicked)
                            },
                            shape = RoundedCornerShape(
                                topEnd = 25F,
                                bottomEnd = 25F,
                                topStart = 15F,
                                bottomStart = 15F
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            )
                        ) {
                            Text(stringResource(Res.string.btn_participant))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCustomCouponSection() {
    QurbanTicketingTheme {
        CustomCouponSection(
            recipientCustomCoupon = "",
            participantCustomCoupon = "",
            onRecipientClicked = {},
            onDeleteRecipient = {},
            onParticipantClicked = {},
            onDeleteParticipant = {},
            modifier = Modifier.padding(vertical = LocalScreenPadding.current.vertical)
        )
    }
}