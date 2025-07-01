package com.org.basshead.utils.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.basshead.utils.core.DesertWhite
import com.org.basshead.utils.core.PrimaryOrange

@Composable
fun ErrorScreen(
    errorMessage: String,
    onDismiss: () -> Unit,
) {
    // Dimmed background overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center,
    ) {
        // Centered dialog box
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .background(DesertWhite, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = errorMessage,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(contentColor = PrimaryOrange),
            ) {
                Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
