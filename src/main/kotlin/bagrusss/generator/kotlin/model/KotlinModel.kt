package bagrusss.generator.kotlin.model

import bagrusss.generator.model.Model
import bagrusss.generator.model.ModelBuilder
import com.squareup.kotlinpoet.ClassName

abstract class KotlinModel : Model {


    protected constructor(builder: ModelBuilder) : super(builder)

    constructor(packageName: String, className: String) : super(packageName, className)

    constructor(clazz: ClassName) : super(clazz)

    override fun getFileExtension() = ".kt"

    override fun getFileName() = className.simpleName() + ".kt"

}