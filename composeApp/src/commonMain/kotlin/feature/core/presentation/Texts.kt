package feature.core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

object Texts {

    @Composable
    fun DisplaySmall(
        text : String
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    @Composable
    fun TitleLarge(
        modifier: Modifier = Modifier,
        text : String,
        textAlign : TextAlign = TextAlign.Start
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign
        )
    }
    @Composable
    fun TitleMedium(
        modifier : Modifier = Modifier,
        style : TextStyle = MaterialTheme.typography.titleMedium,
        text : String,
        textAlign : TextAlign = TextAlign.Start,
        overflow : TextOverflow = TextOverflow.Ellipsis,
        onTextLayout : (TextLayoutResult) -> Unit = {},
        maxLines : Int = Int.MAX_VALUE,
        color : Color = MaterialTheme.colorScheme.onSurface
    ){
        Text(
            modifier = modifier,
            text = text,
            style = style,
            color = color,
            textAlign = textAlign,
            overflow = overflow,
            onTextLayout = onTextLayout,
            maxLines = maxLines
        )
    }

    @Composable
    fun TitleSmall(
        modifier : Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Start,
        text : String,
        style : TextStyle = MaterialTheme.typography.titleSmall,
        maxLines : Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        color : Color = MaterialTheme.colorScheme.onSurface,
        onTextLayout: (TextLayoutResult) -> Unit = {}
    ){
        Text(
            modifier = modifier,
            textAlign = textAlign,
            text = text,
            style = style,
            color = color,
            maxLines = maxLines,
            overflow = overflow,
            onTextLayout = onTextLayout
        )
    }

    @Composable
    fun BodyLarge(
        modifier: Modifier = Modifier,
        text : String
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    @Composable
    fun BodyMedium(
        modifier : Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Start,
        text : String,
        style : TextStyle = MaterialTheme.typography.bodyMedium,
        color : Color = MaterialTheme.colorScheme.onSurface,
    ){
        Text(
            modifier = modifier,
            textAlign = textAlign,
            text = text,
            style = style,
            color = color
        )
    }


    @Composable
    fun BodySmall(
        modifier : Modifier = Modifier,
        text : String
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }



}