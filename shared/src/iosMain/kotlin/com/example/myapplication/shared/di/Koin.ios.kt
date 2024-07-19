import co.touchlab.kermit.Logger

class Koin {
    fun initKoin() {
        Logger.i("Call from swift call initKoin")
        com.example.myapplication.shared.di.initKoin()
    }
}




