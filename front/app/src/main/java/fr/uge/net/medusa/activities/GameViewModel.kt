package fr.uge.net.medusa.activities

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.uge.net.medusa.api.ApiProvider
import fr.uge.net.medusa.data.NearCard
import fr.uge.net.medusa.models.NearRequest
import fr.uge.net.medusa.services.DeviceLocationService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.osmdroid.util.GeoPoint

class GameViewModel : ViewModel() {
    sealed interface GameStatus {
        data object Loading : GameStatus
        data object Ready : GameStatus
        data object NoInternet : GameStatus
        data object PermissionRequired : GameStatus
        data object LocationDisabled : GameStatus
        data object NoPosition : GameStatus
    }

    val apiService = ApiProvider.getRealApi();

    var cards by mutableStateOf(listOf<NearCard>())
        private set

    var userPosition by mutableStateOf(DEFAULT_USER_POSITION)
        private set

    var gameStatus by mutableStateOf<GameStatus>(GameStatus.Loading)
        private set

    var isBootstrapped by mutableStateOf(false)
        private set

    private var positionLoopJob: Job? = null
    private var bootstrapJob: Job? = null

    fun updateUserPosition(position: GeoPoint) {
        userPosition = position
    }

    fun runBootstrap(context: Context) {
        if (bootstrapJob?.isActive == true || isBootstrapped) {
            return
        }

        val appContext = context.applicationContext
        bootstrapJob = viewModelScope.launch {
            while (isActive && !isBootstrapped) {
                refreshGameStatusAndPosition(appContext)
                if (gameStatus == GameStatus.Ready) {
                    isBootstrapped = true
                    break
                }
                if (gameStatus.requiresUserActionOnLoading()) {
                    break
                }
                delay(BOOTSTRAP_RETRY_DELAY_MS)
            }
        }
    }

    fun startPositionLoop(context: Context) {
        if (positionLoopJob?.isActive == true) {
            return
        }

        val appContext = context.applicationContext
        positionLoopJob = viewModelScope.launch {
            while (isActive) {
                refreshGameStatusAndPosition(appContext)
                delay(POLLING_DELAY_MS)
            }
        }
    }

    fun stopPositionLoop() {
        positionLoopJob?.cancel()
        positionLoopJob = null
    }

    fun forceRefresh(context: Context) {
        viewModelScope.launch {
            refreshGameStatusAndPosition(context.applicationContext)
            if (gameStatus == GameStatus.Ready) {
                isBootstrapped = true
            } else if (!isBootstrapped) {
                runBootstrap(context)
            }
        }
    }

    private suspend fun refreshGameStatusAndPosition(context: Context) {
        if (!DeviceLocationService.hasInternetConnection(context)) {
            gameStatus = GameStatus.NoInternet
            return
        }

        if (!DeviceLocationService.hasLocationPermission(context)) {
            gameStatus = GameStatus.PermissionRequired
            return
        }

        if (!DeviceLocationService.isLocationEnabled(context)) {
            gameStatus = GameStatus.LocationDisabled
            return
        }

        val currentPosition = withTimeoutOrNull(POSITION_TIMEOUT_MS) {
            DeviceLocationService.getCurrentPosition(context)
        }
        if (currentPosition == null) {
            gameStatus = GameStatus.NoPosition
            return
        }

        val nearRequest = NearRequest(currentPosition.latitude, currentPosition.longitude);
        cards = apiService.near(nearRequest).cards;

        updateUserPosition(currentPosition)
        gameStatus = GameStatus.Ready
    }

    override fun onCleared() {
        stopPositionLoop()
        bootstrapJob?.cancel()
        super.onCleared()
    }

    private fun GameStatus.requiresUserActionOnLoading(): Boolean {
        return this == GameStatus.NoInternet ||
            this == GameStatus.PermissionRequired ||
            this == GameStatus.LocationDisabled
    }

    companion object {
        private const val POLLING_DELAY_MS = 3_000L
        private const val BOOTSTRAP_RETRY_DELAY_MS = 1_000L
        private const val POSITION_TIMEOUT_MS = 5_000L
        private val DEFAULT_USER_POSITION = GeoPoint(48.8566, 2.3522)
    }
}
