package com.ph32395.lab_8


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

data class Movie(
    val title: String,
    val year: String,
    val posterUrl: String,
    val duration: String,
    val releaseDate: String,
    val genre: String,
    val shortDescription: String
)

enum class ListType {
    ROW, COLUMN, GRID
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                MovieScreen(navController)
            }
    }
}
}


@Composable
fun MovieScreen(navController: NavController, viewModel: MovieViewModel = viewModel()) {
    val movies by viewModel.movies.observeAsState(emptyList())
    var listType by remember { mutableStateOf(ListType.ROW) }
    Column {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { listType = ListType.ROW }) {
                Text(text = "Row")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { listType = ListType.COLUMN }) {
                Text(text = "Column")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { listType = ListType.GRID }) {
                Text(text = "Grid")
            }
        }
        when (listType) {
            ListType.ROW -> MovieRow(movies = movies)
            ListType.COLUMN -> MovieColumn(movies = movies)
            ListType.GRID -> MovieGrid(movies = movies)
        }
    }
}



//LazyRow
@Composable
fun MovieRow(movies: List<Movie>) {
    LazyRow (
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies.size) {
                index -> MovieItem(movie = movies[index], listType = ListType.ROW)
        }
    }
}

// LazyColumn
@Composable
fun MovieColumn(movies: List<Movie>) {
    LazyColumn (
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies.size) {
                index -> MovieColumnItem(movie = movies[index], listType = ListType.COLUMN)
        }
    }
}

//LazyGrid
@Composable
fun MovieGrid(movies: List<Movie>) {
//    Tao ham trang thai
    val gridState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
        state = gridState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(movies.size) {
                index -> MovieItem(movie = movies[index], listType = ListType.GRID)
        }
    }
}

//Tao MovieColumnItem
@Composable
fun MovieColumnItem(movie: Movie, listType: ListType) {
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(model = movie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .then(getItemSizeModifier(listType))
                    .wrapContentHeight()
            )

            Column (
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                BoldValueText(label = "Thời lượng: ", value = movie.duration)
                BoldValueText(label = "Khởi chiếu: ", value = movie.releaseDate)
                BoldValueText(label = "Thể loại: ", value = movie.genre)

                Text(text = "Tóm tắt nội dung",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )

                Text(text = movie.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(2.dp)
                )
            }

        }
    }
}

// Ham getItemSizeModifier set chieu rong
@Composable
private fun getItemSizeModifier(listType: ListType): Modifier {
    return when(listType) {
        ListType.ROW -> Modifier.width(175.dp)
        ListType.COLUMN -> Modifier.width(130.dp)
        ListType.GRID -> Modifier.fillMaxWidth()
    }
}

// Ham BolValueText
@Composable
fun BoldValueText(label: String, value: String,
                  style: TextStyle = MaterialTheme.typography.bodySmall)
{
    Text(buildAnnotatedString {
        append(label)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(value)
        }
    }, style = style)
}



//Tao MovieItem
@Composable
fun MovieItem(movie: Movie, listType: ListType) {
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column (
            modifier = Modifier
                .then(getItemSizeModifier(listType = listType))
        ) {
//            Giup hien thi anh tai tu nguon URL hay tai nguyen
            AsyncImage(model = movie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            )
            Column (modifier = Modifier.padding(8.dp)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                BoldValueText(label = "Thời lượng: ", value = movie.duration)
                BoldValueText(label = "Khởi chiếu: ", value = movie.releaseDate)
            }
        }
    }
}

