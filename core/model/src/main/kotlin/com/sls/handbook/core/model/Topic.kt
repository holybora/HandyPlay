package com.sls.handbook.core.model

data class Topic(
    val id: String,
    val name: String,
    val categoryId: String,
) {
    companion object {
        const val ID_TTL_CACHE = "kf_7"
        const val ID_GALLERY = "ui_1"
        const val ID_FEVER = "ui_2"

        // Design Patterns - Creational
        const val ID_DP_FACTORY_METHOD = "dp_1"
        const val ID_DP_ABSTRACT_FACTORY = "dp_2"
        const val ID_DP_PROTOTYPE = "dp_3"

        // Design Patterns - Structural
        const val ID_DP_ADAPTER = "dp_4"
        const val ID_DP_DECORATOR = "dp_5"
        const val ID_DP_FACADE = "dp_6"

        // Design Patterns - Behavioral
        const val ID_DP_OBSERVER = "dp_7"
        const val ID_DP_STRATEGY = "dp_8"
        const val ID_DP_COMMAND = "dp_9"
        const val ID_DP_STATE_MACHINE = "dp_10"
    }
}
