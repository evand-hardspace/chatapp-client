package com.evandhardspace.core.designsystem.component.dropdown

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.iconSize
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
fun ChatAppDropdownMenu(
    expanded: Boolean,
    items: List<DropdownItem>,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(x = 0.dp, y = MaterialTheme.paddings.quarter),
    onDismiss: () -> Unit,
) {
    val atLeastOneIconExists = items.any { it.icon != null }

    DropdownMenu(
        expanded = expanded,
        offset = offset,
        shape = MaterialTheme.shapes.large,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.extended.surfaceOutline,
        ),
        modifier = modifier,
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                modifier = Modifier.semantics(mergeDescendants = true) { },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
                    ) {
                        if (item.icon != null) {
                            Icon(
                                modifier = Modifier
                                    .size(MaterialTheme.iconSize.large)
                                    .semantics { hideFromAccessibility() },
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.contentColor,
                            )
                        } else if (atLeastOneIconExists) {
                            Spacer(
                                modifier = Modifier
                                    .size(MaterialTheme.iconSize.large)
                                    .semantics { hideFromAccessibility() },
                            )
                        }
                        Text(
                            text = item.title,
                            color = item.contentColor,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                },
                onClick = item.onClick,
            )
            if (index != items.lastIndex) {
                ChatAppHorizontalDivider()
            }
        }
    }
}
