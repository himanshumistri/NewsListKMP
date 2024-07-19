package com.example.myapplication.shared.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.myapplication.shared.main.MainComponent
import com.example.myapplication.shared.welcome.WelcomeComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    fun popScreen(screenConfig: DefaultRootComponent.Config)

    sealed class Child {
        class Main(val component: MainComponent) : Child()
        class Welcome(val component: WelcomeComponent) : Child()
    }
}
