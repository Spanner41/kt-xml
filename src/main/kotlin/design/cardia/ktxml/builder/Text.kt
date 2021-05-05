package design.cardia.ktxml.builder

open class Text(var text: String = "") : Node {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = format.escape(text, version)
}
