package design.cardia.ktxml.builder

class ProcessingInstructionElement(
    private val type: String,
    attributes: MutableMap<String, () -> Any> = mutableMapOf()
) : NodeWithAttributes(attributes) {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String = "<?$type${attributesToXml(format, version)}?>"
}
