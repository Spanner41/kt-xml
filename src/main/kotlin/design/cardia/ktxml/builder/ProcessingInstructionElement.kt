package design.cardia.ktxml.builder

class ProcessingInstructionElement(
    type: String,
    attributes: MutableMap<String, () -> Any> = mutableMapOf()
) : Element(type, attributes) {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<?$type ${attributesToXml()}?>"
}