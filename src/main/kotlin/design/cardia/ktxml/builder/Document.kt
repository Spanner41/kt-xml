package design.cardia.ktxml.builder

class Document(
    private val version: XmlVersion,
    private val encoding: XmlEncoding,
    private val child: Document.() -> Element
) {
    fun toXml(format: XmlFormat) =
        """<?xml version="${version.value}" encoding="${encoding.value}"?>${format.lineSeparator}${child().toXml(format, version, encoding)}"""
}
