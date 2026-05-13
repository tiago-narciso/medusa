package fr.uge.net.medusa.api

import fr.uge.net.medusa.mockApi.MockGameApi

/**
 * Provides a shared API instance for the application.
 *
 * This class creates the API implementation only once
 * and reuses the same instance throughout the app.
 *
 * Currently it returns a MockGameApi implementation,
 * but it could later return a real Retrofit API
 * implementation without changing the UI code.
 *
 * This helps separate:
 * - UI logic
 * - API implementation details
 */

class ApiProvider {
    companion object {
        private var apiInstance: IGameApi? = null

        fun getMockApi(): IGameApi {
            if (apiInstance == null) {
                // todo: check how to get build info to create either mock or real game api
                apiInstance = MockGameApi()
            }
            return apiInstance!!
        }
        fun getRealApi(): IGameApi {
            if (apiInstance == null) {
                // todo: check how to get build info to create either mock or real game api
                apiInstance = RealGameApi()
            }
            return apiInstance!!
        }

    }
}