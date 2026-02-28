package com.sls.handbook.core.model

sealed class Topic {
    abstract val id: String
    abstract val name: String
    abstract val categoryId: String

    sealed class KotlinFundamental : Topic() {
        override val categoryId: String get() = "kotlin_fundamentals"

        data object TtlCache : KotlinFundamental() {
            override val id: String get() = "kf_7"
            override val name: String get() = "TTL Cache"
        }
    }

    sealed class Ui : Topic() {
        override val categoryId: String get() = "ui"

        data object Gallery : Ui() {
            override val id: String get() = "ui_1"
            override val name: String get() = "Gallery"
        }

        data object Fever : Ui() {
            override val id: String get() = "ui_2"
            override val name: String get() = "Fever"
        }
    }

    sealed class DesignPattern : Topic() {
        override val categoryId: String get() = "design_patterns"

        data object FactoryMethod : DesignPattern() {
            override val id: String get() = "dp_1"
            override val name: String get() = "Factory Method"
        }

        data object AbstractFactory : DesignPattern() {
            override val id: String get() = "dp_2"
            override val name: String get() = "Abstract Factory"
        }

        data object Prototype : DesignPattern() {
            override val id: String get() = "dp_3"
            override val name: String get() = "Prototype"
        }

        data object Adapter : DesignPattern() {
            override val id: String get() = "dp_4"
            override val name: String get() = "Adapter"
        }

        data object Decorator : DesignPattern() {
            override val id: String get() = "dp_5"
            override val name: String get() = "Decorator"
        }

        data object Facade : DesignPattern() {
            override val id: String get() = "dp_6"
            override val name: String get() = "Facade"
        }

        data object Observer : DesignPattern() {
            override val id: String get() = "dp_7"
            override val name: String get() = "Observer"
        }

        data object Strategy : DesignPattern() {
            override val id: String get() = "dp_8"
            override val name: String get() = "Strategy"
        }

        data object Command : DesignPattern() {
            override val id: String get() = "dp_9"
            override val name: String get() = "Command"
        }

        data object StateMachine : DesignPattern() {
            override val id: String get() = "dp_10"
            override val name: String get() = "State Machine"
        }
    }
}
