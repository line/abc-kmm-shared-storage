package com.linecorp.abc.sharedstorage.annotations

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment) =
        Processor(environment.codeGenerator, environment.logger)
}