package com.tracker.trackDailyHub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tracker.trackDailyHub.ui.theme.Gray800
import com.tracker.trackdailyhub.R

@Composable
fun NavigationCustomButtons(modifier: Modifier, onStatisticClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .background(Gray800, shape = RoundedCornerShape(100))
                .padding(vertical = 10.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = stringResource(id = R.string.start_screen),
                color = Color.White,
                fontSize = 14.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
        }
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            Text(
                modifier = Modifier.clickable {
                    onStatisticClick()
                },
                fontSize = 14.sp,
                text = stringResource(id = R.string.statistic_screen),
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
        }
    }
}