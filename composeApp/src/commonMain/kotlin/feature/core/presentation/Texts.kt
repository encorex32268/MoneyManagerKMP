package feature.core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.nunitosans_10pt_bold
import moneymanagerkmp.composeapp.generated.resources.nunitosans_10pt_regular
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
        font: FontResource = Res.font.nunitosans_10pt_bold,
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = textAlign,
            fontFamily = FontFamily(Font(font))
        )
    }
    @Composable
    fun TitleMedium(
        modifier : Modifier = Modifier,
        fonSize: TextUnit = 24.sp,
        style : TextStyle = TextStyle(
            fontFamily = FontFamily(Font(Res.font.nunitosans_10pt_bold)),
            fontSize = fonSize
        ),
        text : String,
        textAlign : TextAlign = TextAlign.Start,
        overflow : TextOverflow = TextOverflow.Ellipsis,
        onTextLayout : (TextLayoutResult) -> Unit = {},
        maxLines : Int = Int.MAX_VALUE,
        color : Color = MaterialTheme.colorScheme.onSurface,
        ){
        Text(
            modifier = modifier,
            text = text,
            style = style,
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
        style : TextStyle = MaterialTheme.typography.titleSmall,
        maxLines : Int = Int.MAX_VALUE,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        color : Color = MaterialTheme.colorScheme.onSurface,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        font: FontResource = Res.font.nunitosans_10pt_bold
    ){
        Text(
            modifier = modifier,
            textAlign = textAlign,
            text = text,
            style = style,
            color = color,
            maxLines = maxLines,
            overflow = overflow,
            onTextLayout = onTextLayout,
            fontFamily = FontFamily(Font(font))
        )
    }

    @Composable
    fun BodyLarge(
        modifier: Modifier = Modifier,
        text : String,
        font: FontResource = Res.font.nunitosans_10pt_regular
    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily(Font(font))

        )
    }

    @Composable
    fun BodyMedium(
        modifier : Modifier = Modifier,
        textAlign: TextAlign = TextAlign.Start,
        text : String,
        style : TextStyle = MaterialTheme.typography.bodyMedium,
        color : Color = MaterialTheme.colorScheme.onSurface,
        font: FontResource = Res.font.nunitosans_10pt_regular
    ){
        Text(
            modifier = modifier,
            textAlign = textAlign,
            text = text,
            style = style,
            color = color,
            fontFamily = FontFamily(Font(font))

        )
    }


    @Composable
    fun BodySmall(
        modifier: Modifier = Modifier,
        text: String,
        font: FontResource = Res.font.nunitosans_10pt_regular

    ){
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily(Font(font))

        )
    }



}