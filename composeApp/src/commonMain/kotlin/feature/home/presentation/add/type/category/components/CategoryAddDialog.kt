package feature.home.presentation.add.type.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import feature.core.presentation.CategoryList
import feature.core.presentation.model.CategoryUi

@Composable
fun CategoryAddDialog(
    categoryUi: CategoryUi,
    onDismissRequest: () -> Unit = {},
    onDone: (CategoryUi) -> Unit = {}
) {

    var currentCategoryUi by remember {
        mutableStateOf<CategoryUi>(categoryUi)
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            val keyboard = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        keyboard?.hide()
                        focusManager.clearFocus(true)
                    }
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                ,
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = CategoryList.getCategoryIconById(categoryUi.id.toLong()),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(12.dp))
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(36.dp)
                            .border(
                                0.5.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        value = currentCategoryUi.name,
                        onValueChange = {
                            currentCategoryUi = currentCategoryUi.copy(
                                name = it
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                        decorationBox = {
                            Box(
                                contentAlignment = Alignment.Center
                            ){
                                it()
                            }
                        }
                    )
                    IconButton(
                        onClick = {
                            onDone(currentCategoryUi)
                            println("Add ${currentCategoryUi}")
                            onDismissRequest()
                        }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }

                }
            }
        }
    )
}