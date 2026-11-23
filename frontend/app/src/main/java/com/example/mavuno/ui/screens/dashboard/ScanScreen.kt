package com.example.mavuno.ui.screens.dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.mavuno.ui.components.DashHeader
import com.example.mavuno.ui.layouts.DashboardLayout
import com.example.mavuno.ui.theme.CropTheme
import java.io.File

// --------------------------------------------------
// MAIN SCAN SCREEN
// --------------------------------------------------

@Composable
fun ScanScreen(navController: NavHostController) {
    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                selectedImage = uri
            }
        }

    if (selectedImage != null) {
        ReviewPhotoScreen(
            imageUri = selectedImage,
            onRetake = { selectedImage = null },
            onAnalyze = {
                navController.navigate("plant/1")
            },
            navController = navController
        )
    } else {
        DashboardLayout {
            CameraScreenContent(
                onBack = {
                    navController.popBackStack()
                },
                onImageCaptured = { uri ->
                    selectedImage = uri
                },
                galleryLauncher = galleryLauncher
            )
        }
    }
}

// --------------------------------------------------
// CAMERA SCREEN CONTENT
// --------------------------------------------------

@Composable
fun CameraScreenContent(
    onBack: () -> Unit = {},
    onImageCaptured: (Uri) -> Unit,
    galleryLauncher: androidx.activity.result.ActivityResultLauncher<String>
) {

    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var lensFacing by remember {
        mutableIntStateOf(CameraSelector.LENS_FACING_BACK)
    }

    var flashEnabled by remember {
        mutableStateOf(false)
    }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var camera by remember {
        mutableStateOf<Camera?>(null)
    }

    val deepGreen = MaterialTheme.colorScheme.primary
    val paleGreen = MaterialTheme.colorScheme.primaryContainer
    val textGreen = CropTheme.colors.heading
    val mutedGreen = CropTheme.colors.tag

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasPermission = granted
        }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        DashHeader(head = "Scan Plant", onBack = onBack)

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "A closer look for healthier crops.",
            fontSize = 17.sp,
            color = mutedGreen,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (hasPermission) {

            // CAMERA PREVIEW
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.98f)
                    .clip(RoundedCornerShape(38.dp))
            ) {

                CameraPreview(
                    lensFacing = lensFacing,
                    onCameraReady = { capture, activeCamera ->
                        imageCapture = capture
                        camera = activeCamera
                    }
                )

                // PHOTO LABEL
                Text(
                    text = "Photo",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(deepGreen)
                        .padding(
                            horizontal = 22.dp,
                            vertical = 10.dp
                        )
                )

                // FLASH BUTTON
                IconButton(
                    onClick = {
                        flashEnabled = !flashEnabled
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                ) {
                    Icon(
                        imageVector = if (flashEnabled)
                            Icons.Default.FlashOn
                        else
                            Icons.Default.FlashOff,
                        contentDescription = "Toggle flash",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // SCANNING FRAME
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .fillMaxHeight(0.62f)
                        .align(Alignment.Center)
                        .border(
                            BorderStroke(
                                2.dp,
                                Color.White
                            ),
                            RoundedCornerShape(42.dp)
                        )
                )

                // SCANNING INSTRUCTION
                Text(
                    text = "Keep one affected leaf in the frame",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .clip(CircleShape)
                        .background(deepGreen.copy(alpha = 0.90f))
                        .padding(
                            horizontal = 24.dp,
                            vertical = 10.dp
                        )
                )
            }

            // FLASH CONTROL
            LaunchedEffect(camera, flashEnabled) {
                camera?.cameraControl?.enableTorch(
                    flashEnabled
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // CAMERA CONTROLS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // UPLOAD
                CameraAction(
                    icon = Icons.Default.AddPhotoAlternate,
                    label = "Upload",
                    deepGreen = deepGreen,
                    textGreen = textGreen,
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                )

                // CAPTURE
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .border(
                            3.dp,
                            deepGreen,
                            CircleShape
                        )
                        .padding(7.dp)
                        .clip(CircleShape)
                        .background(deepGreen)
                        .clickable(
                            enabled = imageCapture != null
                        ) {
                            imageCapture?.let { capture ->
                                takePhoto(
                                    context = context,
                                    imageCapture = capture,
                                    onImageCaptured = { uri ->
                                        onImageCaptured(uri)
                                    }
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Capture image",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // FLIP CAMERA
                CameraAction(
                    icon = Icons.Default.FlipCameraAndroid,
                    label = "Flip",
                    deepGreen = deepGreen,
                    textGreen = textGreen,
                    onClick = {
                        lensFacing =
                            if (lensFacing ==
                                CameraSelector.LENS_FACING_BACK
                            ) {
                                CameraSelector.LENS_FACING_FRONT
                            } else {
                                CameraSelector.LENS_FACING_BACK
                            }

                        flashEnabled = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // SCANNING TIPS
            ScanningTips(
                deepGreen = deepGreen,
                paleGreen = paleGreen,
                textGreen = textGreen
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Capture a photo or upload one from your gallery.",
                fontSize = 14.sp,
                color = mutedGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

        } else {

            PermissionMessage(
                deepGreen = deepGreen,
                textGreen = textGreen,
                onRequestPermission = {
                    permissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            )
        }
    }
}

// --------------------------------------------------
// CAMERA PREVIEW
// --------------------------------------------------

@Composable
fun CameraPreview(
    lensFacing: Int,
    onCameraReady: (ImageCapture, Camera) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previewView by remember {
        mutableStateOf<PreviewView?>(null)
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode =
                    PreviewView.ImplementationMode.COMPATIBLE
            }.also {
                previewView = it
            }
        }
    )

    DisposableEffect(
        previewView,
        lifecycleOwner,
        lensFacing
    ) {

        val view = previewView

        if (view == null) {
            onDispose {}
        } else {

            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)

            var disposed = false
            var boundProvider: ProcessCameraProvider? = null

            val preview = Preview.Builder().build()

            val capture = ImageCapture.Builder()
                .setCaptureMode(
                    ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                )
                .build()

            cameraProviderFuture.addListener({

                if (!disposed) {

                    try {

                        val provider =
                            cameraProviderFuture.get()

                        boundProvider = provider

                        preview.surfaceProvider =
                            view.surfaceProvider

                        val selector = CameraSelector.Builder()
                            .requireLensFacing(lensFacing)
                            .build()

                        provider.unbindAll()

                        val activeCamera =
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                capture
                            )

                        onCameraReady(
                            capture,
                            activeCamera
                        )

                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }

            }, ContextCompat.getMainExecutor(context))

            onDispose {

                disposed = true

                boundProvider?.unbind(
                    preview,
                    capture
                )
            }
        }
    }
}

// --------------------------------------------------
// CAMERA ACTION BUTTONS
// --------------------------------------------------

@Composable
fun CameraAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    deepGreen: Color,
    textGreen: Color,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = deepGreen,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = textGreen
        )
    }
}




// --------------------------------------------------
// SCANNING TIPS
// --------------------------------------------------

@Composable
fun ScanningTips(
    deepGreen: Color,
    paleGreen: Color,
    textGreen: Color
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(paleGreen)
            .padding(22.dp)
    ) {

        Text(
            text = "For a clearer result",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = deepGreen
        )

        Spacer(modifier = Modifier.height(18.dp))

        Spacer(modifier = Modifier.height(14.dp))

        TipItem(
            icon = Icons.Default.WbSunny,
            text = "Use daylight and avoid strong shadows.",
            deepGreen = deepGreen,
            textGreen = textGreen
        )

        Spacer(modifier = Modifier.height(14.dp))

        TipItem(
            icon = Icons.Default.CenterFocusStrong,
            text = "Hold steady. Keep spots and edges in focus.",
            deepGreen = deepGreen,
            textGreen = textGreen
        )

        Spacer(modifier = Modifier.height(14.dp))

        TipItem(
            icon = Icons.Default.Spa,
            text = "Photograph one leaf, without covering it.",
            deepGreen = deepGreen,
            textGreen = textGreen
        )
    }
}

@Composable
fun TipItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    deepGreen: Color,
    textGreen: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = deepGreen,
            modifier = Modifier.size(25.dp)
        )

        Text(
            text = text,
            fontSize = 14.sp,
            color = textGreen
        )
    }
}

