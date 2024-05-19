package feature.add.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import feature.core.presentation.CategoryList
import feature.core.presentation.components.CircleIcon

class AddScreen: Screen {

    @Composable
    override fun Content() {
//        Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
//            Text("AddScreen")
//        }
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(4),
//            modifier = Modifier
//                .fillMaxWidth()
//        ){
//            items(
//                CategoryList.items
//            ){
//                println("${it.id}")
//                it.categoryId.let {
//                    CircleIcon(
//                        imageResId = CategoryList.getCategoryIconById(it),
//                        backgroundColor = CategoryList.getColorByCategory(it),
//                        modifier = Modifier.weight(1f),
//                    )
//                }
//            }
//            item {
//                Spacer(
//                    modifier = Modifier.height(100.dp)
//                )
//            }
//        }
    }
}