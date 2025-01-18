package com.example.recipeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun RecipeScreen(modifier: Modifier = Modifier, navigateToDetails: (Category) -> Unit) {
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.categoriesState

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewState.loading -> {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            }

            viewState.error != null -> {
                Text("Something went wrong!")
            }

            else -> {
                CategoryScreen(viewState.list, navigateToDetails)
            }
        }
    }
}

@Composable
fun CategoryScreen(categories: List<Category>, navigateToDetails: (Category) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        items(categories) { eachCategory ->
            CategoryItem(eachCategory, navigateToDetails)
        }
    }
}

@Composable
fun CategoryItem(category: Category, navigateToDetails: (Category) -> Unit) {
    Column(
        modifier = Modifier
            .padding(2.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
            .clickable {
                navigateToDetails(category)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(category.strCategoryThumb),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        )
        Text(
            text = category.strCategory,
            fontSize = 18.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeScreenPreview() {
    RecipeScreen() {}
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    val item = Category(
        idCategory = "2",
        strCategory = "Chicken",
        strCategoryThumb = "https://www.themealdb.com/images/category/chicken.png",
        strCategoryDescription = "Chicken is a type of domesticated fowl, a subspecies of the red junglefowl. It is one of the most common and widespread domestic animals, with a total population of more than 19 billion as of 2011.[1] Humans commonly keep chickens as a source of food (consuming both their meat and eggs) and, more rarely, as pets."
    )
    CategoryItem(item, {})
}
