package com.portes.ufctracker.feature.events.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun AlertDialogComponent(onDismissDialog: () -> Unit, onSaveClick: (String) -> Unit) {
    var nickname by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissDialog() }, properties = DialogProperties()) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {

                Text(
                    text = "Agrega tu nickname o nombre",
                    modifier = Modifier.padding(8.dp)
                )

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it }, modifier = Modifier.padding(8.dp),
                    label = { Text("Nickname/Nombre") }
                )

                Row {
                    TextButton(
                        onClick = { onDismissDialog() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancelar")
                    }

                    Button(
                        onClick = {
                            onSaveClick(nickname)
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Agregar")
                    }
                }
            }
        }
    }
}
