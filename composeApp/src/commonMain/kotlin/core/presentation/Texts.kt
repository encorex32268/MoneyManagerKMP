package core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.notosanslao_bold
import moneymanagerkmp.composeapp.generated.resources.notosanslao_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource








@OptIn(ExperimentalResourceApi::class)
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
        textAlign : TextAlign = TextAlign.Start,
        font: FontResource = Res.font.notosanslao_bold,
        overflow: TextOverflow = TextOverflow.Ellipsis,
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily(Font(font)),
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign,
            overflow = overflow
        )
    }
    @Composable
    fun TitleMedium(
        modifier : Modifier = Modifier,
        text : String,
        textAlign : TextAlign = TextAlign.Start,
        overflow : TextOverflow = TextOverflow.Ellipsis,
        onTextLayout : (TextLayoutResult) -> Unit = {},
        maxLines : Int = Int.MAX_VALUE,
        color : Color = MaterialTheme.colorScheme.onSurface,
        fontSize: TextUnit = 16.sp
        ){
        Text(
            modifier = modifier,
            text = text,
            style =  TextStyle(
                fontFamily = FontFamily(Font(Res.font.notosanslao_bold)),
                fontSize = fontSize
            ),
            color = color,
            textAlign = textAlign,
            overflow = overflow,
            onTextLayout = onTextLayout,
            maxLines = maxLines,

        )
    }

    @Composable
    fun TitleSmall(
        modifier : Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Start,
        text : String,
        maxLines : Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        color : Color = MaterialTheme.colorScheme.onSurface,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        font: FontResource = Res.font.notosanslao_bold,
        fontSize: TextUnit = 12.sp
    ){
        Text(
            modifier = modifier,
            textAlign = textAlign,
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = fontSize,
                fontFamily = FontFamily(Font(font))
            ),
            color = color,
            maxLines = maxLines,
            overflow = overflow,
            onTextLayout = onTextLayout
        )
    }

    @Composable
    fun BodyLarge(
        modifier: Modifier = Modifier,
        text : String,
        font: FontResource = Res.font.notosanslao_regular
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily(Font(font))
            ),
            color = MaterialTheme.colorScheme.onSurface

        )
    }

    @Composable
    fun BodyMedium(
        modifier : Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Start,
        text : String,
        color : Color = MaterialTheme.colorScheme.onSurface,
        font: FontResource = Res.font.notosanslao_regular
    ){
        Text(
            modifier = modifier,
            textAlign = textAlign,
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily =  FontFamily(Font(font))
            ),
            color = color,

        )
    }


    @Composable
    fun BodySmall(
        modifier: Modifier = Modifier,
        text: String,
        font: FontResource = Res.font.notosanslao_regular,
        maxLines: Int = 2

    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily(Font(font)),
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = maxLines,
            overflow = TextOverflow.Clip

        )
    }



}