// --------------------------------------------------
// PERMISSION MESSAGE
// --------------------------------------------------

@Composable
fun PermissionMessage(
    deepGreen: Color,
    textGreen: Color,
    onRequestPermission: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = null,
            tint = deepGreen,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Camera permission is required to scan crops.",
            color = textGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}

// --------------------------------------------------
// PHOTO CAPTURE
// --------------------------------------------------

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit
) {

    CallTakePhoto(context, imageCapture, onImageCaptured)
}

private fun CallTakePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit
) {
    val photoFile = File(
        context.cacheDir,
        "mavuno_${System.currentTimeMillis()}.jpg"
    )

    ViewUtil.savePhotoToFile(context, imageCapture, photoFile, onImageCaptured)
}

object ViewUtil {
    fun savePhotoToFile(
        context: Context,
        imageIcon: ImageCapture,
        photoFile: File,
        onImageCaptured: (Uri) -> Unit
    ) {
        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile)
                .build()

        imageCaptureSave(context, imageIcon, outputOptions, photoFile, onImageCaptured)
    }

    private fun imageCaptureSave(
        context: Context,
        imageIcon: ImageCapture,
        outputOptions: ImageCapture.OutputFileOptions,
        photoFile: File,
        onImageCaptured: (Uri) -> Unit
    ) {
        imageIcon.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),

            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    onImageCaptured(
                        Uri.fromFile(photoFile)
                    )
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {
                    exception.printStackTrace()
                }
            }
        )
    }
}
