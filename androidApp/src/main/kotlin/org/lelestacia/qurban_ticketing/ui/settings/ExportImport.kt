package org.lelestacia.qurban_ticketing.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_export_data
import qurbanticketing.composeapp.generated.resources.export_data_body
import qurbanticketing.composeapp.generated.resources.export_data_label

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExportImport(
    title: String,
    body: String,
    buttonLabel: String,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LocalScreenPadding.current.horizontal)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLargeEmphasized,
            modifier = Modifier.padding(start = 4.dp)
        )

        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = LocalScreenPadding.current.vertical - 4.dp,
                horizontal = LocalScreenPadding.current.horizontal - 4.dp
            )
        )

        OutlinedButton(
            onClick = onButtonClicked,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface.copy(0.9F)
            ),
            shape = RoundedCornerShape(25F),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(buttonLabel)
        }
    }
}

@Preview(showBackground = true, locale = "id")
@Composable
private fun PreviewExportImport() {
    QurbanTicketingTheme {
        ExportImport(
            title = stringResource(Res.string.export_data_label),
            body = stringResource(Res.string.export_data_body),
            buttonLabel = stringResource(Res.string.btn_export_data),
            onButtonClicked = {},
            modifier = Modifier.padding(vertical = LocalScreenPadding.current.vertical)
        )
    }
}