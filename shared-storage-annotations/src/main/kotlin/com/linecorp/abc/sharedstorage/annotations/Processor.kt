package com.linecorp.abc.sharedstorage.annotations

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import java.io.OutputStream

class Processor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val className = SharedStorage::class.qualifiedName.toString()
        val symbols = resolver.getSymbolsWithAnnotation(className)
        val ret = symbols.filter { !it.validate() }
        val validSymbols = symbols.filter { it.validate() }
        validSymbols.forEach { it.accept(Visitor(), Unit) }
        logger.info("Processor::Start -> validSymbols: ${validSymbols.count()}")
        return ret.toList()
    }

    override fun finish() {
        logger.info("Processor::Finish")
    }

    override fun onError() {
        logger.info("Processor::Error")
    }

    inner class Visitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (classDeclaration.classKind != ClassKind.INTERFACE) {
                return
            }

            logger.info("Visitor::visitClassDeclaration")

            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val interfaceName = classDeclaration.simpleName.asString()
            val className = "Shared$interfaceName"
            val file = codeGenerator.createNewFile(Dependencies(true, classDeclaration.containingFile!!), packageName , className)
            val code =
                """
                package $packageName
                
                import com.linecorp.abc.sharedstorage.SharedStorage
                
                object $className: $interfaceName {
                    ${generatedProperties(classDeclaration)}
                }
            """.trimIndent()
            file.appendText(code)
            file.close()
        }

        private fun generatedProperties(classDeclaration: KSClassDeclaration): String {
            val secureClassName = Secure::class.simpleName.toString()
            var str = ""
            classDeclaration.getAllProperties().forEach { it ->
                val pName = it.simpleName.asString()
                val isSecure = it.annotations.firstOrNull { annotation ->
                    annotation.shortName.asString() == secureClassName
                } != null
                val getterName = if (isSecure) "secureLoad" else "load"
                val setterName = if (isSecure) "secureSave" else "save"
                val defaultValue = when (it.type.element.toString()) {
                    "String" -> "\"\""
                    "Boolean" -> "false"
                    "Int" -> "0"
                    "Long" -> "0L"
                    "Float"-> "0.0f"
                    else -> "0"
                }

                str += """
                    override var $pName: ${it.type}
                    get() {
                        return SharedStorage.$getterName("$pName", $defaultValue)
                    }
                    set(value) {
                        SharedStorage.$setterName(value, "$pName")
                    }
                    """
            }
            logger.info("Visitor::generatedProperties -> $str")
            return str
        }
    }
}

private fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}