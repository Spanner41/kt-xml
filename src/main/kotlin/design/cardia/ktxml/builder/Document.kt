package design.cardia.ktxml.builder

class Document(
    val version: XmlVersion,
    val encoding: XmlEncoding,
    childBuilder: Document.() -> Element
) {
    val child = childBuilder()

    val xmlInstructionElement = ProcessingInstructionElement("xml").apply {
        "version" to version.value
        "encoding" to encoding.value
    }
}
