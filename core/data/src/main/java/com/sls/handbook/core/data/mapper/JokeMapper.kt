package com.sls.handbook.core.data.mapper

import com.sls.handbook.core.model.Joke
import com.sls.handbook.core.network.model.JokeResponse

internal fun JokeResponse.toDomain(): Joke =
    Joke(
        setup = setup,
        punchline = punchline,
    )
