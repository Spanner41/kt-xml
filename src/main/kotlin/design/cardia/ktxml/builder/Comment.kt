package design.cardia.ktxml.builder

class Comment(private val text: String) : Node {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<!-- $text -->"
}
