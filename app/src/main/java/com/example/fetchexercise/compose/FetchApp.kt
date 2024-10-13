package com.example.fetchexercise.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fetchexercise.R
import com.example.fetchexercise.api.DataItem
import com.example.fetchexercise.compose.components.LoadingWheel
import com.example.fetchexercise.viewmodels.MainState
import com.example.fetchexercise.viewmodels.MainViewModel

/**
 * Filters items by non-empty, non-null names, sorts item names lexicographically,
 * then groups them by list ID
 * @param items The items to sort
 * @return A filtered and sorted map by List Id
 */
fun filterAndGroupItems(items: List<DataItem>): Map<Int?, List<DataItem>> {
    return items.filter { !it.name.isNullOrEmpty() }
        .sortedBy { it.name }
        .groupBy { it.listId }
}

/**
 * Displays a list of items
 * @param state The current app state
 * @param itemsState The list of items to display
 */
@Composable
fun ItemList(
    state: MainState,
    itemsState: List<DataItem>
) {
    /**
     * Displays a group header for a given list ID, and then lists all items associated with
     * that list ID in items
     * @param listId The list ID to display
     * @param items A mapping of list ID to items
     */
    fun LazyListScope.displayItemGroup(listId: Int, items: Map<Int?, List<DataItem>>) {
        // Group header
        item {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shadow(4.dp)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = "${stringResource(R.string.list_id)}: $listId",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // Items in group
        items[listId]?.let { itemList ->
            items(itemList) { item ->
                item.name?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = item.id.toString())
                        Text(text = item.name)
                    }
                }
            }
        }
    }

    // Items header
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp)
            .clip(RoundedCornerShape(8.dp, 8.dp))
            .shadow(8.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(R.string.items),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(4.dp)
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(4.dp),
        ) {
            Text(text = stringResource(R.string.item_id))
            Text(text = stringResource(R.string.item_name))
        }
    }

    // Lazy column to display items
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoadingItems) {
            item {
                LoadingWheel {
                    Text(text = stringResource(R.string.loading_items))
                }
            }
        } else {
            // filter items by name, then group by List ID
            val items = filterAndGroupItems(itemsState)

            if(items.isEmpty()) {
                item {
                    Text(text = stringResource(R.string.no_items))
                }
            } else {
                items.keys.sortedBy { it }.forEach { listId ->
                    listId?.let { displayItemGroup(listId, items) }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String = "User") {
    Text(
        text = buildAnnotatedString {
            append(stringResource(R.string.welcome))
            append(", ")
            pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
            append(name)
            toAnnotatedString()
        },
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(4.dp)
    )
}

@Composable
fun FetchApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val itemsState by viewModel.data.collectAsState(emptyList())
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.getListItems()
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(40.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {

            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            Greeting("User")
            ItemList(state, itemsState)
        }
    }
}