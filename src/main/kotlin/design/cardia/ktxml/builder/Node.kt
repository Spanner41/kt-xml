package design.cardia.ktxml.builder

interface Node {
    fun toXml(format: XmlFormat, version: XmlVersion, encoding: XmlEncoding, indentLevel: Int = 0): String
    fun children(): List<Node>
}
