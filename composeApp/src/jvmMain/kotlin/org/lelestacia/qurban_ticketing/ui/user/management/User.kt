package org.lelestacia.qurban_ticketing.ui.user.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.model.User
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed

@Composable
fun User(
    userIndexed: Pair<User, Int>,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = when {
                    userIndexed.second % 2 == 0 -> {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    }

                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .padding(
                horizontal = 20.dp,
                vertical = 6.dp
            )
    ) {
        Text(
            text = userIndexed.first.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2F)
        )

        Text(
            text = stringResource(userIndexed.first.status.uiText),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1F)
        )

        Text(
            text =
                if (userIndexed.first.status == Status.Participant) {
                    stringResource(userIndexed.first.type?.uiText ?: Type.Cow.uiText)
                } else
                    "-",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1F)
        )

        IconButton(
            onClick = {
                lifecycle.handleWhenLifecycleResumed(onResumed = onEdit)
            },
            modifier = Modifier.weight(0.5F)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}