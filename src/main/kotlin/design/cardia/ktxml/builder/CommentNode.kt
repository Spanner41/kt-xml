package design.cardia.ktxml.builder

class CommentNode(private val text: String) : XmlContent() {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding): String = "<!-- $text -->"
}
