package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NovaMainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingArcVisualizer
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlowCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SpaceCardBg
import com.example.ui.theme.SpaceCardBorder
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpaceSurface
import com.example.ui.theme.SpaceSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@Composable
fun HomeScreen(
    viewModel: NovaMainViewModel,
    modifier: Modifier = Modifier
) {
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val amplitude by viewModel.rmsAmplitude.collectAsState()
    val statusText by viewModel.currentStatusText.collectAsState()
    val liveMode by viewModel.liveModeActive.collectAsState()
    val recognizedSpeech by viewModel.recognizedSpeech.collectAsState()
    val isAccessibilityActive by viewModel.isAccessibilityActive.collectAsState()

    var manualInputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isAccessibilityActive) NeonEmerald else WarningAmber)
                    )
                    Text(
                        text = "NOVA CORE v2.5",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
                Text(
                    text = "Autonomous Jarvis Companion",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            // Live Mode Toggle Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (liveMode) NeonPurple.copy(alpha = 0.25f) else SpaceCardBg)
                    .border(
                        1.dp,
                        if (liveMode) NeonPurple else SpaceCardBorder,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { viewModel.toggleLiveMode() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("live_mode_toggle"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.GraphicEq,
                        contentDescription = "Live Mode",
                        tint = if (liveMode) NeonPurple else TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (liveMode) "LIVE ACTIVE" else "LIVE MODE",
                        color = if (liveMode) NeonPurple else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Central Jarvis Arc Visualizer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentAlignment = Alignment.Center
        ) {
            GlowingArcVisualizer(
                modifier = Modifier.size(240.dp),
                amplitude = amplitude,
                isListening = isListening,
                isSpeaking = isSpeaking,
                isProcessing = isProcessing
            )

            // Center Interactive Mic Button
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                SpaceSurfaceVariant,
                                SpaceDarkBg
                            )
                        )
                    )
                    .border(
                        2.dp,
                        if (isListening) CyberCyan else if (isSpeaking) NeonPurple else ElectricBlue,
                        CircleShape
                    )
                    .clickable { viewModel.toggleVoiceListening() }
                    .testTag("main_mic_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        isSpeaking -> Icons.Filled.VolumeUp
                        isListening -> Icons.Filled.Mic
                        isProcessing -> Icons.Filled.Bolt
                        else -> Icons.Filled.MicOff
                    },
                    contentDescription = "Voice Control",
                    tint = when {
                        isSpeaking -> NeonPurple
                        isListening -> CyberCyan
                        isProcessing -> NeonEmerald
                        else -> TextPrimary
                    },
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Status & Transcription Bar
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = 12.dp,
            borderColor = if (isListening) CyberCyan.copy(alpha = 0.5f) else SpaceCardBorder
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = statusText,
                    color = when {
                        isListening -> CyberCyan
                        isSpeaking -> NeonPurple
                        isProcessing -> NeonEmerald
                        else -> TextSecondary
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                if (recognizedSpeech.isNotBlank()) {
                    Text(
                        text = "\"$recognizedSpeech\"",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Quick Command Shortcuts Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AVTOMATLASHTIRILGAN BUYRUQLAR (QUICK ACTIONS)",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // Quick Action Grid / Cards
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                icon = Icons.Filled.Send,
                accentColor = CyberCyan,
                title = "Telegram: Onamga 'Bordim'",
                subtitle = "Appni ochish, kontaktni topish va matn kiritib yuborish",
                tag = "Telegram GUI Automation",
                testTag = "action_telegram",
                onClick = { viewModel.triggerTelegramQuickAction() }
            )

            QuickActionCard(
                icon = Icons.Filled.Visibility,
                accentColor = NeonEmerald,
                title = "Ekran Tahlili (Multimodal Vision)",
                subtitle = "Ekranda nima borligini tahlil qilish va ovozli javob berish",
                tag = "On-Demand Screen Vision",
                testTag = "action_screen_vision",
                onClick = { viewModel.triggerScreenVision() }
            )

            QuickActionCard(
                icon = Icons.Filled.Description,
                accentColor = ElectricBlue,
                title = "DOCX Hujjat: Avtomobil rasmi bilan",
                subtitle = "Avtomatik internet qidiruv, rasm yuklab olish va DOCX yaratish",
                tag = "File & Media Generator",
                testTag = "action_docx_car",
                onClick = { viewModel.triggerDocxCarGeneration() }
            )
        }

        // Manual Text Command Input
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = manualInputText,
                    onValueChange = { manualInputText = it },
                    placeholder = { Text("Buyruq yoki savol yozing...", color = TextMuted, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = SpaceCardBg,
                        unfocusedContainerColor = SpaceCardBg
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("home_text_input")
                )

                IconButton(
                    onClick = {
                        if (manualInputText.isNotBlank()) {
                            val text = manualInputText.trim()
                            manualInputText = ""
                            viewModel.handleUserPrompt(text, isVoice = false)
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberCyan)
                        .testTag("home_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = SpaceDarkBg,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Telemetry HUD Specs
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = 12.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TelemetryStat("MODEL", viewModel.preferencesManager.selectedModel.substringAfterLast("-").uppercase(), CyberCyan)
                TelemetryStat("LANGUAGE", viewModel.preferencesManager.primaryLanguage.uppercase(), NeonEmerald)
                TelemetryStat("TOKENS", "${viewModel.preferencesManager.totalTokensUsed}", ElectricBlue)
                TelemetryStat("ACCESSIBILITY", if (isAccessibilityActive) "ON" else "OFF", if (isAccessibilityActive) NeonEmerald else AlertRed)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    accentColor: Color,
    title: String,
    subtitle: String,
    tag: String,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag),
        color = SpaceCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, SpaceCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SpaceSurface)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = tag,
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TelemetryStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
