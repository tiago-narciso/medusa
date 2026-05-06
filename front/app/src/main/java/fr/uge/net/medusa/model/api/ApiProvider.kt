package fr.uge.net.medusa.model.api

class ApiProvider {
    companion object {
        private var apiInstance: IGameApi? = null

        fun getApi(): IGameApi {
            if (apiInstance == null) {
                // todo: check how to get build info to create either mock or real game api
                apiInstance = MockGameApi()
            }
            return apiInstance!!
        }
    }
}