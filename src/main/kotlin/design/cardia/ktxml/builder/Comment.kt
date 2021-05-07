package design.cardia.ktxml.builder

class Comment(val text: String) : NodeWithoutChildren {
    override fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int): String = "<!-- $text -->"
}
