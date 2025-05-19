# truthshell_android

## File Upload (Image / Audio)

To analyze an image or audio file via the multipart `/analyze/file` endpoint, first convert your `File` into a `MultipartBody.Part`, then call the repository:

```kotlin
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

// Suppose `file` is a java.io.File object (e.g. from camera or file picker)
val requestBody = file
    .asRequestBody("application/octet-stream".toMediaType())
val filePart = MultipartBody.Part
    .createFormData("file", file.name, requestBody)

// Then invoke the repository method (mode = "image" | "audio")
val response = truthRepository.analyzeFile(
    apiKey = BuildConfig.SERVER_API_KEY,
    mode = "image",
    filePart = filePart
)
```
