package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 12.dp, top = 32.dp)
                .align(Alignment.Start),
            style = MaterialTheme.typography.bodyMedium
        )

        EditNumberField(
            label = R.string.bill_amount,
            value = amountInput,
            onValueChange = { amountInput = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            icon = R.drawable.baseline_money_24,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )

        EditNumberField(
            label = R.string.tip_percentage,
            value = tipInput,
            onValueChange = { tipInput = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            icon = R.drawable.baseline_percent_24,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.round_up_tip),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = roundUp,
                onCheckedChange = { roundUp = it }
            )
        }

        Text(
            text = stringResource(R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.Start),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    icon: Int,
    modifier: Modifier = Modifier
) {
    val pinkBackground = Color(0xFFF5E0E0)

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        leadingIcon = { Icon(painterResource(icon), contentDescription = null) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = pinkBackground,
            unfocusedContainerColor = pinkBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    MaterialTheme {
        TipTimeLayout()
    }
}